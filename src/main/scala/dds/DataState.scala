package dds
import dds.config.DefaultEntities.defaultSub
import org.omg.dds.sub.InstanceState

object DataState {
  def apply() = defaultSub.createDataState()

  def allSamples() = DataState()
    .withAnyInstanceState()
    .withAnySampleState()
    .withAnyViewState()

  val allData =
    DataState()
      .withAnySampleState()
      .withAnyViewState()
      .`with`(org.omg.dds.sub.InstanceState.ALIVE)

  val newData =
    DataState()
      .withAnyViewState()
      .`with`(org.omg.dds.sub.InstanceState.ALIVE)
      .`with`(org.omg.dds.sub.SampleState.NOT_READ)

  val oldData =
    DataState()
      .withAnyViewState()
      .`with`(org.omg.dds.sub.InstanceState.ALIVE)
      .`with`(org.omg.dds.sub.SampleState.READ)

  val newInstances =
    DataState()
      .`with`(org.omg.dds.sub.ViewState.NEW)
        .withAnySampleState()
          .withAnyInstanceState()

  val notAliveInstances =
    DataState()
      .`with`(org.omg.dds.sub.InstanceState.NOT_ALIVE_NO_WRITERS)
      .withAnySampleState()
      .withAnyViewState()

  val disposedInstances =
    DataState()
      .`with`(org.omg.dds.sub.InstanceState.NOT_ALIVE_DISPOSED)
      .withAnySampleState()
      .withAnyViewState()

}
