package com.ywb.tuyue.bean;

import com.simple.util.db.annotation.SimpleColumn;
import com.simple.util.db.annotation.SimpleId;
import com.simple.util.db.annotation.SimpleTable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mhdt on 2017/12/18.
 */

public class Food extends BaseBean implements Serializable {


    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    @SimpleTable(name = "t_food")
    public static class ResultBean implements Serializable {
        /**
         * type_id : 4
         * price : 111
         * name : fsdafsfsaf
         * id : 4
         * create_at : 1515067096000
         * url : https://www.yuwubao.cn:443/pictures/20180104/1515067014.png
         */
        @SimpleId
        @SimpleColumn(name = "id")
        private int id;//id
        @SimpleColumn(name = "type_id")
        private int type_id;//类型
        @SimpleColumn(name = "price")
        private double price;//价格
        @SimpleColumn(name = "name")
        private String name;//菜名
        @SimpleColumn(name = "create_at")
        private long create_at;
        @SimpleColumn(name = "url")
        private String url;//下载地址
        @SimpleColumn(name = "description")
        private String description;//说明
        @SimpleColumn(name = "number")
        private int number = 1;//数量

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCreate_at() {
            return create_at;
        }

        public void setCreate_at(long create_at) {
            this.create_at = create_at;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
