package com.navercorp.pinpoint.web.service;

import com.navercorp.pinpoint.common.server.util.AgentLifeCycleState;
import com.navercorp.pinpoint.web.scatter.DotGroups;
import com.navercorp.pinpoint.web.scatter.ScatterData;
import com.navercorp.pinpoint.web.vo.AgentInfo;
import com.navercorp.pinpoint.web.vo.Application;
import com.navercorp.pinpoint.web.vo.ApplicationRealtimeStat;
import com.navercorp.pinpoint.web.vo.Range;
import com.navercorp.pinpoint.web.vo.scatter.Dot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * description :
 * <p></p>
 *
 * @author Camus
 * @createTime 2018/3/9 15:47
 * @lastUpdater Camus
 * @lastUpdateTime 2018/3/9 15:47
 **/
@Service
public class ApplicationStatService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private  AgentInfoService agentInfoService;

    @Autowired
    private ScatterChartService scatterService;

    private final long TEN_MINUTE_OFFSET = 1000 * 60 * 10;

    private final int DUBBO_SLOW_LIMIT = 300;

    private final int WEB_SLOW_LIMIT = 500;

    public ApplicationRealtimeStat stat_(Application app, Date nowTime){
        return new ApplicationRealtimeStat("testApp",3,2,300,30,100,60,0.1d,0.2d,0.8d, nowTime,true);
    }

    /**
     * 统计并组装数据<br/>
     * <p>
     *     agentCount 节点数
     *     agentLiveCount 节点存活数
     *     reqTotal 请求数
     *     req_error 请求错误数
     *     rsp_time_avg 平均响应时间
     *     slow_count 慢查询数
     *     req_error_rate 错误率 = req_error/reqTotal
     *     slow_rate 慢查询率 = slow_count/reqTotal
     *     healthy_rate 健康度  = ( agentLiveCount / agentCount * 50 + (1-(req_error/reqTotal+slow_count/reqTotal)) * 50 )/100
     *
     * <p/>
     * @param app
     * @param nowTime 当前统计时间
     * @return 组装的统计数据
     */
    public ApplicationRealtimeStat stat(Application app, Date nowTime){
        boolean isDubbo = app.getServiceTypeCode() == 1110;
        final long to = (nowTime.getTime()/1000l)*1000l ;
        final long from = to - TEN_MINUTE_OFFSET;
        final String appname = app.getName();
        ApplicationRealtimeStat statData = new ApplicationRealtimeStat(appname,0,0,0l,
                0l,0,0l,0d,0d,0d, nowTime,true);
        try{
            buildAgentData(statData, appname, to);
            final Range range = Range.createUncheckedRange(from, to);
            final int limit = 5000;
            ScatterData scatterData = scatterService.selectScatterData(appname, range, 1974,6, limit, true);
            int docSize = scatterData.getDotSize();
            List<Dot> dots = new LinkedList<>();
            addDots(scatterData, dots);
            while(docSize >= limit){
                long lastFrom = scatterData.getOldestAcceptedTime();
                final Range tmpRange = Range.createUncheckedRange(from, lastFrom);
                scatterData = scatterService.selectScatterData(appname, tmpRange, 1974,6, limit, true);
                docSize = scatterData.getDotSize();
                addDots(scatterData, dots);
            }
            setResponseData(statData, dots,  isDubbo);
        }catch (Exception e){
            logger.warn("task error:{}",e.getMessage());
        }

        //return new ApplicationRealtimeStat("testApp",3,2,300,30,100,60,0.1d,0.2d,0.8d, nowTime,true);
        return statData;
    }

    private void addDots(ScatterData scatterData, List<Dot> dots){
        Map<Long, DotGroups> sortedScatterDataMap = scatterData.getSortedScatterDataMap();
        for (Map.Entry<Long, DotGroups> entry : sortedScatterDataMap.entrySet()) {
            DotGroups dotGroups = entry.getValue();
            Set<Dot> dotSet = dotGroups.getSortedDotSet();
            dots.addAll(dotSet);
        }
    }

    private void setResponseData(ApplicationRealtimeStat statData, List<Dot> dots, boolean isDubbo){
        long totalRspTime = 0l;
        long totalCount = 0l;
        long errorCount = 0l;
        long slowCount = 0l;
        int slowLimit = isDubbo ? DUBBO_SLOW_LIMIT : WEB_SLOW_LIMIT;
        for(Dot dot : dots){
            if( dot.getSimpleExceptionCode() == Dot.FAILED_STATE ) errorCount ++ ;
            if( dot.getElapsedTime() > slowLimit )slowCount ++ ;
            totalRspTime += dot.getElapsedTime();
            totalCount ++ ;

        }
        statData.setReqTotal( totalCount );
        statData.setReqError( errorCount );
        statData.setSlowCount( slowCount );
        statData.setRspTimeAvg( totalCount > 0 ? (int) (totalRspTime/totalCount) : 0);
        double errorRate = totalCount > 0 ? (double) errorCount / (double)totalCount : 0d;
        double slowRate = totalCount > 0 ? (double)slowCount / (double)totalCount : 0d;
        double temp = (1d-(errorRate + slowRate));
        double healthyRate = ( (double)(statData.getAgentLiveCount()) / (double)(statData.getAgentCount()) * 50d + ( temp > 0 ? temp : 0d) * 50d )/100d;
        statData.setReqErrorRate(formateDouble(errorRate));
        statData.setSlowRate(formateDouble(slowRate));
        statData.setHealthyRate(formateDouble(healthyRate));
    }

    private void buildAgentData(ApplicationRealtimeStat statData, String appname, long to){
        Set<AgentInfo> agentSet = agentInfoService.getAgentsByApplicationName(appname, to);
        statData.setAgentCount(agentSet.size());
        int liveCount = 0;
        for(AgentInfo agent : agentSet){
            AgentLifeCycleState status = agent.getStatus().getState();
            if(status != AgentLifeCycleState.SHUTDOWN && status != AgentLifeCycleState.UNEXPECTED_SHUTDOWN ){
                liveCount ++ ;
            }
        }
        statData.setAgentLiveCount(liveCount);
    }

    private double formateDouble(double d){
        BigDecimal   b   =   new   BigDecimal(d);
        return b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
