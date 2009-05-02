package com.ericdaugherty.itunesexport.parser

import xml.Node

import java.util.Date

/**
 * Represents the data for a single track in the iTunes Music Library.
 *
 * @author Eric Daugherty
 */
class Track(xml: Node) extends PListParser {

  // Parse the XML into a pListMap
  private val pListMap : Map[String,Any] = parsePList(xml)

  // Provide concrete impl for PListParser helper method
  def getPListValue(key: String, default: Any): Any = getPListValue(pListMap, key, default)

//  val trackId: Double = getPListDouble("Track ID")
  val trackId: Double = getValue[Double]("Track ID", 0)
  val name: String = getPListString("Name")
  val artist: String = getPListString("Artist")
  val genre: String = getPListString("Genre")
  val fileType: String = getPListString("Kind")
  val fileSize: Double = getPListDouble("Size")
  val time: Double = getPListDouble("Total Time")
  val timeInSeconds = (time/1000).toInt
  val dateModified: Date = getPListDate("Date Modified")
  val dateAdded: Date = getPListDate("Date Added")
  val bitRate: Double = getPListDouble("Bit Rate")
  val sampleRate: Double = getPListDouble("Sample Rate")
  val playCount: Double = getPListDouble("Play Count")
  val playDate: Date = getPListDate("Play Date UTC")
  val rating: Double = getPListDouble("Rating")
  val trackType: String = getPListString("Track Type")
  val location: String = getPListString("Location")
//  val inLibrary: Boolean = getPListBoolean("Disabled", true)
  val disabled = getValue("Disabled", false)

  override def toString = {
    "Track ID: " + trackId + "\n" +
      "Name: " + name + "\n" +
      "Artist: " + artist + "\n" +
      "Genre: " + genre + "\n" +
      "File Type: " + fileType + "\n" +
      "File Size: " + fileSize + "\n" +
      "Time: " + time + "\n" +
      "Date Modified: " + dateModified + "\n" +
      "Date Added: " + dateAdded + "\n" +
      "Bit Rate: " + bitRate +  "\n" +
      "Sample Rate: " + sampleRate + "\n" +
      "Play Count: " + playCount + "\n" +
      "Play Date: " + playDate + "\n" +
      "Rating: " + rating + "\n" +
      "Track Type: " + trackType + "\n" +
      "Location: " + location + "\n" +
//      "In Library: " + inLibrary + "\n" +
      "Disabled: " + disabled + "\n"
  }
}
