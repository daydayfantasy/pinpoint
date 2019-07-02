package com.navercorp.pinpoint.web.dao.hbase;

import com.navercorp.pinpoint.common.hbase.HBaseTables;
import com.navercorp.pinpoint.common.hbase.HbaseOperations2;
import com.navercorp.pinpoint.common.hbase.ResultsExtractor;
import com.navercorp.pinpoint.common.hbase.RowMapper;
import com.navercorp.pinpoint.common.server.bo.ApiMetaDataBo;
import com.navercorp.pinpoint.common.server.bo.SqlMetaDataBo;
import com.navercorp.pinpoint.web.mapper.SqlMetaDataMapper;
import com.sematext.hbase.wd.RowKeyDistributorByHashPrefix;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.junit.Before;
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
 * @createTime 2018/4/28 15:21
 * @lastUpdater zhaotengfei
 * @lastUpdateTime 2018/4/28 15:21
 **/
public class HbaseSqlMetaDataDaoTest2  extends BaseDaoTester {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HbaseOperations2 hbaseOperations2;

//    @Autowired
//    @Qualifier("sqlMetaDataMapper")
    private RowMapper<List<SqlMetaDataBo>> sqlMetaDataMapper ;

    private RowKeyDistributorByHashPrefix rowKeyDistributorByHashPrefix ;

    private SqlMetaDataExtractor SqlMetaDataExtractor;

    @Before
    public void before(){
        rowKeyDistributorByHashPrefix = new com.sematext.hbase.wd.RowKeyDistributorByHashPrefix(new com.navercorp.pinpoint.common.hbase.distributor.RangeOneByteSimpleHash(0, 36,32));
        SqlMetaDataMapper sqlMetaDataMapper_ = new SqlMetaDataMapper();
        sqlMetaDataMapper_.setRowKeyDistributorByHashPrefix(rowKeyDistributorByHashPrefix);
        sqlMetaDataMapper = sqlMetaDataMapper_;
        SqlMetaDataExtractor = new SqlMetaDataExtractor(sqlMetaDataMapper);
    }

    @Test
    public void testScanSqlMetaData(){
        SqlMetaDataBo apiMetaDataBo = new SqlMetaDataBo();
//        byte[] sqlId = getDistributedKey(apiMetaDataBo.toRowKey());
        Scan scan = new Scan();
//        scan.setStartRow(sqlId);
//        scan.setStartRow(Bytes.toBytes(c));
//        scan.setStopRow(Bytes .toBytes(c));
//        scan.setStopRow(sqlId3);
//        scan.setReversed(true);
//        scan.setMaxVersions(1);
        scan.setCaching(500);
//        scan.setMaxResultSize(1);
        scan.addFamily(HBaseTables.SQL_METADATA_VER2_CF_SQL);
        logger.info("scan:[{}]",scan.toString());
        List<List<SqlMetaDataBo>> res =  hbaseOperations2.find(HBaseTables.SQL_METADATA_VER2, scan, SqlMetaDataExtractor);
        int x = 0;
        for(List<SqlMetaDataBo> l : res){
        int y = 0;
            for(SqlMetaDataBo b : l){
                System.out.println(b.toString());
//                System.out.println(x+"/"+y+":"+b.toString());
                y++;
            }
            x++;
        }

    }

    private byte[] getDistributedKey(byte[] rowKey) {
        return rowKeyDistributorByHashPrefix.getDistributedKey(rowKey);
    }

    public static class SqlMetaDataExtractor  implements ResultsExtractor<List<List<SqlMetaDataBo>>> {

        private RowMapper<List<SqlMetaDataBo>> sqlMetaDataMapper;
        public  SqlMetaDataExtractor(RowMapper<List<SqlMetaDataBo>> sqlMetaDataMapper){
            this.sqlMetaDataMapper = sqlMetaDataMapper;
        }

        @Override
        public List<List<SqlMetaDataBo>> extractData(ResultScanner results) throws Exception {
            int found = 0;
            List<List<SqlMetaDataBo>> res = new LinkedList<>();
            for (Result result : results) {
                res.add(sqlMetaDataMapper.mapRow(result, found++));
            }
            return res;
        }

    }
}
