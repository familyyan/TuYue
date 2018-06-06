package com.ywb.tuyue;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.ywb.tuyue.db.DataBase;
import com.ywb.tuyue.db.SharedPreferenceHelper;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.net.FakeApiInterceptor;
import com.ywb.tuyue.utils.DeviceUtils;
import com.ywb.tuyue.utils.SystemUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Peng on 2017/8/30
 * e-mail: phlxplus@163.com
 * description: 进入APP之前的配置
 */

public class AppContext extends Application {
    private static AppContext application;
    private final static String TAG = "AppContext";

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new BezierRadarHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new BallPulseFooter(context);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        if (SystemUtils.isInMainProgress(this)) {
            int openCount = DataBase.getInt(AppConfig.openCount);
            Log.d(TAG, "启动次数:" + openCount);

            DataBase.saveBoolean(AppConfig.ISAUTOLOGOIN, false);
            //设置默认值
            if (openCount == 0) {
                firstInitApp();
            }
            openCount = openCount + 1;
            DataBase.saveInt(AppConfig.openCount, openCount);
        }

        //拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = OkHttpUtils.getInstance().
                getOkHttpClient().
                newBuilder().
                connectTimeout(60, TimeUnit.SECONDS).
                readTimeout(60, TimeUnit.SECONDS).
                writeTimeout(60, TimeUnit.SECONDS).
                addInterceptor(httpLoggingInterceptor).
                addInterceptor(new FakeApiInterceptor()).
                build();

        OkHttpUtils.getInstance().changeOkHttpClient(okHttpClient);

        SharedPreferenceHelper.init(this);
        MyDownLoadManager.init(this);
        Log.d(TAG, "sha1" + getCertificateSHA1Fingerprint(this));

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
//        //log打印级别，决定了log显示的详细程度
//        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
//        //log颜色级别，决定了log在控制台显示的颜色
//        loggingInterceptor.setColorLevel(Level.INFO);
//        builder.addInterceptor(loggingInterceptor);

        //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));

        OkGo.getInstance().init(this)                                //必须调用初始化
                .setOkHttpClient(builder.build());                   //建议设置OkHttpClient，不设置将使用默认的


        //=============================Jpush初始化===============================//

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        //设置别名
        //之后再看设备别名,这个就是你可以为客户端设置别名,这样在你推送的时候,
        //你就推送到和你设置别名匹配的客户端上,使用直接调用setAlias()方法即可(比如用户登录完,你可以把用户名作为该用户的别名)
//获取Mac地址
        String macAddress= DeviceUtils.getMac();

        Log.d(TAG,"当前设备的Mac地址是："+macAddress);

        JPushInterface.setAlias(this, //上下文对象
                //"tuyue123456", //别名
                macAddress,
                new TagAliasCallback() {//回调接口,i=0表示成功,其它设置失败
                    @Override
                    public void gotResult(int responseCode, String s, Set<String> set) {
                        if (responseCode==0) {
                            System.out.println("jpush alias@@@@@别名设置成功");
                        }
                        Log.d("alias", "set alias result is" + responseCode);
                    }
                });


        //设置标签(TAG)
//        这个和上面别名区别在于,这个可以发送好多个客户端,只要匹配这个标签即可
//        方法是调用setTag()这个方法
//        说下这个方法把,这个方法的第二个参数是个set集合,为什么呢?因为每个人可能有好多个兴趣爱好,这样就可能有多个标签.
//       这里我们假设第一个用户的TAG是sport和game;第二个用户的TAG是music和game
//        Set<String> sets = new HashSet<>();
//        sets.add("movie");//运行第二个模拟器上时把这个注掉
//        sets.add("game");
////      sets.add("music");//运行第二个模拟器上时把这个打开
//        JPushInterface.setTags(this, sets, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//                Log.d("alias", "set tag result is" + i);
//            }
//        });

        //=============================Jpush初始化===============================//

    }

    public static AppContext getApplication() {
        return application;
    }
    /**
     * 安装后第一次进入
     */
    private void firstInitApp() {
        DataBase.saveBoolean(AppConfig.ISAUTOLOGOIN, true);
    }

    //这个是获取SHA1的方法
    public static String getCertificateSHA1Fingerprint(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取当前要获取SHA1值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数Context应该是对应包的上下文。
        String packageName = context.getPackageName();
        //返回包括在包中的签名信息
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            //获得包的所有内容信息类
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //签名信息
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        //将签名转换为字节数组流
        InputStream input = new ByteArrayInputStream(cert);
        //证书工厂类，这个类实现了出厂合格证算法的功能
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        //X509证书，X.509是一种非常通用的证书格式
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            //加密算法的类，这里的参数可以使MD4,MD5等加密算法
            MessageDigest md = MessageDigest.getInstance("SHA1");
            //获得公钥
            byte[] publicKey = md.digest(c.getEncoded());
            //字节到十六进制的格式转换
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }
    //这里是将获取到得编码进行16进制转换
    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }

}
