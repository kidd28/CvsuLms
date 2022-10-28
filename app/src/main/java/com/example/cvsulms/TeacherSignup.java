package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import java.util.Locale;

public class TeacherSignup extends AppCompatActivity {
    EditText TeachEmail,TeachName, EmployeeNumber, TeachPass,Department;
    Button signup;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference reference;
    ImageView background;
    String Email, name,uid;
    GoogleSignInAccount account;
    Uri personPhoto;
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
        Department = findViewById(R.id.Dept);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Teachers");

        Email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name").toUpperCase(Locale.ROOT);
        uid = getIntent().getStringExtra("uid");

        account = GoogleSignIn.getLastSignedInAccount(TeacherSignup.this);
        if (account != null) {
            personPhoto = account.getPhotoUrl();
        }

        TeachEmail.setText(Email);
        TeachName.setText(name);
        Glide
                .with(TeacherSignup.this)
                .load(getDrawable(R.drawable.cvsu_bg))
                .centerCrop()
                .into(background);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                String EmployNumber = EmployeeNumber.getText().toString().trim();
                String department = Department.getText().toString().trim();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email",Email );
                hashMap.put("uid", uid);
                hashMap.put("name", name);
                hashMap.put("Employee Number", EmployNumber);
                hashMap.put("image", personPhoto.toString());
                hashMap.put("cover", "");
                hashMap.put("bio", "--");
                hashMap.put("Birthday", "--");
                hashMap.put("Address", "--");
                hashMap.put("Role", "Teacher");
                hashMap.put("Department",department);

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
        });


    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(TeacherSignup.this, MainActivity.class));
        TeacherSignup.this.finish();
    }
}