package com.ywb.tuyue.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by penghao on 2018/4/24.
 * description：
 */

public class BookBean extends BaseBean {


    /**
     * result : {"ads":[{"id":1,"title":"tezt","img":"//192.168.100.123/pictures/20180424/1524569239.jpeg","resource_file":"","content":"fdsfafd","createTime":"2018-04-24 04:16:25"},{"id":2,"title":"fdsaf","img":"//192.168.100.123/pictures/20180424/1524568603.jpeg","resource_file":null,"content":"fdsafsa","createTime":"2018-04-24 04:17:14"}],"books":[{"books":[{"id":1,"name":"呵呵呵呵","img":"//localhost/pictures/20180424/1524568603.jpeg","type":null,"book_url":"http://192.168.100.123/movie/20180424/1524572563.pdf","createTime":"2018-04-24 04:18:06"}],"name":"武侠","id":1},{"books":[{"id":2,"name":"15115154","img":"//localhost/pictures/20180424/1524568603.jpeg","type":null,"book_url":"http://192.168.100.123/movie/20180424/1524572669.pdf","createTime":"2018-04-24 04:19:21"}],"name":"科幻","id":2}]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        private List<AdsBean> ads;
        private List<BooksBeanX> books;

        public List<AdsBean> getAds() {
            return ads;
        }

        public void setAds(List<AdsBean> ads) {
            this.ads = ads;
        }

        public List<BooksBeanX> getBooks() {
            return books;
        }

        public void setBooks(List<BooksBeanX> books) {
            this.books = books;
        }

        public static class AdsBean {
            /**
             * id : 1
             * title : tezt
             * img : //192.168.100.123/pictures/20180424/1524569239.jpeg
             * resource_file :
             * content : fdsfafd
             * createTime : 2018-04-24 04:16:25
             */

            private int id;
            private String title;
            private String img;
            private String resource_file;
            private String content;
            private String createTime;

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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getResource_file() {
                return resource_file;
            }

            public void setResource_file(String resource_file) {
                this.resource_file = resource_file;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }

        public static class BooksBeanX implements Serializable {
            /**
             * books : [{"id":1,"name":"呵呵呵呵","img":"//localhost/pictures/20180424/1524568603.jpeg","type":null,"book_url":"http://192.168.100.123/movie/20180424/1524572563.pdf","createTime":"2018-04-24 04:18:06"}]
             * name : 武侠
             * id : 1
             */

            private String name;
            private int id;
            private List<BooksBean> books;

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

            public List<BooksBean> getBooks() {
                return books;
            }

            public void setBooks(List<BooksBean> books) {
                this.books = books;
            }

            public static class BooksBean implements Serializable {
                /**
                 * id : 1
                 * name : 呵呵呵呵
                 * img : //localhost/pictures/20180424/1524568603.jpeg
                 * type : null
                 * book_url : http://192.168.100.123/movie/20180424/1524572563.pdf
                 * createTime : 2018-04-24 04:18:06
                 */

                private int id;
                private String name;
                private String img;
                private String author;
                private Object type;
                private String book_url;
                private String createTime;

                public String getAuthor() {
                    return author;
                }

                public void setAuthor(String author) {
                    this.author = author;
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

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public Object getType() {
                    return type;
                }

                public void setType(Object type) {
                    this.type = type;
                }

                public String getBook_url() {
                    return book_url;
                }

                public void setBook_url(String book_url) {
                    this.book_url = book_url;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }
            }
        }
    }
}
