package com.ywb.tuyue.ui.food;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.FoodOlderAdapter;
import com.ywb.tuyue.bean.BaseBean;
import com.ywb.tuyue.bean.Food;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.db.dao.FoodDao;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.Urls;
import com.ywb.tuyue.ui.BaseFragment;
import com.ywb.tuyue.utils.DateUtils;
import com.ywb.tuyue.utils.DeviceUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.OnClick;
import okhttp3.Request;

/**
 * Created by mhdt on 2017/12/16.
 * 订单
 */
public class FoodOlderFrag extends BaseFragment {
    RecyclerView recyclerView;
    FoodOlderAdapter foodOlderAdapter;
    TextView textView, emptyView;
    TextView xiaDan;
    private Timer timer;
    public static final int MAX = 60;
    private int maxTime = MAX;//倒计时最大时间
    private String phoneNo = "18629613520", phoneCode, carNoStr, seatNumber, menuInfo = "";
    private Food.ResultBean resultBean;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            resultBean = (Food.ResultBean) intent.getExtras().getSerializable("order");
            List<Food.ResultBean> foodList = FoodDao.getInstance().getFoodList();
            if (foodList != null) {
                for (int i = 0; i < foodList.size(); i++) {
                    if (foodList.get(i).getId() == resultBean.getId()) {
                        resultBean.setNumber(foodList.get(i).getNumber() + 1);
                    }
                }
            }
            FoodDao.getInstance().updateFood(resultBean);
            Log.d("FoodDao", "size: " + FoodDao.getInstance().getFoodList().size());
            foodOlderAdapter.getTotalPrice(FoodDao.getInstance().getFoodList());
            setXiaDanBg();
        }
    };

    @Override
    protected int getViewId() {
        return R.layout.frag_foodolder;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mActivity.registerReceiver(receiver, new IntentFilter("send_my_order"));
        recyclerView = findViewById(R.id.recyclerView);
        xiaDan = findViewById(R.id.xiaDan);
        emptyView = findViewById(R.id.emptyView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(foodOlderAdapter = new FoodOlderAdapter(mActivity, FoodDao.getInstance().getFoodList()));
        foodOlderAdapter.getTotalPrice(FoodDao.getInstance().getFoodList());
    }

    public void setXiaDanBg() {
        if (FoodDao.getInstance().getFoodList() == null) {
            xiaDan.setBackgroundResource(R.drawable.shape_gray);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            xiaDan.setBackgroundResource(R.drawable.shape_red);
            emptyView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.xiaDan)
    public void onViewClicked() {
        if (!DateUtils.isBetweenTime()) {
            showNotInBusinessHoursDialog();
            return;
        }
        if (null != dialog && dialog.isShowing()) {
            return;
        }
        if (FoodDao.getInstance().getFoodList() != null) {
            showDialog();
        }
    }

    /**
     * 不在服务时间c
     */
    private void showNotInBusinessHoursDialog() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_not_in, null);
        new AlertDialog.Builder(mActivity).setView(view).create().show();
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    maxTime--;
                    textView.setText(getTextHtml());
                    textView.setClickable(false);
                    if (maxTime < 0) {
                        textView.setClickable(true);
                        textView.setText("获取验证码");
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                maxTime = MAX;
                                if (timer != null)
                                    timer.cancel();
                                phoneNo = phone.getText().toString().trim();
                                if (TextUtils.isEmpty(phoneNo)) {
                                    Toast.makeText(mActivity, "手机号不能为空！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (!isMobileNum(phoneNo)) {
                                    Toast.makeText(mActivity, "手机号码有误！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                getPhoneCode();
                            }
                        });
                    }
                }
            });
        }
    }

    AlertDialog dialog;
    TextView phone;
    TextView submit;

    private void showDialog() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_xiadan, null);
        textView = view.findViewById(R.id.getCode);
        final TextView cancel = view.findViewById(R.id.btn_cancel);
        final TextView carNo = view.findViewById(R.id.select);
        final TextView chatNo = view.findViewById(R.id.select1);
        phone = view.findViewById(R.id.phone_no);
        final EditText phoneC = view.findViewById(R.id.phone_code);
        dialog = new AlertDialog//
                .Builder(mActivity)//
                .setCancelable(false)//
                .setView(view)//
                .create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        submit = view.findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNo = phone.getText().toString().trim();
                phoneCode = phoneC.getText().toString().trim();
                carNoStr = carNo.getText().toString().trim();
                seatNumber = chatNo.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNo)) {
                    Toast.makeText(mActivity, "手机号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isMobileNum(phoneNo)) {
                    Toast.makeText(mActivity, "手机号码有误！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phoneCode)) {
                    Toast.makeText(mActivity, "验证码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(carNoStr)) {
                    Toast.makeText(mActivity, "请输入车厢号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(seatNumber)) {
                    Toast.makeText(mActivity, "请输入座位号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkPhoneCode();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNo = phone.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNo)) {
                    Toast.makeText(mActivity, "手机号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isMobileNum(phoneNo)) {
                    Toast.makeText(mActivity, "手机号码有误！", Toast.LENGTH_SHORT).show();
                    return;
                }
                getPhoneCode();
            }
        });
    }

    /**
     * 判别手机是否为正确手机号码； 号码段分配如下：
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
     */
    private boolean isMobileNum(String mobiles) {
        // Pattern p = Pattern
        // .compile("^((\\(\\d{3}\\))|(\\d{3}\\-))?13[0-9]\\d{8}|15[89]\\d{8}");
        // Matcher m = p.matcher(mobiles);
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 校验验证码
     */
    private void checkPhoneCode() {
        OkHttpUtils//
                .post()//
                .tag(this)//
                .url(Urls.validateCode)//
                .addParams(AppConfig.token_key, AppConfig.token_value)//
                .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                .addParams("type", "1")//
                .addParams("telephone", phoneNo)//
                .addParams("authCode", phoneCode)//
                .build()//
                .execute(new AppGsonCallback<BaseBean>(new RequestModel(mActivity)) {
                    @Override
                    public void onResponseOK(BaseBean response, int id) {
                        super.onResponseOK(response, id);
                        //校验通过后执行下单操作
                        submitOrder();
                    }
                });
    }

    /**
     * 下单
     */
    private void submitOrder() {
        List<Food.ResultBean> beans = FoodDao.getInstance().getFoodList();
        if (beans == null) {
            return;
        }
        menuInfo = "";
        for (int i = 0; i < beans.size(); i++) {
            menuInfo += beans.get(i).getId() + "," + beans.get(i).getNumber() + ";";
        }
        OkHttpUtils//
                .post()//
                .tag(this)//
                .url(Urls.submitOrder)//
                .addParams(AppConfig.token_key, AppConfig.token_value)//
                .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                .addParams("imei", DeviceUtils.getUniqueId(mActivity))//PAD唯一识别码
                .addParams("totalFee", foodOlderAdapter.getTotalPrice() + "")//订单金额
                .addParams("carId", carNoStr)//餐车定位ID
                .addParams("seatNumber", seatNumber)//座位
                .addParams("telephone", phoneNo)//手机
                .addParams("menuInfo", menuInfo)//菜单详情：格式为:菜品1ID,菜品1数量;...
                .build()//
                .execute(new AppGsonCallback<BaseBean>(new RequestModel(mActivity)) {
                    @Override
                    public void onResponseOK(BaseBean response, int id) {
                        super.onResponseOK(response, id);
                        Toast.makeText(mActivity, "下单成功", Toast.LENGTH_SHORT).show();
                        ((FoodOlderActivity) mActivity).getFoodMenuFrag().getMyOrderList();
                        FoodDao.getInstance().delete();
                        dialog.dismiss();
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        submit.setClickable(true);
                        foodOlderAdapter.getTotalPrice(FoodDao.getInstance().getFoodList());
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        submit.setClickable(false);
                    }
                });
    }

    /**
     * 获取验证码
     */
    private void getPhoneCode() {
        timer = new Timer();
        timer.schedule(new MyTimerTask(), 1000, 1000);//开启倒计时
        OkHttpUtils//
                .post()//
                .tag(this)//
                .url(Urls.sendVeriCode)//
                .addParams(AppConfig.token_key, AppConfig.token_value)//
                .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                .addParams("type", "1")//
                .addParams("telephone", phoneNo)//
                .build()//
                .execute(new AppGsonCallback<BaseBean>(new RequestModel(mActivity).setShowProgress(false)) {
                    @Override
                    public void onResponseOK(BaseBean response, int id) {
                        super.onResponseOK(response, id);

                    }
                });
    }

    /**
     * 解决23API以上失效问题
     *
     * @return
     */
    private CharSequence getTextHtml() {
        CharSequence charSequence;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml("<font color=\"#2CC9BE\">" +
                    maxTime + "s后重新获取 </font>", Html.FROM_HTML_MODE_LEGACY);
        } else {
            charSequence = Html.fromHtml("<font color=\"#2CC9BE\">" +
                    maxTime + "s后重新获取 </font>");
        }
        return charSequence;
    }

    @Override
    public void onDestroyView() {
        mActivity.unregisterReceiver(receiver);
        super.onDestroyView();
    }
}
