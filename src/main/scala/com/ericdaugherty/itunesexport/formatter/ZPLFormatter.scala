package com.ericdaugherty.itunesexport.formatter

import java.io.{File, PrintWriter}
import java.text.MessageFormat

import parser.{Track, Playlist}

/**
 * Formats a given playlist as an ZPL playlist used by the MS Zune.
 *
 * @author Eric Daugherty
 */
class ZPLFormatter extends Formatter {

  def writePlaylist(directory: String, playlist: Playlist) {
    // Write out each track using a PrintWriter
    withPrintWriter(new File(directory, parseFileName(playlist) + ".zpl")) { writer =>
      writer.println("<?zpl version=\"1.0\"?>")
      writer.println(<smil>
  <head>
    <meta name="Generator" content="Zune -- 1.3.5728.0" />
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