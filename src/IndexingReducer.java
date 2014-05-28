
import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;



public class IndexingReducer extends MapReduceBase implements Reducer<Pair, Text, Text, IntWritable> {
	@Override
	public void reduce(Pair key, Iterator<Text> values,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
		
		int index = 0;
		while (values.hasNext()) {
			index++;
			
			String line = values.next().toString();
			output.collect(new Text(line), new IntWritable(index));
		}
		
	}
	
}

