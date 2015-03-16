package dds

import org.omg.dds.`type`.TypeSupport


object Topic {
  import org.omg.dds.domain.DomainParticipant
  import org.omg.dds.topic.TopicQos
  import scala.collection.JavaConversions._

  def apply[T: Manifest](dp: DomainParticipant, name: String, regTypeName: String): org.omg.dds.topic.Topic[T] = {
    val env = dp.getEnvironment
    val cls = manifest[T].runtimeClass
    val ts = TypeSupport.newTypeSupport(cls, regTypeName, env)
    dp.createTopic(name, ts).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }

  def apply[T: Manifest](dp: DomainParticipant, name: String): org.omg.dds.topic.Topic[T] = {
    dp.createTopic(name, manifest[T].runtimeClass).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }

  def apply[T](name: String, regTypeName: String)(implicit m: Manifest[T], dp: DomainParticipant): org.omg.dds.topic.Topic[T] = {
    val env = dp.getEnvironment
    val cls = manifest[T].runtimeClass
    val ts = TypeSupport.newTypeSupport(cls, regTypeName, env)
    dp.createTopic(name, ts).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }

  def apply[T](name: String)(implicit m: Manifest[T], dp: DomainParticipant): org.omg.dds.topic.Topic[T] = {
    dp.createTopic(name, manifest[T].runtimeClass).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }

  def apply[T: Manifest](dp: DomainParticipant, name: String, regTypeName: String, qos: TopicQos): org.omg.dds.topic.Topic[T] = {
    val env = dp.getEnvironment
    val cls = manifest[T].runtimeClass
    val ts = TypeSupport.newTypeSupport(cls, regTypeName, env)
    dp.createTopic(name, ts, qos, null, List()).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }

  def apply[T: Manifest](dp: DomainParticipant, name: String, qos: TopicQos): org.omg.dds.topic.Topic[T] = {
    dp.createTopic(name, manifest[T].runtimeClass, qos, null, List()).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }

  def apply[T](name: String, regTypeName: String, qos: TopicQos)(implicit m: Manifest[T], dp: DomainParticipant): org.omg.dds.topic.Topic[T] = {
    val env = dp.getEnvironment
    val cls = manifest[T].runtimeClass
    val ts = TypeSupport.newTypeSupport(cls, regTypeName, env)
    dp.createTopic(name, ts, qos, null, List()).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }

  def apply[T](name: String, qos: TopicQos)(implicit m: Manifest[T], dp: DomainParticipant): org.omg.dds.topic.Topic[T] = {
    dp.createTopic(name, manifest[T].runtimeClass, qos, null, List()).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }
}
