package com.ywb.tuyue.bean;

import java.util.List;

/**
 * Created by penghao on 2018/4/23.
 * description：
 */

public class GameBean extends BaseBean {


    /**
     * result : {"ads":[{"img":"http://192.168.1.6:8080/pictures/20180416/1523850729.jpg","id":1,"title":"fdsaf","content":"fdsafjkldsajflksfjl"},{"img":"https://www.yuwubao.cn:443/pictures/20180116/1516083934.jpg","id":2,"title":"fdsafjdl","content":"fda"}],"games":[{"img":"http://192.168.1.6:8080/pictures/20180416/1523850729.jpg","game_zip":"fdsaf","name":"113123","id":1},{"img":"http://192.168.1.6:8080/pictures/20180416/1523850729.jpg","game_zip":"fdsa","name":"fdsaf","id":2}]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<AdsBean> ads;
        private List<GamesBean> games;

        public List<AdsBean> getAds() {
            return ads;
        }

        public void setAds(List<AdsBean> ads) {
            this.ads = ads;
        }

        public List<GamesBean> getGames() {
            return games;
        }

        public void setGames(List<GamesBean> games) {
            this.games = games;
        }

        public static class AdsBean {
            /**
             * img : http://192.168.1.6:8080/pictures/20180416/1523850729.jpg
             * id : 1
             * title : fdsaf
             * content : fdsafjkldsajflksfjl
             */

            private String img;
            private int id;
            private String title;
            private String content;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        public static class GamesBean {
            /**
             * img : http://192.168.1.6:8080/pictures/20180416/1523850729.jpg
             * game_zip : fdsaf
             * name : 113123
             * id : 1
             */

            private String img;
            private String game_zip;
            private String name;
            private int id;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getGame_zip() {
                return game_zip;
            }

            public void setGame_zip(String game_zip) {
                this.game_zip = game_zip;
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
        }
    }
}
