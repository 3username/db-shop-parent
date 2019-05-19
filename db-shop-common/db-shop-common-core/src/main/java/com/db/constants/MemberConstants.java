package com.db.constants;

/**
 * 会员常量
 */
public interface MemberConstants {
    // token
    String MEMBER_TOKEN_KEYPREFIX = "mt.mb.login";

    // 安卓的登陆类型
    String MEMBER_LOGIN_TYPE_ANDROID = "Android";
    // IOS的登陆类型
    String MEMBER_LOGIN_TYPE_IOS = "IOS";

    // PC的登陆类型
    String MEMBER_LOGIN_TYPE_PC = "PC";

    // 登陆超时时间 有效期 90天
    Long MEMBRE_LOGIN_TOKEN_TIME = 77776000L;
}
