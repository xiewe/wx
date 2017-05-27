package com.framework.shiro;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.framework.AppConstants;
import com.framework.entity.User;
import com.framework.exception.IncorrectCaptchaException;
import com.framework.service.UserService;
import com.framework.utils.Digests;
import com.framework.utils.Encodes;

@Service
public class ShiroRealm extends AuthorizingRealm {
	private static final Logger log = LoggerFactory.getLogger(ShiroRealm.class);
	private static final int INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	private static final String ALGORITHM = "SHA-1";
	protected boolean useCaptcha = false;// 是否使用验证码

	@Autowired
	protected UserService userService;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 给ShiroRealm提供编码信息，用于密码密码比对
	 */
	public ShiroRealm() {
		super();
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(
				ALGORITHM);
		matcher.setHashIterations(INTERATIONS);
		setCredentialsMatcher(matcher);
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// Collection<?> collection = principals.fromRealm(getName());
		// if (collection == null || collection.isEmpty()) {
		// return null;
		// }
		// User user = (User) collection.iterator().next();
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		user = userService.get(user.getId());

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

//		for (UserRole userRole : user.getUserRoles()) {
//			// 获取角色: 对非系统角色需要获取原生的角色，以匹配java类shiro注解角色的拦截
//			if (userRole.getRole().getCategory() == AppConstants.ROLE_TYPE_GENERAL) {
//				for (RolePermission o : userRole.getRole().getRolePermissions()) {
//					info.addRole(o.getPermission().getSn().split(":")[0]);
//				}
//			} else {
//				info.addRole(userRole.getRole().getName());
//			}
//			// 获取操作权限
//			Collection<String> permissions = new HashSet<String>();
//			for (RolePermission o : userRole.getRole().getRolePermissions()) {
//				permissions.add(o.getPermission().getSn());
//
//			}
//
//			info.addStringPermissions(permissions);
//		}

		log.info(user.getUsername() + "拥有的角色:" + info.getRoles());
		log.info(user.getUsername() + "拥有的权限:" + info.getStringPermissions());
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken arg0) throws AuthenticationException {

		if (useCaptcha) {
			CaptchaUsernamePasswordToken token = (CaptchaUsernamePasswordToken) arg0;
			String parm = token.getCaptcha();

			if (StringUtils.isEmpty(parm)
					|| !PatchcaServlet.validate(SecurityUtils.getSubject()
							.getSession().getId().toString(),
							parm.toLowerCase())) {
				throw new IncorrectCaptchaException("验证码错误！");
			}
		}
		UsernamePasswordToken token = (UsernamePasswordToken) arg0;
		User user = null;
		try {
			user = userService.getByUsername(token.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (user != null) {
			if (user.getStatus() == AppConstants.USER_STATUS_DISABLED) {
				throw new DisabledAccountException();
			}

			byte[] salt = Encodes.decodeHex(user.getSalt());

			if (null != SecurityUtils.getSubject().getPreviousPrincipals())
				doGetAuthorizationInfo(SecurityUtils.getSubject()
						.getPreviousPrincipals());
			return new SimpleAuthenticationInfo(user, user.getPassword(),
					ByteSource.Util.bytes(salt), getName());
		} else {
			return null;
		}
	}

	/**
	 * 更新用户授权信息缓存.参数为doGetAuthenticationInfo调用时return new
	 * SimpleAuthenticationInfo传入类型
	 */
	public void clearCachedAuthorizationInfo(Object principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 更新用户认证信息缓存.参数为doGetAuthenticationInfo调用时return new
	 * SimpleAuthenticationInfo传入类型
	 */
	public void clearCachedAuthenticationInfo(Object principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principal, getName());
		clearCachedAuthenticationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				log.debug("Clear all cached authorization info :" + key);
				cache.remove(key);
			}
		}
	}

	/**
	 * 清除所有用户认证信息缓存.
	 */
	public void clearAllCachedAuthenticationCacheInfo() {
		Cache<Object, AuthenticationInfo> cache = getAuthenticationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				log.debug("Clear all cached authentication info :" + key);
				cache.remove(key);
			}
		}
	}

	public static class HashPassword {
		public String salt;
		public String password;
	}

	public static HashPassword encryptPassword(String plainPassword) {
		HashPassword result = new HashPassword();
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		result.salt = Encodes.encodeHex(salt);

		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt,
				INTERATIONS);
		result.password = Encodes.encodeHex(hashPassword);
		return result;
	}

	/**
	 * 
	 * 验证密码
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param password
	 *            密文密码
	 * @param salt
	 *            Salt值
	 * @return
	 */
	public static boolean validatePassword(String plainPassword,
			String password, String salt) {
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(),
				Encodes.decodeHex(salt), INTERATIONS);
		String oldPassword = Encodes.encodeHex(hashPassword);
		return password.equals(oldPassword);
	}

	/**
	 * 设置 useCaptcha 的值
	 * 
	 * @param useCaptcha
	 */
	public void setUseCaptcha(boolean useCaptcha) {
		this.useCaptcha = useCaptcha;
	}

}
