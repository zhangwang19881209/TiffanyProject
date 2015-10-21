package logdemo.wjj.com.Tiffany;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import logdemo.wjj.com.Tiffany.Custom.IconEditText;
import logdemo.wjj.com.Tiffany.Utils.BaseActivity;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    final static String TAG = RegisterActivity.class.getName() + ".TAG";
    private long exitTime = 0;
    private int recLen = 61;
    private boolean isTiming = false;

    private IconEditText password, user, phone, message;
    private Button registerButton, send_msg;
    private TextView go_to_login;

    Timer timer = new Timer();

    TimerTask task = new TimerTask() {
        public void run() {
            recLen--;
            //修改界面的相关设置只能在UI线程中执行
            runOnUiThread(new Runnable() {
                public void run() {
                    if (recLen >= 0) {
                        mHandler.sendEmptyMessage(recLen);
                    } else {
                        timer.cancel();
                        recLen = 61;
                    }
                }
            });
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int reclenInt = msg.what;
            if (reclenInt > 0) {
                send_msg.setClickable(false);
                send_msg.setText(reclenInt + "秒");
            } else {
                send_msg.setClickable(true);
                send_msg.setText(getResources().getString(R.string.send_Msg));
            }
        }
    };

    @Override
    protected void findById() {
        password = (IconEditText) findViewById(R.id.password);
        user = (IconEditText) findViewById(R.id.user);
        phone = (IconEditText) findViewById(R.id.phone);
        message = (IconEditText) findViewById(R.id.message);

        registerButton = (Button) findViewById(R.id.registerbutton);
        send_msg = (Button) findViewById(R.id.send_msg);

        go_to_login = (TextView) findViewById(R.id.go_to_login);
    }

    @Override
    protected void setListener() {
        registerButton.setOnClickListener(this);
        send_msg.setOnClickListener(this);
        go_to_login.setOnClickListener(this);
    }

    @Override
    protected void logic() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void onClick(View v) {
        int flag = v.getId();
        switch (flag) {
            case R.id.registerbutton:
                if (LoginLogic()) {
                    //異步操作
                    ShowToast(RegisterActivity.this,"Reguster Success");
                }
                break;
            case R.id.send_msg:
                if (isTiming == false) {
                    sendMsg();
                }
                break;
            case R.id.go_to_login:
                PageSwitch.goToLoginActivity(RegisterActivity.this);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                //返回桌面
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
                startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("---->RegisterActivity onDestroy stopNetworkService");
        stopNetworkService();
    }

    //重寫finish實現動畫效果
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.scale_in, R.anim.alpha_out);
    }

    //發短信,模擬的 你懂的
    private void sendMsg() {
        timer.schedule(task, 1000, 1000);
    }

    //註冊業務邏輯
    private boolean LoginLogic() {
        //申明一個集合來處理一系列判斷，一個個寫 太麻煩了。。感覺還有更簡便的。有空想想
        Hashtable<String, String> hashMap = new Hashtable();
        hashMap.put("message", message.getText().toString());
        hashMap.put("phone", phone.getText().toString());
        hashMap.put("password", password.getText().toString());
        hashMap.put("username", user.getText().toString());
        //遍曆Map也會有性能問題，但是這也就 4個字符串，又不多，就隨便了
        // 在tools的PhoneUtils類裏 有一些符合操作的工具類，可以使用
        Iterator iterator = hashMap.entrySet().iterator();
        SharedPreferences sharedPreferences=getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor localEditor = sharedPreferences.edit();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();

            LogUtils.d("--->RegisterActivity  LoginLogic()" + "key :" + key + " val " + val);

            if (val.equals(null) || val.length() <= 0) {
                ShowToast(RegisterActivity.this, key + " No fill");
                localEditor.clear().commit();
                return false;
            }else{
                localEditor.putString(key,val);
                localEditor.commit();
            }
        }

        return true;
    }
}
