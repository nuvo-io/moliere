package dds

import org.omg.dds.core.policy.PolicyFactory
import dds.prelude._
import scala.collection.JavaConversions._

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

  def listen(fun: PartialFunction[Any, T]): Int = reader.listen(fun)

  def deaf(lid: Int): Unit = reader.deaf(lid)


  def take(): List[T] = this.reader.select().dataState(DataState.allData).take().map(_.getData).toList

  def write(t: T): Unit = this.writer.write(t)

  def write(ts: List[T]): Unit = ts.foreach(this.writer.write)

}

