package dds

/**
 * Allows to treat a stream of events as a list and express declaratively
 * the processing.
 */
abstract class Iteratee[T] {
  def doM(f: T => Unit): Unit
  def filter(p: T => Boolean): Iteratee[T]
  def map[Q](f: T => Q): Iteratee[Q]
  def dropWhile(p: T => Boolean): Iteratee[T]
  def takeWhile(p: T => Boolean): Iteratee[T]

  def push(d: T)
}
