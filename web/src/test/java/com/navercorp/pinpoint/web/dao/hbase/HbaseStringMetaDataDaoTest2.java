package com.navercorp.pinpoint.web.dao.hbase;

import com.navercorp.pinpoint.common.hbase.HBaseTables;
import com.navercorp.pinpoint.common.hbase.HbaseOperations2;
import com.navercorp.pinpoint.common.hbase.ResultsExtractor;
import com.navercorp.pinpoint.common.hbase.RowMapper;
import com.navercorp.pinpoint.common.server.bo.ApiMetaDataBo;
import com.navercorp.pinpoint.common.server.bo.SqlMetaDataBo;
import com.navercorp.pinpoint.common.server.bo.StringMetaDataBo;
import com.navercorp.pinpoint.web.dao.hbase.BaseDaoTester;
import com.sematext.hbase.wd.RowKeyDistributorByHashPrefix;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * description :
 * <p></p>
 *
 * @author Camus
 * @createTime 2018/5/3 10:15
 * @lastUpdater zhaotengfei
 * @lastUpdateTime 2018/5/3 10:15
 **/
public class HbaseStringMetaDataDaoTest2  extends BaseDaoTester {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HbaseOperations2 hbaseOperations2;

    @Autowired
    @Qualifier("stringMetaDataMapper")
    private RowMapper<List<StringMetaDataBo>> stringMetaDataMapper;

    @Autowired
    @Qualifier("metadataRowKeyDistributor")
    private RowKeyDistributorByHashPrefix rowKeyDistributorByHashPrefix;

    @Autowired
    private StringMetaDataExtractor StringMetaDataExtractor;

    private byte[] getDistributedKey(byte[] rowKey) {
        return rowKeyDistributorByHashPrefix.getDistributedKey(rowKey);
    }

    @Test
    public void testScanStringMetaData(){
        Scan scan = new Scan();
        scan.setCaching(500);
        scan.addFamily(HBaseTables.STRING_METADATA_CF_STR);
        List<List<StringMetaDataBo>> res =  hbaseOperations2.find(HBaseTables.STRING_METADATA, scan, StringMetaDataExtractor);
        int x = 0;
        for(List<StringMetaDataBo> l : res){
            int y = 0;
            for(StringMetaDataBo b : l){
                System.out.println(b.toString());
//                System.out.println(x+"/"+y+":"+b.toString());
                y++;
            }
            x++;
        }
    }

    @Component
    public static class StringMetaDataExtractor  implements ResultsExtractor<List<List<StringMetaDataBo>>> {
        @Autowired
        @Qualifier("stringMetaDataMapper")
        private RowMapper<List<StringMetaDataBo>> stringMetaDataMapper;

        @Override
        public List<List<StringMetaDataBo>> extractData(ResultScanner results) throws Exception {
            int found = 0;
            List<List<StringMetaDataBo>> res = new LinkedList<>();
            for (Result result : results) {
                res.add(stringMetaDataMapper.mapRow(result, found++));
            }
            return res;
        }
    }
}
