package com.ericdaugherty.itunesexport

import com.ericdaugherty.itunesexport.parser.Library
import formatter.M3UFormatter
import java.io.File

object ParserTester extends Application{
  
//  println("Starting Parser")
//  val start = new java.util.Date().getTime()
//  val library = new Library("""C:\Users\eric.daugherty.GENECA00\Music\iTunes\iTunes Music.xml""")
//  val total = new java.util.Date().getTime() - start;
//  println(total)
//
//  println(library)
//
//  println("~~~~~~~~~~~~~~~~~~~~~~~~")
//
//  println (library.playlists(8))
//
//  library.playlists(8).tracks.foreach(println)
//
//  println("Finished Parser")

  val library = new Library("""C:\Users\eric.daugherty.GENECA00\Music\iTunes\iTunes Music.xml""")
  new M3UFormatter().writePlaylist("", library.playlists(0))
}
