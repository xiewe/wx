package com.uc.service;

import java.util.List;

import com.uc.entity.UserStatusInfo;

public interface UserStatusInfoService {

    public static final String USERSTATUS_KEY = "userstatus";

    public Boolean add(UserStatusInfo o);

    public Long delete(double id);

    public Long delete(UserStatusInfo o);

    public Long deleteAll();

    public Boolean update(UserStatusInfo o);

    public List<UserStatusInfo> findByPage(int pageSize, int pageNum);

    public List<UserStatusInfo> findAll();

    public UserStatusInfo findOne(double id);

    public Long findCount();
}
