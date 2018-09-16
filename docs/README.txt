iTunesExport Console Application
http://www.ericdaugherty.com/dev/itunesexport/

Usage

iTunes Export is a command line application that uses the Java Runtime 
Environment (JRE) to execute.  To use it, open a command window to the 
directory where iTunes Export is installed and execute the following command:

java -jar itunesexport.jar

If iTunes Export is run without any parameters, it will attempt to locate
your iTunes Music Library and will write a .m3u playlist file for each
playlist defined in the iTunes Music Library.

The location of the iTunes Music Library can be overridden using a command
line parameter.  Example:

java -jar itunesexport.jar "C:\My Music\iTunes Music Library.xml"

iTunesExport supports a variety of command line parameters.  Use the -? or -h 
parameters to view the available options.  Example:

java -jar itunesexport.jar -h
