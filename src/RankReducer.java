import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class RankReducer extends MapReduceBase implements Reducer<Pair, Text, Text, Text> {
	@Override
	public void reduce(Pair key, Iterator<Text> values,
			OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {
		
		// we should get the list in decreasing order
		// that means that the index of the first element will be the size of the values
		
		int size = 0;
		int index = 0;
		int rank = 0;
		
		Integer prevCount = null;
		while (values.hasNext()) {
			index++;
			
			String line = values.next().toString();
			String[] fields = line.split(Utils.REGEX_SEPARATOR);
			Integer count = Integer.parseInt(fields[4]);
			
			if(prevCount == null){
				size = Integer.parseInt(fields[5]);
			}
			
			if (prevCount == null || count != prevCount){
				rank = index;
				prevCount = count;
			}

			String fraction = (new StringBuilder())
								.append(rank)
								.append('/')
								.append(size)
								.toString();
			
			// skips 5th column which was the indexing
			String newKey = Utils.joinFields(fields, 0, 4);
			output.collect(new Text(newKey), new Text(fraction));
		}
		
	}

}

