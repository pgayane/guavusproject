import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;


public class CountJob {
	public static void main(String[] args) throws Throwable {

		JobConf conf = new JobConf(CountJob.class);
        conf.setJobName("count");
        conf.setOutputKeyClass(Text.class); 
        conf.setOutputValueClass(IntWritable.class);
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        conf.setMapperClass(CountMapper.class);
        conf.setReducerClass(CountReducer.class);
        
		FileInputFormat.addInputPath(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		
		JobClient.runJob(conf);
	}
}
