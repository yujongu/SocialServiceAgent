package com.yujongu.socialserviceagent;

import java.util.Date;

public class ProfileInfo {
    private String name;
    private String pictureUrl;
    private MilitaryTypeEnum militaryType;
    private Date startService;
    private Date endService;
    private int discoutDays;
    private long totalDays;
    private int totalMonths;
    private String paidLeaveDays;
    private int personalLeaveDays;
    private int rewardDays;
    private int specialDays;
    private int sickDays;

    ProfileInfo(String name, String pictureUrl) {
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.militaryType = MilitaryTypeEnum.ARMY;
        this.startService = new Date();
        this.endService = new Date();
        this.personalLeaveDays = 0;
        this.rewardDays = 0;
        this.specialDays = 0;
        this.sickDays = 0;
    }

    public int getTotalMonths() {
        return totalMonths;
    }

    public void setTotalMonths(int totalMonths) {
        this.totalMonths = totalMonths;
    }

    public long getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(long totalDays) {
        this.totalDays = totalDays;
    }

    public int getDiscoutDays() {
        return discoutDays;
    }

    public void setDiscoutDays(int discoutDays) {
        this.discoutDays = discoutDays;
    }

    public int getSickDays() {
        return sickDays;
    }

    public void setSickDays(int sickDays) {
        this.sickDays = sickDays;
    }

    public int getRewardDays() {
        return rewardDays;
    }

    public void setRewardDays(int rewardDays) {
        this.rewardDays = rewardDays;
    }

    public int getSpecialDays() {
        return specialDays;
    }

    public void setSpecialDays(int specialDays) {
        this.specialDays = specialDays;
    }

    public String getPaidLeaveDays() {
        return paidLeaveDays;
    }

    public void setPaidLeaveDays(String paidLeaveDays) {
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
