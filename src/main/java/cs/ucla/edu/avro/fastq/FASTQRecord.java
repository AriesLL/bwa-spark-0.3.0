/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package cs.ucla.edu.avro.fastq;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class FASTQRecord extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"FASTQRecord\",\"namespace\":\"cs.ucla.edu.avro.fastq\",\"fields\":[{\"name\":\"name\",\"type\":\"bytes\"},{\"name\":\"seq\",\"type\":\"bytes\"},{\"name\":\"quality\",\"type\":\"bytes\"},{\"name\":\"seqLength\",\"type\":\"int\"},{\"name\":\"comment\",\"type\":\"bytes\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.nio.ByteBuffer name;
  @Deprecated public java.nio.ByteBuffer seq;
  @Deprecated public java.nio.ByteBuffer quality;
  @Deprecated public int seqLength;
  @Deprecated public java.nio.ByteBuffer comment;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public FASTQRecord() {}

  /**
   * All-args constructor.
   */
  public FASTQRecord(java.nio.ByteBuffer name, java.nio.ByteBuffer seq, java.nio.ByteBuffer quality, java.lang.Integer seqLength, java.nio.ByteBuffer comment) {
    this.name = name;
    this.seq = seq;
    this.quality = quality;
    this.seqLength = seqLength;
    this.comment = comment;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return name;
    case 1: return seq;
    case 2: return quality;
    case 3: return seqLength;
    case 4: return comment;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: name = (java.nio.ByteBuffer)value$; break;
    case 1: seq = (java.nio.ByteBuffer)value$; break;
    case 2: quality = (java.nio.ByteBuffer)value$; break;
    case 3: seqLength = (java.lang.Integer)value$; break;
    case 4: comment = (java.nio.ByteBuffer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'name' field.
   */
  public java.nio.ByteBuffer getName() {
    return name;
  }

  /**
   * Sets the value of the 'name' field.
   * @param value the value to set.
   */
  public void setName(java.nio.ByteBuffer value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'seq' field.
   */
  public java.nio.ByteBuffer getSeq() {
    return seq;
  }

  /**
   * Sets the value of the 'seq' field.
   * @param value the value to set.
   */
  public void setSeq(java.nio.ByteBuffer value) {
    this.seq = value;
  }

  /**
   * Gets the value of the 'quality' field.
   */
  public java.nio.ByteBuffer getQuality() {
    return quality;
  }

  /**
   * Sets the value of the 'quality' field.
   * @param value the value to set.
   */
  public void setQuality(java.nio.ByteBuffer value) {
    this.quality = value;
  }

  /**
   * Gets the value of the 'seqLength' field.
   */
  public java.lang.Integer getSeqLength() {
    return seqLength;
  }

  /**
   * Sets the value of the 'seqLength' field.
   * @param value the value to set.
   */
  public void setSeqLength(java.lang.Integer value) {
    this.seqLength = value;
  }

  /**
   * Gets the value of the 'comment' field.
   */
  public java.nio.ByteBuffer getComment() {
    return comment;
  }

  /**
   * Sets the value of the 'comment' field.
   * @param value the value to set.
   */
  public void setComment(java.nio.ByteBuffer value) {
    this.comment = value;
  }

  /** Creates a new FASTQRecord RecordBuilder */
  public static cs.ucla.edu.avro.fastq.FASTQRecord.Builder newBuilder() {
    return new cs.ucla.edu.avro.fastq.FASTQRecord.Builder();
  }
  
  /** Creates a new FASTQRecord RecordBuilder by copying an existing Builder */
  public static cs.ucla.edu.avro.fastq.FASTQRecord.Builder newBuilder(cs.ucla.edu.avro.fastq.FASTQRecord.Builder other) {
    return new cs.ucla.edu.avro.fastq.FASTQRecord.Builder(other);
  }
  
  /** Creates a new FASTQRecord RecordBuilder by copying an existing FASTQRecord instance */
  public static cs.ucla.edu.avro.fastq.FASTQRecord.Builder newBuilder(cs.ucla.edu.avro.fastq.FASTQRecord other) {
    return new cs.ucla.edu.avro.fastq.FASTQRecord.Builder(other);
  }
  
  /**
   * RecordBuilder for FASTQRecord instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<FASTQRecord>
    implements org.apache.avro.data.RecordBuilder<FASTQRecord> {

    private java.nio.ByteBuffer name;
    private java.nio.ByteBuffer seq;
    private java.nio.ByteBuffer quality;
    private int seqLength;
    private java.nio.ByteBuffer comment;

    /** Creates a new Builder */
    private Builder() {
      super(cs.ucla.edu.avro.fastq.FASTQRecord.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(cs.ucla.edu.avro.fastq.FASTQRecord.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.name)) {
        this.name = data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.seq)) {
        this.seq = data().deepCopy(fields()[1].schema(), other.seq);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.quality)) {
        this.quality = data().deepCopy(fields()[2].schema(), other.quality);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.seqLength)) {
        this.seqLength = data().deepCopy(fields()[3].schema(), other.seqLength);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.comment)) {
        this.comment = data().deepCopy(fields()[4].schema(), other.comment);
        fieldSetFlags()[4] = true;
      }
    }
    
    /** Creates a Builder by copying an existing FASTQRecord instance */
    private Builder(cs.ucla.edu.avro.fastq.FASTQRecord other) {
            super(cs.ucla.edu.avro.fastq.FASTQRecord.SCHEMA$);
      if (isValidValue(fields()[0], other.name)) {
        this.name = data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.seq)) {
        this.seq = data().deepCopy(fields()[1].schema(), other.seq);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.quality)) {
        this.quality = data().deepCopy(fields()[2].schema(), other.quality);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.seqLength)) {
        this.seqLength = data().deepCopy(fields()[3].schema(), other.seqLength);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.comment)) {
        this.comment = data().deepCopy(fields()[4].schema(), other.comment);
        fieldSetFlags()[4] = true;
      }
    }

    /** Gets the value of the 'name' field */
    public java.nio.ByteBuffer getName() {
      return name;
    }
    
    /** Sets the value of the 'name' field */
    public cs.ucla.edu.avro.fastq.FASTQRecord.Builder setName(java.nio.ByteBuffer value) {
      validate(fields()[0], value);
      this.name = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'name' field has been set */
    public boolean hasName() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'name' field */
    public cs.ucla.edu.avro.fastq.FASTQRecord.Builder clearName() {
      name = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'seq' field */
    public java.nio.ByteBuffer getSeq() {
      return seq;
    }
    
    /** Sets the value of the 'seq' field */
    public cs.ucla.edu.avro.fastq.FASTQRecord.Builder setSeq(java.nio.ByteBuffer value) {
      validate(fields()[1], value);
      this.seq = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'seq' field has been set */
    public boolean hasSeq() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'seq' field */
    public cs.ucla.edu.avro.fastq.FASTQRecord.Builder clearSeq() {
      seq = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'quality' field */
    public java.nio.ByteBuffer getQuality() {
      return quality;
    }
    
    /** Sets the value of the 'quality' field */
    public cs.ucla.edu.avro.fastq.FASTQRecord.Builder setQuality(java.nio.ByteBuffer value) {
      validate(fields()[2], value);
      this.quality = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'quality' field has been set */
    public boolean hasQuality() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'quality' field */
    public cs.ucla.edu.avro.fastq.FASTQRecord.Builder clearQuality() {
      quality = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'seqLength' field */
    public java.lang.Integer getSeqLength() {
      return seqLength;
    }
    
    /** Sets the value of the 'seqLength' field */
    public cs.ucla.edu.avro.fastq.FASTQRecord.Builder setSeqLength(int value) {
      validate(fields()[3], value);
      this.seqLength = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'seqLength' field has been set */
    public boolean hasSeqLength() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'seqLength' field */
    public cs.ucla.edu.avro.fastq.FASTQRecord.Builder clearSeqLength() {
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'comment' field */
    public java.nio.ByteBuffer getComment() {
      return comment;
    }
    
    /** Sets the value of the 'comment' field */
    public cs.ucla.edu.avro.fastq.FASTQRecord.Builder setComment(java.nio.ByteBuffer value) {
      validate(fields()[4], value);
      this.comment = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'comment' field has been set */
    public boolean hasComment() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'comment' field */
    public cs.ucla.edu.avro.fastq.FASTQRecord.Builder clearComment() {
      comment = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    public FASTQRecord build() {
      try {
        FASTQRecord record = new FASTQRecord();
        record.name = fieldSetFlags()[0] ? this.name : (java.nio.ByteBuffer) defaultValue(fields()[0]);
        record.seq = fieldSetFlags()[1] ? this.seq : (java.nio.ByteBuffer) defaultValue(fields()[1]);
        record.quality = fieldSetFlags()[2] ? this.quality : (java.nio.ByteBuffer) defaultValue(fields()[2]);
        record.seqLength = fieldSetFlags()[3] ? this.seqLength : (java.lang.Integer) defaultValue(fields()[3]);
        record.comment = fieldSetFlags()[4] ? this.comment : (java.nio.ByteBuffer) defaultValue(fields()[4]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
