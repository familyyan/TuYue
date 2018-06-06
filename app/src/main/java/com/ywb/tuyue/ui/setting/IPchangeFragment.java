package com.ywb.tuyue.ui.setting;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ywb.tuyue.R;
import com.ywb.tuyue.net.Urls;
import com.ywb.tuyue.ui.BaseFragment;
import butterknife.BindView;
/**
 * Created by penghao on 2018/4/25.
 * description：
 */
public class IPchangeFragment extends BaseFragment {
    @BindView(R.id.change_ip)
    EditText changeIp;
    @BindView(R.id.change_port)
    EditText changePort;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.current_IP)
    TextView currentIP;
    @BindView(R.id.reset)
    TextView reset;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private final static String PORT = "8080";
    private final static String PORT_1905 = "8087";
    private final static String URL_HEAD = "http://";
    @Override
    protected int getViewId() {
        return R.layout.frag_change_ip;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(rootView);
            }
        });
        currentIP.setText("当前访问IP：" + Urls.HEAD_URL);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newIp = changeIp.getText().toString().trim();
                String newPort = changePort.getText().toString().trim();
                if (!TextUtils.isEmpty(newIp)) {
                    new AlertDialog//
                            .Builder(mActivity)//
                            .setMessage("此项操作有可能导致无法连接服务器，输入不规范会导致设备闪退，请问是否继续修改？")//
                            .setTitle("重要提示").setNegativeButton("取消", null)//
                            .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (TextUtils.isEmpty(newPort)) {
                                        Urls.HEAD_URL = URL_HEAD + newIp;
                                        Urls.HEAD_URL_1905 = URL_HEAD + newIp;
                                    } else {
                                        Urls.HEAD_URL = URL_HEAD + newIp + ":" + newPort;
                                        Urls.HEAD_URL_1905 = URL_HEAD + newIp + ":" + newPort;
                                    }
                                    Urls.HEAD_URL_IMAGE = URL_HEAD + newIp;
                                    Urls.change();
                                    currentIP.setText("当前访问IP：" + Urls.HEAD_URL);
                                    Toast.makeText(mActivity, "操作成功", Toast.LENGTH_SHORT).show();
                                }
                            }).show();

                } else {
                    Toast.makeText(mActivity, "请输入IP地址", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog//
                        .Builder(mActivity)//
                        .setMessage("您的设备将恢复至原始IP地址[192.168.1.6]")//
                        .setTitle("重要提示").setNegativeButton("取消", null)//
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Urls.HEAD_URL = Urls.TEST_HEAD_URL;
                                Urls.HEAD_URL_IMAGE = Urls.TEST_HEAD_URL_IMAGE;
                                Urls.HEAD_URL_1905 = Urls.TEST_HEAD_URL_1905;
                                Urls.change();
                                currentIP.setText("当前访问IP：" + Urls.HEAD_URL);
                                Toast.makeText(mActivity, "操作成功", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }
    //隐藏键盘
    public void dismiss(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            // 强制隐藏软键盘
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
