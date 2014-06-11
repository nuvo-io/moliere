package dds

import org.omg.dds.core.policy.PolicyFactory
import scala.collection.JavaConversions._
import java.util.concurrent.TimeUnit

object Partition {
  def apply(name: String)(implicit pf: PolicyFactory) = pf.Partition().withName(name)
  def apply(names: List[String])(implicit pf: PolicyFactory) = pf.Partition().withName(names)
}

object Reliability {
  def BestEffort(implicit pf: PolicyFactory) = pf.Reliability().withBestEffort()
  def Reliable(implicit pf: PolicyFactory) = pf.Reliability().withReliable()
}

object Durability {
  def Volatile(implicit pf: PolicyFactory) = pf.Durability().withVolatile()
  def TransientLocal(implicit pf: PolicyFactory) = pf.Durability().withTransientLocal()
  def Transient(implicit pf: PolicyFactory) = pf.Durability().withTransient()
  def Persistent(implicit pf: PolicyFactory) = pf.Durability().withPersitent()
}

object History {
  def KeepLast(depth: Int)(implicit pf: PolicyFactory) = pf.History().withKeepLast(depth)
  def KeepAll()(implicit pf: PolicyFactory) = pf.History().withKeepAll()
}

object TimeBasedFilter {
  def apply(duration: Int)(implicit pf: PolicyFactory) =
    pf.TimeBasedFilter().withMinimumSeparation(duration, TimeUnit.MILLISECONDS)

  def apply(duration: Int, unit: TimeUnit)(implicit pf: PolicyFactory) =
    pf.TimeBasedFilter().withMinimumSeparation(duration, unit)
}

object ContentFilter {
  def apply(script: String)(implicit pf: PolicyFactory) = {
    val f = new com.prismtech.cafe.utils.JavaScriptFilter(script)
    pf.ContentFilter().withFilter(f.asInstanceOf[org.omg.dds.sub.DataReader.Filter[_]])
  }
}
