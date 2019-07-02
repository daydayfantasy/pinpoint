package com.navercorp.pinpoint.web.dao.hbase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.pinpoint.common.hbase.HbaseOperations2;
import com.navercorp.pinpoint.common.trace.ServiceType;
import com.navercorp.pinpoint.common.trace.ServiceTypeFactory;
import com.navercorp.pinpoint.web.applicationmap.rawdata.LinkData;
import com.navercorp.pinpoint.web.applicationmap.rawdata.LinkDataMap;
import com.navercorp.pinpoint.web.vo.Application;
import com.navercorp.pinpoint.web.vo.Range;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * description :
 * <p></p>
 *
 * @author Camus
 * @createTime 2018/5/17 10:32
 * @lastUpdater zhaotengfei
 * @lastUpdateTime 2018/5/17 10:32
 **/
public class HbaseMapStatisticsCallerDaoTest2   extends BaseDaoTester {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HbaseMapStatisticsCallerDao HbaseMapStatisticsCallerDao;

    @Test
    public void testSelectCaller() throws JsonProcessingException {
        Application app = new Application("inception", ServiceTypeFactory.of(1010, "TOMCAT"));
        Range range = new Range(1514803380000l, 1514976180000l);
        LinkDataMap linkDataMap = HbaseMapStatisticsCallerDao.selectCaller(app, range);
        ObjectMapper mapper = new ObjectMapper();
        Collection<LinkData> list =  linkDataMap.getLinkDataList();
        for (LinkData linkData:list
             ) {
        String json = mapper.writeValueAsString(linkData);
//        System.out.println(json);

        }
        String json = mapper.writeValueAsString(linkDataMap);
        System.out.println(json);

    }
}
