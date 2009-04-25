package com.ericdaugherty.itunesexport.parser

import xml.Node
import collection.immutable.Map
import collection.mutable.HashMap

import java.util.Date
import java.text.SimpleDateFormat

/** 
 * This trait provide the ability to parse PList XML into key/value pairs.  It also provide conviennce
 * methods to access the values in a typesafe manor.
 * 
 * To use pass the node containing the key, integer, string, date, true and false xml tags to parsePList.
 * 
 * Then define a concrete implementation of the helper method in the subclass:
 * 
 * def getPListValue(key: String, default: Any): Any = getPListValue(pListMap, key, default)
 * 
 * @author Eric Daugherty
 */
trait PListParser {

  /** Performs pattern matching on the XML to produce a key value map of the values */
  def parsePList(parentNode: Node) : Map[String,Any] = {
    var key = "";
    var map = new HashMap[String,Any]()
    for(node <- parentNode.child) {
      node match {         
        case <key>{contents}</key> => key = contents.toString
        case <integer>{contents}</integer> => map += (key -> contents.text.toDouble)
        case <string>{contents}</string> => map += (key -> contents.text.toString)
        case <date>{contents}</date> => map += (key -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(contents.text)) //TODO: I think this ignores the time zone.
        case <true/> => map += (key -> true)
        case <false/> => map += (key -> false)
        case _ => 
      }
    }
    // Convert to Immutable map and return
    Map() ++ map
  }

  /** Abstract method the subclass needs to implement */
    def getPListValue(key: String, default: Any) : Any

  /** Type generic way to get values.  Only works if you pass a default, not sure how to handle default values in this mannor. */
  def getValue[T](key: String, default: T) : T = getPListValue(key, default).asInstanceOf[T]
  
  /** Returns the value if the key exists or the default otherwise. */
  def getPListValue(map: Map[String, Any], key: String, default: Any) = {     
    if (map.contains(key)) map(key) else default    
  } 

  // Type Safe helper methods with default values
  // TODO: Can these be eliminated to use a generic getValue that provides a default of the right type?

  def getPListBoolean(key: String) : Boolean = getValue(key, true)
  
  def getPListDate(key: String) : Date = getValue(key, new Date())
  
  def getPListDouble(key: String) : Double = getValue(key, 0.asInstanceOf[Double])
  
  def getPListString(key: String) : String = getValue(key, "")
}
