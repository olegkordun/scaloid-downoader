package me.kordun.downloader

object Status extends Enumeration {
  type Status = Value
  val Started, Downloading, Complete, Error, Paused, Cancelled = Value
}
