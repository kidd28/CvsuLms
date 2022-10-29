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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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
import java.util.Locale;

public class StudentSignup extends AppCompatActivity {

    EditText StuEmail, StuName, StuCourseSec, StuNumber,StuPass;
    Button signup;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference reference;
    ImageView background;
    String Email, name,uid;
    GoogleSignInAccount account;
    GoogleSignInClient mGoogleSignInClient;
    Uri personPhoto;
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

        Email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name").toUpperCase(Locale.ROOT);
        uid = getIntent().getStringExtra("uid");

        StuEmail.setText(Email);
        StuName.setText(name);

        account = GoogleSignIn.getLastSignedInAccount(StudentSignup.this);
        if (account != null) {
             personPhoto = account.getPhotoUrl();
        }

        Glide
                .with(StudentSignup.this)
                .load(getDrawable(R.drawable.cvsu_bg))
                .centerCrop()
                .into(background);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                String CourseSec = StuCourseSec.getText().toString().trim();
                String StudentNumber = StuNumber.getText().toString().trim();

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email", Email);
                hashMap.put("uid", uid);
                hashMap.put("name", name);
                hashMap.put("secCode", CourseSec);
                hashMap.put("StudentNumber", StudentNumber);
                hashMap.put("PhoneNumber", "");
                hashMap.put("image", personPhoto.toString());
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
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(StudentSignup.this, MainActivity.class));
        StudentSignup.this.finish();
    }
}