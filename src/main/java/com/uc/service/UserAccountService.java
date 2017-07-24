package com.uc.service;

import java.util.List;

import com.uc.entity.UserAccount;

public interface UserAccountService {

    public static final String USERACCOUNT_KEY = "useraccount";

    public Boolean add(UserAccount o);

    public Long delete(UserAccount o);

    public Long deleteAll();

    public Boolean update(UserAccount o);

    public List<UserAccount> findByPage(int pageSize, int pageNum);

    public List<UserAccount> findAll();

    public UserAccount findOne(int id);

    public Long findCount();
}
