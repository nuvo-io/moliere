package dds

import dds.core.Duration
import org.omg.dds.core.policy.PolicyFactory
import scala.collection.JavaConverters._
import java.util.concurrent.TimeUnit


object WriterDataLifecycle {
  def AutoDisposeInstance(implicit pf: PolicyFactory) = pf.WriterDataLifecycle().withAutDisposeUnregisteredInstances(true)

  def ManualDisposeInstance(implicit pf: PolicyFactory) = pf.WriterDataLifecycle().withAutDisposeUnregisteredInstances(false)
}

object ReaderDataLifecycle {
  def NoAutoPurgeDisposedSamples(implicit pf: PolicyFactory) = {
    implicit val env = pf.getEnvironment
    pf.ReaderDataLifecycle().withAutoPurgeDisposedSamplesDelay(Duration.infinite())
  }

  def AutoPurgeDisposedSamples(duration: Long, unit: TimeUnit)(implicit pf: PolicyFactory) =
    pf.ReaderDataLifecycle().withAutoPurgeDisposedSamplesDelay(duration, unit)

  def NoAutoPurgeNoWriterSamples(implicit pf: PolicyFactory) = {
    implicit val env = pf.getEnvironment
    pf.ReaderDataLifecycle().withAutoPurgeDisposedSamplesDelay(Duration.infinite())
  }

  def AutoPurgeNoWriterSamples(duration: Long, unit: TimeUnit)(implicit pf: PolicyFactory) =
    pf.ReaderDataLifecycle().withAutoPurgeDisposedSamplesDelay(duration, unit)
}

object ResourceLimits {
  def apply(maxSamples: Int, maxInstance: Int, maxSamplesInstance: Int)(implicit pf: PolicyFactory) =
    pf.ResourceLimits()
      .withMaxInstances(maxInstance)
      .withMaxSamples(maxSamples)
      .withMaxSamplesPerInstance(maxSamplesInstance)
}

object DurabilityService {
  def apply(history: org.omg.dds.core.policy.History,
            rlimits: org.omg.dds.core.policy.ResourceLimits)(implicit pf: PolicyFactory): org.omg.dds.core.policy.DurabilityService =
    DurabilityService.apply(history, rlimits, 0, TimeUnit.SECONDS)(pf)


  def apply(history: org.omg.dds.core.policy.History,
            rlimits: org.omg.dds.core.policy.ResourceLimits,
            cleanupDelay: Long, unit: TimeUnit
           )(implicit pf: PolicyFactory): org.omg.dds.core.policy.DurabilityService =
    pf.DurabilityService()
      .withHistoryDepth(history.getDepth)
      .withHistoryKind(history.getKind)
      .withMaxInstances(rlimits.getMaxInstances)
      .withMaxSamples(rlimits.getMaxSamples)
      .withMaxSamplesPerInstance(rlimits.getMaxSamplesPerInstance)
      .withServiceCleanupDelay(cleanupDelay, unit)
}

object Partition {
  def apply(name: String)(implicit pf: PolicyFactory) = pf.Partition().withName(name)

  def apply(names: List[String])(implicit pf: PolicyFactory) = pf.Partition().withName(names.asJava)
}

object Reliability {
  def BestEffort(implicit pf: PolicyFactory) = pf.Reliability().withBestEffort()

  def Reliable(implicit pf: PolicyFactory) = pf.Reliability().withReliable()
}

object Durability {
  def Volatile(implicit pf: PolicyFactory) = pf.Durability().withVolatile()

  def TransientLocal(implicit pf: PolicyFactory) = pf.Durability().withTransientLocal()

  def Transient(implicit pf: PolicyFactory) = pf.Durability().withTransient()

  def Persistent(implicit pf: PolicyFactory) = pf.Durability().withPersistent()
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

object Lifespan {
  def apply(duration: Int, unit: TimeUnit)(implicit pf: PolicyFactory) = pf.Lifespan().withDuration(duration, unit)
}

object Deadline {
  def apply(duration: Int, unit: TimeUnit)(implicit pf: PolicyFactory) = pf.Deadline().withPeriod(duration, unit)
}

object Ownership {
  def Shared(implicit pf: PolicyFactory) = pf.Ownership().withShared()

  def Exclusife(implicit pf: PolicyFactory) = pf.Ownership().withExclusive()
}

object OwnershipStrength {
  def apply(strenght: Int)(implicit pf: PolicyFactory) = pf.OwnershipStrength().withValue(strenght)
}

object ContentFilter {
  def apply(script: String)(implicit pf: PolicyFactory) = {
    import scala.collection.JavaConversions._
    // This code has a dependency to cafe, which is not nice. We should refactor this into
    // another class that deals with DDS runtime specificity.
    if (config.dds == config.CAFE) {
      val cls = Class.forName("com.prismtech.cafe.utils.JavaScriptFilter")
      val ctors = cls.getConstructors()
      val policy =
        for (
          ctor <- ctors.find(ctr => ctr.getParameterTypes.length == 1 && ctr.getParameterTypes()(0).getCanonicalName == "java.lang.String");
          f <- {
            try {
              Some(ctor.newInstance(script))
            } catch {
              case e: RuntimeException => None
            }
          }) yield pf.ContentFilter().withFilter(f.asInstanceOf[org.omg.dds.sub.DataReader.Filter[_]])

      policy.getOrElse {
        throw new RuntimeException("Unable to create ContentFilter")
      }
    } else throw new RuntimeException("ContentFilter QoS is only supported for Caf")
  }
}
