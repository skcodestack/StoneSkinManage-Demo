package github.com.stoneskin.skin.config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/6/12
 * Version  1.0
 * Description:
 */

public class SkinUtil {

    static volatile SkinUtil mSkinUtil = null;
    Context mContext;

    private SkinUtil(Context context){
        this.mContext = context.getApplicationContext();
    }

    public static SkinUtil getInstance(Context context){
        if(mSkinUtil == null) {
            synchronized (SkinUtil.class) {
                if(mSkinUtil == null){
                    mSkinUtil = new SkinUtil(context);
                }
            }
        }
        return mSkinUtil;
    }

    /**
     * 保存皮肤路径
     * @param skinPath
     */
    public void saveSkinPath(String skinPath){
        SharedPreferences preferences = mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(SkinConfig.SKIN_PATH_NAME,skinPath).commit();
    }

    /**
     * 获取皮肤路径
     * @return
     */
    public String  getSkinPath() {
        SharedPreferences preferences = mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE);
        String path = preferences.getString(SkinConfig.SKIN_PATH_NAME, "");
        return path;
    }
}
