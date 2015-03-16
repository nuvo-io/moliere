package dds

import org.omg.dds.core.ServiceEnvironment
import org.omg.dds.domain.DomainParticipantFactory

package object config {

  val ospl = "ospl"
  val cafe = "cafe"
  val dds = ospl

  val osplProvider = "org.opensplice.dds.core.OsplServiceEnvironment"
  val cafeProvider = "com.prismtech.cafe.core.ServiceEnvironmentImpl"

  val provider = if (dds == ospl) osplProvider else cafe

  def setupEnv() = {
    System.setProperty(ServiceEnvironment.IMPLEMENTATION_CLASS_NAME_PROPERTY,
      provider);
    ServiceEnvironment.createInstance(Thread.currentThread().getContextClassLoader)

  }
  lazy implicit val env =setupEnv()

  object DefaultEntities {
    private def initDP() = {
      val dpf = DomainParticipantFactory.getInstance(env)
      dpf.createParticipant()
    }
    implicit lazy val defaultDomainParticipant = initDP()
    implicit lazy val defaultPub = defaultDomainParticipant .createPublisher()
    implicit lazy val defaultSub = defaultDomainParticipant .createSubscriber()
    implicit lazy val defaultPolicyFactory = env.getSPI.getPolicyFactory()
  }
}
