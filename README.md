# QYApplication
起源SDK接入文档
一、开发环境，接入sdk
1，复制QYGame-QYSDK-V1.0.0-R14.jar到工程的libs目录下，并在build.gradle文件中进行依赖配置，即在dependencies
中添加如compile fileTree(dir: 'libs', include: ['*.jar'])，
或者compile files('libs/MobileGameBAR-RSDK-V1.0.0-R14.jar')
2，复制“服务条款”网页terms_of_service.html至assets/html中
3，在项目的 AndroidManifest.xml 文件,进行如下修改:
a,增加权限声明(下图中所示的权限为SDK所必需的,游戏可根据需要额外增加其它权限声 明);
b,增加 FloatService声明；
c,横竖屏设置configChanges；
修改后的AndroidManifest.xml如下:
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.qygamesdk.qiyuan.qyapplication">

    <uses-permission android:name="ANDROID.PERMISSION.MOUNT_UNMOUNT_FILESYSTEMS" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="ANDROID.PERMISSION.WRITE_SETTINGS" />
    <!-- 自动添加短信验证码权限 -->
    <uses-permission android:name="android.permission.RECEIVE_SMS" />
    <uses-permission android:name="android.permission.READ_SMS" />
    <uses-permission android:name="android.permission.WRITE_SMS" />
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.webkit.permission.PLUGIN" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@android:style/Theme.NoTitleBar.Fullscreen">
        <activity
            android:name=".MainActivity"
            android:configChanges="screenSize|orientation|keyboardHidden"
            android:screenOrientation="sensor"
            android:theme="@android:style/Theme.NoTitleBar.Fullscreen">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <service android:name="com.yiyou.gamesdk.container.FloatService" />
    </application>

</manifest>

二、接口说明
消息通知机制
SDK 的大部分 API 调用使用了接口回调的方式通知结果消息给游戏,游戏通过实现抽象方法 callback,就可以获取接口调用的结果. 对于大部分callback, 返回值0为成功, 其余情况为失败,message将给予失败情况可读说明.该抽象方法定义在接口 IOperateCallback<T> 中,具体的抽象方法定义为:
public void onResult(int code, T resultData);
参数说明如下:
参数	说明
code	状态码,定义在 QYCodeDef 中,具体的可以参考 javadoc 文档 或者下表说明. 0 表示成功， 其余值表示失败.
resultData	接口返回值泛型,具体的类型会在各个接口回调传入时声明.具体传入的 类型可以参考下几节的例程.
code 状态说明:
状态码	说明
UNKNOWN	未知错误
SUCCESS	所有 API 接口回调函数成功处理时的状态码,表明操作已成功完成。

三、接口具体使用（RGameSDK）
3.1  RGameSDK的初始化
说明：首先,您需要在程序开始的地方初始化 RGameSDK对象,主要通过调用 init 方法,并设 置 Activity 对象和初始化完成后的回调函数。需要设置了 Activity 对象后 SDK 才能正常地运行。 在初始化失败的状态下,游戏不应继续调用 SDK 的其余 API,此方法必须在 UI 线程中调用。
3.1.1 方法定义
public void init(Activity context, GameParamInfo info, boolean isDebug, int orientation, IOperateCallback<String> callback)
3.1.2  参数说明

     * @param context 当前Activity
     * @param info 存储游戏信息，如gameId，sdkkey等内容
     * @param isDebug 调试模式，true：代表调试模式，false：代表生产模式
     * @param orientation 横竖屏模式 Configuration.ORIENTATION_LANDSCAPE、Configuration.ORIENTATION_PORTRAIT
     * @param callback 回调接口  i == 0 代表成功
3.1.3 代码示例
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

3.2登录接口
说明：调用该接口，界面会弹出登录弹窗，界面包含了号码登录，游客登录，服务条款等接口。
3.2.1  方法定义
public void login(Activity activity, IOperateCallback<String> callback)
3.2.2  参数说明
 * @param activity 当前Activity
 * @param callback 回调接口  code == 0 或者 code== QYCodeDef.SUCCESS，代表成功
3.2.3  代码示例
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

3.3，支付调用
3.3.1  方法定义
public void payH5(Activity activity, String s, String s1, IOperateCallback<String> iOperateCallback)
3.3.2  参数说明
* @param activity  当前Activity
* @param s   referer  支付域名
* @param s1  payUrl  支付链接地址
* @param iOperateCallback  回调，当前版本设置为null
3.3.3  代码示例

String payUrl = "http://www.373yx.com/payment/preview?cliBuyerId=19000&cliSellerId=2018111415564890400010102c2&" +
        "cpOrderNo=" + System.currentTimeMillis() +
        "&cpPrice=0.01&cpOrderTitle=%E9%A6%96%E5%85%851";
QYGameSDK.getInstance().payH5(MainActivity.this, "http://www.373yx.com", payUrl, null);
3.4，退出登录（切换账号）
说明，退出登录需要设置退出接口，同时，退出登录与切换账号都会调用该接口
3.4.1  接口定义
void setLogoutListener(IOperateCallback<String> iOperateCallback)
void logout()
3.4.2  参数说明
* @param iOperateCallback  退出回调
3.4.3  代码示例
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
四、代码混淆要求
如果游戏发布时采用 proguard 进行代码混淆,请在 proguard 配置文件加入以下代码,以避免对SDK 进行混淆,否则会造成 SDK 部分功能不正常。
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep  class com.yiyou.gamesdk.container.**{
     public <methods>;
     public <fields>;
}
-keep  class com.yiyou.gamesdk.outer.model.**{*;}
-keep  class com.yiyou.gamesdk.outer.**{
     public <methods>;
     public <fields>;
}
-keep  class com.google.gson1.**{*;}
