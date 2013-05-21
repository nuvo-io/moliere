package dds


object Status {
  import org.omg.dds.core.ServiceEnvironment

  def AllStatus(implicit env: ServiceEnvironment) = env.getSPI.allStatusKinds()

  def NoStatus(implicit env: ServiceEnvironment) = env.getSPI.noStatusKinds()
}
