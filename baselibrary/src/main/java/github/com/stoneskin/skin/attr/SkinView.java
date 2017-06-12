package github.com.stoneskin.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/5/16
 * Version  1.0
 * Description:  view 和 要更换值的属性结合
 */

public class SkinView {
    private View mView;

    private List<SkinAttr> mAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView=view;
        this.mAttrs=skinAttrs;
    }

    /**
     * 当前view中属性更换资源
     */
    public void skin(){
        for (SkinAttr attr : mAttrs) {
            attr.skin(mView);
        }
    }
}
