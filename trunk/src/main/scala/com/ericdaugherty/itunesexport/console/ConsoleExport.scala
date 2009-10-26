package com.ericdaugherty.itunesexport.console

import java.io.File

import formatter._
import parser.Library

/**
 * Entrypoint for command line iTunesExport.
 *
 * @author Eric Daugherty
 */
object ConsoleExport extends Version {

  // General TODOs:
  // Fix encoding to support UTF-8 Chars
  // Add file copy logic from flex version

  val preamble = "iTunesExport-Scala " + version + " http://www.ericdaugherty.com/dev/itunesexport/scala/\n" +
  "Use -h or -? to display available parameters"

  val help = preamble + "\n\n" +
    "Usage:\n" +
    "java -jar itunesexport.jar <-library=iTunes Music Library XML File>\n" +
    "<-outputDir=Directory to write the Playlist (and optionally music) files to.>\n" +
    "<-playlistType=M3U, EXT, WPL, ZPL, or MPL (defaults to M3U)>\n" +
    "<-musicPath=Base path to the music files.  This will override the MusicLibrary path in iTunes>\n" +
    "<-musicPathOld=Use this parameter to override the MusicLibrary path replaced by the musicPath parameter>\n" +
    "<-includePlaylist=Playlist Name 1, Playlist Name 2>\n" +
    "<-excludePlaylist=Playlist Name 1, Playlist Name 2>\n" +
//    "<-includeUTFBOM Add this parameter to include the UTF-8 Byte Order Mark>\n" - Not Sure this works
    "<-useM3U8Ext Add this parameter to use the .m3u8 extension instead .m3u (M3U and EXT Only)>\n" +
    "<-fileTypes=ALL, MP3, MP3M4A (defaults to MP3) - Selects which music files to include in the export (MP3M4A includes both MP3 and M4A files)>\n" +
    "<-includeDisabled Add this parameter to include songs that have been un-checked (disabled) in iTunes>"

  def main(args: Array[String]) {

    if(parameterExists(args, "?") || parameterExists (args, "h")) { println(help); exit() }

    println(preamble)

    // Determine library location and open library
    val defaultLibraryLocation = Library.defaultLocation
    val libraryLocation = getParameter(args, "library", defaultLibraryLocation)
    if(!new File(libraryLocation).exists) {println("The specified file does not exist: " + libraryLocation); System.exit(0)}
    println("Parsing Music Library: " + libraryLocation)
    val library = new Library(libraryLocation)

    // Determine/create output diretory location
    val defaultOutputDirectory = new File("").getAbsolutePath()
    val outputDirectoryString = getParameter(args, "outputDir", defaultOutputDirectory)
    val outputDirectoryFile = new File(outputDirectoryString)
    if(!outputDirectoryFile.exists) {
      if(!outputDirectoryFile.mkdirs()) {println("Unable to create the output directory: " + outputDirectoryString); System.exit(0)}
    }
    println("Writing output to: " + outputDirectoryString)

    // Parse other input parameters
    val playlistType = getParameter(args, "playlistType", "m3u").toLowerCase()
    val includePlaylistString = getParameter(args, "includePlaylist", "")
    val includePlaylistNames : Array[String] = includePlaylistString.split(',').map(name => name.trim).filter(name => name.length > 0)
    val excludePlaylistString = getParameter(args, "excludePlaylist", "")
    val excludePlaylistNames : Array[String] = excludePlaylistString.split(',').map(name => name.trim).filter(name => name.length > 0)

    // Initialize settings container for Formatters
    val settings = new FormatterSettings() {
      val outputDirectory = outputDirectoryFile
      val musicPath = getParameter(args, "musicPath", library.musicFolder)
      val musicPathOld = getParameter(args, "musicPathOld", library.musicFolder)
      val includeUTFBOM = parameterExists(args, "includeUTFBOM")
      val useM3U8 = parameterExists(args, "useM3U8Ext")
      val fileType = getParameter(args, "fileTypes", "MP3")
      val includeDisabled = parameterExists(args, "includeDisabled")
    }

    val formatter = playlistType match {
      case "m3u" => new M3UFormatter(settings)
      case "ext" => new M3UExtFormatter(settings)
      case "wpl" => new WPLFormatter(settings)
      case "zpl" => new ZPLFormatter(settings)
      case "mpl" => new MPLFormatter(settings)
      case _ =>  println("Unsupported Playlist Type: " + playlistType); System.exit(0); null
    }

    val playlists = library.playlists.filter(playlist => playlist.visible && playlist.tracks.length > 0 && includePlaylist(playlist.name, includePlaylistNames, excludePlaylistNames))
    playlists.foreach(playlist => formatter.writePlaylist(playlist))
  }

  /**
   * Returns the specified parameter from the args array or the default value of the parameter is not present.
   */
  private def getParameter(args: Array[String], parameterName: String, default: String) : String = {
    val argList = args.filter(arg => arg.startsWith("-"+parameterName))
    if(argList.length == 0) default else argList(0).drop(argList(0).indexOf('=')+1)
  }

  /**
   * Returns true if the speicfied parameter was passed in.
   */
  private def parameterExists(args: Array[String], parameterName: String) : Boolean = {
    val argList = args.filter(arg => arg.startsWith("-"+parameterName))
    argList.length > 0
  }

  /**
   * Determines whether a playlist should be included in the export
   * based on the include and exclude command line parameters.
   */
  private def includePlaylist(playlistName:String, includePlaylistNames:Array[String], excludePlaylistNames:Array[String]) : Boolean = {
    if(includePlaylistNames.length > 0) {
      if(includePlaylistNames.contains(playlistName)) true
      else false
    }
    else if(excludePlaylistNames.length > 0) {
      if(excludePlaylistNames.contains(playlistName)) false
      else true
    }
    else true
  }
}