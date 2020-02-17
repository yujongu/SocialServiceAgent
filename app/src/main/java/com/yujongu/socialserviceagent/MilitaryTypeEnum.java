package com.yujongu.socialserviceagent;

public enum MilitaryTypeEnum {
    ARMY("Army","육군", 21, 0), MARINE("Marien", "해병대", 21, 1),
    NAVY("Navy", "해군", 23, 2), AIRFORCE("Airforce", "공군", 24, 3),
    POLICE("Police", "의경", 21, 4), SSA("SSA", "사회복무요원", 24,5 ),
    FIRE("Fire", "의무소방", 23, 6);

    private int value;
    private String kName;
    private String eName;
    private int position;

    MilitaryTypeEnum(String eName, String kName, int value, int position) {
        this.value = value;
        this.eName = eName;
        this.kName = kName;
        this.position = position;
    }

    public String getEName(){
        return eName;
    }

    public String getKName(){
        return kName;
    }

    public int getValue(){
        return value;
    }

    public int getPosition(){
        return position;
    }



}
