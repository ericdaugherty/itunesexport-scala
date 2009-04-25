package com.ericdaugherty.itunesexport

import org.scalatest.SuperSuite

/**
 * Test Suite aggregates all iTunesExport test Suites.
 *
 * @author Eric Daugherty
 */
class TestSuite extends SuperSuite (
  List (
      new formatter.FormatterTest(),
      new parser.TrackTest(),
      new parser.PlaylistTest(),
      new parser.LibraryTest()
    )
)