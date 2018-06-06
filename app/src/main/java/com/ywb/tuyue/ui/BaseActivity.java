package com.ywb.tuyue.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ywb.tuyue.AppManager;
import com.ywb.tuyue.jpush.ExampleUtil;
import com.ywb.tuyue.R;
import com.ywb.tuyue.receiver.NetWorkStateReceiver;
import com.ywb.tuyue.utils.UiHelper;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mhdt on 2017/12/14.
 * Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static boolean isForeground = false; //receiver接收消息时使用
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.ywb.tuyue.ui.web.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";

    protected Activity mContext;
    private Unbinder unbinder;
    private boolean isResume;
    private NetWorkStateReceiver netWorkStateReceiver;

    public static void Go(Context from, Class to, Intent intent) {
        if (intent != null) {
            intent.setClass(from, to);
        } else {
            intent = new Intent(from, to);
        }
        from.startActivity(intent);
    }

    public static void Go(Context from, Class to) {
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(getViewId());
        unbinder = ButterKnife.bind(this);
        initView(savedInstanceState);
        setHeader();
        //注册推送消息的广播
        registerMessageReceiver();
    }
    protected abstract int getViewId();
    protected abstract void initView(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        UiHelper.getInstance().destoryDialog();
        unbinder.unbind();
        //unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    /**
     * 返回键处理
     */
    protected View.OnClickListener onBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            back();
        }
    };

    protected void back() {
        super.onBackPressed();
    }

    protected void setHeader() {

    }

    @Override
    protected void onResume() {
        //监听网络变化
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        super.onResume();
        isResume = true;

        isForeground = true;
    }

    /**
     * 注册接受广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(netWorkStateReceiver);

        super.onPause();
        isResume = false;
        isForeground = false;
    }

    public final void onNetWorkChange() {
        if (isResume) {
            onNetWorkConnection();
        }
    }
    protected void onNetWorkConnection() {

    }

    class MessageReceiver extends BroadcastReceiver{
        String imageUrl;
        @Override
        public void onReceive(Context context, Intent intent) {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String message = intent.getStringExtra(KEY_MESSAGE);
                    System.out.println("接受从广播中传过来的自定义消息内容是:"+ message+"/////////");
                    //解析后台推送过来的消息
                  if(!ExampleUtil.isEmpty(message)){
                      try
                      {
                          JSONObject jsonObject=new JSONObject(message);
                          imageUrl=jsonObject.getString("picUrl");
                          String name=jsonObject.getString("name");
                          int id=jsonObject.getInt("id");
                          System.out.println("图片的url是:"+ imageUrl+"\n"+"字段名是："+name+"\n"+"字段ID："+id+"/////////");
                      }
                      catch (Exception e) {
                          e.printStackTrace();
                      }
                  }
                  showPushMessage(imageUrl);
                }
        }
    }

    private void showPushMessage(String imageUrl) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        //判断Context是否为空
       Context context =BaseActivity.this;


        if(null!=context){
            System.out.println("开始展示dialog"+"///////////////////////");

            Dialog dialog=new Dialog(context,R.style.PushDialog);

            View view = LayoutInflater.from(context).inflate(R.layout.push_dialog, null, false);

            ImageView imageView=  view.findViewById(R.id.push_image);
            System.out.println("开始下载图片了！！！！"+"/////////////////////////");
            Glide.with(getApplicationContext()).load(imageUrl).centerCrop().into(imageView);
            //loadIntoUseFitWidth(getApplicationContext(),imageUrl,imageView);
            System.out.println("推送广告图片下载成功！！！！"+"/////////////////////////");

            ImageView  cancel=view.findViewById(R.id.iv_cancle);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();

            attributes.type=WindowManager.LayoutParams.TYPE_PHONE;

            window.setGravity(Gravity.BOTTOM);
            attributes.width = width - 10;
            attributes.height = height - 300;
            window.setAttributes(attributes);


           //Dialog调用已经销毁的activity
            if(!isFinishing()){
                  dialog.show();
            }

            System.out.println("dialog展示成功！！！"+"///////////////////////");
       }
    }

    /**
     * 得到栈顶Activity的名称
     * @param context
     * @return
     */
    public String  getTopActivity(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = activityManager.getRunningTasks(1);


        if (list != null){
            ComponentName componentName = list.get(0).topActivity;

            //包名
            String packName = componentName.getPackageName();//packName=com.dr.dr_testappmanager

            //包名+类名，这是是我们需要的
            String className = componentName.getClassName();//className=com.dr.dr_testappmanager.MainActivity

            String nameStr  = componentName.toString();//ComponentInfo{com.dr.dr_testappmanager/com.dr.dr_testappmanager.MainActivity}

            return className;
        }
        return null;

    }

}
