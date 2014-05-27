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
		String[] fields = value.toString().split("\\t");
		
		// substitute timestamp with date
		fields[3] = fields[3].trim().substring(0,8);
		String newKey = fields[0].trim() + "\t" + fields[1] + "\t" +
					fields[2] + "\t" + fields[3];
		output.collect(new Text(newKey), new IntWritable(1));
	  
	 }
}
