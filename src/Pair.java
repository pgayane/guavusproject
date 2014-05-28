import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;


public class Pair implements WritableComparable<Pair>{
	private String key;
	private Integer value;
	
	public Pair() {	}
	
	public Pair(String k, Integer v){
		this.key = k;
		this.value = v;
	}
	
	@Override
	public String toString() {
		return (new StringBuilder())
				.append('{')
				.append(key)
				.append(',')
				.append(value)
				.append('}')
				.toString();
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		key = WritableUtils.readString(in);
		value = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		WritableUtils.writeString(out, key);
		out.writeInt(value);
	}

	@Override
	public int compareTo(Pair o) {
		int result = key.compareTo(o.key);
		if(0 == result) {
			result = value.compareTo(o.value);
		}
		return result;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	
}
