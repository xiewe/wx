package com.framework.utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * 
 */
public class IDUtil {
    private static AtomicInteger i = new AtomicInteger(1);
    private static final int I_MAX = 10000;

    private static int getNumber() {
        int ret = i.getAndIncrement();
        if (ret >= I_MAX) {
            i.set(ret / I_MAX);
        }
        return 10000 + ret;
    }

    public static String getSN() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date()) + getNumber();
    }

	private static SecureRandom random = new SecureRandom();

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间有-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid2() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 使用SecureRandom随机生成Long.
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	/**
	 * 基于Base62编码的SecureRandom随机生成bytes.
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return EncodesUtil.encodeBase62(randomBytes);
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
		System.out.println(randomBase62(16));
	}
}
