package logdemo.wjj.com.Tiffany;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Ezreal on 2015/10/16.
 */
public class PageSwitch {

    public static void goToLoginActivity(Activity activity) {
        Intent intent=new Intent(activity,LoginActivity.class);
        activity.startActivity(intent);
    }

    public static void goToRegisterActivity(Activity activity) {
        Intent intent=new Intent(activity,RegisterActivity.class);
        activity.startActivity(intent);
    }

}
