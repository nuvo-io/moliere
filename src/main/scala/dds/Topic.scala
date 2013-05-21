package dds


object Topic {
  import org.omg.dds.domain.DomainParticipant
  import org.omg.dds.topic.TopicQos
  import scala.collection.JavaConversions._


  def apply[T: Manifest](dp: DomainParticipant, name: String): org.omg.dds.topic.Topic[T] = {
    dp.createTopic(name, manifest[T].runtimeClass).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }

  def apply[T](name: String)(implicit m: Manifest[T], dp: DomainParticipant): org.omg.dds.topic.Topic[T] = {
    dp.createTopic(name, manifest[T].runtimeClass).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }

  def apply[T: Manifest](dp: DomainParticipant, name: String, qos: TopicQos): org.omg.dds.topic.Topic[T] = {
    dp.createTopic(name, manifest[T].runtimeClass, qos, null, List()).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }

  def apply[T](name: String, qos: TopicQos)(implicit m: Manifest[T], dp: DomainParticipant): org.omg.dds.topic.Topic[T] = {
    dp.createTopic(name, manifest[T].runtimeClass, qos, null, List()).asInstanceOf[org.omg.dds.topic.Topic[T]]
  }
}
