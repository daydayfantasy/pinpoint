package com.navercorp.pinpoint.web.dao.hbase;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.pinpoint.common.hbase.HbaseOperations2;
import com.navercorp.pinpoint.common.hbase.RowMapper;
import com.navercorp.pinpoint.common.server.bo.SpanBo;
import com.navercorp.pinpoint.common.server.bo.serializer.RowKeyEncoder;
import com.navercorp.pinpoint.common.server.bo.serializer.trace.v2.SpanEncoder;
import com.navercorp.pinpoint.common.util.TransactionId;
import com.navercorp.pinpoint.common.util.TransactionIdUtils;
import com.navercorp.pinpoint.web.mapper.CellTraceMapper;
import net.minidev.json.JSONUtil;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.shaded.org.mortbay.util.ajax.JSON;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jettison.json.JSONString;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * description :
 * <p></p>
 *
 * @author Camus
 * @createTime 2018/5/8 15:49
 * @lastUpdater zhaotengfei
 * @lastUpdateTime 2018/5/8 15:49
 **/
public class HbaseTraceV2DaoTest2  extends BaseDaoTester {

    @Autowired
    HbaseTraceDaoV2 HbaseTraceDaoV2;

    @Autowired
    private HbaseOperations2 template2;

    @Autowired
    @Qualifier("traceRowKeyEncoderV2")
    private RowKeyEncoder<TransactionId> rowKeyEncoder;


    private RowMapper<List<SpanBo>> spanMapperV2;


    @Value("#{pinpointWebProps['web.hbase.selectSpans.limit'] ?: 500}")
    private int selectSpansLimit;

    @Value("#{pinpointWebProps['web.hbase.selectAllSpans.limit'] ?: 500}")
    private int selectAllSpansLimit;

    private final Filter spanFilter = createSpanQualifierFilter();

    @Autowired
    @Qualifier("spanMapperV2")
    public void setSpanMapperV2(RowMapper<List<SpanBo>> spanMapperV2) {
        final Logger logger = LoggerFactory.getLogger(spanMapperV2.getClass());
        if (logger.isDebugEnabled()) {
            spanMapperV2 = CellTraceMapper.wrap(spanMapperV2);
        }
        this.spanMapperV2 = spanMapperV2;
    }

    @Test
    public void testScan() throws JsonProcessingException {
        //inception1^1514861408116^16
        //davincicode1^1514430694842^609
        String traceIdParam = "inception1^1514861408116^7";
//        String traceIdParam = "davincicode1^1514430694842^635";
        final TransactionId transactionId = TransactionIdUtils.parseTransactionId(traceIdParam);
        List<SpanBo> spans= HbaseTraceDaoV2.selectSpan(transactionId);
        int i = 0;
        ObjectMapper mapper = new ObjectMapper();
        for (SpanBo span: spans) {
            String json = mapper.writeValueAsString(span);
            System.out.println(json);
//            System.out.println(++i+":"+json);
        }
        Bytes.toBytes(123l);
    }

    @Test
    public void testSelectSpan(){

    }

    public QualifierFilter createSpanQualifierFilter() {
        byte indexPrefix = SpanEncoder.TYPE_SPAN;
        BinaryPrefixComparator prefixComparator = new BinaryPrefixComparator(new byte[] {indexPrefix});
        QualifierFilter qualifierPrefixFilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, prefixComparator);
        return qualifierPrefixFilter;
    }

}
