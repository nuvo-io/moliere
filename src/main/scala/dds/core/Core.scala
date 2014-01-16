package dds.core

import java.util.concurrent.TimeUnit
import org.omg.dds.core.ServiceEnvironment

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
  def all()(implicit env: ServiceEnvironment) = env.getSPI.allStatusKinds()
  def none()(implicit env: ServiceEnvironment) = env.getSPI.noStatusKinds()
}
