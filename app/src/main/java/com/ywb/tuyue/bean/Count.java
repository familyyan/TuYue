package com.ywb.tuyue.bean;

import com.simple.util.db.annotation.SimpleColumn;
import com.simple.util.db.annotation.SimpleId;
import com.simple.util.db.annotation.SimpleTable;

/**
 * Created by penghao on 2018/4/26.
 * description：
 */
@SimpleTable(name = "t_count")
public class Count {
    @SimpleColumn(name = "createTime")
    private String createTime;
    @SimpleId
    @SimpleColumn(name = "countId")
    private int countId;
    //click
    @SimpleColumn(name = "homeAd1")
    private int homeAd1;
    @SimpleColumn(name = "homeAd2")
    private int homeAd2;
    @SimpleColumn(name = "unLock")
    private int unLock;
    @SimpleColumn(name = "gameAd")
    private int gameAd;
    @SimpleColumn(name = "bookAd")
    private int bookAd;
    @SimpleColumn(name = "movies")
    private int movies;
    @SimpleColumn(name = "game")
    private int game;
    @SimpleColumn(name = "music")
    private int music;
    @SimpleColumn(name = "book")
    private int book;
    @SimpleColumn(name = "food")
    private int food;
    @SimpleColumn(name = "city")
    private int city;
    @SimpleColumn(name = "subway")
    private int subway;
    @SimpleColumn(name = "openPad")
    private int openPad;
    //stay
    @SimpleColumn(name = "homeAd1Time")
    private int homeAd1Time;
    @SimpleColumn(name = "homeAd2Time")
    private int homeAd2Time;
    @SimpleColumn(name = "moviesTime")
    private int moviesTime;
    @SimpleColumn(name = "gameTime")
    private int gameTime;
    @SimpleColumn(name = "musicTime")
    private int musicTime;
    @SimpleColumn(name = "bookTime")
    private int bookTime;
    @SimpleColumn(name = "foodTime")
    private int foodTime;
    @SimpleColumn(name = "cityTime")
    private int cityTime;
    @SimpleColumn(name = "subwayTime")
    private int subwayTime;
    //设备IMEI
    @SimpleColumn(name = "padImei")
    private String padImei;

    public String getPadImei() {
        return padImei;
    }

    public void setPadImei(String padImei) {
        this.padImei = padImei;
    }

    public int getOpenPad() {
        return openPad;
    }

    public void setOpenPad(int openPad) {
        this.openPad = openPad;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return countId;
    }

    public void setId(int id) {
        this.countId = id;
    }

    public int getHomeAd1() {
        return homeAd1;
    }

    public void setHomeAd1(int homeAd1) {
        this.homeAd1 = homeAd1;
    }

    public int getHomeAd2() {
        return homeAd2;
    }

    public void setHomeAd2(int homeAd2) {
        this.homeAd2 = homeAd2;
    }

    public int getUnLock() {
        return unLock;
    }

    public void setUnLock(int unLock) {
        this.unLock = unLock;
    }

    public int getGameAd() {
        return gameAd;
    }

    public void setGameAd(int gameAd) {
        this.gameAd = gameAd;
    }

    public int getBookAd() {
        return bookAd;
    }

    public void setBookAd(int bookAd) {
        this.bookAd = bookAd;
    }

    public int getMovies() {
        return movies;
    }

    public void setMovies(int movies) {
        this.movies = movies;
    }

    public int getGame() {
        return game;
    }

    public void setGame(int game) {
        this.game = game;
    }

    public int getMusic() {
        return music;
    }

    public void setMusic(int music) {
        this.music = music;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getSubway() {
        return subway;
    }

    public void setSubway(int subway) {
        this.subway = subway;
    }

    public int getHomeAd1Time() {
        return homeAd1Time;
    }

    public void setHomeAd1Time(int homeAd1Time) {
        this.homeAd1Time = homeAd1Time;
    }

    public int getHomeAd2Time() {
        return homeAd2Time;
    }

    public void setHomeAd2Time(int homeAd2Time) {
        this.homeAd2Time = homeAd2Time;
    }

    public int getMoviesTime() {
        return moviesTime;
    }

    public void setMoviesTime(int moviesTime) {
        this.moviesTime = moviesTime;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public int getMusicTime() {
        return musicTime;
    }

    public void setMusicTime(int musicTime) {
        this.musicTime = musicTime;
    }

    public int getBookTime() {
        return bookTime;
    }

    public void setBookTime(int bookTime) {
        this.bookTime = bookTime;
    }

    public int getFoodTime() {
        return foodTime;
    }

    public void setFoodTime(int foodTime) {
        this.foodTime = foodTime;
    }

    public int getCityTime() {
        return cityTime;
    }

    public void setCityTime(int cityTime) {
        this.cityTime = cityTime;
    }

    public int getSubwayTime() {
        return subwayTime;
    }

    public void setSubwayTime(int subwayTime) {
        this.subwayTime = subwayTime;
    }
}
