package com.navercorp.pinpoint.web.vo;

import java.util.Date;

/**
 * description :
 * <p>应用实时（10m周期）统计</p>
 *
 * @author zhaotengfei
 * @createTime 2018/3/8 19:53
 * @lastUpdater zhaotengfei
 * @lastUpdateTime 2018/3/8 19:53
 **/
public class ApplicationRealtimeStat {

    private Long id;

    private String applicationId;

    private int agentCount;

    private int agentLiveCount;

    private long reqTotal;

    private long reqError;

    private int rspTimeAvg;

    private long slowCount;

    private double reqErrorRate;

    private double slowRate;

    private double healthyRate;

    private Date statTime;

    private boolean isShow;

    public ApplicationRealtimeStat(){}

    public ApplicationRealtimeStat(String applicationId, int agentCount, int agentLiveCount, long reqTotal, long reqError, int rspTimeAvg, long slowCount, double reqErrorRate, double slowRate, double healthyRate, Date statTime, boolean isShow) {
        this.applicationId = applicationId;
        this.agentCount = agentCount;
        this.agentLiveCount = agentLiveCount;
        this.reqTotal = reqTotal;
        this.reqError = reqError;
        this.rspTimeAvg = rspTimeAvg;
        this.slowCount = slowCount;
        this.reqErrorRate = reqErrorRate;
        this.slowRate = slowRate;
        this.healthyRate = healthyRate;
        this.statTime = statTime;
        this.isShow = isShow;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public int getAgentCount() {
        return agentCount;
    }

    public void setAgentCount(int agentCount) {
        this.agentCount = agentCount;
    }

    public int getAgentLiveCount() {
        return agentLiveCount;
    }

    public void setAgentLiveCount(int agentLiveCount) {
        this.agentLiveCount = agentLiveCount;
    }

    public long getReqTotal() {
        return reqTotal;
    }

    public void setReqTotal(long reqTotal) {
        this.reqTotal = reqTotal;
    }

    public long getReqError() {
        return reqError;
    }

    public void setReqError(long reqError) {
        this.reqError = reqError;
    }

    public int getRspTimeAvg() {
        return rspTimeAvg;
    }

    public void setRspTimeAvg(int rspTimeAvg) {
        this.rspTimeAvg = rspTimeAvg;
    }

    public long getSlowCount() {
        return slowCount;
    }

    public void setSlowCount(long slowCount) {
        this.slowCount = slowCount;
    }

    public double getReqErrorRate() {
        return reqErrorRate;
    }

    public void setReqErrorRate(double reqErrorRate) {
        this.reqErrorRate = reqErrorRate;
    }

    public double getSlowRate() {
        return slowRate;
    }

    public void setSlowRate(double slowRate) {
        this.slowRate = slowRate;
    }

    public double getHealthyRate() {
        return healthyRate;
    }

    public void setHealthyRate(double healthyRate) {
        this.healthyRate = healthyRate;
    }

    public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    @Override
    public String toString() {
        return "ApplicationRealtimeStat{" +
                "id=" + id +
                ", applicationId='" + applicationId + '\'' +
                ", agentCount=" + agentCount +
                ", agentLiveCount=" + agentLiveCount +
                ", reqTotal=" + reqTotal +
                ", reqError=" + reqError +
                ", rspTimeAvg=" + rspTimeAvg +
                ", slowCount=" + slowCount +
                ", reqErrorRate=" + reqErrorRate +
                ", slowRate=" + slowRate +
                ", healthyRate=" + healthyRate +
                ", statTime=" + statTime +
                ", isShow=" + isShow +
                '}';
    }
}
