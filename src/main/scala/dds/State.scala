package dds

import dds.prelude._
import scala.collection.JavaConverters._

trait AbstractState[T] {

  def listen(fun: T => Any): Int

  def deaf(lid: Int): Unit

  def read(): List[T]

  def write(t: T): Unit

  def write(ts: List[T]): Unit

  def close(): Unit

  def map[Q](in: T => Q, out: Q => T): AbstractState[Q]
}


class StateMappedCons[A,B](val inject: A => B, val eject: B => A, val next: AbstractState[A]) extends AbstractState[B] {

  override def listen(fun: (B) => Any): Int = {
    val c = (x: A) => fun(inject(x))
    next.listen(c)
  }

  override def deaf(lid: Int): Unit = next.deaf(lid)

  override def read(): List[B] = next.read().map(inject)

  override def write(t: B): Unit = next.write(eject(t))

  override def write(ts: List[B]): Unit = ts.map(eject).foreach(next.write)

  override def close(): Unit = next.close()

  override def map[C](in: B => C, out: C => B): StateMappedCons[B, C] = new StateMappedCons(in, out, this)

}

/**
  * Created by veda on 22/12/2016.
  */
trait State[T] extends AbstractState[T] {

  val topic: org.omg.dds.topic.Topic[T]
  val writer: org.omg.dds.pub.DataWriter[T]
  val reader: org.omg.dds.sub.DataReader[T]

  override def listen(fun: T => Any): Int = reader.listen {
    case DataAvailable(_) => reader.read().asScala.filter(_.getData != null).map(s => fun(s.getData))
  }

  override def deaf(lid: Int): Unit = reader.deaf(lid)

  def read(): List[T] = this.reader.select().dataState(DataState.allData).read().asScala.filter(_.getData != null).map(_.getData).toList

  def write(t: T): Unit = this.writer.write(t)

  def write(ts: List[T]): Unit = ts.foreach(this.writer.write)

  def close(): Unit = {
    this.topic.close()
    this.writer.close()
    this.reader.close()
  }

  def map[Q](in: T => Q, out: Q => T): AbstractState[Q] = new StateMappedCons(in, out, this)

}
