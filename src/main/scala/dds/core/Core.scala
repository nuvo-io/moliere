package dds.core

import java.util
import java.util.concurrent.TimeUnit
import org.omg.dds.core.ServiceEnvironment
import org.omg.dds.core.status._

object Duration {
  def apply(d: Long, unit: TimeUnit)(implicit env: ServiceEnvironment) = env.getSPI.newDuration(d, unit)
  def zero()(implicit env: ServiceEnvironment) = env.getSPI.zeroDuration()
  def infinite()(implicit env: ServiceEnvironment) = env.getSPI.infiniteDuration()
}

object Time {
  def apply(d: Long, unit: TimeUnit)(implicit env: ServiceEnvironment) = env.getSPI.newTime(d, unit)
  def invalid()(implicit env: ServiceEnvironment) = env.getSPI.invalidTime()
}

object WaitSet {

  def apply()(implicit env: ServiceEnvironment) = env.getSPI.newWaitSet()
}

object Status {

  val all = List[Class[_ <: Status]](
    classOf[DataAvailableStatus],
    classOf[SubscriptionMatchedStatus],
    classOf[LivelinessChangedStatus],
    classOf[RequestedDeadlineMissedStatus],
    classOf[OfferedDeadlineMissedStatus],
    classOf[RequestedDeadlineMissedStatus]
  )
  val none = List[Class[_ <: Status]]()
}
