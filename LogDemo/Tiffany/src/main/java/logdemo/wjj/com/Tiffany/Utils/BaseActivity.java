package logdemo.wjj.com.Tiffany.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;


public abstract class BaseActivity extends Activity {

    protected static final String TAG = BaseActivity.class.getName() + ".TAG";

    Intent network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        findById();

        setListener();

        logic();

        //监听网络
        if (network == null) {
            network = new Intent(BaseActivity.this, NetworkStateService.class);
        }
        startService(network);
    }

    //FindById
    protected abstract void findById();

    //setListener
    protected abstract void setListener();

    //Logic
    protected abstract void logic();

    protected abstract int getLayout();

    //判断是否有网络
    protected boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断WIFI
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //关闭网络判断
    protected void stopNetworkService() {

        if (network != null) {
            stopService(network);
        }
    }

    protected void ShowToast(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("---->BaseActivity onDestroy stopNetworkService");
        stopNetworkService();
    }
}
