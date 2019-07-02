package com.navercorp.pinpoint.web.task;

import com.navercorp.pinpoint.web.dao.ApplicationRealtimeStatDao;
import com.navercorp.pinpoint.web.service.ApplicationStatService;
import com.navercorp.pinpoint.web.service.CommonService;
import com.navercorp.pinpoint.web.vo.Application;
import com.navercorp.pinpoint.web.vo.ApplicationRealtimeStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * description :
 * <p>定时刷新微服务大盘数据，10分钟一次</p>
 *
 * @author Camus
 * @createTime 2018/3/8 20:43
 * @lastUpdater Camus
 * @lastUpdateTime 2018/3/8 20:43
 **/
@Component
public class ApplicationRealtimeStatTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationRealtimeStatDao statDao;

    @Autowired
    private CommonService commonService;

    @Autowired
    ApplicationStatService statService;

    private ExecutorService exec = Executors.newFixedThreadPool(10);

    @Scheduled(cron = "0 0/10 * * * ?")
    public void run() {
        logger.info("application stat task start...");
        final Date nowTime = new Date();
        //获取服务列表
        List<Application> appList =  commonService.selectAllApplicationNames();
        logger.info("application list size:{}",appList.size());
        final int appSize = appList.size();
        if(appSize == 0)return;
        List<ApplicationRealtimeStat> insertList = new ArrayList<>();
        // 并发查询
        CompletionService<ApplicationRealtimeStat> cs = new ExecutorCompletionService(exec);

        //提交任务
        for(Application app : appList){
            cs.submit(new StatTask(app, statService, nowTime));
        }

        //获取结果
        for(int i = 0;i < appSize;i++){
            ApplicationRealtimeStat stat = null;
            try {
                stat = cs.take().get();
                logger.debug("task result:{}",stat);
                insertList.add(stat);
            } catch (Exception e) {
                logger.warn("thread  get future result error:{}",e.getMessage());
                continue;
            }
        }

//        insertList.add(new ApplicationRealtimeStat("testApp",3,2,300,30,100,60,0.1d,0.2d,0.8d, nowTime,true));
        logger.info("application stat data list size:{}",insertList.size());
        //修改历史数据状态为不显示
        if(insertList.size() > 0){
            statDao.updateIsShow();
            //持久化新的统计数据
            statDao.insertList(insertList);
        }
        logger.info("application stat task end!!!");
    }


    private static class StatTask implements Callable{

        private Application app;


        private ApplicationStatService statService;

        private Date nowTime;

        StatTask(Application app, ApplicationStatService statService, Date nowTime){
            this.app = app;
            this.statService = statService;
            this.nowTime = nowTime;
        }

        @Override
        public ApplicationRealtimeStat call() {
            return this.statService.stat(this.app, nowTime);
        }
    }

}
