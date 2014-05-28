import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class PairGrouping extends WritableComparator {
    protected PairGrouping() {
        super(Pair.class, true);
    }  
    @SuppressWarnings("rawtypes")
    @Override
    public int compare(WritableComparable w1, WritableComparable w2) {
        Pair k1 = (Pair)w1;
        Pair k2 = (Pair)w2;
         
        return k1.getKey().compareTo(k2.getKey());
    }
}
