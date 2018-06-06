package com.ywb.tuyue.db.dao;


import com.simple.util.db.operation.TemplateDAO;
import com.ywb.tuyue.AppContext;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.db.DBHelper;

import java.util.List;

/**
 * function
 * Created by mhdt on 2016/12/3.12:38
 * update by: 统计
 */
public class CountDao extends TemplateDAO<Count> {
    private static CountDao instance;

    public CountDao() {
        super(new DBHelper(AppContext.getApplication()));
    }

    public static CountDao getInstance() {
        if (instance == null) {
            synchronized (CountDao.class) {
                if (instance == null) {
                    instance = new CountDao();
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
    public Count getLastCount() {
        List<Count> counts = find();
        if (counts != null && counts.size() > 0) {
            return counts.get(counts.size() - 1);
        } else {
            return null;
        }
    }

    public void updateCount(Count count) {
        if (count == null) {
            return;
        }
        if (get(count.getId()) == null) {
            insert(count);
        } else {
            update(count);
        }
    }
}
