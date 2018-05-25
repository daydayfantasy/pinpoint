package com.navercorp.pinpoint.web.alarm;

import com.aixuexi.thor.sms_mail.MAILConstant;
import com.aixuexi.transformers.mq.ONSMQProducer;
import com.aixuexi.transformers.msg.MailSend;
import com.aixuexi.transformers.msg.SmsSend;
import com.aixuexi.thor.sms_mail.SMSConstant;
import com.aixuexi.transformers.msg.SmsSend;
import com.navercorp.pinpoint.web.alarm.checker.AlarmChecker;
import com.navercorp.pinpoint.web.service.ApmRecordService;
import com.navercorp.pinpoint.web.service.UserGroupService;
//import com.navercorp.pinpoint.web.service.UserServiceImpl;
import com.navercorp.pinpoint.web.vo.ApmRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jacob on 2018/5/18.
 */
@Component
public class AlarmMessageSenderImple implements AlarmMessageSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserGroupService userGroupService;

    @Autowired
    ONSMQProducer  mqProducer;

    @Autowired
    ApmRecordService apmRecordService;

    //发送短信
    @Override
    public void sendSms(AlarmChecker checker, int sequenceCount) {

        List<String> receivers = userGroupService.selectPhoneNumberOfMember(checker.getUserGroupId());
        if (receivers.size() == 0) {
            return;
        }
        String checkName = checker.getRule().getCheckerName();
        String  applicationId = checker.getRule().getApplicationId();
        String userGroupId = checker.getRule().getUserGroupId();
        String serviceType = checker.getRule().getServiceType();
        List<String> sms =checker.getSmsMessage();
        StringBuilder smsMessage = new StringBuilder();
        for (String message : sms) {
           smsMessage.append(message);
        }
        Date d = new Date();
       String time =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);

        ApmRecord apmRecord =  new ApmRecord();
        apmRecord.setApplicationId(applicationId);
        apmRecord.setServiceType(serviceType);
        apmRecord.setInTime(d);
        apmRecord.setSmsMessage(smsMessage.toString());
        apmRecord.setCheckName(checkName);
        logger.info("告警类型:{} 在{}的{}\n" +
                "告警时间:{}<br/>\n" +
                "告警信息:{}<br/>",checkName,applicationId,serviceType,time,smsMessage);

        SmsSend.SmsSendObject.Builder builder=   SmsSend.SmsSendObject.newBuilder() ;
        builder.putParam("checkName",checkName).putParam("applicationId",applicationId).putParam("userGroupId",userGroupId).putParam("serviceType",serviceType)
                .putParam("time",time) .putParam("smsMessage",smsMessage.toString());
        builder.setSignName(SMSConstant.SIGN_AIXUEXI);
        builder.setTemplateCode("SMS_135775352");
        builder.addAllPhones(receivers);
        builder.setBusinessType("短信告警信息发送");
        builder.setOperatorId(1);
        //true为实时发送，false为晚上23:30-6：00延迟到早上7点发送,默认为false
        builder.setDelay(true);
        apmRecordService.insertApmRecord(apmRecord);
        mqProducer.send(builder);

    }


    //发送邮件
    @Override
    public void  sendEmail(AlarmChecker checker, int sequenceCount) {
        List<String> receivers = userGroupService.selectEmailOfMember(checker.getUserGroupId());
        if (receivers.size() == 0) {
            return;
        }
        String checkName = checker.getRule().getCheckerName();
        String  applicationId = checker.getRule().getApplicationId();
        String userGroupId = checker.getRule().getUserGroupId();
        String serviceType = checker.getRule().getServiceType();
        List<String> sms =checker.getSmsMessage();
        StringBuilder smsMessage = new StringBuilder();

        for (String message : sms) {
            smsMessage.append(message);
        }

        Date d = new Date();
        String time =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);

        ApmRecord apmRecord =  new ApmRecord();
        apmRecord.setApplicationId(applicationId);
        apmRecord.setServiceType(serviceType);
        apmRecord.setInTime(d);
        apmRecord.setSmsMessage(smsMessage.toString());
        apmRecord.setCheckName(checkName);
        logger.info("告警类型:{} 在{}的{}\n" +
                "告警时间:{}<br/>\n" +
                "告警信息:{}<br/>",checkName,applicationId,serviceType,time,smsMessage);
        MailSend.MailSendObject.Builder builder=MailSend.MailSendObject.newBuilder();
        builder.putParam("checkName",checkName).putParam("applicationId",applicationId).putParam("userGroupId",userGroupId).putParam("serviceType",serviceType)
             .putParam("time",time).putParam("smsMessage",smsMessage.toString());
        builder.setTemplateCode("ALARM_EMAIL");
        //邮件主题
        builder.setSubject("【pinpoint告警】"+ applicationId+"/"+checkName);
        builder.addAllToMails(receivers);
        builder.setBusinessType("邮件告警信息发送");
        builder.setOperatorId(1);
        apmRecordService.insertApmRecord(apmRecord);
        mqProducer.send(builder);
    }
}
