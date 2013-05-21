package dds


abstract class Event

abstract class ReaderEvent extends Event

case class DataAvailable[T](reader: org.omg.dds.sub.DataReader[T]) extends ReaderEvent



