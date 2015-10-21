package logdemo.wjj.com.Tiffany;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

import logdemo.wjj.com.Tiffany.Anim.Techniques;
import logdemo.wjj.com.Tiffany.Anim.YoYo;
import logdemo.wjj.com.Tiffany.Utils.BaseActivity;

public class Main extends BaseActivity {

    final static String TAG = Main.class.getName() + ".TAG";

    private final static boolean DEBUG = true;

    private ImageView imageView;
    private YoYo.YoYoString rope;
    //計時器
    private int recLen = 5;

    Timer timer = new Timer();

    TimerTask task = new TimerTask() {
        public void run() {
            recLen--;
            //修改界面的相关设置只能在UI线程中执行
            runOnUiThread(new Runnable() {
                public void run() {
                    if (recLen < 3) {
                        timer.cancel();
                        goToNext();
                    }
                }
            });
        }
    };


    @Override
    protected void findById() {
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void logic() {
        //漸漸放大
        rope = YoYo.with(Techniques.ZoomInUp).duration(1800).playOn(imageView);

        timer.schedule(task, 1000, 1000);
    }

    @Override
    protected int getLayout() {
        return R.layout.main;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("---->Main onDestroy stopNetworkService");
        stopNetworkService();
    }

    private void goToNext() {
        if (isNetworkConnected(Main.this)) {
            SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
            String name=sharedPreferences.getString("username", "");
            Intent a;
            if(name.equals(null) || name.length() <= 0){
                a = new Intent(Main.this, RegisterActivity.class);
            }else{
                a = new Intent(Main.this, LoginActivity.class);
            }
            startActivity(a);
            overridePendingTransition(R.anim.scale_in, R.anim.alpha_out);

        } else {
            Intent intent = null;
            // 先判断当前系统版本
            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            } else {
                intent = new Intent();
                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
            }
            startActivity(intent);
        }
    }


}
