package com.ericdaugherty.itunesexport.console

import formatter.M3UFormatter
import java.io.File

import parser.{Playlist, Library}

/**
 * Entrypoint for command line iTunesExport.
 *
 * @author Eric Daugherty
 */
object Export extends Version {

  val preamble = "iTunesExport-Scala " + version + " http://www.ericdaugherty.com/dev/itunesexport/scala/\n\n" +
    "iTunesExport will attempt to export playlists from library at default location unless specified as a parameter.\n\n"

  def main(args: Array[String]) {

    println(preamble)

    if(args.length > 1) { println("Please specify the location of the iTunes Music Library XML file.  Use quotes if the path has spaces in it."); return}

    val location =
    if(args.length == 0) {
      println("Using default directory location.")
      val homeDirectory = System.getProperty("user.home")
      homeDirectory + "/Music/iTunes/iTunes Music Library.xml"
    }
    else {
      args(0)
    }

    if(!new File(location).exists) {println("The specified file does not exist: " + location); return}

    println("Parsing Music Library: " + location)

    val formatter = new M3UFormatter()
    val library = new Library(location)
    val playlists = library.playlists.filter(playlist => playlist.visible)
    playlists.foreach(playlist => formatter.writePlaylist(System.getProperty("user.dir"), playlist))
  }
}