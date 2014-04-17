package cs.ucla.edu.bwaspark.datatype

import scala.collection.mutable.MutableList

class MemSeedType(rbeg_i: Long, qbeg_i: Int, len_i: Int) {
  var rBeg: Long = rbeg_i
  var qBeg: Int = qbeg_i
  var len: Int = len_i
}

class MemChainType(pos_i: Long, seeds_i: MutableList[MemSeedType]) {
  var pos: Long = pos_i
  var seeds: MutableList[MemSeedType] = seeds_i
}
