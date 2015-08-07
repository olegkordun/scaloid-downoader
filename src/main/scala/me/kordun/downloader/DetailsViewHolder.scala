package me.kordun.downloader

import java.io.File
import Status._
import android.graphics.{BitmapFactory, Bitmap}
import android.widget.{ImageView, TextView}
import android.view.View._

class DetailsViewHolder extends ViewHolder {

  var URL: TextView = null
  var avgSpeed: TextView = null
  var elapsed: TextView = null
  var size: TextView = null
  var downloaded: TextView = null
  var imgPreview:ImageView = null
  override def update(download: Download): Unit = {
    super.update(download)
    URL.setText(download.url)

    avgSpeed.setText(download.averageSpeed.toString)
    elapsed.setText(download.elapsed.toString)
    size.setText(download.size.toString)
    downloaded.setText(download.downloaded.toString)
    if(download.status == Complete)
    {
       try{
         val imgFile:File = new  File(download.fullFilePath);

         if(imgFile.exists()){
           val myBitmap:Bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath);
           imgPreview.setImageBitmap(myBitmap);
         }
         imgPreview.setVisibility(VISIBLE)
       }
      catch {case e:Exception =>{}}
    }
  }
}