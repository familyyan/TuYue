package com.ywb.tuyue.db.dao;


import com.simple.util.db.operation.TemplateDAO;
import com.ywb.tuyue.AppContext;
import com.ywb.tuyue.bean.User;
import com.ywb.tuyue.db.DBHelper;

import java.util.List;

/**
 * function
 * Created by mhdt on 2016/12/3.12:38
 * update by:
 */
public class UserDao extends TemplateDAO<User> {
    private static UserDao instance;

    public UserDao() {
        super(new DBHelper(AppContext.getApplication()));
    }

    public static UserDao getInstance() {
        if (instance == null) {
            synchronized (UserDao.class) {
                if (instance == null) {
                    instance = new UserDao();
                }
            }
        }
        return instance;
    }

    /**
     * 获取最近的用户信息
     *
     * @return
     */
    public User getLastUser() {
        List<User> users = find();
        if (users != null && users.size() > 0) {
            return users.get(users.size() - 1);
        } else {
            return null;
        }
    }

    public void updateUser(User user) {
        if (get(user.getAccountId()) == null) {
            insert(user);
        } else {
            update(user);
        }
    }
}
