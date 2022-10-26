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

public class StudentLogin extends AppCompatActivity {
    Button login;
    TextView signup;
    EditText StuEmail,StuPass;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        login = findViewById(R.id.lgnbtn);
        signup = findViewById(R.id.signupbtn);
        StuEmail=findViewById(R.id.Email);
        StuPass=findViewById(R.id.Password);
        mAuth = FirebaseAuth.getInstance();
        background = findViewById(R.id.bg);
        user = FirebaseAuth.getInstance().getCurrentUser();

        Glide
                .with(StudentLogin.this)
                .load(getDrawable(R.drawable.cvsu_bg))
                .centerCrop()
                .into(background);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String V_email = StuEmail.getText().toString().trim();
                String V_pass = StuPass.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(V_email).matches()) {
                    StuEmail.setError("Invalid Email");
                    StuEmail.setFocusable(true);
                } else if (StuPass.length() < 6) {
                    StuPass.setError("Password should contain atleast 6 characters");
                    StuPass.setFocusable(true);
                } else {
                    LoginUser(V_email, V_pass);
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentLogin.this, StudentSignup.class));
                StudentLogin.this.finish();
            }
        });
    }
    private void LoginUser(String v_email, String v_pass) {
        mAuth.signInWithEmailAndPassword(v_email, v_pass)
                .addOnCompleteListener(StudentLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Students");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot sec : snapshot.getChildren()) {
                                        FirebaseUser user1 = mAuth.getCurrentUser();
                                        if(snapshot.hasChild(user1.getUid())){
                                            Toast.makeText(StudentLogin.this, "Log in Successfully.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(StudentLogin.this, StudentDashboard.class));
                                            Toast.makeText(StudentLogin.this, "Welcome " + user1.getEmail(), Toast.LENGTH_SHORT).show();
                                            StudentLogin.this.finish();
                                        } else {
                                            Toast.makeText(StudentLogin.this, "The Email you entered is registered as a teacher", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(StudentLogin.this, "Logging you in as a teacher", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(StudentLogin.this, TeacherDashboard.class));
                                            StudentLogin.this.finish();
                                        }}}
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            // Sign in success, update UI with the signed-in user's information
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(StudentLogin.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(StudentLogin.this, MainActivity.class));
        StudentLogin.this.finish();
    }
}