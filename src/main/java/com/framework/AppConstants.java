package com.framework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.framework.utils.PropertiesUtil;

public interface AppConstants {

	/**
	 * 系统全局线程池 -- 阻塞系数0.9可根据项目实际情况调整，取值在0和1之间，计算密集型任务的阻塞系数为0，IO密集型任务的阻塞系数接近1；
	 */
	final ExecutorService EXECUTOR_THREAD_POOL = Executors.newFixedThreadPool((int) (Runtime.getRuntime()
	        .availableProcessors() / (1 - 0.9)));

	/** 服务器地址+端口 ***/
	public final static String SERVER_HOST = PropertiesUtil.getInstance().getKeyValue("server.host");
	public final static String SERVER_PORT = PropertiesUtil.getInstance().getKeyValue("server.port");
	public final static String SERVER_PROTOCOL = PropertiesUtil.getInstance().getKeyValue("server.protocol");
	public final static String SERVER_PROJECT = PropertiesUtil.getInstance().getKeyValue("server.project");

	/** 服务器地址 ***/
	public final static String SERVER_ROOT_URL = PropertiesUtil.getInstance().getKeyValue("server.protocol") + "://"
	        + PropertiesUtil.getInstance().getKeyValue("server.host") + ":"
	        + PropertiesUtil.getInstance().getKeyValue("server.port") + "/"
	        + PropertiesUtil.getInstance().getKeyValue("server.project") + "/";
	
	public final static int SUCCESS = 0;
	public final static int FAILED = -1;

	public final static Integer PAGE_SIZE_DEFAULT = 15;

	/**
	 * token 有效期7*24小时，转换毫秒7*24*3600*1000,单位毫秒
	 */
	public static final int TOKEN_EXPIRES = 604800000;

	/**
	 * 登录用户
	 */
	public final static String LOGIN_USER = "login_user";

	/**
	 * 验证码
	 */
	public final static String CAPTCHA_KEY = "captcha";

	/**
	 * 日志参数
	 */
	public final static String LOG_ARGUMENTS = "log_arguments";

	/**
	 * 动态查询，参数前缀
	 */
	public final static String SEARCH_PREFIX = "search_";

	/**
	 * 内部动态查询参数常量
	 */
	public final static String NEST_DYNAMIC_SEARCH = "$nest_dynamic_search$";

	/**
	 * 内部动态查询参数常量，logical
	 */
	public final static String NEST_DYNAMIC_SEARCH_LOGICAL = "$nest_dynamic_search_logical$";

	/**
	 * 用户状态 0-enabled, 1-disabled
	 */
	public static final int USER_STATUS_ENABLED = 0;
	public static final int USER_STATUS_DISABLED = 1;

	/**
	 * 用户类型 0-系统用户不能删除；1-管理后台用户；2-终端注册用户
	 */
	public static final int USER_TYPE_SUPERADMIN = 0;
	public static final int USER_TYPE_CONSOLE = 1;
	public static final int USER_TYPE_APP = 2;

	/**
	 * 下载路径
	 */
	public static final String DOWNLOAD_PATH = "download.path";

	/**
	 * 文件类型：语音
	 */
	public static final int UPLOAD_FILE_TYPE_VOICE = 1;
	/**
	 * 文件类型：图片
	 */
	public static final int UPLOAD_FILE_TYPE_IMAGE = 2;

}
