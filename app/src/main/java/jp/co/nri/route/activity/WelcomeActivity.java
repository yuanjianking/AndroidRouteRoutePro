package jp.co.nri.route.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.util.List;

import butterknife.OnClick;
import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseActivity;
import jp.co.nri.route.util.AppUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class WelcomeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final int RC_CAMERA_AND_LOCATION = 0x1001;
    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected int contentView() {
        return R.layout.activity_welcome;
    }

    /**
     * View初期化
     */
    @Override
    protected void initView() {
        setStatusBarColor(R.color.white, R.color.colorStatus, R.color.white);
    }

    @Override
    protected void initData() {
        methodRequiresTwoPermission();
    }

    /**
     * ジャンプページ
     *
     * @param view view
     */
    @OnClick({R.id.btnLogin, R.id.btnSignUp})
    void onClick(View view) {
        if (!EasyPermissions.hasPermissions(this, perms)){
            methodRequiresTwoPermission();
            return;
        }
        Intent intent;
        switch (view.getId()) {
            case R.id.btnLogin:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSignUp:
                intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "位置決め権限を授権してください",
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> list) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> list) {
        checkPermission();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            checkPermission();
        }
    }


    /**
     * ユーザーは、位置決め権限を許可することを拒否します，システム設定ページにジャンプ。
     */
    private void checkPermission(){
        if (!EasyPermissions.hasPermissions(this, perms)) {
            AppUtil.showAlertDialog(this, "手伝いをする", "現在のアプリケーションに必要な権限がない。\n\nクリックしてください\"Ok\"-\"権限\"-必要な権限を開く。",
                "Cancel", "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE);
                    }
                });
        }
    }
}
