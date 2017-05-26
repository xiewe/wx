package com.framework.dao;

import com.framework.entity.MSysRolePermission;

public interface MSysRolePermissionMapper {
    int insert(MSysRolePermission record);

    int insertSelective(MSysRolePermission record);
}