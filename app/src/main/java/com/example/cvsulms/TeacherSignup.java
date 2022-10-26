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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TeacherSignup extends AppCompatActivity {
    EditText TeachEmail,TeachName, EmployeeNumber, TeachPass;
    Button signup;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference reference;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_signup);

        TeachEmail = findViewById(R.id.TeacherEmail);
        TeachName = findViewById(R.id.TeacherName);
        EmployeeNumber = findViewById(R.id.EmployeeNumber);
        TeachPass = findViewById(R.id.TeacherPass);
        signup = findViewById(R.id.TeachSignup);
        background = findViewById(R.id.bg);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Teachers");

        Glide
                .with(TeacherSignup.this)
                .load(getDrawable(R.drawable.cvsu_bg))
                .centerCrop()
                .into(background);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = TeachEmail.getText().toString().trim();
                String pass = TeachPass.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    TeachEmail.setError("Invalid Email");
                    TeachEmail.setFocusable(true);
                } else if (pass.length() < 6) {
                    TeachPass.setError("Password should contain atleast 6 characters");
                    TeachPass.setFocusable(true);
                } else {
                    registerTeacher(email, pass);
                }
            }
        });


    }

    private void registerTeacher(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    user = FirebaseAuth.getInstance().getCurrentUser();

                    String email = user.getEmail();
                    String uid = user.getUid();
                    String name = TeachName.getText().toString().trim();
                    String EmployNumber = EmployeeNumber.getText().toString().trim();

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("name", name);
                    hashMap.put("Employee Number", EmployNumber);
                    hashMap.put("image", "");
                    hashMap.put("cover", "");
                    hashMap.put("bio", "--");
                    hashMap.put("Birthday", "--");
                    hashMap.put("Address", "--");
                    hashMap.put("Role", "Teacher");

                    reference.child(user.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            user.reload();
                            Toast.makeText(TeacherSignup.this, "Registered.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TeacherSignup.this, TeacherDashboard.class);
                            startActivity(intent);
                            TeacherSignup.this.finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TeacherSignup.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {

                    Toast.makeText(TeacherSignup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TeacherSignup.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TeacherSignup.this, TeacherLogin.class));
        TeacherSignup.this.finish();
    }
}