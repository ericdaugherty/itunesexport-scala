import sbt._

class ProjectSettings(info: ProjectInfo) extends DefaultProject(info)
{
  override def mainClass = Some("com.ericdaugherty.itunesexport.console.ConsoleExport")
  override def manifestClassPath = Some("scala-library.jar")
  
  override def testOptions = ExcludeTests("com.ericdaugherty.itunesexport.TestSuite" :: Nil) :: super.testOptions.toList

  /* Setup Bindary distribution */
  def srcDistName = "iTunesExportScala-src-" + version + ".zip"
  def srcDistPaths = descendents("src" +++ "docs" +++ "project" / "build" / "src", "*" ) +++ ("project") ** ("*.properties")
  lazy val distSrc = zipTask(srcDistPaths, outputPath, srcDistName) dependsOn(`package`)

  def distName = "iTunesExportScala-" + version + ".zip"
//  def distPaths = ((outputPath ##) / defaultJarName) +++ descendents("docs" ##, "*") +++ scalaJars.foreach(jar => yield path(jar.toString))
  def distPaths = ((outputPath ##) / defaultJarName) +++ descendents("docs" ##, "*") +++ ("project" / "boot" / "scala-2.7.4" / "lib" ##) ** "scala-library.jar"
  lazy val dist = zipTask(distPaths, outputPath, distName) dependsOn(distSrc)


}