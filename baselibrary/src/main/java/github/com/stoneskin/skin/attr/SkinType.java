package github.com.stoneskin.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import github.com.stoneskin.skin.SkinManager;
import github.com.stoneskin.skin.SkinResource;


/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/5/16
 * Version  1.0
 * Description:  皮肤属性的类型
 */

public  enum  SkinType {

    TEXT_COLOR("textColor") {
        @Override
        public void skin(View mView, String mResName) {
            SkinResource resource = getSkinResource();
            ColorStateList colorStateList = resource.getColorByName(mResName);
            Log.e("tag","textColor=====>"+mResName+"==="+colorStateList);

            if(colorStateList == null){
                return;
            }
            TextView tv= (TextView) mView;
            tv.setTextColor(colorStateList);
        }
    },BACKGROUND("background") {
        @Override
        public void skin(View mView, String mResName) {
            SkinResource resource = getSkinResource();
            Drawable drawable = resource.getDrawableByName(mResName);
            if(drawable != null){
                mView.setBackgroundDrawable(drawable);
                return;
            }
            ColorStateList color = resource.getColorByName(mResName);
            if(color!=null){
                mView.setBackgroundColor(color.getDefaultColor());
            }
        }
    },SRC("src") {
        @Override
        public void skin(View mView, String mResName) {
            SkinResource resource = getSkinResource();
            Drawable drawable = resource.getDrawableByName(mResName);
            if(drawable != null) {
                ImageView iv = (ImageView) mView;
                 iv.setImageDrawable(drawable);
            }
        }
    };

    private String mResName;
    SkinType(String name){
        this.mResName=name;
    }

    public abstract void skin(View mView, String mResName);

    public String getResName() {
        return mResName;
    }

    public SkinResource getSkinResource(){
        return SkinManager.getInstance().getSkinResource();
    }
}
