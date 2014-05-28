# guavusproject
=============
## Problem description 
We have files (TSV) using this structure 
 
service_type (String) subscriber id (Long) service_name (String) timestamp (yyyyMMddhhmm) 
 
Using Hadoop, we first want to calculate the number of unique timestamp done by every 
subscriber per service_type, service_name, subscriber_id, day (yyyyMMdd). 
 
Using this result, the next step(s) are to sort by count and then rank them. For the ranking, you 
need to add a column that include a fraction with the rank as the nominator and the total 
subscriber in the segment as the denominator. See the example below.
### example
gds1	|1236	|browsing	|201307051051
gds1	|1236	|browsing	|201307051051
gds1	|1235	|browsing	|201307051050
gds1	|1236	|browsing	|201307051055
gds1	|1235	|browsing	|201307061051
gds1	|1233	|browsing	|201307061050
gds1	|1233	|browsing	|201307061051
gds1	|1233	|browsing	|201307061052
gds1	|1234	|browsing	|201307061051
gds1	|1234	|browsing	|201307061052
gds1	|1237	|browsing	|201307061052
gds1	|1237	|browsing	|201307061053

## Implementation description
* First of all I have removed all duplicate log lines using <code>DistinctInfoJob</code>. (the example contained duplicate line, but if you are sure that input files do not contain duplicates than you can skip this step.)
<code>DistinctInfoJob</code> has very simple mapper which just emits every line as a key and 1 as a value. Afterwards, during grouping all duplicate lines will be group together and passed to reducer. Here reducer just emits a key for each group and the size of the group.
The result for the above example would be:
gds1	|1233	|browsing	|201307061050	|1
gds1	|1233	|browsing	|201307061051	|1
gds1	|1233	|browsing	|201307061052	|1
gds1	|1234	|browsing	|201307061051	|1
gds1	|1234	|browsing	|201307061052	|1
gds1	|1235	|browsing	|201307051050	|1
gds1	|1235	|browsing	|201307061051	|1
gds1	|1236	|browsing	|201307051051	|2
gds1	|1236	|browsing	|201307051055	|1
gds1	|1237	|browsing	|201307061052	|1
gds1	|1237	|browsing	|201307061053	|1

* Second step is counting number of unique timestamp done by every subscriber per service_type, service_name, subscriber_id, day (yyyyMMdd). This is done by <code>CountJob</code>. Map function here emits service_type, service_name, subscriber_id and first 8 charaters of the timestamp as a key and 1 as a value. Reduce function recieves groups of lines with identical service_type, service_name, subscriber_id, day (yyyyMMdd) and emits them with corresponding group size. Lets call service_type, service_name, subscriber_id, day (yyyyMMdd) a group_key.
The result for the above example would be:
gds1	|1233	|browsing	|20130706	|3
gds1	|1234	|browsing	|20130706	|2
gds1	|1235	|browsing	|20130705	|1
gds1	|1235	|browsing	|20130706	|1
gds1	|1236	|browsing	|20130705	|2
gds1	|1237	|browsing	|20130706	|2

Note that this two steps could have been combined, if we try to eliminate duplicates in the reduce function of the CountJob. That would be faster, however that would assume storing groups in memory. As amount of data might be very big, this might not be possible.

* The next part of the problem is to sort the data within the groups by count. For that we need to do a secondary sort. I have created a new combined key type <code>Pair</code>, where the key would be the group_key and the value would be the count. Additionaly <code>PairGrouping</code> comparator implemented so that hadoop still groups the data according to group_key. The <PairPartitioning> partitioner is implemented to partition data according to group_key. Finally two comparators <code>PairIncComparator</code> and <code>PairDescComparator</code> are implemented to sort the data in groups by count in increasing and descreasing order.

* Besides sorting a ranking of the data inbetween the groups should be done. In addition to ranking the total subscriber in the segment should be calculated for forming the fraction for the last column. Here as well we have a choice of using additional MapReduce job or storing groups in memory for calculating total. Once again I chose to create an additional MapReduce job <code>IndexJob</code>. Here mapper emites <code>Pair</code> combined keys of group_key and count, and original line as value. Then <code>PairIncComparator</code> is used to sort by count after grouping by group_key using <code>PairGrouping</code>. Reduce function here gets the sorted groups and emits the same thing with a increasing index. Note that the last element of the group will contain the highest index and, in fact, the size of the group.
The result for the above example would be:
gds1	|1235	|browsing	|20130705	|1	|1
gds1	|1236	|browsing	|20130705	|2	|2
gds1	|1235	|browsing	|20130706	|1	|1
gds1	|1234	|browsing	|20130706	|2	|2
gds1	|1237	|browsing	|20130706	|2	|3
gds1	|1233	|browsing	|20130706	|3	|4

* The last step is ranking the data and forming the fraction column. This is done by <code>RankJob</code>. In this case mapper emits <code>Pair</code> combined keys of group_key and previously assigned indexes, and original line as value. Then <code>PairDescComparator</code> is used to reverse sort by index. As indexes were assigned after sorting the data in increasing order, the data now will also be reverse sorted by count. Note that now the index of the first element of the group will indicate the size of the group. Then reducer takes the first index as a denominator  and calculates rank of the each element as a nominator for the
fraction. 
The result for the above example would be:
gds1	|1236	|browsing	|20130705	|2	|1/2
gds1	|1235	|browsing	|20130705	|1	|2/2
gds1	|1233	|browsing	|20130706	|3	|1/4
gds1	|1237	|browsing	|20130706	|2	|2/4
gds1	|1234	|browsing	|20130706	|2	|2/4
gds1	|1235	|browsing	|20130706	|1	|4/4

## Running the jobs
The commands for running the jobs are in <code>script</code> file