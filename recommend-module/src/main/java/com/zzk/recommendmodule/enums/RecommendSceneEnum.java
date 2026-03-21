package com.zzk.recommendmodule.enums;

import lombok.Getter;

@Getter
public enum RecommendSceneEnum {

    HOME("HOME"),
    RELATED("RELATED"),
    ZONE("ZONE"),
    HOT("HOT");

    private final String code;

    RecommendSceneEnum(String code) {
        this.code = code;
    }
}
