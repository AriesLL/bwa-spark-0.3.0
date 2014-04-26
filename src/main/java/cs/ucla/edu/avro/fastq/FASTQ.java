/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package cs.ucla.edu.avro.fastq;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public interface FASTQ {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"FASTQ\",\"namespace\":\"cs.ucla.edu.avro.fastq\",\"types\":[{\"type\":\"record\",\"name\":\"FASTQRecord\",\"fields\":[{\"name\":\"name\",\"type\":\"bytes\"},{\"name\":\"seq\",\"type\":\"bytes\"},{\"name\":\"quality\",\"type\":\"bytes\"},{\"name\":\"seqLength\",\"type\":\"int\"},{\"name\":\"comment\",\"type\":\"bytes\"}]}],\"messages\":{}}");

  @SuppressWarnings("all")
  public interface Callback extends FASTQ {
    public static final org.apache.avro.Protocol PROTOCOL = cs.ucla.edu.avro.fastq.FASTQ.PROTOCOL;
  }
}