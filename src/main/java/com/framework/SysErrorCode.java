package com.framework;

import java.util.HashMap;
import java.util.Map;

public interface SysErrorCode {

    public static final String SAVE_FAILED = "1001";
    public static final String ADMIN_CANNOT_DELETE = "1002";

    public static final Map<String, String> MAP = new HashMap<String, String>() {
        {
            put(SAVE_FAILED, "保存失败");
            put(ADMIN_CANNOT_DELETE, "Admin不能被删除");
        }
    };

}
