package dds



object TopicQos {
  import org.omg.dds.domain.DomainParticipant
  def apply()(implicit dp: DomainParticipant) = dp.getDefaultTopicQos()
}


object PublisherQos {
  import org.omg.dds.domain.DomainParticipant
  def apply()(implicit dp: DomainParticipant) = dp.getDefaultPublisherQos()
}

object SubscriberQos {
  import org.omg.dds.domain.DomainParticipant
  def apply()(implicit dp: DomainParticipant) = dp.getDefaultSubscriberQos()
}

object DataReaderQos {
  import org.omg.dds.sub.Subscriber
  def apply()(implicit sub: Subscriber) = sub.getDefaultDataReaderQos()
}

object DataWriterQos {
  import org.omg.dds.pub.Publisher
  def apply()(implicit pub: Publisher) = pub.getDefaultDataWriterQos()
}