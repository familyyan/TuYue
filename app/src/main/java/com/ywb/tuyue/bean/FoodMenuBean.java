package com.ywb.tuyue.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by penghao on 2018/1/5.
 * description：
 */

public class FoodMenuBean extends BaseBean implements Serializable {


    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * foodList : [{"type_name":"粥类","type_id":5,"price":12,"name":"小米粥","description":"菜品描述1","id":16,"create_at":1515235847000,"url":"https://www.yuwubao.cn:443/pictures/20180106/1515235839.jpg"},{"type_name":"粥类","type_id":5,"price":12,"name":"青菜粥","description":"菜品描述1","id":15,"create_at":1515235792000,"url":"https://www.yuwubao.cn:443/pictures/20180106/1515235788.jpg"}]
         * name : 粥类
         * id : 5
         */

        private String name;
        private int id;
        private List<FoodListBean> foodList;

        public ResultBean(String name, int id, List<FoodListBean> foodList) {
            this.name = name;
            this.id = id;
            this.foodList = foodList;
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

        public List<FoodListBean> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListBean> foodList) {
            this.foodList = foodList;
        }

        public static class FoodListBean implements Serializable {
            /**
             * type_name : 粥类
             * type_id : 5
             * price : 12
             * name : 小米粥
             * description : 菜品描述1
             * id : 16
             * create_at : 1515235847000
             * url : https://www.yuwubao.cn:443/pictures/20180106/1515235839.jpg
             */

            private String type_name;
            private int type_id;
            private double price;
            private String name;
            private String description;
            private int id;
            private long create_at;
            private String url;

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
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

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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
        }
    }
}
