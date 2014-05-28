import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/* comparator class for comparing pair values in increasing order */
public class PairIncComparator extends WritableComparator {
    protected PairIncComparator() {
        super(Pair.class, true);
    }  
    @SuppressWarnings("rawtypes")
    @Override
    public int compare(WritableComparable w1, WritableComparable w2) {
        Pair k1 = (Pair)w1;
        Pair k2 = (Pair)w2;
         
        int result = k1.getKey().compareTo(k2.getKey());
        if(0 == result) {
            result = k1.getValue().compareTo(k2.getValue());
        }
        return result;
    }
}
