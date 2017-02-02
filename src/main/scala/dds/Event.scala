package dds

import org.omg.dds.core.policy.PolicyFactory
import dds.prelude._
import scala.collection.JavaConverters._

trait AbstractEvent[T] {
  def listen(fun: T => Any): Int

  def deaf(lid: Int): Unit

  def take(): List[T]

  def write(t: T): Unit

  def write(ts: List[T]): Unit

  def close(): Unit

  def map[Q](in: T => Q, out: Q => T): AbstractEvent[Q]

}

class MappedEventCons[T, Q](val inject: T => Q, val eject: Q => T, val next: AbstractEvent[T])  extends AbstractEvent[Q] {

  override def listen(fun: Q => Any): Int = {
    val c  = (t: T) => fun(inject(t))
    next.listen(c)
  }

  override def deaf(lid: Int): Unit = next.deaf(lid)

  override def take(): List[Q] = next.take().map(inject)

  override def write(t: Q): Unit = next.write(eject(t))

  override def write(ts: List[Q]): Unit = ts.map(eject).foreach(next.write)

  override def close(): Unit = next.close()

  override def map[R](in: Q => R, out: R => Q): AbstractEvent[R] = new MappedEventCons[Q, R](in, out, this)

}

/**
  * This class implements the Event DDS Idiom
  */
case class Event[T: Manifest](name: String, durability: org.omg.dds.core.policy.Durability)
                             (implicit dp: org.omg.dds.domain.DomainParticipant,
                              sub: org.omg.dds.sub.Subscriber,
                              pub: org.omg.dds.pub.Publisher,
                              pf: PolicyFactory
                             ) {

  //TODO: Resource Limits should properly be configured.
  lazy val topic: org.omg.dds.topic.Topic[T] = Topic[T](name, HardStateTopic(durability, ResourceLimits(1, 1024, 1024*1024)))
  lazy val writer: org.omg.dds.pub.DataWriter[T] = DataWriter[T](topic, EventWriterQos(durability))
  lazy val reader: org.omg.dds.sub.DataReader[T] = DataReader[T](topic, EventReaderQos(durability))

  def listen(fun: T => Any): Int = reader.listen {
    case DataAvailable(_) => reader.select().dataState(DataState.allData).take.asScala.filter(_.getData != null).map(s => fun(s.getData))
  }

  def deaf(lid: Int): Unit = reader.deaf(lid)

  def take(): List[T] = this.reader.select().dataState(DataState.allData).take().asScala.map(_.getData).toList

  def write(t: T): Unit = this.writer.write(t)

  def write(ts: List[T]): Unit = ts.foreach(this.writer.write)

  def close(): Unit = {
    topic.close()
    writer.close()
    reader.close()
  }
}

