package github.com.stoneskinmanage_demo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import github.com.stoneskin.BaseSkinActivity;
import github.com.stoneskin.skin.SkinManager;

public class secondActivity extends BaseSkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void btn_skin_change(View view){
        File skinDir = new File(Environment.getExternalStorageDirectory(),"skin");

        if(!skinDir.exists()){
            skinDir.mkdirs();
        }
        File file = new File(skinDir.getAbsolutePath(),"SkinOne.skin");
        if(!file.exists()){
            FileUtil.copyAssetsTo(this,"SkinOne.skin",file.getAbsolutePath());
        }
        SkinManager.getInstance().loadSkin(file.getAbsolutePath());
    }
}
