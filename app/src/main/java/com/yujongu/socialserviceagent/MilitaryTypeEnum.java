package com.yujongu.socialserviceagent;

public enum MilitaryTypeEnum {
    ARMY("Army","육군", 18, 0), MARINE("Marien", "해병대", 18, 1),
    NAVY("Navy", "해군", 20, 2), AIRFORCE("Airforce", "공군", 21, 3),
    POLICE("Police", "의경", 18, 4), SSA("SSA", "사회복무요원", 21,5 ),
    FIRE("Fire", "의무소방", 20, 6);

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
