package logdemo.wjj.com.Tiffany;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.nineoldandroids.animation.Animator;

import logdemo.wjj.com.Tiffany.Anim.Techniques;
import logdemo.wjj.com.Tiffany.Anim.YoYo;
import logdemo.wjj.com.Tiffany.Custom.IconEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    final static String TAG = LoginActivity.class.getName() + ".TAG";

    private long exitTime = 0;
    private IconEditText users, password;
    private Button login;
    private TextView go_to_register;
    private String User, Password;
    private YoYo.YoYoString rope;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor localEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.d("--->" + TAG + " onCreate");

        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        localEditor = sharedPreferences.edit();

        users = (IconEditText) findViewById(R.id.user);
        password = (IconEditText) findViewById(R.id.password);

        go_to_register = (TextView) findViewById(R.id.go_to_reg);

        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(this);
        go_to_register.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onClick(View v) {
        int flag = v.getId();
        switch (flag) {
            case R.id.login:
                logic();
                break;
            case R.id.go_to_reg:
                localEditor.clear().commit();
                PageSwitch.goToRegisterActivity(LoginActivity.this);
                break;
        }
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

    private void logic() {
        User = users.getEditText().getText().toString();
        Password = password.getEditText().getText().toString();
        if (!User.equals(sharedPreferences.getString("username", "")) || !Password.equals(sharedPreferences.getString("password", ""))) {
            makeAnim(users, password);
        } else {
            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
        }
    }


    private void makeAnim(View view1, View view2) {
        Techniques technique = (Techniques) users.getTag();
        rope = YoYo.with(technique.Shake)
                .duration(1200)
                .interpolate(new AccelerateDecelerateInterpolator())
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        Toast.makeText(LoginActivity.this, "canceled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(view1);

        rope = YoYo.with(technique.Shake)
                .duration(1200).playOn(view2);
    }

    //重寫finish實現動畫效果
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.scale_in, R.anim.alpha_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
