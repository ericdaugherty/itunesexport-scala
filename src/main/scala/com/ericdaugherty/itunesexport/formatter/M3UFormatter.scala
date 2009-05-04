package com.ericdaugherty.itunesexport.formatter

import java.io.{File, PrintWriter}
import java.text.MessageFormat
import parser.Playlist

/**
 * Formats a given playlist as an m3u playlist file
 *
 * @author Eric Daugherty
 */
class M3UFormatter(settings: FormatterSettings) extends Formatter(settings) with Version {

  def writePlaylist(directory: String, playlist: Playlist) {
    // Write out each track using a PrintWriter
    withPrintWriter(new File(directory, parseFileName(playlist) + ".m3u")) { writer =>      
      writer.println(MessageFormat.format("#Playlist: '{0}' exported by iTunesExport-Scala v{1} http://www.ericdaugherty.com/dev/itunesexport/scala/", playlist.name, version))
      playlist.tracks.foreach(track => writer.println(parseLocation(track)))      
    }
  }
}