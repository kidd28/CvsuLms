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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherLogin extends AppCompatActivity {
    Button login;
    TextView signup;
    EditText Teachemail,TeachPass;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        login = findViewById(R.id.lgnbtn);
        background = findViewById(R.id.bg);
        signup =findViewById(R.id.signupbtn);
        Teachemail=findViewById(R.id.Email);
        TeachPass=findViewById(R.id.Password);
        mAuth = FirebaseAuth.getInstance();

        Glide
                .with(TeacherLogin.this)
                .load(getDrawable(R.drawable.cvsu_bg))
                .centerCrop()
                .into(background);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String V_email = Teachemail.getText().toString().trim();
                String V_pass = TeachPass.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(V_email).matches()) {
                    Teachemail.setError("Invalid Email");
                    Teachemail.setFocusable(true);
                } else if (TeachPass.length() < 6) {
                    TeachPass.setError("Password should contain atleast 6 characters");
                    TeachPass.setFocusable(true);
                } else {
                    LoginUser(V_email, V_pass);
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherLogin.this, TeacherSignup.class));
                TeacherLogin.this.finish();
            }
        });
    }
    private void LoginUser(String v_email, String v_pass) {
        mAuth.signInWithEmailAndPassword(v_email, v_pass)
                .addOnCompleteListener(TeacherLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Teachers");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot sec : snapshot.getChildren()) {
                                        FirebaseUser user1 = mAuth.getCurrentUser();
                                        if(snapshot.hasChild(user1.getUid())){
                                            Toast.makeText(TeacherLogin.this, "Log in Successfully.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(TeacherLogin.this, TeacherDashboard.class));
                                            Toast.makeText(TeacherLogin.this, "Welcome " + user1.getEmail(), Toast.LENGTH_SHORT).show();
                                            TeacherLogin.this.finish();
                                        } else {
                                            Toast.makeText(TeacherLogin.this, "The Email you entered is registered as a student", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(TeacherLogin.this, "Logging you in as a student", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(TeacherLogin.this, StudentDashboard.class));
                                            TeacherLogin.this.finish();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(TeacherLogin.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.reload();

        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(TeacherLogin.this, MainActivity.class));
        TeacherLogin.this.finish();
    }
}