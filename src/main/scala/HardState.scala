import dds._
import org.omg.dds.core.policy.PolicyFactory
import dds.prelude._
import scala.collection.JavaConversions._

/**
  * This class implements the DDS Hard-State Idiom
  */


case class HardState[T: Manifest](name: String, durability: org.omg.dds.core.policy.Durability, history: Int = 1)
                                 (implicit dp: org.omg.dds.domain.DomainParticipant,
                                  sub: org.omg.dds.sub.Subscriber,
                                  pub: org.omg.dds.pub.Publisher,
                                  pf: PolicyFactory) extends State[T]{

  //TODO: Resource Limits should properly be configured.
  lazy val topic: org.omg.dds.topic.Topic[T] = Topic[T](name, HardStateTopic(durability, ResourceLimits(1, 1024, 1024*1024)))
  lazy val writer: org.omg.dds.pub.DataWriter[T] = DataWriter[T](topic, HardStateWriterQos(durability, history))
  lazy val reader: org.omg.dds.sub.DataReader[T] = DataReader[T](topic, HardStateReaderQos(durability, history))


}
