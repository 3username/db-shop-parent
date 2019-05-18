package com.db.core.utils;

import com.db.base.BaseApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

public class Assert extends BaseApiService {
    /**
     * 参数为空抛异常
     * @param object
     * @param message
     */
    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * String参数为空抛异常
     * @param s
     * @param message
     */
    public static void notStringNull(String s, String message) {
        if (StringUtils.isBlank(s)) {
            throw new IllegalArgumentException(message);
        }
    }




    /**
     * 数组
     * @param array
     * @param message
     */
    public static void notEmpty(@Nullable Object[] array, String message) {
        if (ObjectUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }
}
