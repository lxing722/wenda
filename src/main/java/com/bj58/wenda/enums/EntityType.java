package com.bj58.wenda.enums;

public enum EntityType {
    ENTITY_QUESTION(1, "问题"),
    ENTITY_COMMENT(2, "评论"),
    ENTITY_USER(3, "用户")
    ;
    private int code;

    private String desc;

    EntityType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
