package com.ywb.tuyue.ui.setting;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ywb.tuyue.R;
import com.ywb.tuyue.ui.setting.hotspot.ServiceUtil;
import butterknife.BindView;
/**
 * Created by Administrator on 2018/5/30.
 */
public class HotSpotFragment extends com.ywb.tuyue.ui.BaseFragment {
    @BindView(R.id.setHotPassword)
    EditText setPassword;//进入设置界面需要输入的密码
    @BindView(R.id.jumpSetting)
    TextView jumpSetting;//进入设置界面的确定按钮

    private String password = "tuyue123456";

    @Override
    protected int getViewId() {
        return R.layout.activity_hot_spot;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        jumpSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPassword = setPassword.getText().toString().trim();
                if (TextUtils.isEmpty(inputPassword)) {
                    Toast.makeText(getContext(), "请输入密码！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!inputPassword.equals(password)) {
                    Toast.makeText(getContext(), "密码不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                startSetting();
                startService();
            }
        });
    }

    /**
     * 启动service
     */
    private void startService() {
        ServiceUtil.startService(getContext());
    }

    /**
     * 进入设置界面
     */
    public void startSetting() {
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }
}
