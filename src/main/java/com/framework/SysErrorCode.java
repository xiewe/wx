package com.framework;

import java.util.HashMap;
import java.util.Map;

public interface SysErrorCode {
	public static final Map<String, String> MAP = new HashMap<String, String>() {
		{
			put("1001", "保存失败");
		}
	};

}
