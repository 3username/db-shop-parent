package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016092200570974";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDWFb3X6fWTFsydYdXUQUZjAoo/LHsp0v1qjCVogE1R6xj5tjVUVP1vCpjbocQ1qgDie7erR6krwfocxGINRQW+Qod0EKtxJGa6uzotUGVnmpxRSO/F1OWFP4x5mINSpLk5WnqCYzBrMGMyaz6AmoC+hncMGgjw9zHDoC2izvjO5nWuHtx9phzY3h8Xv/iuc5eBsKK6awBo9f5j5z3WgeM+thseyKoZcktplLuEOQyWQahjR1KR0ZH5qGW1973uuNfkJJXKvUJk4RVdW6ZacTE5c1I+eFW1aC7S5tmAjBdlC/MWf9vFlAMdaCLo3zAN8VkZ+UPxzNjmNQwEQW93LIDDAgMBAAECggEAWyU6hDhpg0wLpV3H6ew4iWLFdQv9C9t2ZOxx6MHnKV9MbNQ0dEkS9TmvxqJipO5SHhrKIbQKxER7tc+uZzPhtBUjEjkepLb4vbuEk1JNOgTLVJgW7UUsysQ5jHNwRbk32sE4s1aDgL9DHG4oWBxvf65D9PUIpX64wy7o/w3bREp2kAEq+rDq6FJjF4OyQqvtKMRoioUKTS3gNxz8WzWK/npdXL5xv20XAq7gpIMHVpU7V03c5Mg44fhtEjn2bSt6gkVvrB6jZ4aow28Yw/y5k248iWx/+PB5WOetqlSu0XZpX7VuXWjgOd/HmsdWtNLR5Wl1eDCd9W12nIIY6zbCkQKBgQDyBo5lIsPphO7dJwQTFzVNeccV3TbETMviz3OKBqnNUrVHz1kRwqxWUsSZk7+FQHefbdEZ9JXdpHPwRXu0eXRC/8tz9Lr2tsK2VZmvtNHf0lo1rEt96bHehFwTLhC/o7sgwukWWrYEnC0gDxs9x9jsKHbxoQa3I52bT8Vw2hIINwKBgQDici/TkJOTHxyRp0WdfZHuBJhNlcDTEq0KRE6HwHxxtUVv9Bwr9yTgqu9MBXhWWCavPbZ7FxELPIMiPBqz791du/9F5c4vqIV1YTJRfNEWRx2ppgB/Jv+/XcgO6hqqLoX0kP+yCD5C412tz9kJAgWACyZcplbDxXWe9ylaChYt1QKBgBwcXtWoYBDze7r8GSEOhQZmqpUDiKTsNpI1fjn95CMRBDU/BB1n+xRbsLIV0xzrC/nci3j0S6PewgiaSXAA21wc0ci6GC0ntR3we1VJL0dL62ZoiKwrRJmPzhH8O73GiqmXefM6vgFzElqy5nsahzuGx6nxBhKndFrhvREKgyWpAoGBAKNtCXtEwlzRfUrZT/Kjp2gCRPDpfSpYNi5MsehjUOy3iI+Nu683RJQoiV9yKzEVmYWrokEfmptjZriUbCkkmbyGa2tEuRPIKdlW9nu8+yF63buMJJufx7+SrUmmRuw/e0nQQ9l2/YhEk11Xvik0HkgUw/Dl04Siw4qgkmV7FhqpAoGAWGfyHN8YwVYMdc/gJxF4VUPnnxNPVm+h1t93laJGC3ETRqMl8FWSOuLrG6lbGai4JzK/iLrbFvnc1Xpkt0h8a/li0Rs1psyiKse+lo511QOFKkQZKMQ30A782Ta15fdmo7dZyqS1mkhPjXjvj96PiEBqcWQ9evA4gsipmme+ZN4=";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoq9ewWtIoI9gnvSOI2Iw3fdIyYg2eV7I6BLzQ1RwtimCYYRquVHfCO1u446tF98Y0vxD3xrevuEw9++BjxTG0/rDeB/gZJGGboJfhSsQFHLCUFnGmg3D3MBQqPWiDNIARu2JGlg6IaUqHzDUDV5CAmuslK77CkVI7moYW9MxqfnAVV5aPpWUt+08gTy9yzcVgO7mTQMVYNbn2S6rP3qU2CfGXrqRkcEEGifRB45ODAnMD+nbGGTN83UTh6RVTSbXuF788oTEUDAftU2nMZWMogi0rC+sGhNxFXiHJu5FQjtjNXFUSd/5mKsYqCyk8uMHBl5TklCgkZA8+WvG/ipPrQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

