package me.kordun.downloader

import android.view.View
import android.view.View._
import android.widget.{Button, ProgressBar, TextView}
import android.graphics.Color
import android.graphics.PorterDuff.Mode

class ViewHolder {
  var fileName: TextView = null
  var statusLabel: TextView = null
  var progressBar: ProgressBar = null
  var pause: Button = null
  var resume: Button = null
  var cancel: Button = null

  def update(download: Download) {
    fileName.setText(download.fileName)
    statusLabel.setText(download.status.toString)
    progressBar.setMax(download.size)
    progressBar.setProgress(download.downloaded)


    val status = download.status
    status match {
      case Status.Downloading => {
        pause.setVisibility(VISIBLE)
        resume.setVisibility(INVISIBLE)
        progressBar.getProgressDrawable.setColorFilter(Color.BLUE, Mode.SRC_IN)
      }
      case Status.Paused => {
        pause.setVisibility(INVISIBLE)
        resume.setVisibility(VISIBLE)
        progressBar.getProgressDrawable().setColorFilter(Color.GRAY, Mode.SRC_IN)
      }
      case Status.Complete => {
        pause.setVisibility(VISIBLE)
        pause.setEnabled(false)
        resume.setVisibility(INVISIBLE)
        cancel.setEnabled(false)
        progressBar.getProgressDrawable().setColorFilter(Color.GREEN, Mode.SRC_IN)
      }
      case Status.Error => {
        pause.setVisibility(VISIBLE)
        pause.setEnabled(false)
        resume.setVisibility(INVISIBLE)
        cancel.setEnabled(false)
        progressBar.getProgressDrawable().setColorFilter(Color.RED, Mode.SRC_IN)
      }
      case Status.Cancelled => {
        pause.setVisibility(VISIBLE)
        pause.setEnabled(false)
        resume.setVisibility(INVISIBLE)
        cancel.setEnabled(false)
        progressBar.getProgressDrawable().setColorFilter(Color.DKGRAY, Mode.SRC_IN)
      }
    }

  }

  def setButtonListeners(downloadTask: DownloadRunner): Unit = {
    pause.setOnClickListener(new OnClickListener() {
      def onClick(v: View) {
        downloadTask.pause()
        update(downloadTask.download)
      }
    })
    resume.setOnClickListener(new OnClickListener() {
      def onClick(v: View) {
        downloadTask.resume()
        update(downloadTask.download)
      }
    })
    cancel.setOnClickListener(new OnClickListener() {
      def onClick(v: View) {
        downloadTask.cancel()
        update(downloadTask.download)
      }
    })
  }

}
