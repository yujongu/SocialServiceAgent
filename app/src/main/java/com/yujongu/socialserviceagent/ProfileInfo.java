package com.yujongu.socialserviceagent;

import java.util.Date;

public class ProfileInfo {
    private String name;
    private String pictureUrl;
    private MilitaryTypeEnum militaryType;
    private Date startService;
    private Date endService;
    private Date paidLeaveDays;
    private int personalLeaveDays;

    ProfileInfo(String name, String pictureUrl) {
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.militaryType = MilitaryTypeEnum.ARMY;
        this.startService = new Date();
        this.endService = new Date();
        this.personalLeaveDays = 0;
    }

    public Date getPaidLeaveDays() {
        return paidLeaveDays;
    }

    public void setPaidLeaveDays(Date paidLeaveDays) {
        this.paidLeaveDays = paidLeaveDays;
    }

    public int getPersonalLeaveDays() {
        return personalLeaveDays;
    }

    public void setPersonalLeaveDays(int personalLeaveDays) {
        this.personalLeaveDays = personalLeaveDays;
    }

    public Date getStartService() {
        return startService;
    }

    public void setStartService(Date startService) {
        this.startService = startService;
    }

    public Date getEndService() {
        return endService;
    }

    public void setEndService(Date endService) {
        this.endService = endService;
    }

    public MilitaryTypeEnum getMilitaryType() {
        return militaryType;
    }

    public void setMilitaryType(MilitaryTypeEnum militaryType) {
        this.militaryType = militaryType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
