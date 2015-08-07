package me.kordun.downloader

import java.util
import android.os.{Environment, Looper, Handler, Bundle}
import scala.collection.JavaConverters._
import Status._

object DownloadManager {
  private val DOWNLOADS_KEY = "DOWNLOADS_KEY"
  val path:String = Environment.getExternalStoragePublicDirectory(
    Environment.DIRECTORY_DOWNLOADS).getPath

  var downloadRunners: util.ArrayList[DownloadRunner] = new util.ArrayList[DownloadRunner]

  def restoreDownloads(savedInstanceState: Bundle): Unit = {
    if (savedInstanceState == null) return

    var storedDownloads: List[Download] = List.empty[Download]
    val savedArray = savedInstanceState.getSerializable(DOWNLOADS_KEY)
    if (savedArray != null) {
      storedDownloads = savedArray.asInstanceOf[List[Download]]
    }

    if (storedDownloads.isEmpty) {
      restoreFromStorage()
    }
    downloadRunners.clear
    downloadRunners.addAll(restoreRunners(storedDownloads))
  }

  def restoreRunners(list: List[Download]): util.ArrayList[DownloadRunner] = {
    new util.ArrayList(
      list.map(x => new DownloadRunner(x) {
        this.uiHandler = new Handler(Looper.getMainLooper)
        if (this.download.status == Downloading) this.resume()
      }).asJava)
  }

  def saveDownloads(bundle: Bundle) = {
    bundle.putSerializable(DOWNLOADS_KEY, downloadRunners.asScala.map(x => x.download).toList)
  }

  def stopRunners(): Unit = {
    downloadRunners.asScala.foreach(x => {
      if (x.download.status == Downloading) x.stop()
    })
  }

  def addRunner(runner: DownloadRunner) = {
    runner.uiHandler = new Handler(Looper.getMainLooper)
    downloadRunners.add(runner)
    runner.start()
  }

  def restoreFromStorage() = {
    //TODO:resore from SQLite
  }
}
