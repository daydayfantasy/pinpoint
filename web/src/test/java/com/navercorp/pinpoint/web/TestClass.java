package com.navercorp.pinpoint.web;


import com.aixuexi.thor.sms_mail.MAILConstant;
import com.aixuexi.thor.sms_mail.SMSConstant;
import com.aixuexi.transformers.mq.ONSMQProducer;
import com.aixuexi.transformers.msg.MailSend;
import com.aixuexi.transformers.msg.SmsSend;
import com.navercorp.pinpoint.web.dao.mysql.MysqlApmRecordDao;
import com.navercorp.pinpoint.web.vo.ApmRecord;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by cuiyongyong on 2017/4/24.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-web.xml"})
public class TestClass {

    @Autowired
    ONSMQProducer  mqProducer;

    @Autowired
    MysqlApmRecordDao dao ;


    @Test
    @Ignore
    public  void TestSms(){
        SmsSend.SmsSendObject.Builder builder=   SmsSend.SmsSendObject.newBuilder() ;
        builder.setSignName(SMSConstant.SIGN_AIXUEXI);
        builder.putParam("name","cyy");
        builder.setTemplateCode(SMSConstant.TEMPLATE_CODE_INSTITUTION_CHECK_PASS);
        builder.addPhones("18801316612");
        builder.setBusinessType(SMSConstant.BUSINESS_TYPE_INSTITUTION_CHECK_PASS);
        builder.setOperatorId(1);
        //true为实时发送，false为晚上23:30-6：00延迟到早上7点发送,默认为false
        builder.setDelay(true);
        mqProducer.send(builder);
    }


    @Test
    @Ignore
    public  void TestMail(){
        MailSend.MailSendObject.Builder builder= MailSend.MailSendObject.newBuilder();
        builder.setTemplateCode(MAILConstant.TEMPLATE_CODE_INSTITUTION_CHECK_PASS);
        //邮件主题
        builder.setSubject("test");
        builder.putParam("name","cyy");
        builder.addToMails("cuiyongyong@gaosiedu.com");
        builder.setBusinessType(MAILConstant.BUSINESS_TYPE_INSTITUTION_CHECK_PASS);
        builder.setOperatorId(1);
        mqProducer.send(builder);
    }


    @Test
    public void testRecord(){
       ApmRecord record =  new ApmRecord();
        record.setInTime(new Date());
        record.setSmsMessage("testttt");
        dao.insertApmRecord(record);
    }







}
