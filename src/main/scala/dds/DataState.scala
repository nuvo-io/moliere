package dds
import dds.config.DefaultEntities.defaultSub
import org.omg.dds.sub.InstanceState

object DataState {
  def apply() = defaultSub.createDataState()

  def allSamples() = DataState()
    .withAnyInstanceState()
    .withAnySampleState()
    .withAnyViewState()

  def allData() = DataState().withAnySampleState()

}
