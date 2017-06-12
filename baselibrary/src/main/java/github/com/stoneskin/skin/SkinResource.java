package github.com.stoneskin.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/5/16
 * Version  1.0
 * Description: 皮肤的资源管理类
 */

public class SkinResource {
    //皮肤资源通过这个对象获取
    private  Resources mSkinResources=null;
    private  String mSkinPackageName;

    public SkinResource(Context context, String skinPath){
        try{
            Resources supRes = context.getResources();

            AssetManager assetManager = AssetManager.class.newInstance();

            Method addAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            addAssetPath.invoke(assetManager,skinPath);

            mSkinResources = new Resources(assetManager,supRes.getDisplayMetrics(),supRes.getConfiguration());

            mSkinPackageName = context.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * 通过名称获取drawable
     * @param name
     * @return
     */
    public Drawable getDrawableByName(String name){
        try {
            int resId = mSkinResources.getIdentifier(name, "drawable", mSkinPackageName);
            Drawable drawable = mSkinResources.getDrawable(resId);
            return drawable;

        }catch (Exception ex){

        }
        return null;
    }

    /**
     * 根据名称获取颜色
     * @param name
     * @return
     */
    public ColorStateList getColorByName(String name){
        try {
            int resId = mSkinResources.getIdentifier(name, "color", mSkinPackageName);
            ColorStateList colorStateList= mSkinResources.getColorStateList(resId);
            return colorStateList;

        }catch (Exception ex){
            Log.e("tag","=====>"+ex);
        }
        return null;
    }
}
