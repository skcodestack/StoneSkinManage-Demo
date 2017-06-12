package github.com.stoneskin.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import github.com.stoneskin.skin.attr.SkinAttr;
import github.com.stoneskin.skin.attr.SkinType;


/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/5/16
 * Version  1.0
 * Description: 皮肤属性解析的支持类
 */

public class SkinAttrSupport {

    private static String TAG = "SkinAttrSupport";
    /**
     * 获取所有属性
     * @param context
     * @param attrs
     * @return
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        List<SkinAttr> list=new LinkedList<>();
        int count = attrs.getAttributeCount();
        Log.e(TAG,"attrs===>"+attrs);
        for (int i = 0; i < count; i++) {
            String name = attrs.getAttributeName(i);
            String value = attrs.getAttributeValue(i);
            Log.e(TAG,name+"===="+value);
            SkinType skinType=getSkinType(name);
            if(skinType==null){
                continue;
            }
            String resName=getResName(context,value);
            if(TextUtils.isEmpty(resName)){
                continue;
            }
            SkinAttr skinAttr=new SkinAttr(resName,skinType);
            list.add(skinAttr);
            Log.e(TAG,name+"===="+value+"====="+resName);

        }

        return list;
    }

    /**
     * 获取资源名称
     * @param context
     * @param value
     * @return
     */
    private static String getResName(Context context, String value) {
        if(value.startsWith("@")){
            value = value.substring(1);
            int resId = Integer.parseInt(value);
            return context.getResources().getResourceName(resId);
        }

        return null;
    }

    /**
     * 通过名称获取skintype
     * @param name
     * @return
     */
    private static SkinType getSkinType(String name) {
        SkinType[] values = SkinType.values();
        for (SkinType type : values) {
            if(type.getResName().equals(name)){
                return type;
            }
        }
        return null;
    }
}
