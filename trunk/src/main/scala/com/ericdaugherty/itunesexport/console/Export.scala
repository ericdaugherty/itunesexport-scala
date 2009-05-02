package com.ericdaugherty.itunesexport.console

import formatter._
import java.io.File

import parser.{Playlist, Library}

/**
 * Entrypoint for command line iTunesExport.
 *
 * @author Eric Daugherty
 */
object Export extends Version {

  val preamble = "iTunesExport-Scala " + version + " http://www.ericdaugherty.com/dev/itunesexport/scala/\n\n" +
    "Usage:\n" +
    "java -jar itunesexport.jar <-library=iTunes Music Library XML File>\n" +
    "<-playlistType=M3U, EXT, WPL, or ZPL (defaults to M3U)>"

//    Console.WriteLine( "iTunes Playlist Export Usage" );
//    Console.WriteLine( "iTunesExport <-library=iTunes Music Library File> <-prefix=Path Prefix> <-dir=File Path> <-exclude=\"Playlist Name1,Playlist Name 2\"> <-prefix=Path Prefix> <-dir=File Path> <-include=\"Playlist Name1,Playlist Name 2\">" );
//    Console.WriteLine("Other optional parameters: " + INCLUDE_AAC_ARG + " " + INCLUDE_PROTECTED_AAC_ARG + " " + INCLUDE_LIBRARY_PLAYLIST + " " + COPY_FILES + " " + COPY_FILES_WITH_INDEX + " " + INCLUDE_FOLDER_PLAYLISTS + " " + USE_INTL_EXT_ARG + " " + PLAYLIST_TYPE + "<M3U or EXT or WPL (defaults to M3U)>" );
//    Console.WriteLine();
//    Console.WriteLine( "Each playlist will be written to the current directory as an m3u file." );

  def main(args: Array[String]) {

    println(preamble)

    val defaultLibraryLocation = System.getProperty("user.home") + "/Music/iTunes/iTunes Music Library.xml"

    val libraryLocation = getParameter(args, "library", defaultLibraryLocation)
    val playlistType = getParameter(args, "playlistType", "m3u").toLowerCase()

    if(!new File(libraryLocation).exists) {println("The specified file does not exist: " + libraryLocation); return}

    println("Parsing Music Library: " + libraryLocation)

    val formatter = playlistType match {
      case "m3u" => new M3UFormatter()
      case "ext" => new M3UExtFormatter()
      case "wpl" => new WPLFormatter()
      case "zpl" => new ZPLFormatter()
      case _ => println("Unsupported Playlist Type: " + playlistType + ", defaulting to m3u."); new M3UFormatter()
    }
    
    val library = new Library(libraryLocation)
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