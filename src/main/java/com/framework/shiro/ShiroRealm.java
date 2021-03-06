package com.framework.shiro;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
import com.framework.entity.SysMenuClass;
import com.framework.entity.SysRole;
import com.framework.entity.SysUser;
import com.framework.exception.IncorrectCaptchaException;
import com.framework.service.SysMenuClassService;
import com.framework.service.SysUserService;
import com.framework.utils.Digests;
import com.framework.utils.EncodesUtil;
import com.framework.utils.PropertiesUtil;

@Service
public class ShiroRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(ShiroRealm.class);
    private static final int INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;
    private static final String ALGORITHM = "SHA-1";
    protected boolean useCaptcha = false;// 是否使用验证码

    protected SysUserService sysUserService;

    @Autowired
    private SysMenuClassService sysMenuClassService;

    @Autowired
    private HttpServletRequest request;

    public boolean isUseCaptcha() {
        return useCaptcha;
    }

    public void setUseCaptcha(boolean useCaptcha) {
        this.useCaptcha = useCaptcha;
    }

    public SysUserService getSysUserService() {
        return sysUserService;
    }

    public void setSysUserService(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    public ShiroRealm() {
        super();
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(ALGORITHM);
        matcher.setHashIterations(INTERATIONS);
        setCredentialsMatcher(matcher);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // Collection<?> collection = principals.fromRealm(getName());
        // if (collection == null || collection.isEmpty()) {
        // return null;
        // }
        // User user = (User) collection.iterator().next();
        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        sysUser = sysUserService.get(sysUser.getId());

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        SysRole userRole = sysUser.getSysRole();
        if (userRole != null) {
            info.addRole(userRole.getName());

            List<SysMenuClass> listSMC = sysMenuClassService.findByRoleId(userRole.getId());
            Collection<String> permissions = new HashSet<String>();
            for (SysMenuClass o : listSMC) {
                permissions.add(o.getMethod());
            }

            info.addStringPermissions(permissions);
        }

        log.info(sysUser.getUsername() + "拥有的角色:" + info.getRoles());
        log.info(sysUser.getUsername() + "拥有的权限:" + info.getStringPermissions());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        String inmos = PropertiesUtil.getInstance().getKeyValue("inoms");

        if (useCaptcha) {
            CaptchaUsernamePasswordToken token = (CaptchaUsernamePasswordToken) arg0;
            String parm = token.getCaptcha();

            if (!"1".equals(inmos)) {
                if (StringUtils.isEmpty(parm)
                        || !PatchcaServlet.validate(SecurityUtils.getSubject().getSession().getId().toString(),
                                parm.toLowerCase())) {
                    throw new IncorrectCaptchaException("验证码错误！");
                }
            }
        }
        UsernamePasswordToken token = (UsernamePasswordToken) arg0;
        SysUser sysUser = null;
        try {
            sysUser = sysUserService.findByU(token.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sysUser != null) {
            if (sysUser.getStatus() == AppConstants.USER_STATUS_DISABLED) {
                throw new DisabledAccountException();
            }

            byte[] salt = EncodesUtil.decodeHex(sysUser.getSalt());

            if (null != SecurityUtils.getSubject().getPreviousPrincipals())
                doGetAuthorizationInfo(SecurityUtils.getSubject().getPreviousPrincipals());

            // replace username
            if ("1".equals(inmos)) {
                sysUser.setUsername(((CaptchaUsernamePasswordToken) arg0).getCaptcha());
            }

            return new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(), ByteSource.Util.bytes(salt), getName());
        } else {
            return null;
        }
    }

    public void clearCachedAuthorizationInfo(Object principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    public void clearCachedAuthenticationInfo(Object principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthenticationInfo(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                log.debug("Clear all cached authorization info :" + key);
                cache.remove(key);
            }
        }
    }

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
        result.salt = EncodesUtil.encodeHex(salt);

        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, INTERATIONS);
        result.password = EncodesUtil.encodeHex(hashPassword);
        return result;
    }

    public static boolean validatePassword(String plainPassword, String password, String salt) {
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), EncodesUtil.decodeHex(salt), INTERATIONS);
        String oldPassword = EncodesUtil.encodeHex(hashPassword);
        return password.equals(oldPassword);
    }

}
