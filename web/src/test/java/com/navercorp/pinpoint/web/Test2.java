package com.navercorp.pinpoint.web;

import com.navercorp.pinpoint.web.dao.mysql.MysqlApmRecordDao;
import com.navercorp.pinpoint.web.vo.ApmRecord;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jacob on 2018/6/8.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-web.xml"})
public class Test2 {

    @Autowired
    MysqlApmRecordDao dao ;

   @Test
   @Ignore
    public void testReg(){
      Pattern p=  Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
      Matcher matcher =  p.matcher("zhaojiangbo@com.hh");
       if(matcher.matches()){
           System.out.println("ooo");
       }
    }

    @Test
    public void testRecord(){
        System.out.println("oowooei");
        ApmRecord record =  new ApmRecord();
        record.setInTime(new Date());
        record.setSmsMessage("testttt");
        dao.insertApmRecord(record);
    }
}
