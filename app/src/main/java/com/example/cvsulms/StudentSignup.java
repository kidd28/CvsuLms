package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class StudentSignup extends AppCompatActivity {

    EditText StuEmail, StuName, StuCourseSec, StuNumber,StuPass;
    Button signup;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference reference;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);


        FirebaseApp.initializeApp(this);

        StuEmail =  findViewById(R.id.StudentEmail);
        StuName =  findViewById(R.id.StudentName);
        StuCourseSec =  findViewById(R.id.StudentCourseSec);
        StuNumber =  findViewById(R.id.StudentNumber);
        StuPass =  findViewById(R.id.StudentPassword);
        signup =  findViewById(R.id.StudentSignup);
        background = findViewById(R.id.bg);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Students");

        Glide
                .with(StudentSignup.this)
                .load(getDrawable(R.drawable.cvsu_bg))
                .centerCrop()
                .into(background);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = StuEmail.getText().toString().trim();
                String pass = StuPass.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    StuEmail.setError("Invalid Email");
                    StuEmail.setFocusable(true);
                } else if (pass.length() < 6) {
                    StuPass.setError("Password should contain atleast 6 characters");
                    StuPass.setFocusable(true);
                } else {
                    registerStu(email, pass);
                }
            }
        });
    }
    private void registerStu(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    user = FirebaseAuth.getInstance().getCurrentUser();

                    String email = user.getEmail();
                    String uid = user.getUid();
                    String name = StuName.getText().toString().trim();
                    String CourseSec = StuCourseSec.getText().toString().trim();
                    String StudentNumber = StuNumber.getText().toString().trim();

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("name", name);
                    hashMap.put("Cour&Sec", CourseSec);
                    hashMap.put("StudentNumber", StudentNumber);
                    hashMap.put("PhoneNumber", "");
                    hashMap.put("image", "");
                    hashMap.put("cover", "");
                    hashMap.put("bio", "--");
                    hashMap.put("Birthday", "--");
                    hashMap.put("Address", "--");
                    hashMap.put("Role", "Student");

                    reference.child(user.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            user.reload();
                            Toast.makeText(StudentSignup.this, "Registered.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(StudentSignup.this, StudentDashboard.class);
                            startActivity(intent);
                            StudentSignup.this.finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(StudentSignup.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {

                    Toast.makeText(StudentSignup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentSignup.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(StudentSignup.this, StudentLogin.class));
        StudentSignup.this.finish();
    }
}