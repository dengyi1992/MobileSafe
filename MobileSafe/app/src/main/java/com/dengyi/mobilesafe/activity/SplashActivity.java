package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dengyi.mobilesafe.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import utils.StreamUtils;

public class SplashActivity extends Activity {

    private static final int CODE_UPDATE_DAILOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_NET_ERROR = 2;
    private static final int CODE_JSON_ERROR = 3;
    private static final int CODE_ENTER_HOME = 4;
    public TextView tvVersion;
    public TextView tvProgress;
    String mVersionName;//版本名字
    int mVersionCode;//版本代码
    String mDesc;//版本描述
    String mDownloadUrl;//下载链接

    private Handler mHandler = new Handler() {


        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DAILOG:
                    showUpdateDailog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT)
                            .show();
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT)
                            .show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "数据解析错误",
                            Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;

                default:
                    break;
            }
        };
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        tvVersion= (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本号："+getVersionName());
        tvProgress= (TextView) findViewById(R.id.tv_progress);
        checkVersion();
    }

    /**
     * 获取本地版本名
     * @return
     */
    private String getVersionName(){
        int versionCode;
        String versionName = null;
        PackageManager packageManager= getPackageManager();
        try {
            PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),0);
            //获取包的信息
            versionCode=packageInfo.versionCode;
            versionName=packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
    /**
     * 获取本地版代码
     * @return
     */
    private int getVersionCode(){
        int versionCode = 0;

        PackageManager packageManager= getPackageManager();
        try {
            PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),0);
            //获取包的信息
            versionCode=packageInfo.versionCode;
            return versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
    /**
     *从服务器获取版本信息进行校验
     * 放入子线程
     */
    private void checkVersion(){
     final long startTime=System.currentTimeMillis();
    //启动子线程异步加载

        new Thread(){
            @Override
            public void run() {
                Message msg= Message.obtain();
                HttpURLConnection conn = null;
                try {
/**
 * 模拟器可用ip（10.0.2.2）代替
 */
//                    URL url=new URL("http://10.0.2.2:8080/update.json");
                    URL url = new URL("http://192.168.0.104:8080/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);//链接超时
                    conn.setReadTimeout(5000);//读取超时
                    conn.connect();//连接服务器
                    int responseCode = conn.getResponseCode();//获取响应码
                    if (responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        String result = StreamUtils.readFromStream(inputStream);
                        System.out.println(result);

                        JSONObject jsonObject = new JSONObject(result);
                        mVersionName = jsonObject.getString("versionName");
                        mVersionCode = jsonObject.getInt("versionCode");
                        mDesc = jsonObject.getString("description");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        // System.out.println(mDesc);
                        if (mVersionCode > getVersionCode()) {
                            //弹出升级对话框
                            msg.what = CODE_UPDATE_DAILOG;
//                            showUpdateDailog();
                        } else {
                            enterHome();
                        }
                    }

                } catch (MalformedURLException e) {
                    //url错误解析
                    msg.what= CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    //网络错误
                    msg.what= CODE_NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what= CODE_JSON_ERROR;
                    e.printStackTrace();
                }finally {
                    long endTime=System.currentTimeMillis();
                    long timeUsed=endTime-startTime;
                    if(timeUsed<2000){
                        try {
                            Thread.sleep(2000-timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                    //关闭网络链接
                    if (conn!=null){
                        conn.disconnect();
                    }
                }
            }
        }.start();

    }

    /**
     * 升级对话框
     */
    private void showUpdateDailog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("最新版：" + mVersionName);
        builder.setMessage(mDesc);
     //   builder.setCancelable(false);//体验不好
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
                System.out.println("立即更新");
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("以后再说");
                enterHome();

            }
        });
        //取消监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }
    /**
     * 进入主界面
     */
    private void enterHome(){
        Intent intent= new Intent(this,HomeActivity.class);
        startActivity(intent);
        //跳转下一个页面，并且销毁当前页面
        finish();
    }
    /**
     * 下载apk
     */
    protected void download(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            String target= Environment.getExternalStorageDirectory()+"update.apk";
            tvProgress.setVisibility(View.VISIBLE);
            String target="sdcard/update.apk";
            HttpUtils utils= new HttpUtils();
            utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
                //文件下载进度
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    System.out.println("下载进度：" + current + "/" + total);
                    tvProgress.setText("下载进度："+current*100/total+"%");
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Toast.makeText(SplashActivity.this,"下载成功",Toast.LENGTH_LONG).show();
                    //跳转到系统下载页面,安装
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result),
                            "application/vnd.android.package-archive");
                    startActivity(intent);
                    startActivityForResult(intent, 0);//与onActivityResult配对
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(SplashActivity.this,"下载失败",Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(SplashActivity.this,"没有SD卡",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
