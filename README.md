# StoneSkinManage-Demo
换肤框架
#android插件化开发---换肤#

在自己手写换肤功能之前需要了解关于view的创建过程，如果不了解的朋友可以看下我另外一篇博客：[android中布局和View创建](http://blog.csdn.net/a847427920/article/details/72526630)

从上一篇文章中我们知道在创建view之前，会先调用LayoutInflater中的mFactory2，mFactory，mPrivateFactory的onCreateView，所以我们只要设置Factory，就可以对view创建进行拦截，
先看下效果图：

换肤前

![](/pf0.jpg)

换肤后

![](/pf1.jpg)


下面让我们来一起来看下代码：

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


下面来看下LayoutInflaterFactory实现：

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



大家可以看到，这边其实就是对创建view做了个拦截，用我们直接写的createView来创建view，因为我们这个换肤要保证新版本控件的特性，所以mAppCompatViewInflater.createView（）来创建view,这是对新版本的兼容类：

		public class SkinAppCompatViewInflater {
	
	        private static final Class<?>[] sConstructorSignature = new Class[]{
	                Context.class, AttributeSet.class};
	        private static final int[] sOnClickAttrs = new int[]{android.R.attr.onClick};
	
	        private static final String[] sClassPrefixList = {
	                "android.widget.",
	                "android.view.",
	                "android.webkit."
	        };
	
	        private static final String LOG_TAG = "AppCompatViewInflater";
	
	        private static final Map<String, Constructor<? extends View>> sConstructorMap
	                = new ArrayMap<>();
	
	        private final Object[] mConstructorArgs = new Object[2];
	
	        public final View createView(View parent, final String name, @NonNull Context context,
	                                     @NonNull AttributeSet attrs, boolean inheritContext,
	                                     boolean readAndroidTheme, boolean readAppTheme, boolean wrapContext) {
	            final Context originalContext = context;
	            Log.e("CompatViewInflater","=====>"+inheritContext+"-"+parent+"-"+readAndroidTheme+"-"+readAppTheme);
	            // We can emulate Lollipop's android:theme attribute propagating down the view hierarchy
	            // by using the parent's context
	            if (inheritContext && parent != null) {
	                context = parent.getContext();
	            }
	            if (readAndroidTheme || readAppTheme) {
	                // We then apply the theme on the context, if specified
	                context = themifyContext(context, attrs, readAndroidTheme, readAppTheme);
	            }
	            if(context == originalContext){
	                Log.e("CompatViewInflater","=====>tttttttttttttttttttttttttttttttttttttttttt");
	
	            }
	
	            if (wrapContext) {
	                context = TintContextWrapper.wrap(context);
	            }
	
	            View view = null;
	
	            // We need to 'inject' our tint aware Views in place of the standard framework versions
	            switch (name) {
	                case "TextView":
	                    view = new AppCompatTextView(context, attrs);
	                    break;
	                case "ImageView":
	                    view = new AppCompatImageView(context, attrs);
	                    break;
	                case "Button":
	                    view = new AppCompatButton(context, attrs);
	                    break;
	                case "EditText":
	                    view = new AppCompatEditText(context, attrs);
	                    break;
	                case "Spinner":
	                    view = new AppCompatSpinner(context, attrs);
	                    break;
	                case "ImageButton":
	                    view = new AppCompatImageButton(context, attrs);
	                    break;
	                case "CheckBox":
	                    view = new AppCompatCheckBox(context, attrs);
	                    break;
	                case "RadioButton":
	                    view = new AppCompatRadioButton(context, attrs);
	                    break;
	                case "CheckedTextView":
	                    view = new AppCompatCheckedTextView(context, attrs);
	                    break;
	                case "AutoCompleteTextView":
	                    view = new AppCompatAutoCompleteTextView(context, attrs);
	                    break;
	                case "MultiAutoCompleteTextView":
	                    view = new AppCompatMultiAutoCompleteTextView(context, attrs);
	                    break;
	                case "RatingBar":
	                    view = new AppCompatRatingBar(context, attrs);
	                    break;
	                case "SeekBar":
	                    view = new AppCompatSeekBar(context, attrs);
	                    break;
	            }
	
	            Log.e("SkinAppCompat","======>111111111111111111");
	//            if (view == null && originalContext != context) {
	            if (view == null ) {
	                // If the original context does not equal our themed context, then we need to manually
	                // inflate it using the name so that android:theme takes effect.
	                Log.e("SkinAppCompat","======>22222222222222222222222");
	                view = createViewFromTag(context, name, attrs);
	            }
	
	            if (view != null) {
	                // If we have created a view, check it's android:onClick
	                checkOnClickListener(view, attrs);
	            }
	
	            return view;
	        }
	
	        private View createViewFromTag(Context context, String name, AttributeSet attrs) {
	            if (name.equals("view")) {
	                name = attrs.getAttributeValue(null, "class");
	            }
	
	            try {
	                mConstructorArgs[0] = context;
	                mConstructorArgs[1] = attrs;
	
	                if (-1 == name.indexOf('.')) {
	                    for (int i = 0; i < sClassPrefixList.length; i++) {
	                        final View view = createView(context, name, sClassPrefixList[i]);
	                        if (view != null) {
	                            return view;
	                        }
	                    }
	                    return null;
	                } else {
	                    return createView(context, name, null);
	                }
	            } catch (Exception e) {
	                // We do not want to catch these, lets return null and let the actual LayoutInflater
	                // try
	                return null;
	            } finally {
	                // Don't retain references on context.
	                mConstructorArgs[0] = null;
	                mConstructorArgs[1] = null;
	            }
	        }
	
	        /**
	         * android:onClick doesn't handle views with a ContextWrapper context. This method
	         * backports new framework functionality to traverse the Context wrappers to find a
	         * suitable target.
	         */
	        private void checkOnClickListener(View view, AttributeSet attrs) {
	            final Context context = view.getContext();
	
	            if (!(context instanceof ContextWrapper) ||
	                    (Build.VERSION.SDK_INT >= 15 && !ViewCompat.hasOnClickListeners(view))) {
	                // Skip our compat functionality if: the Context isn't a ContextWrapper, or
	                // the view doesn't have an OnClickListener (we can only rely on this on API 15+ so
	                // always use our compat code on older devices)
	                return;
	            }
	
	            final TypedArray a = context.obtainStyledAttributes(attrs, sOnClickAttrs);
	            final String handlerName = a.getString(0);
	            if (handlerName != null) {
	                view.setOnClickListener(new DeclaredOnClickListener(view, handlerName));
	            }
	            a.recycle();
	        }
	
	        private View createView(Context context, String name, String prefix)
	                throws ClassNotFoundException, InflateException {
	            Constructor<? extends View> constructor = sConstructorMap.get(name);
	
	            try {
	                if (constructor == null) {
	                    // Class not found in the cache, see if it's real, and try to add it
	                    Class<? extends View> clazz = context.getClassLoader().loadClass(
	                            prefix != null ? (prefix + name) : name).asSubclass(View.class);
	
	                    constructor = clazz.getConstructor(sConstructorSignature);
	                    sConstructorMap.put(name, constructor);
	                }
	                constructor.setAccessible(true);
	                return constructor.newInstance(mConstructorArgs);
	            } catch (Exception e) {
	                // We do not want to catch these, lets return null and let the actual LayoutInflater
	                // try
	                return null;
	            }
	        }
	
	        /**
	         * Allows us to emulate the {@code android:theme} attribute for devices before L.
	         */
	        private static Context themifyContext(Context context, AttributeSet attrs,
	                                              boolean useAndroidTheme, boolean useAppTheme) {
	            final TypedArray a = context.obtainStyledAttributes(attrs, android.support.v7.appcompat.R.styleable.View, 0, 0);
	            int themeId = 0;
	            if (useAndroidTheme) {
	                // First try reading android:theme if enabled
	                themeId = a.getResourceId(android.support.v7.appcompat.R.styleable.View_android_theme, 0);
	            }
	            if (useAppTheme && themeId == 0) {
	                // ...if that didn't work, try reading app:theme (for legacy reasons) if enabled
	                themeId = a.getResourceId(android.support.v7.appcompat.R.styleable.View_theme, 0);
	
	                if (themeId != 0) {
	                    Log.i(LOG_TAG, "app:theme is now deprecated. "
	                            + "Please move to using android:theme instead.");
	                }
	            }
	            a.recycle();
	
	            if (themeId != 0 && (!(context instanceof ContextThemeWrapper)
	                    || ((ContextThemeWrapper) context).getThemeResId() != themeId)) {
	                // If the context isn't a ContextThemeWrapper, or it is but does not have
	                // the same theme as we need, wrap it in a new wrapper
	                context = new ContextThemeWrapper(context, themeId);
	            }
	            return context;
	        }
	
	        /**
	         * An implementation of OnClickListener that attempts to lazily load a
	         * named click handling method from a parent or ancestor context.
	         */
	        private static class DeclaredOnClickListener implements View.OnClickListener {
	            private final View mHostView;
	            private final String mMethodName;
	
	            private Method mResolvedMethod;
	            private Context mResolvedContext;
	
	            public DeclaredOnClickListener(@NonNull View hostView, @NonNull String methodName) {
	                mHostView = hostView;
	                mMethodName = methodName;
	            }
	
	            @Override
	            public void onClick(@NonNull View v) {
	                if (mResolvedMethod == null) {
	                    resolveMethod(mHostView.getContext(), mMethodName);
	                }
	
	                try {
	                    mResolvedMethod.invoke(mResolvedContext, v);
	                } catch (IllegalAccessException e) {
	                    throw new IllegalStateException(
	                            "Could not execute non-public method for android:onClick", e);
	                } catch (InvocationTargetException e) {
	                    throw new IllegalStateException(
	                            "Could not execute method for android:onClick", e);
	                }
	            }
	
	            @NonNull
	            private void resolveMethod(@Nullable Context context, @NonNull String name) {
	                while (context != null) {
	                    try {
	                        if (!context.isRestricted()) {
	                            final Method method = context.getClass().getMethod(mMethodName, View.class);
	                            if (method != null) {
	                                mResolvedMethod = method;
	                                mResolvedContext = context;
	                                return;
	                            }
	                        }
	                    } catch (NoSuchMethodException e) {
	                        // Failed to find method, keep searching up the hierarchy.
	                    }
	
	                    if (context instanceof ContextWrapper) {
	                        context = ((ContextWrapper) context).getBaseContext();
	                    } else {
	                        // Can't search up the hierarchy, null out and fail.
	                        context = null;
	                    }
	                }
	
	                final int id = mHostView.getId();
	                final String idText = id == View.NO_ID ? "" : " with id '"
	                        + mHostView.getContext().getResources().getResourceEntryName(id) + "'";
	                throw new IllegalStateException("Could not find method " + mMethodName
	                        + "(View) in a parent or ancestor Context for android:onClick "
	                        + "attribute defined on view " + mHostView.getClass() + idText);
	            }
	        }
	
		}



看过上一篇博客的小伙伴应该对这个类有点也不陌生，这个就是创建view的兼容类。


基本的代码已经看完，现在我们就来写换肤的基础类：

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



下面就是一些简单的保存view以及属性的类，下面来重点看下SkinManager和SkinResource类

SkinManager

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

	


SkinResource

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


###基本使用：

		public class BaseApplication extends Application {

		    @Override
		    public void onCreate() {
		        super.onCreate();
		        SkinManager.getInstance().init(this);
		    }
		}


一句话就行了，但是要注意，资源必须使用@drawable  @color等


如果有自定义属性，第三方控件，可以实现chanageSkin：

	@Override
    public void chanageSkin(SkinResource resource) {
        super.chanageSkin(resource);

    }

更据自己的需求来改写。

知道原理其实也没那么神奇，大家也可以上网看一些第三的库，但是核心原理其实一模一样，也可以自己在这个基础上进行扩展，添加别的一键切换的功能


