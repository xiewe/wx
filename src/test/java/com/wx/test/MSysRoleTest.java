package com.wx.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import com.framework.dao.MSysRoleMapper;
import com.framework.entity.MSysRole;

@WebAppConfiguration
public class MSysRoleTest extends BaseTest {
    @Autowired
    private MSysRoleMapper mSysRoleMapper;

    @Test
    public void testInsert() throws Exception {
        MSysRole r = new MSysRole();
        r.setName("asdf");
        r.setDescription("hello");
        mSysRoleMapper.insert(r);
    }
}
