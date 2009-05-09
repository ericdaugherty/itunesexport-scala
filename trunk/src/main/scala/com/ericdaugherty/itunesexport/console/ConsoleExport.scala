package com.ericdaugherty.itunesexport.console

import java.io.File

import formatter._
import parser.{Playlist, Library}

/**
 * Entrypoint for command line iTunesExport.
 *
 * @author Eric Daugherty
 */
object ConsoleExport extends Version {

  val preamble = "iTunesExport-Scala " + version + " http://www.ericdaugherty.com/dev/itunesexport/scala/\n\n" +
    "Usage:\n" +
    "java -jar itunesexport.jar <-library=iTunes Music Library XML File>\n" +
    "<-playlistType=M3U, EXT, WPL, or ZPL (defaults to M3U)>\n" +
    "<-musicPath=Base path to the music files.  This will override the MusicLibrary path in iTunes\n" +
    "<-musicPathOld=Use this parameter to override the MusicLibrary path replaced by the musicPath parameter>"

//    Console.WriteLine( "iTunes Playlist Export Usage" );
//    Console.WriteLine( "iTunesExport <-library=iTunes Music Library File> <-prefix=Path Prefix> <-dir=File Path> <-exclude=\"Playlist Name1,Playlist Name 2\"> <-prefix=Path Prefix> <-dir=File Path> <-include=\"Playlist Name1,Playlist Name 2\">" );
//    Console.WriteLine("Other optional parameters: " + INCLUDE_AAC_ARG + " " + INCLUDE_PROTECTED_AAC_ARG + " " + INCLUDE_LIBRARY_PLAYLIST + " " + COPY_FILES + " " + COPY_FILES_WITH_INDEX + " " + INCLUDE_FOLDER_PLAYLISTS + " " + USE_INTL_EXT_ARG );
//    Console.WriteLine();
//    Console.WriteLine( "Each playlist will be written to the current directory as an m3u file." );

  def main(args: Array[String]) {

    println(preamble)

    //TODO Handle -h or -? and display preamble

    // Determine library location and open library
    val defaultLibraryLocation = System.getProperty("user.home") + "/Music/iTunes/iTunes Music Library.xml"
    val libraryLocation = getParameter(args, "library", defaultLibraryLocation)
    if(!new File(libraryLocation).exists) {println("The specified file does not exist: " + libraryLocation); System.exit(0)}
    println("Parsing Music Library: " + libraryLocation)
    val library = new Library(libraryLocation)

    // Parse other input parameters
    val playlistType = getParameter(args, "playlistType", "m3u").toLowerCase()

    // Initialize settings container for Formatters
    val settings = new FormatterSettings() {
      val musicPath = getParameter(args, "musicPath", library.musicFolder)
      val musicPathOld = getParameter(args, "musicPathOld", library.musicFolder)
    }

    val formatter = playlistType match {
      case "m3u" => new M3UFormatter(settings)
      case "ext" => new M3UExtFormatter(settings)
      case "wpl" => new WPLFormatter(settings)
      case "zpl" => new ZPLFormatter(settings)
      case _ =>  println("Unsupported Playlist Type: " + playlistType); System.exit(0); null
    }

    val playlists = library.playlists.filter(playlist => playlist.visible)
    playlists.foreach(playlist => formatter.writePlaylist(System.getProperty("user.dir"), playlist))
  }

  /**
   * Returns the specified parameter from the args array or the default value of the parameter is not present.
   */
  private def getParameter(args: Array[String], parameterName: String, default: String) : String = {
    val argList = args.filter(arg => arg.startsWith("-"+parameterName))
    if(argList.length == 0) default else argList(0).drop(argList(0).indexOf('=')+1)
  }

}