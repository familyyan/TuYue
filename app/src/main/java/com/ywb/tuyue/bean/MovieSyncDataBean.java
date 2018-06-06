package com.ywb.tuyue.bean;

import com.simple.util.db.annotation.SimpleColumn;
import com.simple.util.db.annotation.SimpleId;
import com.simple.util.db.annotation.SimpleTable;

import java.util.List;

/**
 * Created by ph on 18-1-12.
 */

public class MovieSyncDataBean extends BaseBean {


    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * totalSize : null
         * moviesList : [{"actor":"电影小图片测试","type_id":8,"director":"电影小图片测试","profile":"电影小图片测试","name":"电影小图片测试","download_url":"http://47.93.158.24:8080/movie/20180112/1515748668.mp4","update_at":1515654057000,"poster_url":"http://47.93.158.24:8080/pictures/20180111/1515653865.jpg","id":40,"detail":"电影小图片测试","create_at":1515654057000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748668.mp4"},{"actor":"fsfaf","type_id":14,"director":"sfsfs","profile":"fsfa","name":"fdsa","download_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4","update_at":1515467250000,"poster_url":"https://www.yuwubao.cn:443/pictures/20180109/1515466354.png","id":39,"detail":"dfdsfaf","create_at":1515467250000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4"},{"actor":"章子怡 / 黄晓明 / 张震 / 王力宏 / 陈楚生 ","type_id":15,"director":"李芳芳","profile":"讲述的是如果提前了解了你所要面对的人生，你是否还会有勇气前来？","name":"无问西东","download_url":"http://47.93.158.24:8080/movie/20180112/1515748668.mp4","update_at":1515220168000,"poster_url":"https://www.yuwubao.cn:443/pictures/20180106/1515219843.jpg","id":31,"detail":"吴岭澜（陈楚生 饰），出发时意气风发，却很快在途中迷失了方向。沈光耀（王力宏 饰），自愿参与了最残酷的战争，他一直在努力去做那些令他害怕，但重要的事。王敏佳（章子怡 饰）最初的错误，只是为了虚荣撒了一个小谎；最初的烦恼，只是在两个优秀的男人中选择一个。但命运，却把她拖入被众人唾骂的深渊。","create_at":1515220168000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748668.mp4"},{"actor":"李冰冰 / 凯南·鲁兹 / 吴尊 / 凯尔希·格兰莫 / 斯黛芬·道森 ","type_id":16,"director":"金波·兰道","profile":"冒险之旅","name":"谜巢","download_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4","update_at":1515219797000,"poster_url":"https://www.yuwubao.cn:443/pictures/20180106/1515219672.jpg","id":30,"detail":"《谜巢》讲述的是生物科技公司研究员卢克·李（吴尊 饰）深入一座古墓随即失去音讯，剧毒生物学博士嘉·李（李冰冰 饰）得知弟弟卢克失踪的消息，随即与医药集团总裁梅森·伯罗斯（凯尔希·格兰莫 饰）组建一支专业的搜救队，在救援队长杰克·雷德利（凯南·鲁兹饰）的带领下，一行人前往卢克·李失踪的地点搜寻，却意外闯入神秘地下世界，一段意想不到冒险之旅就此展开\u2026\u2026","create_at":1515219797000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4"},{"actor":"约翰·塞纳 / 吉娜·罗德里格兹 / 鲍比·坎纳瓦尔 / 安东尼·安德森 / 戴维德·迪格斯","type_id":8,"director":"卡洛斯·沙尔丹哈","profile":"心有蛮牛，细嗅蔷薇，改编自著名童话《公牛费迪南》","name":"公牛历险记","download_url":"http://47.93.158.24:8080/movie/20180112/1515748523.mp4","update_at":1515219449000,"poster_url":"https://www.yuwubao.cn:443/pictures/20180106/1515218481.jpg","id":29,"detail":"公牛历险记#(Ferdinand，暂译)，心有蛮牛，细嗅蔷薇，改编自著名童话《公牛费迪南》，讲述体型庞大的牛费迪南其实温柔又笨拙，他会屈服于命运成为一只勇猛的斗牛吗？给小兔子做牛工呼吸也是够了！WWE巨星约翰·塞纳配音费迪南，蜜汁适合，SNL的凯特·迈克金农，《处女孕事》女主吉娜·罗德里格兹、著名逗比安东尼·安德森参与配音。今年12月15日北美上映，内地有望引进。\n\n《公牛历险记》迅雷下载","create_at":1515219449000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748523.mp4"},{"actor":"本·阿弗莱克 / 亨利·卡维尔 / 盖尔·加朵 / 埃兹拉·米勒 / 杰森·莫玛","type_id":8,"director":"扎克·施奈德","profile":"美国科幻片","name":"正义联盟","download_url":"http://47.93.158.24:8080/movie/20180112/1515748523.mp4","update_at":1515218430000,"poster_url":"https://www.yuwubao.cn:443/pictures/20180106/1515218267.jpg","id":28,"detail":"美国科幻片《正义联盟》讲述的是受到超人无私奉献的影响，蝙蝠侠重燃了对人类的信心，接受了新盟友\u2014\u2014神奇女侠（戴安娜·普林斯）的帮助，去对抗更加强大的敌人。蝙蝠侠和神奇女侠一同寻找并招募了一支超人类联盟来抵挡新觉醒的威胁。但尽管这支队伍集结了超人、蝙蝠侠、神奇女侠、闪电侠、海王和钢骨等人，他们似乎无法阻止敌人对地球的进攻\u2026\u2026","create_at":1515218430000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748523.mp4"},{"actor":"fdsaf","type_id":7,"director":"fdsaf","profile":"f","name":"测试","download_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4","update_at":1515053519000,"poster_url":"https://www.yuwubao.cn:443/pictures/20180104/1515053233.png","id":27,"detail":"fdsaf","create_at":1515053519000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4"},{"actor":"fdsafdfdsfsf","type_id":6,"director":"fdsafff","profile":"fdsaffd","name":"fdsafsfsfaf11111","download_url":"http://47.93.158.24:8080/movie/20180112/1515748668.mp4","update_at":1515051284000,"poster_url":"https://www.yuwubao.cn:443/pictures/20180104/1515051272.png","id":25,"detail":"dsfsaff","create_at":1515051284000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748668.mp4"},{"actor":"fdsaff","type_id":6,"director":"sfdsafsf","profile":"dsfafdsf","name":"fdsafsfsffdsa12111","download_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4","update_at":1515049924000,"id":24,"detail":"dfasfs","create_at":1515049924000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4"},{"actor":"fdsaff","type_id":6,"director":"sfdsafsf","profile":"dsfafdsf","name":"fdsafsfsffdsa1211","download_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4","update_at":1515049922000,"id":23,"detail":"dfasfs","create_at":1515049922000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4"},{"actor":"fdsaff","type_id":6,"director":"sfdsafsf","profile":"dsfafdsf","name":"fdsafsfsffdsa121","download_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4","update_at":1515049920000,"id":22,"detail":"dfasfs","create_at":1515049920000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4"},{"actor":"fdsaff","type_id":6,"director":"sfdsafsf","profile":"dsfafdsf","name":"fdsafsfsffdsa12","download_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4","update_at":1515049918000,"id":21,"detail":"dfasfs","create_at":1515049918000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748583.mp4"},{"actor":"fdsaff","type_id":6,"director":"sfdsafsf","profile":"dsfafdsf","name":"fdsafsfsffdsa1","download_url":"http://47.93.158.24:8080/movie/20180112/1515748668.mp4","update_at":1515049917000,"id":20,"detail":"dfasfs","create_at":1515049917000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748668.mp4"},{"actor":"fdsaff","type_id":6,"director":"sfdsafsf","profile":"dsfafdsf","name":"fdsafsfsffdsa","download_url":"http://47.93.158.24:8080/movie/20180112/1515748399.mp4","update_at":1515049915000,"id":19,"detail":"dfasfs","create_at":1515049915000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748668.mp4"},{"actor":"fdsaff","type_id":6,"director":"sfdsafsf","profile":"dsfafdsf","name":"fdsafsfsf","download_url":"http://47.93.158.24:8080/movie/20180112/1515748399.mp4","update_at":1515049907000,"id":18,"detail":"dfasfs","create_at":1515049907000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748399.mp4"},{"actor":"safsfsaf","type_id":6,"director":"fsfsaf","profile":"fdsfa","name":"fdsaffsaff","download_url":"http://47.93.158.24:8080/movie/20180112/1515748399.mp4","update_at":1515049438000,"id":16,"detail":"sfsfsafs","create_at":1515049438000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748399.mp4"},{"actor":"fsfsfsf","type_id":6,"director":"fsfsf","profile":"fdsaffs","name":"fdsaffsafsfsfdsaf","download_url":"http://47.93.158.24:8080/movie/20180112/1515748399.mp4","update_at":1515049360000,"poster_url":"https://www.yuwubao.cn:443/pictures/20180104/1515049340.png","id":15,"detail":"sfsfdsfsfafd","create_at":1515049360000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748399.mp4"},{"actor":"fsfsfsf","type_id":8,"director":"fsfsf","profile":"fdsaffs","name":"fdsaffsafsfs","download_url":"http://47.93.158.24:8080/movie/20180112/1515748668.mp4","update_at":1515049349000,"poster_url":"https://www.yuwubao.cn:443/pictures/20180104/1515049340.png","id":14,"detail":"sfsfdsfsfafd","create_at":1515049349000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748668.mp4"},{"actor":"fsfs","type_id":8,"director":"fdsfsfs","profile":"fdsaf","name":"疯狂特警队","download_url":"http://47.93.158.24:8080/movie/20180112/1515748399.mp4","update_at":1515049318000,"poster_url":"https://www.yuwubao.cn:443/pictures/20180104/1515049311.png","id":13,"detail":"fsfsfsf","create_at":1515049318000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748399.mp4"},{"actor":"fdsff","type_id":8,"director":"fdsf","profile":"fds","name":"魂牵梦萦f","download_url":"http://47.93.158.24:8080/movie/20180112/1515748399.mp4","update_at":1515048764000,"poster_url":"https://www.yuwubao.cn:443/pictures/20180104/1515048754.png","id":12,"detail":"dsfdsf","create_at":1515048764000,"source_url":"http://47.93.158.24:8080/movie/20180112/1515748399.mp4"}]
         */

        private String totalSize;
        private MovieBannerBean movieBanner;
        private List<MoviesListBean> moviesList;

        public MovieBannerBean getMovieBanner() {
            return movieBanner;
        }

        public void setMovieBanner(MovieBannerBean movieBanner) {
            this.movieBanner = movieBanner;
        }

        public String getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(String totalSize) {
            this.totalSize = totalSize;
        }

        public List<MoviesListBean> getMoviesList() {
            return moviesList;
        }

        public void setMoviesList(List<MoviesListBean> moviesList) {
            this.moviesList = moviesList;
        }

        @SimpleTable(name = "t_banner")
        public static class MovieBannerBean {
            /**
             * duration : 12
             * img_url : https://www.yuwubao.cn:443/pictures/20180301/1519870954.jpg
             * update_at : 1519870961000
             * id : 5
             * title : fsd
             * create_at : 1516595667000
             */
            @SimpleColumn(name = "duration")
            private int duration;//时间
            @SimpleColumn(name = "img_url")
            private String img_url;//地址
            @SimpleColumn(name = "update_at")
            private long update_at;//时间
            @SimpleId
            @SimpleColumn(name = "id")
            private int id;
            @SimpleColumn(name = "title")
            private String title;//标题
            @SimpleColumn(name = "create_at")
            private long create_at;

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
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
        }

    }
}
