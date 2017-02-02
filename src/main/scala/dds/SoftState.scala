package dds

import org.omg.dds.core.policy.PolicyFactory
import dds.prelude._
import scala.collection.JavaConverters._
/**
  * This class implements the DDS Soft State Idiom.
  *
  */
case class SoftState[T: Manifest](name: String, history: Int = 1)
                                 (implicit dp: org.omg.dds.domain.DomainParticipant,
                                  sub: org.omg.dds.sub.Subscriber,
                                  pub: org.omg.dds.pub.Publisher,
                                  pf: PolicyFactory) extends State[T] { outer =>

  lazy val topic: org.omg.dds.topic.Topic[T] = Topic[T](name)
  lazy val writer: org.omg.dds.pub.DataWriter[T] = DataWriter[T](topic, SoftStateWriterQos(history))
  lazy val reader: org.omg.dds.sub.DataReader[T] = DataReader[T](topic, SoftStateReaderQos(history))



}