package com.navercorp.pinpoint.web.dao.hbase;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * description :
 * <p></p>
 *
 * @author Camus
 * @createTime 2018/4/27 16:29
 * @lastUpdater zhaotengfei
 * @lastUpdateTime 2018/4/27 16:29
 **/
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(locations = {"classpath:applicationContext-web.xml"})
@ContextConfiguration(locations = {"classpath:servlet-context.xml", "classpath:applicationContext-web.xml"})
public class BaseDaoTester {
}
