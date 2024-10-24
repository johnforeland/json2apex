import mill._
import $ivy.`com.lihaoyi::mill-contrib-playlib:`,  mill.playlib._

object scalagettingstarted extends PlayModule with SingleModule {

  def scalaVersion = "2.10.5"
  def playVersion = "2.1.1"
//  def twirlVersion = "1.5.1"

}