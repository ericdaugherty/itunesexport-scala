package com.ericdaugherty.itunesexport.formatter

import java.io.File

/**
 * Contains the settings used to export a Library's playlist.
 *
 * @author Eric Daugherty
 */
abstract class FormatterSettings {
  val outputDirectory : File
  val musicPath : String
  val musicPathOld : String
  val includeUTFBOM : Boolean
  val useM3U8 : Boolean
  val fileType : String
  val includeDisabled : Boolean
  val copy : String
  val addIndex : Boolean
  val separator : String
}