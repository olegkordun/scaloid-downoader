package me.kordun.downloader

import me.kordun.downloader.Status._
import scala.util.Random

class Download extends Serializable {
  //serializable class for saving and recovering
  var url: String = ""
  var size = 0
  var downloaded = 0
  var status: Status = Started
  var averageSpeed = 0
  var fileName = ""
  var fullFilePath = ""
  var elapsed = 0

  def this(url:String) {
    this()
    this.url = url
    this.fileName =  getFileName(url)
    this.fullFilePath = DownloadManager.path +"/" + fileName

  }

  private def getFileName(url: String): String = {
    Random.nextPrintableChar() + Random.nextPrintableChar() + url.substring(url.lastIndexOf('/') + 1)
  }
}