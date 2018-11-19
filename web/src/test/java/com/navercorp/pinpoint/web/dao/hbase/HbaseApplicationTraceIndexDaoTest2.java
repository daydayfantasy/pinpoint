//package com.navercorp.pinpoint.web.dao.hbase;
//
//import com.navercorp.pinpoint.common.hbase.HBaseTables;
//import com.navercorp.pinpoint.common.hbase.HbaseOperations2;
//import com.navercorp.pinpoint.common.hbase.RowMapper;
//import com.navercorp.pinpoint.common.server.util.SpanUtils;
//import com.navercorp.pinpoint.common.util.TransactionId;
//import com.navercorp.pinpoint.web.vo.Range;
//import com.navercorp.pinpoint.web.vo.scatter.Dot;
//import com.sematext.hbase.wd.AbstractRowKeyDistributor;
//import org.apache.hadoop.hbase.client.Scan;
//import org.apache.hadoop.hbase.util.Bytes;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import java.util.List;
//
///**
// * description :
// * <p></p>
// *
// * @author Camus
// * @createTime 2018/5/4 11:04
// * @lastUpdater zhaotengfei
// * @lastUpdateTime 2018/5/4 11:04
// **/
//public class HbaseApplicationTraceIndexDaoTest2   extends BaseDaoTester {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private HbaseOperations2 hbaseOperations2;
//
//    @Autowired
//    @Qualifier("transactionIdMapper")
//    private RowMapper<List<TransactionId>> traceIndexMapper;
//
//    @Autowired
//    @Qualifier("traceIndexScatterMapper")
//    private RowMapper<List<Dot>> traceIndexScatterMapper;
//
//    @Autowired
//    @Qualifier("applicationTraceIndexDistributor")
//    private AbstractRowKeyDistributor traceIdRowKeyDistributor;
//
//    private int scanCacheSize = 256;
//
//    @Test
//    public void testScan(){
//
//    }
//
//    private Scan createScan(String applicationName, Range range, boolean scanBackward) {
//        Scan scan = new Scan();
//        scan.setCaching(this.scanCacheSize);
//
//        byte[] bApplicationName = Bytes.toBytes(applicationName);
//        byte[] traceIndexStartKey = SpanUtils.getTraceIndexRowKey(bApplicationName, range.getFrom());
//        byte[] traceIndexEndKey = SpanUtils.getTraceIndexRowKey(bApplicationName, range.getTo());
//
//        if (scanBackward) {
//            // start key is replaced by end key because key has been reversed
//            scan.setStartRow(traceIndexEndKey);
//            scan.setStopRow(traceIndexStartKey);
//        } else {
//            scan.setReversed(true);
//            scan.setStartRow(traceIndexStartKey);
//            scan.setStopRow(traceIndexEndKey);
//        }
//
//        scan.addFamily(HBaseTables.APPLICATION_TRACE_INDEX_CF_TRACE);
//        scan.setId("ApplicationTraceIndexScan");
//
//        // toString() method of Scan converts a message to json format so it is slow for the first time.
//        logger.trace("create scan:{}", scan);
//        return scan;
//    }
//
//}
