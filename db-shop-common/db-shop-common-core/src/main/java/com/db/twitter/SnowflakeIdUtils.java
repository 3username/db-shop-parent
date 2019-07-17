package com.db.twitter;

/**
 *
 * 
 * 
 * @description: 使用雪花算法生成全局id
 */
public class SnowflakeIdUtils {
	private static com.db.twitter.SnowflakeIdWorker idWorker;
	static {
		// 使用静态代码块初始化 SnowflakeIdWorker
		idWorker = new com.db.twitter.SnowflakeIdWorker(1, 1);
	}

	public static String nextId() {
		return idWorker.nextId() + "";
	}

}
