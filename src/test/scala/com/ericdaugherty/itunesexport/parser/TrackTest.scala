package com.ericdaugherty.itunesexport.parser

import java.util.{GregorianCalendar, Calendar}
import org.scalatest.FunSuite

/**
 * Test class for Track
 *
 * @author Eric Daugherty
 */
class TrackTest extends FunSuite {

  test("Track XML Parsing") {
    val track = new Track(xml.XML.loadString(track1String))
    val calendar = Calendar.getInstance()
    assert(track.trackId === 1589)
    assert(track.name === "Caught Up In You")
    assert(track.genre === "80's")
    assert(track.fileType === "MPEG audio file")
    assert(track.fileSize === 5521408)
    assert(track.time === 275983)

    // Test Date Modified
    calendar.setTime(track.dateModified)
    assert(calendar.get(Calendar.YEAR) === 2005)
    assert(calendar.get(Calendar.MONTH) === Calendar.JULY)
    assert(calendar.get(Calendar.DAY_OF_MONTH) === 17)
    // Test Date Modified
    calendar.setTime(track.dateAdded)
    assert(calendar.get(Calendar.YEAR) === 2005)
    assert(calendar.get(Calendar.MONTH) === Calendar.JULY)
    assert(calendar.get(Calendar.DAY_OF_MONTH) === 17)

    assert(track.bitRate === 160)
    assert(track.sampleRate === 44100)
    assert(track.playCount === 2)
    // Test Play Date
    calendar.setTime(track.playDate)
    assert(calendar.get(Calendar.YEAR) === 2006)
    assert(calendar.get(Calendar.MONTH) === Calendar.AUGUST)
    assert(calendar.get(Calendar.DAY_OF_MONTH) === 30)

    //    assert(track.playDate)
    assert(track.rating === 60)
    assert(track.trackType === "File")
    assert(track.location === "file://localhost/M:/Music/38%20Special/Unknown%20Album/Caught%20Up%20In%20You.mp3" )
    assert(track.disabled === false)       
  }

  test("Track XML Parsing 2") {
    val track = new Track(xml.XML.loadString(track2String))

    assert(track.trackId === 1589)
    assert(track.name === "Caught Up In You")
    assert(track.fileType === "")
    assert(track.fileSize === 0)
    assert(track.disabled === true)
  }
  
  //***************************************************************************
  // Test Constants
  //***************************************************************************

  val track1String = """<dict><key>Track ID</key><integer>1589</integer>
			<key>Name</key><string>Caught Up In You</string>
			<key>Artist</key><string>38 Special</string>
			<key>Genre</key><string>80's</string>
			<key>Kind</key><string>MPEG audio file</string>
			<key>Size</key><integer>5521408</integer>
			<key>Total Time</key><integer>275983</integer>
			<key>Date Modified</key><date>2005-07-17T20:59:22Z</date>
			<key>Date Added</key><date>2005-07-17T13:56:06Z</date>
			<key>Bit Rate</key><integer>160</integer>
			<key>Sample Rate</key><integer>44100</integer>
			<key>Play Count</key><integer>2</integer>
			<key>Play Date</key><integer>3239788564</integer>
			<key>Play Date UTC</key><date>2006-08-30T18:16:04Z</date>
			<key>Rating</key><integer>60</integer>
			<key>Album Rating</key><integer>60</integer>
			<key>Album Rating Computed</key><true/>
			<key>Persistent ID</key><string>405C983B39AD457A</string>
			<key>Track Type</key><string>File</string>
			<key>Location</key><string>file://localhost/M:/Music/38%20Special/Unknown%20Album/Caught%20Up%20In%20You.mp3</string>
			<key>File Folder Count</key><integer>4</integer>
			<key>Library Folder Count</key><integer>1</integer>
      </dict>"""

  val track2String = """<dict><key>Track ID</key><integer>1589</integer>
			<key>Name</key><string>Caught Up In You</string>
			<key>Album Rating</key><integer>60</integer>
			<key>Album Rating Computed</key><true/>
			<key>Persistent ID</key><string>405C983B39AD457A</string>
			<key>Disabled</key><true/>
			<key>Track Type</key><string>File</string>
			<key>Location</key><string>file://localhost/M:/Music/38%20Special/Unknown%20Album/Caught%20Up%20In%20You.mp3</string>
			<key>File Folder Count</key><integer>4</integer>
			<key>Library Folder Count</key><integer>1</integer>
      </dict>"""
}