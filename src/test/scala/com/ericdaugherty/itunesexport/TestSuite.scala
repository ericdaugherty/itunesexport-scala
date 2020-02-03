package com.ericdaugherty.itunesexport

import org.scalatest.Suites


/**
 * Test Suite aggregates all iTunesExport test Suites.
 *
 * @author Eric Daugherty
 */
class TestSuite extends Suites (
      new formatter.FormatterTest(),
      new parser.TrackTest(),
      new parser.PlaylistTest(),
      new parser.LibraryTest(),
      new console.ConsoleExportTest()
)