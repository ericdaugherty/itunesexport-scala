package com.ericdaugherty.itunesexport.parser

import xml.Node

/**
 * Represents the data for a single playlist in the iTunes Music Library.  This class parses the
 * XML for a playlist, and uses a trackMap to create an association with each track in the playlist.
 *
 * @author Eric Daugherty
 */
class Playlist(xml: Node, trackMap: Map[Double,Track]) extends PListParser {

  // Parse the XML into a pListMap
  private val pListMap : Map[String,Any] = parsePList(xml)

  // Provide concrete impl for PListParser helper method
  def getPListValue(key: String, default: Any): Any = getPListValue(pListMap, key, default)

  //TODO: Should we replace Illegal file characters here or somewhere else?
  val name: String = getPListString("Name")
  val playlistId: Double = getPListDouble("Playlist ID")
  val playlistPersistentId: String = getPListString("Playlist Persistent ID")
  val distinguishedKind: Double = getPListDouble("Distinguished Kind")
  val music: Boolean = getValue("Music", false)
  val podcasts: Boolean = getValue("Podcasts", false)
  val audiobooks: Boolean = getValue("Audiobooks", false)
  val tvShows: Boolean = getValue("TV Shows", false)
  val movies: Boolean = getValue("Movies", false)
  val purchasedMusic: Boolean = getValue("Purchased Music", false)
  val partyShuffle: Boolean = getValue("Party Shuffle", false)
  val visible: Boolean = getPListBoolean("Visible")
  val allItems: Boolean = getPListBoolean("All Items")
  val geniusTrackId: Double = getPListDouble("Genius Track ID")

  val tracks: Seq[Track] = for(trackId <- (xml \ "array" \ "dict" \ "integer")) yield trackMap(trackId.text.toDouble)

  override val toString = {
    "Name: " + name + "\n" +
            "Playlist ID: " + playlistId + "\n" +
            "Playlist Persistent ID: " + playlistPersistentId + "\n" +
            "All Items: " + allItems + "\n" +
            "Track Count: " + tracks.length
  }
}