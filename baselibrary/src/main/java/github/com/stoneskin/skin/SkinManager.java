package github.com.stoneskin.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import github.com.stoneskin.skin.attr.SkinView;
import github.com.stoneskin.skin.callback.IChangeSkinListener;
import github.com.stoneskin.skin.config.SkinUtil;


/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/5/16
 * Version  1.0
 * Description:  皮肤管理
 */

public class SkinManager {
    static SkinManager mSkinManager = null;
    Map<IChangeSkinListener,List<SkinView>> mSkinViews= new ArrayMap<>();

    private Context mContext = null;
    static {
        mSkinManager=new SkinManager();
    }

    private SkinResource mSkinResource;

    public static SkinManager getInstance(){
        return mSkinManager;
    }

    public void init(Context context){
        mContext = context.getApplicationContext();
        String DefaultResourcePath = mContext.getPackageResourcePath();

        String skinPath = SkinUtil.getInstance(mContext).getSkinPath();
        if(TextUtils.isEmpty(skinPath)) {
            //初始化资源管理
            mSkinResource = new SkinResource(mContext,DefaultResourcePath);
            return;
        }

        File skinFile = new File(skinPath);
        if(!skinFile.exists()){
            SkinUtil.getInstance(mContext).saveSkinPath("");

            //初始化资源管理
            mSkinResource = new SkinResource(mContext,DefaultResourcePath);

            return;
        }

        String  packageName = context.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
        if(TextUtils.isEmpty(packageName)){
            SkinUtil.getInstance(mContext).saveSkinPath("");
            //初始化资源管理
            mSkinResource = new SkinResource(mContext,DefaultResourcePath);
            return;
        }
        //校验签名

        mSkinResource = new SkinResource(mContext,skinPath);

    }

    /**
     * 加载皮肤
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath){
        //校验签名

        //初始化资源管理
        mSkinResource = new SkinResource(mContext,skinPath);
        chanageSkin();

        saveSkinPath(skinPath);
        return 0;
    }

    /**
     * 恢复默认主题
     */
    public void restoreDefault(){
        String skinPath = SkinUtil.getInstance(mContext).getSkinPath();
        if(TextUtils.isEmpty(skinPath)){
            return;
        }
        String DefaultResourcePath = mContext.getPackageResourcePath();

        //初始化资源管理
        mSkinResource = new SkinResource(mContext,DefaultResourcePath);
        chanageSkin();

        saveSkinPath("");
    }

    private void chanageSkin(){
        Set<Map.Entry<IChangeSkinListener, List<SkinView>>> entries = mSkinViews.entrySet();
        for (Map.Entry<IChangeSkinListener, List<SkinView>> entry : entries) {
            List<SkinView> values = entry.getValue();
            for (SkinView view : values) {
                view.skin();
            }
            IChangeSkinListener listener = entry.getKey();
            listener.chanageSkin(mSkinResource);
        }
    }

    /**
     * 获取指定Activity的view集合
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(Activity activity) {
        List<SkinView> skinViews = mSkinViews.get(activity);
        return skinViews;
    }

    /**
     * 保存正在使用皮肤路径
     * @param skinPath
     */
    public void saveSkinPath(String skinPath){
        SkinUtil.getInstance(mContext).saveSkinPath(skinPath);
    }


    public void register(IChangeSkinListener activity, List<SkinView> skinViews) {
        mSkinViews.put(activity,skinViews);
    }
    public void unregister(Activity activity) {
        mSkinViews.remove(activity);
    }
    public SkinResource getSkinResource() {
        return mSkinResource;
    }

    /**
     * 检查是否要换肤
     * @param skinView
     */
    public void checkChangeSkin(SkinView skinView) {
        String skinPath = SkinUtil.getInstance(mContext).getSkinPath();
        if(!TextUtils.isEmpty(skinPath)){
            skinView.skin();
        }

    }
}
