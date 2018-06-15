package com.navercorp.pinpoint.web.dao.hbase;

import com.navercorp.pinpoint.common.hbase.HBaseTables;
import com.navercorp.pinpoint.common.hbase.HbaseOperations2;
import com.navercorp.pinpoint.common.hbase.ResultsExtractor;
import com.navercorp.pinpoint.common.hbase.RowMapper;
import com.navercorp.pinpoint.common.server.bo.ApiMetaDataBo;
import com.navercorp.pinpoint.web.dao.ApiMetaDataDao;
import com.navercorp.pinpoint.web.dao.hbase.BaseDaoTester;
import com.navercorp.pinpoint.web.mapper.AgentInfoMapper;
import com.navercorp.pinpoint.web.vo.AgentInfo;
import com.sematext.hbase.wd.RowKeyDistributorByHashPrefix;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * description :
 * <p></p>
 *
 * @author Camus
 * @createTime 2018/4/27 16:31
 * @lastUpdater zhaotengfei
 * @lastUpdateTime 2018/4/27 16:31
 **/
public class HbaseApiMetaDataDaoTest2 extends BaseDaoTester {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApiMetaDataDao apiMetaDataDao;

    @Autowired
    private HbaseOperations2 hbaseOperations2;

    @Autowired
    @Qualifier("apiMetaDataMapper")
    private RowMapper<List<ApiMetaDataBo>> apiMetaDataMapper;

    @Autowired
    @Qualifier("metadataRowKeyDistributor")
    private RowKeyDistributorByHashPrefix rowKeyDistributorByHashPrefix;

    @Autowired
    private ApiMetaDataExtractor apiMetaDataExtractor;

    @Test
    public void testGetApiMetaData(){
        String agentId="davincicode1";
        long time = 1514430694842l;
        int apiId = -358;
        List<ApiMetaDataBo> list = apiMetaDataDao.getApiMetaData(agentId, time, apiId);
        int count = 0;
        for(ApiMetaDataBo bo : list){
            logger.info("{}",bo);
            count++;
        }
    }

    @Test
    public void testSanApiMetaData(){
        String agentId="davincicode1";
//        String agentId="davincicode1";
        long time = 1514430694842l;
        ApiMetaDataBo apiMetaDataBo = new ApiMetaDataBo(agentId, Long.MAX_VALUE,  0);
        ApiMetaDataBo apiMetaDataBo3 = new ApiMetaDataBo(agentId, 0, Integer.MAX_VALUE);
        ApiMetaDataBo apiMetaDataBo1 = new ApiMetaDataBo("manage-web1", 1515551533427l, 0);
        ApiMetaDataBo apiMetaDataBo2 = new ApiMetaDataBo("manage-web1", 1515551533427l, Integer.MIN_VALUE);
        byte[] sqlId = getDistributedKey(apiMetaDataBo.toRowKey());
        byte[] sqlId1 = getDistributedKey(apiMetaDataBo1.toRowKey());
        byte[] sqlId2 = getDistributedKey(apiMetaDataBo2.toRowKey());
        byte[] sqlId3 = getDistributedKey(apiMetaDataBo3.toRowKey());
        Scan scan = new Scan();
        char c = '\0';
        scan.setStartRow(sqlId);
//        scan.setStartRow(Bytes.toBytes(c));
//        scan.setStopRow(Bytes .toBytes(c));
        scan.setStopRow(sqlId3);
//        scan.setReversed(true);
//        scan.setMaxVersions(1);
        scan.setCaching(500);
//        scan.setMaxResultSize(1);
        scan.addFamily(HBaseTables.API_METADATA_CF_API);
        logger.info("scan:[{}]",scan.toString());
        List<ApiMetaDataBo> res =  hbaseOperations2.find(HBaseTables.API_METADATA, scan, apiMetaDataExtractor);
        int count = 0;
        for(ApiMetaDataBo bo : res){
            System.out.println(bo.toString());
            count++;
        }
    }

    private byte[] getDistributedKey(byte[] rowKey) {
        return rowKeyDistributorByHashPrefix.getDistributedKey(rowKey);
    }

    @Component
    public static class ApiMetaDataExtractor  implements ResultsExtractor<List<ApiMetaDataBo>> {

        @Autowired
        @Qualifier("apiMetaDataMapper")
        private RowMapper<List<ApiMetaDataBo>> apiMetaDataMapper;

        @Override
        public List<ApiMetaDataBo> extractData(ResultScanner results) throws Exception {
            int found = 0;
            List<ApiMetaDataBo> res = new LinkedList<>();
            for (Result result : results) {
                res.addAll(apiMetaDataMapper.mapRow(result, found++));
            }
            return res;
        }
    }
}
