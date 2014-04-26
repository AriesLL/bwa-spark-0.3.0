package cs.ucla.edu.bwaspark

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
//import org.apache.spark.storage.StorageLevel

import scala.collection.mutable.MutableList

import cs.ucla.edu.bwaspark.datatype._
import cs.ucla.edu.bwaspark.worker1.BWAMemWorker1._
import cs.ucla.edu.bwaspark.worker2.BWAMemWorker2._
import cs.ucla.edu.bwaspark.debug.DebugFlag._
import cs.ucla.edu.bwaspark.fastq._
import cs.ucla.edu.avro.fastq._

import java.io.FileReader
import java.io.BufferedReader

object BWAMEMSpark {
  // load reads from the FASTQ file (for testing use)
  private def loadFASTQSeqs(fileName: String): MutableList[String] = {
    
    val reader = new BufferedReader(new FileReader(fileName))
    var line = reader.readLine
    var i = 0    
    var seqs = new MutableList[String]

    while(line != null) {
      if(i % 4 == 1) {
        seqs += line
      }
      i += 1
      line = reader.readLine
    } 

    seqs.foreach(println(_))
    seqs
  }


  class testRead() {
    var seq: String = new String
    var regs: MutableList[MemAlnRegType] = new MutableList[MemAlnRegType]
  }


  def main(args: Array[String]) {
    val sc = new SparkContext("local[24]", "BWA-mem Spark",
       "/home/hadoopmaster/spark/spark-0.9.0-incubating-bin-hadoop2-prebuilt/", List("/home/ytchen/incubator/bwa-spark-0.3.0/target/bwa-spark-0.3.0.jar"))
    //val sc = new SparkContext("spark://Jc11:7077", "BWA-mem Spark",
       //"/home/hadoopmaster/spark/spark-0.9.0-incubating-bin-hadoop2-prebuilt/", List("/home/ytchen/incubator/bwa-spark-0.3.0/target/bwa-spark-0.3.0.jar"))

    //val fastqLoader = new FASTQLocalFileLoader(10000000)
    //val fastqLoader = new FASTQLocalFileLoader(100000000)
    //fastqLoader.storeFASTQInHDFS(sc, "/home/ytchen/genomics/data/HCC1954_1_20reads.fq", "hdfs://Jc11:9000/user/ytchen/data/HCC1954_1_20reads")
    //fastqLoader.storeFASTQInHDFS(sc, "/home/ytchen/genomics/data/HCC1954_1_100reads.fq", "hdfs://Jc11:9000/user/ytchen/data/HCC1954_1_100reads")
    //fastqLoader.storeFASTQInHDFS(sc, "/home/ytchen/genomics/data/ERR013140_1.filt.fastq", "hdfs://Jc11:9000/user/ytchen/data/ERR013140_1.filt.fastq")


    //val fastqRDDLoader = new FASTQRDDLoader(sc, "hdfs://Jc11:9000/user/ytchen/data/HCC1954_1_20reads", 1)
    val fastqRDDLoader = new FASTQRDDLoader(sc, "hdfs://Jc11:9000/user/ytchen/data/HCC1954_1_100reads", 1)
    //val fastqRDDLoader = new FASTQRDDLoader(sc, "hdfs://Jc11:9000/user/ytchen/data/ERR013140_1.filt.fastq", 2)
    val fastqRDD = fastqRDDLoader.RDDLoadAll()

    //loading index files
    val bwaIdx = new BWAIdxType
    val prefix = "/home/pengwei/genomics/ReferenceMetadata/human_g1k_v37.fasta"
    bwaIdx.load(prefix, 0)

    //loading BWA MEM options
    val bwaMemOpt = new MemOptType
    bwaMemOpt.load

    val bwaMemOptGlobal = sc.broadcast(bwaMemOpt)
    val bwaIdxGlobal = sc.broadcast(bwaIdx)
    //debugLevel = 1


    val reads = fastqRDD.map( seq => bwaMemWorker1(bwaMemOptGlobal.value, bwaIdxGlobal.value.bwt, bwaIdxGlobal.value.bns, bwaIdxGlobal.value.pac, null, seq) )

    val c = reads.count
    println("Count: " + c)
    //println("Count: " + reads.map( read => bwaMemWorker2(bwaMemOptGlobal.value, read.regs, bwaIdxGlobal.value.bns, bwaIdxGlobal.value.pac, read.seq, 0) ).reduce(_ + _))

  } 
}
