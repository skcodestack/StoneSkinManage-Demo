package github.com.stoneskinmanage_demo;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import github.com.permissionlib.StonePermission;
import github.com.permissionlib.annotation.PermissionFail;
import github.com.permissionlib.annotation.PermissionSuccess;
import github.com.stoneskin.BaseSkinActivity;
import github.com.stoneskin.skin.SkinManager;

public class MainActivity extends BaseSkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn_skin_change(View view){
        StonePermission.with(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addRequestCode(100)
                .request();

    }
    public void btn_skin_default(View view){
        SkinManager.getInstance().restoreDefault();
    }

    public void btn_jump(View view){
        startActivity(new Intent(this,secondActivity.class));
    }


    @PermissionSuccess(requestCode=100)
    public void success(){
        loadSkin();
    }
    @PermissionFail(requestCode = 100)
    public void fail(){

    }

    private void loadSkin(){
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        StonePermission.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }
}
