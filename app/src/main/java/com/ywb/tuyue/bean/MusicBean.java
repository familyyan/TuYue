package com.ywb.tuyue.bean;

import java.util.List;

/**
 * Created by penghao on 2018/4/26.
 * description：
 */

public class MusicBean extends BaseBean {


    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 1
         * name : 谁是大英雄
         * singer : 张学友
         * duration : 04:30
         * img : /pictures/20180426/1524738997.jpeg
         * music : /movie/20180426/1524740031.mp3
         * lyrics : <p>fdsfsafsf</p><p>fdsfsaf</p><p>fdsfas</p><p>fdsafsaf</p><p>fsdafasdfs</p><p>fdsfsafdsafsaf</p><p>sdfsafsdafsaff</p><p>fdsfasfsaf</p><p>fdsafsf</p><p><br></p>
         * music_name : 张学友 - 谁是大英雄.mp3
         * createTime : 2018-04-26 10:43:04
         */

        private int id;
        private String name;
        private String singer;
        private String duration;
        private String img;
        private String music;
        private String lyrics;
        private String music_name;
        private String createTime;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSinger() {
            return singer;
        }

        public void setSinger(String singer) {
            this.singer = singer;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getMusic() {
            return music;
        }

        public void setMusic(String music) {
            this.music = music;
        }

        public String getLyrics() {
            return lyrics;
        }

        public void setLyrics(String lyrics) {
            this.lyrics = lyrics;
        }

        public String getMusic_name() {
            return music_name;
        }

        public void setMusic_name(String music_name) {
            this.music_name = music_name;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
