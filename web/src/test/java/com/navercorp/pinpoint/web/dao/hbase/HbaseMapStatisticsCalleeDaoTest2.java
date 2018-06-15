package com.navercorp.pinpoint.web.dao.hbase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.pinpoint.common.trace.ServiceTypeFactory;
import com.navercorp.pinpoint.web.applicationmap.rawdata.LinkDataMap;
import com.navercorp.pinpoint.web.vo.Application;
import com.navercorp.pinpoint.web.vo.Range;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * description :
 * <p></p>
 *
 * @author Camus
 * @createTime 2018/5/17 11:58
 * @lastUpdater zhaotengfei
 * @lastUpdateTime 2018/5/17 11:58
 **/
public class HbaseMapStatisticsCalleeDaoTest2  extends BaseDaoTester  {

    @Autowired
    private HbaseMapStatisticsCalleeDao hbaseMapStatisticsCalleeDao;


    @Test
    public void testSelectCallee() throws JsonProcessingException {
        Application app = new Application("inception", ServiceTypeFactory.of(1010, "TOMCAT"));
        Range range = new Range(1514803380000l, 1514976180000l);
        LinkDataMap linkDataMap = hbaseMapStatisticsCalleeDao.selectCallee(app, range);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(linkDataMap);
        System.out.println(json);
    }
}
