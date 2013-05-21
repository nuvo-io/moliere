package dds


object Subscriber {
  import org.omg.dds.domain.DomainParticipant
  import org.omg.dds.sub.SubscriberQos

  def apply(dp: DomainParticipant, qos: SubscriberQos) = dp.createSubscriber(qos)

  def apply(implicit dp: DomainParticipant) = dp.createSubscriber()

  def apply(qos: SubscriberQos)(implicit dp: DomainParticipant) = dp.createSubscriber(qos)

}
