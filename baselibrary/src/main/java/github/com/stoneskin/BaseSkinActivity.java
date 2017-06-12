package github.com.stoneskin;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;



import java.util.ArrayList;
import java.util.List;

import github.com.stoneskin.skin.SkinManager;
import github.com.stoneskin.skin.SkinResource;
import github.com.stoneskin.skin.attr.SkinAttr;
import github.com.stoneskin.skin.attr.SkinView;
import github.com.stoneskin.skin.callback.IChangeSkinListener;
import github.com.stoneskin.skin.support.SkinAppCompatViewInflater;
import github.com.stoneskin.skin.support.SkinAttrSupport;


/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/5/10
 * Version  1.0
 * Description:
 */

public class BaseSkinActivity extends AppCompatActivity implements LayoutInflaterFactory,IChangeSkinListener {
    private static String TAG = "BaseSkinActivity";

    private SkinAppCompatViewInflater mAppCompatViewInflater=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //在onCreate前设置Factory
        /**
         * AppCompatDelegateImplV9--->installViewFactory()
         *
         * LayoutInflater layoutInflater = LayoutInflater.from(mContext);
         * if (layoutInflater.getFactory() == null) {
         * LayoutInflaterCompat.setFactory(layoutInflater, this);
         * }
         *
         */
        //换肤
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(layoutInflater,this);

        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        Log.e(TAG,"======>Name====="+name);
        //1.创建view
        View view =createView(parent, name, context, attrs);
        Log.e(TAG,"======>onCreateView====》"+view);
        //2.解析属性
        if(view!=null) {
            Log.e(TAG,"======>onCreateView====》111111111");
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);
            SkinView skinView=new SkinView(view,skinAttrs);

            //3.交给skinmangager统一管理
            managerSkinView(skinView);

            //4.判断是否要换皮肤
            SkinManager.getInstance().checkChangeSkin(skinView);
        }
        return view;
    }

    /**
     * 管理skinview
     * @param skinView
     */
    private void managerSkinView(SkinView skinView) {

        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if(skinViews==null){
            skinViews=new ArrayList<>();
            SkinManager.getInstance().register(this,skinViews);
        }
        skinViews.add(skinView);
    }


    /**
     * 创建view  来自系统原码
     * @param parent
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        // We only want the View to inherit its context if we're running pre-v21
        final boolean inheritContext = isPre21 && true && shouldInheritContext((ViewParent) parent);

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    @Override
    protected void onDestroy() {
        SkinManager.getInstance().unregister(this);
        super.onDestroy();

    }

    @Override
    public void chanageSkin(SkinResource resource) {

    }



}
