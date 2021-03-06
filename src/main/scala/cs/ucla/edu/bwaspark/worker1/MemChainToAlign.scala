package cs.ucla.edu.bwaspark.worker1

import scala.util.control.Breaks._
import scala.List
import scala.collection.mutable.MutableList

import cs.ucla.edu.bwaspark.datatype._
import cs.ucla.edu.bwaspark.util.BNTSeqUtil._
import cs.ucla.edu.bwaspark.util.SWUtil._

// Used for read test input data
import java.io.{FileReader, BufferedReader}

object MemChainToAlign {
  val MAX_BAND_TRY = 2    

  /**
    *  Read class (testing use)
    */
  class ReadChain(chains_i: MutableList[MemChainType], seq_i: Array[Byte]) {
    var chains: MutableList[MemChainType] = chains_i
    var seq: Array[Byte] = seq_i
  }

  /**
    *  Member variable of all reads (testing use)
    */ 
  var testReadChains: MutableList[ReadChain] = new MutableList
  
  /**
    *  Read the test chain data generated from bwa-0.7.8 (C version) (testing use)
    *
    *  @param fileName the test data file name
    */
  def readTestData(fileName: String) {
    val reader = new BufferedReader(new FileReader(fileName))

    var line = reader.readLine
    var chains: MutableList[MemChainType] = new MutableList
    var chainPos: Long = 0
    var seeds: MutableList[MemSeedType] = new MutableList
    var seq: Array[Byte] = new Array[Byte](101)

    while(line != null) {
      val lineFields = line.split(" ")      

      // Find a sequence
      if(lineFields(0) == "Sequence") {
        chains = new MutableList
        seq = lineFields(2).getBytes
        seq = seq.map(s => (s - 48).toByte) // ASCII => Byte(Int)
      }
      // Find a chain
      else if(lineFields(0) == "Chain") {
        seeds = new MutableList
        chainPos = lineFields(1).toLong
      }
      // Fina a seed
      else if(lineFields(0) == "Seed") {
        seeds += (new MemSeedType(lineFields(1).toLong, lineFields(2).toInt, lineFields(3).toInt))
      }
      // append the current list
      else if(lineFields(0) == "ChainEnd") {
        val cur_seeds = seeds
        chains += (new MemChainType(chainPos, cur_seeds))
      }
      // append the current list
      else if(lineFields(0) == "SequenceEnd") {
        val cur_chains = chains
        val cur_seq = seq 
        testReadChains += (new ReadChain(cur_chains, seq))
      }

      line = reader.readLine
    }

  }


  /**
    *  Print all the chains (and seeds) from all input reads 
    *  (Only for testing use)
    */
  def printAllReads() {
    def printChains(chains: MutableList[MemChainType]) {
      println("Sequence");
      def printSeeds(seeds: MutableList[MemSeedType]) {
        seeds.foreach(s => println("Seed " + s.rBeg + " " + s.qBeg + " " + s.len))
      }
    
      chains.map(p => {
        println("Chain " + p.pos + " " + p.seeds.length)
        printSeeds(p.seeds)
                      } )
    }

    testReadChains.foreach(r => printChains(r.chains))
  }

  /**
    *  Data structure which keep both the length of a seed and its index in the original chain
    */
  class SRTType(len_i: Int, index_i: Int) {
    var len: Int = len_i
    var index: Int = index_i
  }

  /**
    *  The main function of memChainToAlign class
    *
    *  @param opt the MemOptType object
    *  @param pacLen the length of PAC array
    *  @param pac the PAC array
    *  @param queryLen the query length (read length)
    *  @param chain one of the mem chains of the read
    *  @param regs the input alignment registers, which are the registers from the output of the previous call on MemChainToAln().
    *              This parameter is updated iteratively. The number of iterations is the number of chains of this read.
    */
  def memChainToAln(opt: MemOptType, pacLen: Long, pac: Array[Byte], queryLen: Int, query: Array[Byte], 
    chain: MemChainType, regs: MutableList[MemAlnRegType]): MutableList[MemAlnRegType] = {

    var rmax: Array[Long] = new Array[Long](2)   
    var srt: Array[SRTType] = new Array[SRTType](chain.seeds.length) 
    var alnRegs = regs
    var aw: Array[Int] = new Array[Int](2)

 
    // calculate the maximum possible span of this alignment
    rmax = getMaxSpan(opt, pacLen, queryLen, chain)
    //println("rmax(0): " + rmax(0) + ", rmax(1): " + rmax(1))  // debugging message

    // retrieve the reference sequence
    val ret = bnsGetSeq(pacLen, pac, rmax(0), rmax(1))
    var rseq = ret._1
    val rlen = ret._2
    assert(rlen == rmax(1) - rmax(0))

    // debugging message
    //println(rlen)     
    //for(i <- 0 until rlen.toInt)
      //print(rseq(i).toInt)
    //println

    // Setup the value of srt array
    for(i <- 0 to (chain.seeds.length - 1)) 
      srt(i) = new SRTType(chain.seeds(i).len, i)

    srt = srt.sortBy(s => s.len)
    //srt.map(s => println("(" + s.len + ", " + s.index + ")") )  // debugging message

    // The main for loop
    for(k <- (chain.seeds.length - 1) to 0 by -1) {
      val seed = chain.seeds( srt(k).index )
      var i = testExtension(opt, seed, alnRegs)
     
      var checkoverlappingRet = -1

      if(i < alnRegs.length) checkoverlappingRet = checkOverlapping(k + 1, seed, chain, srt)
      
      // no overlapping seeds; then skip extension
      if((i < alnRegs.length) && (checkoverlappingRet == chain.seeds.length)) {
        srt(k).index = 0  // mark that seed extension has not been performed
      }
      else {
        // push the current align reg into the output list
        // initialize a new alnreg
        var reg = new MemAlnRegType
        reg.width = opt.w
        aw(0) = opt.w
        aw(1) = opt.w
        reg.score = -1
        reg.trueScore = -1
     
        // left extension
        if(seed.qBeg > 0) {
          val ret = leftExtension(opt, seed, rmax, query, rseq, reg) 
          reg = ret._1
          aw(0) = ret._2
        }
        else {
          reg.score = seed.len * opt.a
          reg.trueScore = seed.len * opt.a
          reg.qBeg = 0
          reg.rBeg = seed.rBeg
        }
            
        // right extension
        if((seed.qBeg + seed.len) != queryLen) {
          val ret = rightExtension(opt, seed, rmax, query, queryLen, rseq, reg)
          reg = ret._1
          aw(1) = ret._2
        }
        else {
          reg.qEnd = queryLen
          reg.rEnd = seed.rBeg + seed.len
        }
  
        reg.seedCov = computeSeedCoverage(chain, reg)

        if(aw(0) > aw(1)) reg.width = aw(0)
        else reg.width = aw(1)

        // push the current align reg into the output list
        alnRegs += reg
      }

    }

    alnRegs
  }

  /**
    *  Calculate the maximum possible span of this alignment
    *  This private function is used by memChainToAln()
    *
    *  @param opt the input MemOptType object
    *  @param qLen the query length (the read length)
    */
  private def calMaxGap(opt: MemOptType, qLen: Int): Int = {
    val lenDel = ((qLen * opt.a - opt.oDel).toDouble / opt.eDel.toDouble + 1.0).toInt
    val lenIns = ((qLen * opt.a - opt.oIns).toDouble / opt.eIns.toDouble + 1.0).toInt
    var len = -1

    if(lenDel > lenIns)
      len = lenDel
    else
      len = lenIns

    if(len <= 1) len = 1

    val tmp = opt.w << 1

    if(len < tmp) len
    else tmp
  }
 	

  /** 
    *  Get the max possible span
    *  This private function is used by memChainToAln()
    *
    *  @param opt the input opt object
    *  @param pacLen the length of PAC array
    *  @param queryLen the length of the query (read)
    *  @param chain the input chain
    */
  private def getMaxSpan(opt: MemOptType, pacLen: Long, queryLen: Int, chain: MemChainType): Array[Long] = {
    var rmax: Array[Long] = new Array[Long](2)
    val doublePacLen = pacLen << 1
    rmax(0) = doublePacLen
    rmax(1) = 0

    val seedMinRBeg = chain.seeds.map(seed => 
      { seed.rBeg - ( seed.qBeg + calMaxGap(opt, seed.qBeg) ) } ).min
    val seedMaxREnd = chain.seeds.map(seed => 
      { seed.rBeg + seed.len + (queryLen - seed.qBeg - seed.len) + calMaxGap(opt, queryLen - seed.qBeg - seed.len) } ).max
   
    if(rmax(0) > seedMinRBeg) rmax(0) = seedMinRBeg
    if(rmax(1) < seedMaxREnd) rmax(1) = seedMaxREnd
      
    if(rmax(0) <= 0) rmax(0) = 0
    if(rmax(1) >= doublePacLen) rmax(1) = doublePacLen

    // crossing the forward-reverse boundary; then choose one side
    if(rmax(0) < pacLen && pacLen < rmax(1)) {
      // this works because all seeds are guaranteed to be on the same strand
      if(chain.seeds(0).rBeg < pacLen) rmax(1) = pacLen
      else rmax(0) = pacLen
    }

    rmax
  }
   
  /**
    *  Test whether extension has been made before
    *  This private function is used by memChainToAln()
    *
    *  @param opt the input opt object
    *  @param seed the input seed
    *  @param regs the current align registers
    */
  private def testExtension(opt: MemOptType, seed: MemSeedType, regs: MutableList[MemAlnRegType]): Int = {
    var rDist: Long = -1 
    var qDist: Int = -1
    var maxGap: Int = -1
    var minDist: Int = -1
    var w: Int = -1
    var breakIdx: Int = regs.length

    breakable {
      for(i <- 0 to (regs.length - 1)) {
        
        if(seed.rBeg >= regs(i).rBeg && (seed.rBeg + seed.len) <= regs(i).rEnd && seed.qBeg >= regs(i).qBeg && (seed.qBeg + seed.len) <= regs(i).qEnd) {
          // qDist: distance ahead of the seed on query; rDist: on reference
          qDist = seed.qBeg - regs(i).qBeg
          rDist = seed.rBeg - regs(i).rBeg

          if(qDist < rDist) minDist = qDist 
          else minDist = rDist.toInt

          // the maximal gap allowed in regions ahead of the seed
          maxGap = calMaxGap(opt, minDist)

          // bounded by the band width          
          if(maxGap < opt.w) w = maxGap
          else w = opt.w
          
          // the seed is "around" a previous hit
          if((qDist - rDist) < w && (rDist - qDist) < w) { 
            breakIdx = i 
            break
          }

          // the codes below are similar to the previous four lines, but this time we look at the region behind
          qDist = regs(i).qEnd - (seed.qBeg + seed.len)
          rDist = regs(i).rEnd - (seed.rBeg + seed.len)
          
          if(qDist < rDist) minDist = qDist
          else minDist = rDist.toInt

          maxGap = calMaxGap(opt, minDist)

          if(maxGap < opt.w) w = maxGap
          else w = opt.w

          if((qDist - rDist) < w && (rDist - qDist) < w) {
            breakIdx = i
            break
          }          
        }
      }
    }

    breakIdx
  }
    
  /**
    *  Further check overlapping seeds in the same chain
    *  This private function is used by memChainToAln()
    *
    *  @param startIdx the index return by the previous testExtension() function
    *  @param seed the current seed
    *  @param chain the input chain
    *  @param srt the srt array, which record the length and the original index on the chain
    */ 
  private def checkOverlapping(startIdx: Int, seed: MemSeedType, chain: MemChainType, srt: Array[SRTType]): Int = {
    var breakIdx = chain.seeds.length

    breakable {
      for(i <- startIdx to (chain.seeds.length - 1)) {
        if(srt(i).index != 0) {
          val targetSeed = chain.seeds(srt(i).index)

          // only check overlapping if t is long enough; TODO: more efficient by early stopping
          // NOTE: the original implementation may be not correct!!!
          if(targetSeed.len >= seed.len * 0.95) {
            if(seed.qBeg <= targetSeed.qBeg && (seed.qBeg + seed.len - targetSeed.qBeg) >= (seed.len>>2) && (targetSeed.qBeg - seed.qBeg) != (targetSeed.rBeg - seed.rBeg)) {
              breakIdx = i
              break
            }
            if(targetSeed.qBeg <= seed.qBeg && (targetSeed.qBeg + targetSeed.len - seed.qBeg) >= (seed.len>>2) && (seed.qBeg - targetSeed.qBeg) != (seed.rBeg - targetSeed.rBeg)) {
              breakIdx = i
              break
            }
          }
        }
      }
    }

    breakIdx
  }

  /**
    *  Left extension of the current seed
    *  This private function is used by memChainToAln()
    *
    *  @param opt the input MemOptType object
    *  @param seed the current seed
    *  @param rmax the calculated maximal range
    *  @param query the query (read)
    *  @param rseq the reference sequence
    *  @param reg the current align register before doing left extension (the value is not complete yet)
    */
  private def leftExtension(opt: MemOptType, seed: MemSeedType, rmax: Array[Long], query: Array[Byte], rseq: Array[Byte], reg: MemAlnRegType): (MemAlnRegType, Int) = {
    var aw = 0
    val tmp = (seed.rBeg - rmax(0)).toInt
    var qs = new Array[Byte](seed.qBeg)
    var rs = new Array[Byte](tmp)
    var qle = -1
    var tle = -1
    var gtle = -1
    var gscore = -1
    var maxoff = -1

    var regResult = reg
    
    for(i <- 0 to (seed.qBeg - 1)) qs(i) = query(seed.qBeg - 1 - i)
    for(i <- 0 to (tmp - 1)) rs(i) = rseq(tmp - 1 - i)
    
    breakable {
      for(i <- 0 to (MAX_BAND_TRY - 1)) {
        var prev = regResult.score
        aw = opt.w << i
        val results = SWExtend(seed.qBeg, qs, tmp, rs, 5, opt.mat, opt.oDel, opt.eDel, opt.oIns, opt.eIns, aw, opt.penClip5, opt.zdrop, seed.len * opt.a)
        regResult.score = results(0)
        qle = results(1)
        tle = results(2)
        gtle = results(3)
        gscore = results(4)
        maxoff = results(5)

        if(regResult.score == prev || ( maxoff < (aw >> 1) + (aw >> 2) ) ) break
      }
    }

    // check whether we prefer to reach the end of the query
    // local extension
    if(gscore <= 0 || gscore <= (regResult.score - opt.penClip5)) {
      regResult.qBeg = seed.qBeg - qle
      regResult.rBeg = seed.rBeg - tle
      regResult.trueScore = regResult.score
    }
    // to-end extension
    else {
      regResult.qBeg = 0
      regResult.rBeg = seed.rBeg - gtle
      regResult.trueScore = gscore
    }

    (regResult, aw)
  }

  /**
    *  Right extension of the current seed
    *  This private function is used by memChainToAln()
    *
    *  @param opt the input MemOptType object
    *  @param seed the current seed
    *  @param rmax the calculated maximal range
    *  @param query the query (read)
    *  @param queryLen the length of this query
    *  @param rseq the reference sequence
    *  @param reg the current align register before doing left extension (the value is not complete yet)
    */
  private def rightExtension(opt: MemOptType, seed: MemSeedType, rmax: Array[Long], query: Array[Byte], queryLen: Int, rseq: Array[Byte], reg: MemAlnRegType): (MemAlnRegType, Int) = {
    var aw = 0
    var regResult = reg
    var qe = seed.qBeg + seed.len
    var re = seed.rBeg + seed.len - rmax(0)
    var sc0 = regResult.score
    var qle = -1
    var tle = -1
    var gtle = -1
    var gscore = -1
    var maxoff = -1

    assert(re >= 0)

    var qeArray = new Array[Byte](queryLen - qe)
    // fill qeArray
    for(i <- 0 to (queryLen - qe - 1)) qeArray(i) = query(qe + i)

    var reArray = new Array[Byte]((rmax(1) - rmax(0) - re).toInt)
    // fill reArray
    for(i <- 0 to (rmax(1) - rmax(0) - re - 1).toInt) reArray(i) = rseq(re.toInt + i)

    breakable {
      for(i <- 0 to (MAX_BAND_TRY - 1)) {
        var prev = regResult.score
        aw = opt.w << i
        val results = SWExtend(queryLen - qe, qeArray, (rmax(1) - rmax(0) - re).toInt, reArray, 5, opt.mat, opt.oDel, opt.eDel, opt.oIns, opt.eIns, aw, opt.penClip3, opt.zdrop, sc0)
        regResult.score = results(0)
        qle = results(1)
        tle = results(2)
        gtle = results(3)
        gscore = results(4)
        maxoff = results(5)

        if(regResult.score == prev || ( maxoff < (aw >> 1) + (aw >> 2) ) ) break
      }
    }
    // check whether we prefer to reach the end of the query
    // local extension
    if(gscore <= 0 || gscore <= (regResult.score - opt.penClip3)) {
      regResult.qEnd = qe + qle
      regResult.rEnd = rmax(0) + re + tle
      regResult.trueScore += regResult.score - sc0
    }
    else {
      regResult.qEnd = queryLen
      regResult.rEnd = rmax(0) + re + gtle
      regResult.trueScore += gscore - sc0
    }

    (regResult, aw)
  }
    
  /** 
    *  Compute the seed coverage
    *  This private function is used by memChainToAln()
    * 
    *  @param chain the input chain
    *  @param reg the current align register after left/right extension is done 
    */
  private def computeSeedCoverage(chain: MemChainType, reg: MemAlnRegType): Int = {
    var seedcov = 0
    
    for(i <- 0 to (chain.seeds.length - 1)) {
      // seed fully contained
      if(chain.seeds(i).qBeg >= reg.qBeg && 
         chain.seeds(i).qBeg + chain.seeds(i).len <= reg.qEnd &&
         chain.seeds(i).rBeg >= reg.rBeg &&
         chain.seeds(i).rBeg + chain.seeds(i).len <= reg.rEnd)
        seedcov += chain.seeds(i).len   // this is not very accurate, but for approx. mapQ, this is good enough
    }

    seedcov
  }

}

