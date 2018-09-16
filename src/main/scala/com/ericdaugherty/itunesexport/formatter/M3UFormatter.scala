package com.ericdaugherty.itunesexport.formatter

import java.io.File
import java.text.MessageFormat
import parser.Playlist

/**
 * Formats a given playlist as an m3u playlist file
 *
 * @author Eric Daugherty
 */
class M3UFormatter(settings: FormatterSettings) extends Formatter(settings) with Version {

  def writePlaylist(playlist: Playlist) {
    // Write out each track using a PrintWriter
    val extension = if(settings.useM3U8) ".m3u8" else ".m3u"
    withPrintWriter(new File(settings.outputDirectory, parseFileName(playlist) + extension), settings) { writer =>      
      writer.println(MessageFormat.format("#Playlist: {0}{1}{0} exported by iTunesExport-Scala v{2} http://www.ericdaugherty.com/dev/itunesexport/scala/", "'", playlist.name, version))
      filterTracks(playlist.tracks,settings).foreach(track => writer.println(copyFiles(track, playlist)))      
    }
  }
}