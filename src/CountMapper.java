import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class CountMapper  extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
		String[] fields = value.toString().split(Utils.REGEX_SEPARATOR);
		
		// substitute timestamp with a day
		fields[3] = fields[3].trim().substring(0,8);
		
		String newKey = Utils.joinFields(fields, 0, 3);
		output.collect(new Text(newKey), new IntWritable(1));
	  
	 }
}
