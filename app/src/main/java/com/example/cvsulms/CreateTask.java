package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateTask extends AppCompatActivity {
    Button createTask;
    EditText title, description;
    String secCode, subjCode,subj,teacherUid;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        createTask = findViewById(R.id.CreateTask);
        title = findViewById(R.id.Title);
        description = findViewById(R.id.TaskDes);

        firebaseAuth = FirebaseAuth.getInstance();
        user =  firebaseAuth.getCurrentUser();

        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = title.getText().toString();
                String Description =description.getText().toString();

                secCode =getIntent().getExtras().getString("secCode");
                subjCode = getIntent().getExtras().getString("subjCode");
                subj = getIntent().getExtras().getString("subj");
                teacherUid = getIntent().getExtras().getString("teacherUid");
                
                uploadTask(Title, Description,secCode,subjCode,subj,teacherUid);
            }
        });
    }
    private void uploadTask(String title, String description, String secCode, String subjCode, String subj, String teacherUid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
        String TaskId =""+ System.currentTimeMillis();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Title",""+title);
        hashMap.put("Description",description);
        hashMap.put("SecCode", secCode);
        hashMap.put("Subject", subj);
        hashMap.put("SubjCode", subjCode);
        hashMap.put("TeacherUid", user.getUid());
        hashMap.put("TaskId", TaskId);
        reference.child(TaskId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateTask.this, "Task uploaded successfully", Toast.LENGTH_SHORT).show();

                HashMap<String, String> hashMap1 = new HashMap<>();
                hashMap1.put("Title",""+title);
                hashMap1.put("Description",description);
                hashMap1.put("SecCode", secCode);
                hashMap1.put("Subject", subj);
                hashMap1.put("SubjCode", subjCode);
                hashMap1.put("TeacherUid", user.getUid());
                hashMap1.put("TaskId", TaskId);

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Sections");
                reference1.child(secCode+subjCode).child("Task").child(TaskId).setValue(hashMap1);

                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Subjects");
                reference2.child(secCode+subjCode).child("Task").child(TaskId).setValue(hashMap1);

                startActivity(new Intent(CreateTask.this,TeacherSectionUi.class));
                CreateTask.this.finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateTask.this, "Task failed to upload, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity( new Intent(CreateTask.this, TeacherDashboard.class));
        CreateTask.this.finish();
        finishAffinity();
        finishAndRemoveTask();
    }
}