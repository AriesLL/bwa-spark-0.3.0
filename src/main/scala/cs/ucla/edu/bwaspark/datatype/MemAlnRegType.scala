package cs.ucla.edu.bwaspark.datatype

import java.io.ObjectInputStream
import java.io.ObjectOutputStream

import scala.Serializable

class MemAlnRegType extends Serializable {
  var rBeg: Long = _       // [rBeg,rEnd): reference sequence in the alignment
  var rEnd: Long = _       
  var qBeg: Int = _        // [qBeg,qEnd): query sequence in the alignment
  var qEnd: Int = _
  var score: Int = _       // best local SW score
  var trueScore: Int = _   // actual score corresponding to the aligned region; possibly smaller than $score
  var sub: Int = _         // 2nd best SW score
  var csub: Int = _        // SW score of a tandem hit
  var subNum: Int = _      // approximate number of suboptimal hits
  var width: Int = _       // actual band width used in extension
  var seedCov: Int = _     // length of regions coverged by seeds
  var secondary: Int = _   // index of the parent hit shadowing the current hit; <0 if primary
  var hash: Long = _


  private def writeObject(out: ObjectOutputStream) {
    out.writeLong(rBeg)
    out.writeLong(rEnd)
    out.writeInt(qBeg)
    out.writeInt(qEnd)
    out.writeInt(score)
    out.writeInt(trueScore)
    out.writeInt(sub)
    out.writeInt(csub)
    out.writeInt(subNum)
    out.writeInt(width)
    out.writeInt(seedCov)
    out.writeInt(secondary)
    out.writeLong(hash)
  }

  private def readObject(in: ObjectInputStream) {
    rBeg = in.readObject.asInstanceOf[Long]
    rEnd = in.readObject.asInstanceOf[Long]
    qBeg = in.readObject.asInstanceOf[Int]
    qEnd = in.readObject.asInstanceOf[Int]
    score = in.readObject.asInstanceOf[Int]
    trueScore = in.readObject.asInstanceOf[Int]
    sub = in.readObject.asInstanceOf[Int]
    csub = in.readObject.asInstanceOf[Int]
    subNum = in.readObject.asInstanceOf[Int]
    width = in.readObject.asInstanceOf[Int]
    seedCov = in.readObject.asInstanceOf[Int]
    secondary = in.readObject.asInstanceOf[Int]
    hash = in.readObject.asInstanceOf[Long]
  }

  private def readObjectNoData() {

  }  
}

