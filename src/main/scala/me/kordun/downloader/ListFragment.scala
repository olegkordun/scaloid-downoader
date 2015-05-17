package me.kordun.downloader


import android.app.{Activity, Fragment}


import android.os.Bundle

import android.view.View.OnClickListener
import android.view.{LayoutInflater, ViewGroup, View}
import android.widget._

class ListFragment extends Fragment with OnClickListener {
  var manager = DownloadManager
  private var listView: ListView = null
  private var urlField: EditText = null
  private var startBtn: Button = null


  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    println("onCreateView")
    val view: View = inflater.inflate(R.layout.list_fragment, container, true)

    listView = view.findViewById(R.id.list).asInstanceOf[ListView]
    listView.setAdapter(new ViewArrayAdapter(getActivity , manager.downloadRunners))
    listView.setOnItemClickListener(getActivity.asInstanceOf[MainActivity])

    urlField = view.findViewById(R.id.url).asInstanceOf[EditText]

    startBtn = view.findViewById(R.id.startBtn).asInstanceOf[Button]
    startBtn.setOnClickListener(this)
    view
  }

  override def onClick(v: View): Unit = {
    val url: String = urlField.getText.toString

    if ((url startsWith "http:") || (url startsWith "https:")) {
      val d:Download = new Download(url)
      manager.addRunner(new DownloadRunner(d))

      urlField.setText("")
    }
    else {
      urlField.setError(getResources.getString(R.string.wrong_url))
    }
  }


}
