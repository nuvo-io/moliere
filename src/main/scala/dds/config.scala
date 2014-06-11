package dds

import org.omg.dds.core.ServiceEnvironment
import org.omg.dds.domain.DomainParticipantFactory

package object config {

  val provider = "org.opensplice.osplj.core.ServiceEnvironmentImpl"
  def setupEnv() = {
    System.setProperty(ServiceEnvironment.IMPLEMENTATION_CLASS_NAME_PROPERTY,
      provider);
    ServiceEnvironment.createInstance(Thread.currentThread().getContextClassLoader)

  }
  lazy implicit val env =setupEnv()

  val defaultDomain = 0

  object DefaultEntities {
    private def initDP() = {
      val dpf = DomainParticipantFactory.getInstance(env)
      dpf.createParticipant(defaultDomain)
    }
    implicit lazy val defaultDomainParticipant = initDP()
    implicit lazy val defaultPub = defaultDomainParticipant .createPublisher()
    implicit lazy val defaultSub = defaultDomainParticipant .createSubscriber()
    implicit lazy val defaultPolicyFactory = env.getSPI.getPolicyFactory()
  }
}
