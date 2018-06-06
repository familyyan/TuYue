package com.ywb.tuyue.bean;

/**
 * Created by penghao on 2018/1/6.
 * description：
 */

public class MyOrderDetail {
    /**
     * order_code : E201711250004
     * dining_car_id : C2017121812012302454
     * total_price : 35.6
     * seat_number : 07车09A
     * update_at : 1514280713000
     * telephone : 18600210012
     * id : 4
     * create_at : 1514280711000
     * order_state : 100
     */

    private String order_code;
    private String dining_car_id;
    private double total_price;
    private String seat_number;
    private long update_at;
    private String telephone;
    private int id;
    private long create_at;
    private String order_state;

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getDining_car_id() {
        return dining_car_id;
    }

    public void setDining_car_id(String dining_car_id) {
        this.dining_car_id = dining_car_id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(String seat_number) {
        this.seat_number = seat_number;
    }

    public long getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(long update_at) {
        this.update_at = update_at;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

}
