package com.db.pay.factory;

import com.db.pay.strategy.PayStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**初始化不同的策略行为*/
public class StrategyFactory {
    private static Map<String, PayStrategy> strategyBean =
            new ConcurrentHashMap<String, PayStrategy>();
    public static PayStrategy getPayStrategy(String classAddres) {
        if(StringUtils.isBlank(classAddres)){
            return null;
        }
        try {
            // 1.使用Java的反射机制初始化子类
            Class<?> forName = Class.forName(classAddres);
            // 2.反射机制初始化对象
            PayStrategy payStrategy = (PayStrategy) forName.newInstance();
            strategyBean.put(classAddres, payStrategy);
            return payStrategy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
