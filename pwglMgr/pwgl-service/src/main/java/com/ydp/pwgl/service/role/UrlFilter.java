package com.ydp.pwgl.service.role;

import java.io.Serializable;

/**
 * Created by 29632 on 2017/4/26.
 */
public class UrlFilter implements Serializable{

    private String rolename; //角色名称，如超级管理员
    private String url; //地址
    private String roles; //角色名称，如admin
    private String permissions; //所需要的权限，可省略
    private int sort;   //排序字段

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
