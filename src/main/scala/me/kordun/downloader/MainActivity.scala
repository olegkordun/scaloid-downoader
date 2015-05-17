package me.kordun.downloader

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view._
import android.widget.AdapterView.OnItemClickListener
import android.widget._


class MainActivity extends FragmentActivity with OnItemClickListener {
  var manager = DownloadManager

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
  }

  override def onSaveInstanceState(savedInstanceState: Bundle) {
    manager.saveDownloads(savedInstanceState)
    super.onSaveInstanceState(savedInstanceState)

  }

  override def onRestoreInstanceState(savedInstanceState: Bundle): Unit = {
    manager.restoreDownloads(savedInstanceState)
    super.onRestoreInstanceState(savedInstanceState)
  }

  override def onItemClick(parent: AdapterView[_], view: View, position: Int, id: Long): Unit =
  {
    val i: Intent = new Intent(this, classOf[DetailsActivity])
    i.putExtra("position", position)
    startActivity(i)
  }
}
