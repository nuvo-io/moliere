package dds

import org.omg.dds.core.ServiceEnvironment
import org.omg.dds.domain.DomainParticipantFactory
import org.omg.dds.sub.InstanceState

package object config {

  val SERVICE_ENVIRONMENT = "dds.service.environment"
  val DDS_RUNTIME = "dds.runtime"
  val CAFE_SERVICE_ENV = "com.prismtech.cafe.core.ServiceEnvironmentImpl"
  val OSPL_SERVICE_ENV = "org.opensplice.dds.core.OsplServiceEnvironment"
  val CAFE = "cafe"
  val OSPL = "ospl"

  val dds = {
    val ddsRuntime = System.getProperty(DDS_RUNTIME)
    if (ddsRuntime == null) CAFE else ddsRuntime
  }

  val serviceEnvironment: String = {
    val svcEnv = System.getProperty(SERVICE_ENVIRONMENT)
    if (svcEnv == null) {
      if (dds ==  CAFE) CAFE_SERVICE_ENV
      else if (dds == OSPL) OSPL_SERVICE_ENV
      else throw new RuntimeException("You need to select the DDS runtime via dds.runtime=(cafe|ospl) " +
        "or provide the service environment via the dds.service.environment property")
    } else svcEnv
  }

  val provider = serviceEnvironment

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

  object DefaultValues {
    lazy val liveSampleStatus = {
      val s = DefaultEntities.defaultSub.createDataState()
      s.withAnySampleState().withAnyViewState().`with`(InstanceState.ALIVE)
      s
    }
  }
}
