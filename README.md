bwa-spark-0.3.0
===============
Achieved in bwa-spark-0.2.0:
(1) Worker1 is verified
(2) FASTQ RDD is done
(3) Worker2 implementation is done before the output to SAM/ADAM format

Goal in bwa-spark-0.3.0:
(1) Finish SAM/ADAM output
(2) RDD integration

Development NOTE:
(1) The order after sorting INFLUENCES the results. The result will be slightly different from the original C version.
    This occurs in MemChainToAlign(), MemSortAndDedup() and MemMarkPrimarySe().
