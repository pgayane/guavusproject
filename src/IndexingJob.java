import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;


public class IndexingJob {
	public static void main(String[] args) throws Throwable{
		
		JobConf conf = new JobConf(IndexingJob.class);
        conf.setJobName("indexing");
        conf.setOutputKeyClass(Text.class); 
        conf.setOutputValueClass(IntWritable.class);
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
    
        conf.setMapperClass(RankMapper.class);
        conf.setReducerClass(IndexingReducer.class);
        conf.setMapOutputKeyClass(Pair.class);
        conf.setMapOutputValueClass(Text.class);
         
        conf.setPartitionerClass(PairPartitioning.class);
        conf.setOutputValueGroupingComparator(PairGrouping.class);
        conf.setOutputKeyComparatorClass(PairIncComparator.class);
    
		FileInputFormat.addInputPath(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
         
		JobClient.runJob(conf);   
	}
}
