package com.framework.entity;

public class MSysRolePermission {
    private Integer roleId;

    private Integer menuId;

    private Integer menuClassId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getMenuClassId() {
        return menuClassId;
    }

    public void setMenuClassId(Integer menuClassId) {
        this.menuClassId = menuClassId;
    }
}