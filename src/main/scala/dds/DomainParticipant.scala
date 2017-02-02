package dds

import org.omg.dds.domain.{DomainParticipantQos, DomainParticipantFactory}
import org.omg.dds.core.ServiceEnvironment
import scala.collection.JavaConverters._

object DomainParticipant {
  def apply(domainId: Int)(implicit env: ServiceEnvironment) = {
    val dpf = DomainParticipantFactory.getInstance(env)
    dpf.createParticipant(domainId)
  }

  def apply(domainId: Int, qos: DomainParticipantQos)(implicit env: ServiceEnvironment) = {
    val dpf = DomainParticipantFactory.getInstance(env)

    dpf.createParticipant(domainId, qos, null, dds.core.Status.none.asJava)
  }
}
