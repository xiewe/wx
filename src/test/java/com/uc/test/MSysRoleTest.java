package com.uc.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import com.framework.entity.SysRole;
import com.framework.service.SysRoleService;

@WebAppConfiguration
public class MSysRoleTest extends BaseTest {
    @Autowired
    private SysRoleService sysRoleService;

    @Test
    public void testInsert() throws Exception {
        SysRole r = new SysRole();
        r.setName("asdf");
        r.setDescription("hello");
        sysRoleService.saveOrUpdate(r);
    }
}
