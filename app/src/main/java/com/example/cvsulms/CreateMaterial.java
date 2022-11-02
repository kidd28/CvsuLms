package com.example.cvsulms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateMaterial extends AppCompatActivity {
    Button createMaterial;
    EditText title, description;
    String secCode, subjCode,subj,teacherUid;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_material);

        createMaterial = findViewById(R.id.CreateMat);
        title = findViewById(R.id.Title);
        description = findViewById(R.id.MatDes);

        firebaseAuth = FirebaseAuth.getInstance();
        user =  firebaseAuth.getCurrentUser();

        createMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = title.getText().toString();
                String Description =description.getText().toString();

                secCode =getIntent().getExtras().getString("secCode");
                subjCode = getIntent().getExtras().getString("subjCode");
                subj = getIntent().getExtras().getString("subj");
                teacherUid = getIntent().getExtras().getString("teacherUid");

                uploadMaterial(Title, Description,secCode,subjCode,subj,teacherUid);
            }
        });
    }

    private void uploadMaterial(String title, String description, String secCode, String subjCode, String subj, String teacherUid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Materials");
        String MaterialId =""+ System.currentTimeMillis();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Title",""+title);
        hashMap.put("Description",description);
        hashMap.put("SecCode", secCode);
        hashMap.put("Subject", subj);
        hashMap.put("SubjCode", subjCode);
        hashMap.put("TeacherUid", user.getUid());
        hashMap.put("MaterialId", MaterialId);
        reference.child(MaterialId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateMaterial.this, "Task uploaded successfully", Toast.LENGTH_SHORT).show();
                HashMap<String, String> hashMap1 = new HashMap<>();
                hashMap1.put("Title",""+title);
                hashMap1.put("Description",description);
                hashMap1.put("SecCode", secCode);
                hashMap1.put("Subject", subj);
                hashMap1.put("SubjCode", subjCode);
                hashMap1.put("TeacherUid", user.getUid());
                hashMap1.put("TaskId", MaterialId);

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Sections");
                reference1.child(secCode+subjCode).child("Task").child(MaterialId).setValue(hashMap1);

                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Subjects");
                reference2.child(secCode+subjCode).child("Task").child(MaterialId).setValue(hashMap1);

                startActivity(new Intent(CreateMaterial.this,TeacherSectionUi.class));
                CreateMaterial.this.finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateMaterial.this, "Task failed to upload, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity( new Intent(CreateMaterial.this, TeacherDashboard.class));
        CreateMaterial.this.finish();
        finishAffinity();
        finishAndRemoveTask();
    }
}