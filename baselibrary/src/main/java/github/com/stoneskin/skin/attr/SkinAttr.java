package github.com.stoneskin.skin.attr;

import android.view.View;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/5/16
 * Version  1.0
 * Description:    view 属性以及属性类型
 */

public class SkinAttr {
    //资源名
    private String mResName;
    //属性类型
    private SkinType mSkinType;

    public SkinAttr(String name, SkinType skinType) {
        this.mResName=name;
        this.mSkinType=skinType;
    }

    /**
     * 跟换view的属性资源
     * @param mView
     */
    public void skin(View mView) {
        mSkinType.skin(mView,mResName);
    }
}
