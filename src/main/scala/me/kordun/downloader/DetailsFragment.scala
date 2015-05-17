package me.kordun.downloader

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget._



class DetailsFragment() extends Fragment {
  private var itemIndex: Int = 0

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

  }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    itemIndex = getArguments().getInt("position")
    val runner: DownloadRunner = DownloadManager.downloadRunners.get(itemIndex)

    var details: View = new View(getActivity)
    details = inflater.inflate(R.layout.details_fragment, container, false);


    val view: DetailsViewHolder = new DetailsViewHolder()
    view.fileName = details.findViewById(R.id.downloadFileName).asInstanceOf[TextView]
    view.statusLabel = details.findViewById(R.id.status).asInstanceOf[TextView]
    view.URL = details.findViewById(R.id.url).asInstanceOf[TextView]

    view.elapsed = details.findViewById(R.id.elapsed).asInstanceOf[TextView]
    view.downloaded = details.findViewById(R.id.downloaded).asInstanceOf[TextView]
    view.avgSpeed = details.findViewById(R.id.avgSpeed).asInstanceOf[TextView]
    view.size = details.findViewById(R.id.size).asInstanceOf[TextView]
    view.imgPreview = details.findViewById(R.id.imageView).asInstanceOf[ImageView]

    view.progressBar = details.findViewById(R.id.downloadProgressBar).asInstanceOf[ProgressBar]
    view.pause = details.findViewById(R.id.pauseButton).asInstanceOf[Button]
    view.resume = details.findViewById(R.id.resumeButton).asInstanceOf[Button]
    view.cancel = details.findViewById(R.id.cancelButton).asInstanceOf[Button]
    view.setButtonListeners(runner)
    view.update(runner.download)

    runner.setDetailsViewCallback(() => {
      viewCallback(runner, view)
    })

    details
  }

  def viewCallback(runner: DownloadRunner, viewHolder: DetailsViewHolder): Unit = {
    if (runner.uiHandler == null || viewHolder == null) return

    runner.uiHandler.postDelayed(new Runnable() {
      override def run(): Unit = {
        viewHolder.update(runner.download)
      }
    }, 100)
  }
}
