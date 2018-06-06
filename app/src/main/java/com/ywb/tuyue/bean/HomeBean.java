package com.ywb.tuyue.bean;

import java.util.List;

/**
 * Created by penghao on 2018/1/3.
 * description：
 */

public class HomeBean extends BaseBean {

    /**
     * result : {"thirdParty":{"game_url":"gameUrl123","music_url":"musicUrl123","book_url":"bookUrl123"},"hotMovie":[{"PROFILE":"一句话简介","poster_url":"海报图片","NAME":"电影名称"}],"advert":[{"img_url":"/Users/ccrtmb/IdeaProjects/UploadFile/测绘-地块修改.png","update_at":1514273909000,"id":5,"title":"test3","create_at":1514273909000,"content":"testcontent","status":"1"},{"img_url":"/Users/ccrtmb/IdeaProjects/UploadFile/测绘-地块修改.png","update_at":1514187899000,"id":4,"title":"title2","create_at":1514186072000,"content":"2testContent","status":"1"}]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * thirdParty : {"game_url":"gameUrl123","music_url":"musicUrl123","book_url":"bookUrl123"}
         * hotMovie : [{"PROFILE":"一句话简介","poster_url":"海报图片","NAME":"电影名称"}]
         * advert : [{"img_url":"/Users/ccrtmb/IdeaProjects/UploadFile/测绘-地块修改.png","update_at":1514273909000,"id":5,"title":"test3","create_at":1514273909000,"content":"testcontent","status":"1"},{"img_url":"/Users/ccrtmb/IdeaProjects/UploadFile/测绘-地块修改.png","update_at":1514187899000,"id":4,"title":"title2","create_at":1514186072000,"content":"2testContent","status":"1"}]
         */

        private ThirdPartyBean thirdParty;
        private List<HotMovieBean> hotMovie;
        private List<AdvertBean> advert;

        public ThirdPartyBean getThirdParty() {
            return thirdParty;
        }

        public void setThirdParty(ThirdPartyBean thirdParty) {
            this.thirdParty = thirdParty;
        }

        public List<HotMovieBean> getHotMovie() {
            return hotMovie;
        }

        public void setHotMovie(List<HotMovieBean> hotMovie) {
            this.hotMovie = hotMovie;
        }

        public List<AdvertBean> getAdvert() {
            return advert;
        }

        public void setAdvert(List<AdvertBean> advert) {
            this.advert = advert;
        }

        public static class ThirdPartyBean {
            /**
             * game_url : gameUrl123
             * music_url : musicUrl123
             * book_url : bookUrl123
             * food_url : food_url
             */

            private String game_url;
            private String music_url;
            private String book_url;
            private String food_url;

            public String getFood_url() {
                return food_url;
            }

            public void setFood_url(String food_url) {
                this.food_url = food_url;
            }

            public String getGame_url() {
                return game_url;
            }

            public void setGame_url(String game_url) {
                this.game_url = game_url;
            }

            public String getMusic_url() {
                return music_url;
            }

            public void setMusic_url(String music_url) {
                this.music_url = music_url;
            }

            public String getBook_url() {
                return book_url;
            }

            public void setBook_url(String book_url) {
                this.book_url = book_url;
            }
        }

        public static class HotMovieBean {
            /**
             * PROFILE : 一句话简介
             * poster_url : 海报图片
             * NAME : 电影名称
             */

            private String profile;
            private String poster_url;
            private String name;
            private String localPath;

            public String getLocalPath() {
                return localPath;
            }

            public void setLocalPath(String localPath) {
                this.localPath = localPath;
            }

            public String getProfile() {
                return profile;
            }

            public void setProfile(String profile) {
                this.profile = profile;
            }

            public String getPoster_url() {
                return poster_url;
            }

            public void setPoster_url(String poster_url) {
                this.poster_url = poster_url;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class AdvertBean {
            /**
             * img_url : /Users/ccrtmb/IdeaProjects/UploadFile/测绘-地块修改.png
             * update_at : 1514273909000
             * id : 5
             * title : test3
             * create_at : 1514273909000
             * content : testcontent
             * status : 1
             */

            private String img_url;
            private long update_at;
            private int id;
            private String title;
            private long create_at;
            private String content;
            private String contentPd;
            private String status;
            private String localPath;

            public String getContentPd() {
                return contentPd;
            }

            public void setContentPd(String contentPd) {
                this.contentPd = contentPd;
            }

            public String getLocalPath() {
                return localPath;
            }

            public void setLocalPath(String localPath) {
                this.localPath = localPath;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public long getUpdate_at() {
                return update_at;
            }

            public void setUpdate_at(long update_at) {
                this.update_at = update_at;
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

            public long getCreate_at() {
                return create_at;
            }

            public void setCreate_at(long create_at) {
                this.create_at = create_at;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
