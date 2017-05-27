package com.framework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.framework.utils.PropertiesUtil;

/**
 * 常量
 *
 * @author dengyong
 *
 */
public interface AppConstants {

	/**
	 * 系统全局线程池 -- 阻塞系数0.9可根据项目实际情况调整，取值在0和1之间，计算密集型任务的阻塞系数为0，IO密集型任务的阻塞系数接近1；
	 */
	final ExecutorService EXECUTOR_THREAD_POOL = Executors
			.newFixedThreadPool((int) (Runtime.getRuntime().availableProcessors() / (1 - 0.9)));

	/** bizchat服务器地址 ***/
	public final static String BIZCHAT_SERVER_ADDR = PropertiesUtil.getInstance().getKeyValue("bizchat.server.addr");

	/** 服务器地址 ***/
	public final static String SERVER_URL = "http://" + PropertiesUtil.getInstance().getKeyValue("bizchat.server.addr") + "/cwbizchat/";

	/** xmpp服务器地址 ***/
	public final static String XMPP_SERVER_HOST = PropertiesUtil.getInstance().getKeyValue("xmpp.server.host");

	/** xmpp服务器名称 ***/
	public final static String XMPP_SERVER_NAME = PropertiesUtil.getInstance().getKeyValue("xmpp.server.name");

	/** xmpp服务器端口 ***/
	public final static String XMPP_SERVER_PORT = PropertiesUtil.getInstance().getKeyValue("xmpp.server.port");

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
	 * 用户状态 0-enabled
	 */
	public static final int USER_STATUS_ENABLED = 0;

	/**
	 * 用户状态 1-disabled
	 */
	public static final int USER_STATUS_DISABLED = 1;

	/**
	 * 用户类型 0-普通；
	 */
	public static final int USER_CATEGORY_GENERAL = 0;

	/**
	 * 用户类型 1-系统
	 */
	public static final int USER_CATEGORY_SYSTEM = 1;

	/**
	 * 角色类型 0-普通角色
	 */
	public static final int ROLE_TYPE_GENERAL = 0;
	/**
	 * 角色类型 1-系统角色不可编辑删除
	 */
	public static final int ROLE_TYPE_SYSTEM = 1;
	/**
	 * 权限类型 0-文件夹菜单
	 */
	public static final int PERMISSION_TYPE_FOLDER = 0;
	/**
	 * 权限类型 1-业务模块菜单可拥有子级
	 */
	public static final int PERMISSION_TYPE_MODULE = 1;
	/**
	 * 权限类型 2-CUDR等自定义操作，最底层不能再有子级;
	 */
	public static final int PERMISSION_TYPE_FINAL = 2;

	/**
	 * 下载路径
	 */
	public static final String DOWNLOAD_PATH = "download.path";

	/**
	 * 系统消息推送URL
	 */
	public static final String PUSH_MESSAGE_URL = "push.message.url";

	/**
	 * 上传临时路径
	 */
	public static final String UPLOAD_TEMP_PATH = "upload";

	/**
	 * 终端广告图片下载路径
	 */
	public static final String DOWNLOAD_ADVERTISE_PATH = "advertise";

	/**
	 * 上传终端广告图片临时路径
	 */
	public static final String UPLOAD_ADVERTISE_TEMP_PATH = UPLOAD_TEMP_PATH + "/advertise";

	/**
	 * 文件类型：语音
	 */
	public static final int UPLOAD_FILE_TYPE_VOICE = 1;
	/**
	 * 文件类型：图片
	 */
	public static final int UPLOAD_FILE_TYPE_IMAGE = 2;

	/**
	 * 上传文件，功能模块类型：用户
	 */
	public static final int UPLOAD_MODULE_TYPE_USER = 1;
	/**
	 * 上传文件，功能模块类型：商旅行程
	 */
	public static final int UPLOAD_MODULE_TYPE_BUSINESSTRIP = 2;
	/**
	 * 上传文件，功能模块类型：产品信息
	 */
	public static final int UPLOAD_MODULE_TYPE_PRODUCT = 3;
	/**
	 * 上传文件，功能模块类型：服务信息
	 */
	public static final int UPLOAD_MODULE_TYPE_SERVICE = 4;

	/**
	 * 上传文件，功能模块类型：离线附件
	 */
	public static final int UPLOAD_MODULE_TYPE_CHAT = 5;

	/**
	 * 终端用户头像下载路径
	 */
	public static final String DOWNLOAD_USER_PATH = "user";
	/**
	 * 商旅行程语音，图片下载路径
	 */
	public static final String DOWNLOAD_JOURNEY_PATH = "journey";
	/**
	 * 产品信息语音，图片下载路径
	 */
	public static final String DOWNLOAD_PRODUCT_PATH = "product";
	/**
	 * 服务信息语音，图片下载路径
	 */
	public static final String DOWNLOAD_SERVICE_PATH = "service";
	/**
	 * IM离线附件 chat
	 */
	public static final String DOWNLOAD_CHAT_PATH = "chat";

	/** 基础数据的资源res_key分配的初始值 ******/

	/** 行业res_key初始值 ****/
	public static final String RESKEY_INDUSTRY_INIT = "1000000";
	/** 地域res_key初始值 ****/
	public static final String RESKEY_REGION_INIT = "2000001";
	/** 身份res_key初始值 ****/
	public static final String RESKEY_IDENTITY_INIT = "3000001";
	/** 身份res_key初始值 ****/
	public static final String RESKEY_SERVICE_INIT = "4000001";

	/** APP数据列表每页显示的记录数 ****/
	public static final int APP_PAGE_NUM = 10;
	/** APP全文搜索列表每页显示的记录数 ****/
	public static final int APP_FULL_PAGE_NUM = 3;

	/**
	 * 新用户注册推送内容
	 */
	public static final String PUSH_CONTENT = "register_push_content";

}
