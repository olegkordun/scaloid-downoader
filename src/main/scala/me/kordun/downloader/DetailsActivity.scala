package me.kordun.downloader

import android.app.{FragmentTransaction, Activity, Fragment}
import android.os.Bundle
import android.view.{View, ViewGroup, LayoutInflater, Menu}
import android.widget.{Button, ListView, EditText, TextView}

class DetailsActivity extends Activity{

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.details_fragment)

    val i:Int = getIntent getIntExtra("position",0)

    val bundle:Bundle = new Bundle()
    bundle.putInt("position", i);

    val details:Fragment = new DetailsFragment()
    details.setArguments(bundle)

    val ft: FragmentTransaction = getFragmentManager.beginTransaction()
    ft.replace(R.id.DetailContainer, details)
    ft.commit
  }
}