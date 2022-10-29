package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateSection extends AppCompatActivity {

EditText Year,Section, SectionCode, Subject, SubjCode;
Button create;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_section);

        Year = findViewById(R.id.Year);
        Section = findViewById(R.id.Section);
        SectionCode = findViewById(R.id.SectionCode);
        Subject = findViewById(R.id.Subject);
        SubjCode = findViewById(R.id.SubCode);
        create = findViewById(R.id.Create);

        firebaseAuth = FirebaseAuth.getInstance();
        user =  firebaseAuth.getCurrentUser();


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = Year.getText().toString();
                String section =Section.getText().toString();
                String sectioncode = SectionCode.getText().toString();
                String subject = Subject.getText().toString();
                String subjcode = SubjCode.getText().toString();
                createSection(year,section, sectioncode, subject, subjcode);

            }
        });
    }

    private void createSection(String yearlvl, String sec, String sectionCode, String subJect ,String subjCode) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sections");

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Yearlvl",""+yearlvl);
        hashMap.put("Section",sec);
        hashMap.put("SecCode", sectionCode);
        hashMap.put("Subject", subJect);
        hashMap.put("SubjCode", subjCode);
        hashMap.put("TeacherUid", user.getUid());

        reference.child(sectionCode+subjCode).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseUser user =firebaseAuth.getCurrentUser();
                        Toast.makeText(CreateSection.this, "Section created successfully", Toast.LENGTH_SHORT).show();
                        HashMap<String, String> hashMap1 = new HashMap<>();
                        hashMap1.put("uid", firebaseAuth.getUid());
                        hashMap1.put("email",user.getEmail());
                        hashMap1.put("role","Teacher");
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Sections");
                        reference1.child(sectionCode+subjCode).child("Members").child(firebaseAuth.getUid()).setValue(hashMap1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseUser user =firebaseAuth.getCurrentUser();
                                        Toast.makeText(CreateSection.this, "Section created successfully", Toast.LENGTH_SHORT).show();
                                        HashMap<String, String> hashMap2 = new HashMap<>();
                                        hashMap2.put("Yearlvl",""+yearlvl);
                                        hashMap2.put("Section",sec);
                                        hashMap2.put("SecCode", sectionCode);
                                        hashMap2.put("Subject", subJect);
                                        hashMap2.put("SubjCode", subjCode);
                                        hashMap2.put("TeacherUid", user.getUid());
                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Subjects");
                                        reference2.child(sectionCode+subjCode).setValue(hashMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(CreateSection.this, "Group Created..", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(CreateSection.this,TeacherDashboard.class));
                                                CreateSection.this.finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateSection.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateSection.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onBackPressed() {
        startActivity( new Intent(CreateSection.this, TeacherDashboard.class));
        CreateSection.this.finish();
        finishAffinity();
        finishAndRemoveTask();
    }
}