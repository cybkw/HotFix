package com.bkw.hotfix;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.scex.library.Constants;
import com.scex.library.FileUtils;
import com.scex.library.FixDexUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;

import io.reactivex.functions.Consumer;

public class TwoActivity extends BaseActivity {
    public static final String TAG = "TwoActivity";

    TextView textView;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_two);

        textView = findViewById(R.id.text);
        RxPermissions rxPermissions = new RxPermissions(this);


        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Culcate culcate = new Culcate();
                textView.setText(String.valueOf(culcate.cul()));
                Log.d(TAG, "VALUE====" + culcate.cul());
            }
        });

        if (!rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            Log.d(TAG, String.valueOf(aBoolean));
                        }
                    });
        }
        findViewById(R.id.btn_fix).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fix();
            }
        });
    }

    private void fix() {
        //通过服务器下载的dex文件，
        File sourceFile = new File(Environment.getExternalStorageDirectory(), Constants.DEX_FIX_NAME);
        Log.d(TAG, sourceFile.getName());
        //目标路径，私有目录的临时文件夹odex
        File targetFile = new File(getDir(Constants.DEX_DIR, Context.MODE_PRIVATE).getAbsolutePath() + File.separator + Constants.DEX_FIX_NAME);
        //如果已存在文件，如之前下载的dex
        if (targetFile.exists()) {
            targetFile.delete();
            Log.d(TAG, "删除已存在的dex文件");
        }
        Log.d(TAG, targetFile.getAbsolutePath());
        try {
            //复制下载的dex文件到app私有目录下
            FileUtils.copyFile(sourceFile, targetFile);
            Log.d(TAG, "赋值dex文件完成");

            //复制完成后，执行dex修复文件
            FixDexUtils.loadFileDex(this);
            showToas("修复完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showToas(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
