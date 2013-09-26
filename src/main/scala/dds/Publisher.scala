package dds


object Publisher {
  import org.omg.dds.domain.DomainParticipant
  import org.omg.dds.pub.PublisherQos

  def apply(dp: DomainParticipant, qos: PublisherQos) =  dp.createPublisher(qos)

  def apply()(implicit dp: DomainParticipant) = dp.createPublisher()

  def apply(qos: PublisherQos)(implicit dp: DomainParticipant) = dp.createPublisher(qos)

}
