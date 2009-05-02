package com.ericdaugherty.itunesexport.formatter

import java.io.{File, PrintWriter}
import java.text.MessageFormat

import parser.{Track, Playlist}

/**
 * Formats a given playlist as an WPL playlist used by Windows Media Player.
 *
 * @author Eric Daugherty
 */
class WPLFormatter extends Formatter {

  def writePlaylist(directory: String, playlist: Playlist) {
    // Write out each track using a PrintWriter
    withPrintWriter(new File(directory, parseFileName(playlist) + ".wpl")) { writer =>
      writer.println("<?wpl version=\"1.0\"?>")
      writer.println(<smil>
  <head>
    <author />
    <title>{playlist.name}</title>
  </head>
  <body>
    <seq>
    {for(track <- playlist.tracks) yield <media src={parseLocation(track)}></media>}
    </seq>
  </body>
</smil>)
    }
  }
}
