
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class RankMapper  extends MapReduceBase implements Mapper<LongWritable, Text, Pair, Text> {
	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Pair, Text> output, Reporter reporter)
			throws IOException {
		String[] fields = value.toString().split(Utils.REGEX_SEPARATOR);
		
		int[] indexes = {0,2,3};
		String newKey = Utils.joinFields(fields, indexes); 
		int count = Integer.parseInt(fields[fields.length-1].trim());
		output.collect(new Pair(newKey, count), value);
	  
	 }
}