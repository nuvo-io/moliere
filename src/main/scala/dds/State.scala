package dds

import dds.prelude._
import org.omg.dds.core.policy.PolicyFactory
import scala.collection.JavaConversions._

/**
  * Created by veda on 22/12/2016.
  */
trait State[T] {
    val topic: org.omg.dds.topic.Topic[T]
    val writer: org.omg.dds.pub.DataWriter[T]
    val reader: org.omg.dds.sub.DataReader[T]

    def listen(fun: PartialFunction[Any, T]): Int = reader.listen(fun)

    def deaf(lid: Int): Unit = reader.deaf(lid)

    def read(): List[T] = this.reader.select().dataState(DataState.allData).read().map(_.getData).toList

    def write(t: T): Unit = this.writer.write(t)

    def write(ts: List[T]): Unit = ts.foreach(this.writer.write)
}
