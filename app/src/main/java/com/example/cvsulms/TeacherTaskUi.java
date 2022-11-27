package com.example.cvsulms;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

public class TeacherTaskUi extends AppCompatActivity{
    TextView coursecode,subjectname,title,description,filename;
    String Title, Description,SecCode, SubjCode,Subject,TeacherUid,FileId,Filelink,TaskId,FileName;
    Toolbar toolbar;
    Button download;

    private static final int WRITE_EXTERNAL_STORAGE= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_task_ui);

        coursecode = findViewById(R.id.CourseCode);
        subjectname = findViewById(R.id.SubjectName);
        title = findViewById(R.id.Title);
        description = findViewById(R.id.Description);
        filename = findViewById(R.id.FileName);
        toolbar = findViewById(R.id.toolbar);
        download = findViewById(R.id.Download);

        Title =getIntent().getExtras().getString("Title");
        Description =getIntent().getExtras().getString("Description");
        SecCode =getIntent().getExtras().getString("SecCode");
        SubjCode =getIntent().getExtras().getString("SubjCode");
        Subject =getIntent().getExtras().getString("Subject");
        TeacherUid =getIntent().getExtras().getString("TeacherUid");
        FileId =getIntent().getExtras().getString("FileId");
        Filelink =getIntent().getExtras().getString("Filelink");
        TaskId =getIntent().getExtras().getString("TaskId");
        FileName =getIntent().getExtras().getString("FileName");


        title.setText(Title);
        subjectname.setText(Subject);
        coursecode.setText(SubjCode);
        description.setText(Description);
        toolbar.setTitle(SecCode);

        if(Filelink.equals("null")){
            filename.setVisibility(View.GONE);
            download.setVisibility(View.GONE);
        }else{
            filename.setText(FileName);
        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        String [] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permission,WRITE_EXTERNAL_STORAGE);
                    }else {
                        savefile();
                    }
                }else {
                    savefile();
                }
            }
        });

    }
    private void savefile() {
        if (!Filelink.equals("")){
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://drive.google.com/uc?export=download&id="+FileId));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setTitle(FileName);
            request.setDescription("Downloading File..");

            File file = new File(Environment.getExternalStorageDirectory()+"/Download",FileName);

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationUri(Uri.fromFile(file));
            DownloadManager manager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);

            Toast.makeText(this, "Downloading File..",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                } else {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT);
                }
            }

        }
    }
}