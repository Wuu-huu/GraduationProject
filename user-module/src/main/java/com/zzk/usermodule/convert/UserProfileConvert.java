package com.zzk.usermodule.convert;

import com.zzk.usermodule.entity.UserProfile;
import com.zzk.usermodule.entity.UserSetting;
import com.zzk.usermodule.vo.UserProfileVO;
import com.zzk.usermodule.vo.UserSettingVO;

/**
 * 用户域对象转换器。
 */
public final class UserProfileConvert {

    private UserProfileConvert() {
    }

    public static UserProfileVO toProfileVO(UserProfile profile) {
        if (profile == null) {
            return null;
        }
        return UserProfileVO.builder()
                .uid(profile.getUid())
                .nickname(profile.getNickname())
                .avatarUrl(profile.getAvatarUrl())
                .backgroundUrl(profile.getBackgroundUrl())
                .gender(profile.getGender())
                .birthday(profile.getBirthday())
                .signature(profile.getSignature())
                .province(profile.getProvince())
                .city(profile.getCity())
                .authType(profile.getAuthType())
                .authDesc(profile.getAuthDesc())
                .build();
    }

    public static UserSettingVO toSettingVO(UserSetting setting) {
        if (setting == null) {
            return null;
        }
        return UserSettingVO.builder()
                .uid(setting.getUid())
                .openRecommend(intToBoolean(setting.getOpenRecommend()))
                .openPush(intToBoolean(setting.getOpenPush()))
                .openDm(intToBoolean(setting.getOpenDm()))
                .openFollowVisible(intToBoolean(setting.getOpenFollowVisible()))
                .openFavoriteVisible(intToBoolean(setting.getOpenFavoriteVisible()))
                .build();
    }

    public static Integer booleanToInt(Boolean value) {
        return Boolean.TRUE.equals(value) ? 1 : 0;
    }

    public static Boolean intToBoolean(Integer value) {
        return value != null && value == 1;
    }
}
