package me.kordun.downloader

import java.io.{InputStream, RandomAccessFile}
import java.net.{URL, HttpURLConnection}

import android.os.Handler
import me.kordun.downloader.Status._

class DownloadRunner(var download: Download) extends Runnable {
  var uiHandler: Handler = null
  //dummy empty callbacks
  var viewUpdate: () => Unit = () => ()
  var detailsViewUpdate: () => Unit = () => ()

  var shouldContinue: Boolean = true
  private val BUFFER_SIZE: Int = 1024


  override def run(): Unit = {
    try {
      val startTime: Long = System.currentTimeMillis()
      var file: RandomAccessFile = null
      var stream: InputStream = null
      val url: URL = new URL(download.url)
      val connection: HttpURLConnection = url.openConnection().asInstanceOf[HttpURLConnection];


      // portion of file to download.
      if(download.downloaded > 0) {
        connection.setRequestProperty("Range",
          "bytes=" + (download.downloaded ) + "-")
      }

      connection.connect

      // Make sure response code is in the 200 range.
      if (connection.getResponseCode / 100 != 2) {
        download.status = Error
        throw new Exception("Wrong response code")
      }

      val sizeOfLastBlock:Int =  connection.getContentLength

      if(download.downloaded == 0) download.size = sizeOfLastBlock
      // Check for valid content length.
      if (sizeOfLastBlock < 1) {
        download.status = Error
        throw new Exception("Wrong size")
      }

      // Open file
      file = new RandomAccessFile(download.fullFilePath, "rw")
      file.seek(download.downloaded)
      stream = connection.getInputStream
      try {
        while (download.status == Downloading && shouldContinue) {

          var buffer: Array[Byte] = null
          if (download.size - download.downloaded > BUFFER_SIZE) {
            buffer = new Array[Byte](BUFFER_SIZE)
          }
          else {
            buffer = new Array[Byte](download.size  - download.downloaded)
          }

          // Read into buffer.
          val read: Int = stream.read(buffer)
          if (read != -1) {
            // Write buffer to file.
            file.write(buffer, 0, read)
            download.downloaded += read
          }


          if (download.downloaded >= download.size) {
            download.status = Complete
          }
          download.elapsed = ((System.currentTimeMillis() - startTime) / 1000).asInstanceOf[Int]
          download.averageSpeed = Math.round(download.downloaded / (download.elapsed + 1))

          updateView()
        }
      } catch {
        case e: Exception => throw new Exception
      }
      finally {
        // Close file.
        if (file != null) {
          try {
            file.close
          }
          catch {
            case e: Exception => {
              e.printStackTrace()
            }
          }
        }

        // Close connection to server.
        if (stream != null) {
          try {
            stream.close();
          } catch {
            case e: Exception => {e.printStackTrace()}
          }
        }
      }
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
      }
        download.status = Error
        updateView()
    }
  }

  def setViewCallback(callback: () => Unit): Unit = {
    viewUpdate = callback
  }

  def setDetailsViewCallback(callback: () => Unit): Unit = {
    detailsViewUpdate = callback
  }

  def updateView(): Unit = {
    viewUpdate()
    detailsViewUpdate()
  }

  def pause() {
    download.status = Paused
  }

  def cancel(): Unit = {
    download.status = Cancelled
  }

  def resume() {
    shouldContinue = true
    download.status = Downloading
    new Thread(this).start()

  }

  def start(): Unit = {
    resume()
  }

  def stop() = {
    shouldContinue = false
  }


}
