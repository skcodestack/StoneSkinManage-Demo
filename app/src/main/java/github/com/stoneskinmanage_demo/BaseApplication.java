package github.com.stoneskinmanage_demo;

import android.app.Application;



import github.com.stoneskin.skin.SkinManager;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/4/25
 * Version  1.0
 * Description:
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        ExpectionCrashHandler.getInstance().init(this);

        SkinManager.getInstance().init(this);
    }
}
