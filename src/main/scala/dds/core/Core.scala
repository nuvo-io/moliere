package dds.core

import java.util.concurrent.TimeUnit


object Duration {
  import org.omg.dds.core.ServiceEnvironment
  def apply(d: Long, unit: TimeUnit)(implicit env: ServiceEnvironment) = env.getSPI.newDuration(d, unit)
  def zero()(implicit env: ServiceEnvironment) = env.getSPI.zeroDuration()
  def infinite()(implicit env: ServiceEnvironment) = env.getSPI.infiniteDuration()
}

object Time {
  import org.omg.dds.core.ServiceEnvironment
  def apply(d: Long, unit: TimeUnit)(implicit env: ServiceEnvironment) = env.getSPI.newTime(d, unit)
  def invalid()(implicit env: ServiceEnvironment) = env.getSPI.invalidTime()
}

object WaitSet {
  import org.omg.dds.core.ServiceEnvironment
  def apply()(implicit env: ServiceEnvironment) = env.getSPI.newWaitSet()
}

