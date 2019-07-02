package com.navercorp.pinpoint.web.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * apm_record
 * @author 
 */
public class ApmRecord implements Serializable {
    private Integer id;

    private String checkName;

    private String applicationId;

    private String serviceType;

    private Date inTime;

    private String smsMessage;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ApmRecord other = (ApmRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getApplicationId()== null ? other.getApplicationId()== null : this.getApplicationId().equals(other.getApplicationId()))
            && (this.getServiceType() == null ? other.getServiceType() == null : this.getServiceType().equals(other.getServiceType()))
            && (this.getInTime() == null ? other.getInTime() == null : this.getInTime().equals(other.getInTime()))
            && (this.getSmsMessage() == null ? other.getSmsMessage() == null : this.getSmsMessage().equals(other.getSmsMessage()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getApplicationId()  == null) ? 0 : getApplicationId().hashCode());
        result = prime * result + ((getServiceType() == null) ? 0 : getServiceType().hashCode());
        result = prime * result + ((getInTime() == null) ? 0 : getInTime().hashCode());
        result = prime * result + ((getSmsMessage()== null) ? 0 : getSmsMessage().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", applicationid=").append(applicationId);
        sb.append(", servicetype=").append(serviceType);
        sb.append(", time=").append(inTime);
        sb.append(", smsmessage=").append(smsMessage);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    public String  getApplicationId(){
        return this.applicationId;
    }
}