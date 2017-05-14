package com.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderSNGenerator {
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
}
