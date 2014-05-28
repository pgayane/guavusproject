import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;


public class PairPartitioning implements Partitioner<Pair, Text> {
	 
    @Override
    public int getPartition(Pair key, Text val, int numPartitions) {
        int hash = key.getKey().hashCode();
        int partition = hash % numPartitions;
        return partition;
    }

	@Override
	public void configure(JobConf arg0) {
		// TODO Auto-generated method stub
		
	}
 
}
