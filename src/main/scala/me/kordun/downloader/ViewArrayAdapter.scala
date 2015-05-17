package me.kordun.downloader


import java.util.ArrayList
import java.util.concurrent.ExecutorService

import android.os.{Looper, HandlerThread, Handler}
import android.view.View._
import Status._

import android.content.Context
import android.view.{ViewGroup, View, LayoutInflater}
import android.widget.{Button, ProgressBar, TextView, ArrayAdapter}


class ViewArrayAdapter(context: Context, objects: ArrayList[DownloadRunner])
  extends ArrayAdapter[DownloadRunner](context, android.R.layout.list_content, objects) {

  override def getView(position: Int, convertView: View, parent: ViewGroup): View = {
    var row: View = convertView
    val downloadRunner: DownloadRunner = getItem(position)
    var view: ViewHolder = null

    if (row == null) {
      val inflater: LayoutInflater = getContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
      row = inflater.inflate(R.layout.row, parent, false)

      view = new ViewHolder
      view.fileName = row.findViewById(R.id.downloadFileName).asInstanceOf[TextView]
      view.statusLabel = row.findViewById(R.id.status).asInstanceOf[TextView]
      view.progressBar = row.findViewById(R.id.downloadProgressBar).asInstanceOf[ProgressBar]
      view.pause = row.findViewById(R.id.pauseButton).asInstanceOf[Button]
      view.resume = row.findViewById(R.id.resumeButton).asInstanceOf[Button]
      view.cancel = row.findViewById(R.id.cancelButton).asInstanceOf[Button]

      row.setTag(view)
    }
    else {
      view = row.getTag.asInstanceOf[ViewHolder]
    }
    downloadRunner.setViewCallback(() => {
      viewCallback(downloadRunner, view)
    })

    view.update(downloadRunner.download)
    view.setButtonListeners(downloadRunner)

    row
  }

  def viewCallback(runner: DownloadRunner, viewHolder: ViewHolder): Unit = {
    if (runner.uiHandler == null || viewHolder == null) return
    runner.uiHandler.postDelayed(new Runnable() {
      override def run(): Unit = {
        viewHolder.update(runner.download)
      }
    }, 500)
  }
}



