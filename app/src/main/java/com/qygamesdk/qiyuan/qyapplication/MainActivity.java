package com.qygamesdk.qiyuan.qyapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qygame.qysdk.container.QYGameSDK;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.consts.QYCodeDef;
import com.qygame.qysdk.outer.model.GameParamInfo;

public class MainActivity extends FragmentActivity {
    public static final String TAG = "QYGAMESDK:MAINACTIVITY";
    Button login, payH5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        payH5 = findViewById(R.id.payH5);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginImpl();
            }
        });
        payH5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payUrl = "http://www.373yx.com/payment/preview?cliBuyerId=19000&cliSellerId=2018111415564890400010102c2&" +
                        "cpOrderNo=" + System.currentTimeMillis() +
                        "&cpPrice=0.01&cpOrderTitle=%E9%A6%96%E5%85%851";
                payH5(payUrl);
            }
        });
        initSdk();
    }

    private void initSdk() {

        GameParamInfo paramInfo = new GameParamInfo();
        paramInfo.setGameId("2018111415564890400010102c2");
        paramInfo.setSdkKey("7dc18ce3418bcfb6ffa6e72ba1943884");

        QYGameSDK.getInstance().init(this, paramInfo, false, this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                Configuration.ORIENTATION_LANDSCAPE : Configuration.ORIENTATION_PORTRAIT, new IOperateCallback<String>() {
            @Override
            public void onResult(int i, String s) {
                if (i == 0) {
                    Log.d(TAG,"QYGameSDK初始化成功！");
                } else {
                    Log.d(TAG,"QYGameSDK初始化失败！");
                }
            }
        });
    }

    private void loginImpl() {
        QYGameSDK.getInstance().login(this, new IOperateCallback<String>() {
            @Override
            public void onResult(int code, String s) {
                if (code == QYCodeDef.SUCCESS) {
                    Log.d(TAG,"QYGameSDK登录成功！");
                    login.setText("退出");
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            logoutImpl();
                        }
                    });
                } else {
                    Log.d(TAG,"QYGameSDK登录失败！");
                }
            }
        });
    }

    private void logoutImpl() {
        QYGameSDK.getInstance().setLogoutListener(new IOperateCallback<String>() {
            @Override
            public void onResult(int code, String s) {
                if (code == QYCodeDef.SUCCESS) {
                    Log.d(TAG,"QYGameSDK退出成功！");
                    login.setText("登录");
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loginImpl();
                        }
                    });
                } else {
                    Log.d(TAG,"QYGameSDK退出失败！");
                }
            }
        });
        QYGameSDK.getInstance().logout();
    }

    private void payH5(String payUrl) {
//        QYGameSDK.getInstance().payH5(MainActivity.this, "http://www.373yx.com", payUrl, null);
        Long cliBuyerId = 19000L ;
        String cliSellerId = "2018111415564890400010102c2";
        String cpOrderNo = System.currentTimeMillis() + "";
        String cpOrderTitle = "%E9%A6%96%E5%85%851";
        float cpPrice = 0.01f;
        QYGameSDK.getInstance().payH5(MainActivity.this, cliBuyerId, cliSellerId, cpOrderNo, cpOrderTitle, cpPrice);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged" + newConfig.orientation);
    }

}
