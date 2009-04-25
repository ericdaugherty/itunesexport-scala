package com.ericdaugherty.itunesexport.formatter

import java.io.File

import parser.{Track, Playlist}
import scala.xml.XML.loadString

import org.scalatest.FunSuite

/**
 * Test class for Formatter Trait
 *
 * @author Eric Daugherty
 */
class FormatterTest extends FunSuite {

  /** Simple class to allow us to test the Formatter trait */
  class TestFormatter extends Formatter {
    def writePlaylist(directory: String, playlist:Playlist)  {
      ""
    }
  }

  test("parseFileName") {
    val formatter = new TestFormatter()

    assert(formatter.parseFileName(playlist1) === "__Hel_o_World_How_Are_You__")
  }

  test("parseLocation - Windows") {
    val formatter = new TestFormatter()

    val track = new Track(xml.XML.loadString(trackLocationString))
    assert(formatter.parseLocation(track) === "M:/Music/38 Special/Unknown Album/Caught Up In You.mp3".replace('/', File.separatorChar))
  }

  test("parseLocation - Windows UNC") {
    val formatter = new TestFormatter()

    val track = new Track(xml.XML.loadString(trackLocation2String))
    assert(formatter.parseLocation(track) === "//MyServer/Music/38 Special/Unknown Album/Caught Up In You.mp3".replace('/', File.separatorChar))
  }

  test("parseLocation - OS X") {
    val formatter = new TestFormatter()

    val track = new Track(xml.XML.loadString(trackLocation3String))
    assert(formatter.parseLocation(track) === "/Users/Me/Music/38 Special/Unknown Album/Caught Up In You.mp3".replace('/', File.separatorChar))
  }

  //***************************************************************************
  // Test Constants
  //***************************************************************************

  val playlist1 = new Playlist(loadString("""
    <dict>
      <key>Name</key><string>&lt;/Hel|o*World\How:Are"You?&gt;</string>
    </dict>
  """), null)

  val trackLocationString = """<dict><key>Track ID</key><integer>1589</integer>
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

  val trackLocation2String = """<dict><key>Track ID</key><integer>1589</integer>
			<key>Name</key><string>Caught Up In You</string>
			<key>Album Rating</key><integer>60</integer>
			<key>Album Rating Computed</key><true/>
			<key>Persistent ID</key><string>405C983B39AD457A</string>
			<key>Disabled</key><true/>
			<key>Track Type</key><string>File</string>
			<key>Location</key><string>file://localhost//MyServer/Music/38%20Special/Unknown%20Album/Caught%20Up%20In%20You.mp3</string>
			<key>File Folder Count</key><integer>4</integer>
			<key>Library Folder Count</key><integer>1</integer>
      </dict>"""

  val trackLocation3String = """<dict><key>Track ID</key><integer>1589</integer>
			<key>Name</key><string>Caught Up In You</string>
			<key>Album Rating</key><integer>60</integer>
			<key>Album Rating Computed</key><true/>
			<key>Persistent ID</key><string>405C983B39AD457A</string>
			<key>Disabled</key><true/>
			<key>Track Type</key><string>File</string>
			<key>Location</key><string>file://localhost/Users/Me/Music/38%20Special/Unknown%20Album/Caught%20Up%20In%20You.mp3</string>
			<key>File Folder Count</key><integer>4</integer>
			<key>Library Folder Count</key><integer>1</integer>
      </dict>"""
}