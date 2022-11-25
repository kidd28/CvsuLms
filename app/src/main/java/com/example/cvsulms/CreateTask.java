package com.example.cvsulms;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class CreateTask extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    Button createTask,uploadFile;
    EditText title, description;
    String subj,teacherUid;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String TaskId;

    Boolean upfile = false;


    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int PICK_FILE_REQUEST = 100;

    static DriveServiceHelper mDriveServiceHelper;
    static String folderId="";
    String secCode, subjCode;

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        createTask = findViewById(R.id.CreateTask);
        uploadFile = findViewById(R.id.uploadFile);
        title = findViewById(R.id.Title);
        description = findViewById(R.id.TaskDes);
        secCode =getIntent().getExtras().getString("secCode");
        subjCode = getIntent().getExtras().getString("subjCode");
        subj = getIntent().getExtras().getString("subj");
        teacherUid = getIntent().getExtras().getString("teacherUid");
        TaskId = getIntent().getExtras().getString("TaskId");

        firebaseAuth = FirebaseAuth.getInstance();
        user =  firebaseAuth.getCurrentUser();

         requestSignIn();

         createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = title.getText().toString();
                String Description =description.getText().toString();
                uploadTask(Title, Description,secCode,subjCode,subj,teacherUid);
            }
        });

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upfile = true;
                createFolder();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    requestPermission();
                }else{
                    requestPermissionBelowR();
                }
            }
        });
    }

    private void requestPermissionBelowR() {
        String[] perms ={Manifest.permission.READ_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(this, perms)) {
                    uploadFile();
                } else {
                    EasyPermissions.requestPermissions(this, "We need permissions because this and that",
                            123, perms);
                    showSettingsDialog();
                }
            }
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestPermission() {
        String[] perms ={Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                if (EasyPermissions.hasPermissions(this, perms)) {
                    uploadFile();
                } else {
                    EasyPermissions.requestPermissions(this, "We need permissions because this and that",
                            123, perms);
                    showSettingsDialog();
                }
            } else {
                uploadFile();
            }
        }else {
            if (EasyPermissions.hasPermissions(this, perms)) {
                uploadFile();
            } else {
                EasyPermissions.requestPermissions(this, "We need permissions because this and that",
                        123, perms);
                showSettingsDialog();
            }
        }
    }

    private void uploadTask(String title, String description, String secCode, String subjCode, String subj, String teacherUid) {
        if(!upfile) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Title",""+title);
            hashMap.put("Description",description);
            hashMap.put("SecCode", secCode);
            hashMap.put("Subject", subj);
            hashMap.put("SubjCode", subjCode);
            hashMap.put("TeacherUid", user.getUid());
            hashMap.put("FileId","null");
            hashMap.put("Filelink", "null");
            reference.child(TaskId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CreateTask.this, "Task uploaded successfully", Toast.LENGTH_SHORT).show();
                    HashMap<String, Object> hashMap1 = new HashMap<>();
                    hashMap1.put("Title",""+title);
                    hashMap1.put("Description",description);
                    hashMap1.put("SecCode", secCode);
                    hashMap1.put("Subject", subj);
                    hashMap1.put("FileId","null");
                    hashMap1.put("Filelink", "null");
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Sections");
                    reference1.child(secCode+subjCode).child("Task").child(TaskId).updateChildren(hashMap1);
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Subjects");
                    reference2.child(secCode+subjCode).child("Task").child(TaskId).updateChildren(hashMap1);
                    startActivity(new Intent(CreateTask.this,TeacherSectionUi.class));
                    CreateTask.this.finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateTask.this, "Task failed to upload, please try again", Toast.LENGTH_SHORT).show();
                }
            });

        }else {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Title",""+title);
        hashMap.put("Description",description);
        hashMap.put("SecCode", secCode);
        hashMap.put("Subject", subj);
        hashMap.put("SubjCode", subjCode);
        hashMap.put("TeacherUid", user.getUid());
        reference.child(TaskId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateTask.this, "Task uploaded successfully", Toast.LENGTH_SHORT).show();
                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("Title",""+title);
                hashMap1.put("Description",description);
                hashMap1.put("SecCode", secCode);
                hashMap1.put("Subject", subj);
                hashMap1.put("SubjCode", subjCode);
                hashMap1.put("TeacherUid", user.getUid());
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Sections");
                reference1.child(secCode+subjCode).child("Task").child(TaskId).updateChildren(hashMap1);
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Subjects");
                reference2.child(secCode+subjCode).child("Task").child(TaskId).updateChildren(hashMap1);
                startActivity(new Intent(CreateTask.this,TeacherSectionUi.class));
                CreateTask.this.finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateTask.this, "Task failed to upload, please try again", Toast.LENGTH_SHORT).show();
            }
        });}
    }
    @Override
    public void onBackPressed() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Tasks").child(TaskId);
        ref.removeValue();

        startActivity( new Intent(CreateTask.this, TeacherDashboard.class));
        CreateTask.this.finish();
        finishAffinity();
        finishAndRemoveTask();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateTask.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    openSettings();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
    // navigating user to app settings
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void openSettings() {
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,uri);
        intent.setData(uri);
        startActivity(intent);
        startActivityForResult(intent, 101);
    }

    /**
     * Starts a sign-in activity using {@link #REQUEST_CODE_SIGN_IN}.
     */
    private void requestSignIn() {
        Log.d(TAG, "Requesting sign-in");

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                        .requestEmail()
                        .build();
        googleSignInClient = GoogleSignIn.getClient(this, signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(googleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == Activity.RESULT_OK && resultData != null) {
                    handleSignInResult(resultData);
                }
                break;

            case PICK_FILE_REQUEST:
                if(resultData == null){
                    //no data present
                    return;
                }
                // Get the Uri of the selected file
                Uri selectedFileUri = resultData.getData();
                Log.e(TAG, "selected File Uri: "+selectedFileUri );
                // Get the path
                String selectedFilePath =FileUtils.getPath(this,selectedFileUri);
                Log.e(TAG,"Selected File Path:" + selectedFilePath);
                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    if (mDriveServiceHelper != null) {
                        mDriveServiceHelper.uploadFileToGoogleDrive(selectedFilePath)
                                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean result) {
                                        Toast.makeText(getApplicationContext(), "File uploaded ...!!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Couldn't able to upload file, error: "+e, Toast.LENGTH_SHORT).show();
                                        System.out.println(e);
                                    }
                                });
                    }
                }
                else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }
    /**
     * Handles the {@code result} of a completed sign-in activity initiated from {@link
     * #requestSignIn()}.
     */
    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Log.d(TAG, "Signed in as " + googleAccount.getEmail());
                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("Drive API Migration")
                                    .build();
                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService,secCode,TaskId);
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Unable to sign in."+exception, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // This method will get call when user click on sign-in button
    public void signIn(View view) {
        requestSignIn();
    }
    // This method will get call when user click on upload file button
    public void uploadFile() {

        Intent intent;
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", "*/*");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            Log.e(TAG, "uploadFile: if" );
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT); // or ACTION_OPEN_DOCUMENT
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            Log.e(TAG, "uploadFile: else" );
        }
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);

    }
    // This method will get call when user click on create folder button
    public void createFolder() {
        if (mDriveServiceHelper != null) {

            // check folder present or not
            mDriveServiceHelper.isFolderPresent()
                    .addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String id) {
                            if (id.isEmpty()){
                                mDriveServiceHelper.createFolder()
                                        .addOnSuccessListener(new OnSuccessListener<String>() {
                                            @Override
                                            public void onSuccess(String fileId) {
                                                Log.e(TAG, "folder id: "+fileId );
                                                folderId=fileId;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {

                                                Log.e(TAG, "Couldn't create file.", exception);
                                                System.out.println(exception);
                                            }
                                        });
                            }else {
                                folderId=id;

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e(TAG, "Couldn't create file..", exception);
                        }
                    });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
        requestSignIn();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        showSettingsDialog();
    }


}