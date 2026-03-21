package com.zzk.recommendmodule.enums;

import lombok.Getter;

@Getter
public enum RecommendSourceEnum {

    HOT("HOT"),
    LATEST("LATEST"),
    CATEGORY_PREF("CATEGORY_PREF"),
    TAG_PREF("TAG_PREF"),
    ITEM_CF("ITEM_CF"),
    ZONE_HOT("ZONE_HOT"),
    FALLBACK("FALLBACK");

    private final String code;

    RecommendSourceEnum(String code) {
        this.code = code;
    }
}
