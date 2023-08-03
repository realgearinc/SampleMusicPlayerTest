package com.realgear.samplemusicplayertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import com.realgear.samplemusicplayertest.threads.UIThread;
import com.realgear.samplemusicplayertest.utils.PermissionManager;

public class MainActivity extends AppCompatActivity {

    private UIThread m_vThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionManager.requestPermission(this, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE, 100);
        PermissionManager.requestPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE, 100);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }

        this.m_vThread = new UIThread(this);

    }
}