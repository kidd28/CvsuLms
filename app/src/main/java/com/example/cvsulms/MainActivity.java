package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    Button student,teacher;
    ImageView background;
    FirebaseAuth mAuth;
    FirebaseUser user;
    GoogleSignInAccount account;
    GoogleSignInClient mGoogleSignInClient;
    Boolean isStudent, isTeacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        student = findViewById(R.id.student);
        teacher = findViewById(R.id.teacher);
        background = findViewById(R.id.bg);
        mAuth = FirebaseAuth.getInstance();
        isStudent = false;
        isTeacher =false;

        Glide
                .with(MainActivity.this)
                .load(getDrawable(R.drawable.cvsu_bg))
                .centerCrop()
                .into(background);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        student.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                isStudent = true;
            }
        });

        teacher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                isTeacher = true;
            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed:" + e, Toast.LENGTH_SHORT).show();
                System.out.println(e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                            // Sign in success, update UI with the signed-in user's information
                            if (isNew) {
                                String[] split = user1.getEmail().split("@");
                                String domain = split[1];
                                if(domain.equals("cvsu.edu.ph"))
                                {
                                    if(isStudent){
                                    Intent intent = new Intent(MainActivity.this, StudentSignup.class);
                                    intent.putExtra("email", account.getEmail());
                                    intent.putExtra("uid", user1.getUid());
                                    intent.putExtra("name", account.getDisplayName().substring(0, 1).toUpperCase() + account.getDisplayName().substring(1).toLowerCase());
                                    startActivity(intent);}
                                    if(isTeacher){
                                        Intent intent = new Intent(MainActivity.this, TeacherSignup.class);
                                        intent.putExtra("email", account.getEmail());
                                        intent.putExtra("uid", user1.getUid());
                                        intent.putExtra("name", account.getDisplayName().substring(0, 1).toUpperCase() + account.getDisplayName().substring(1).toLowerCase());
                                        startActivity(intent);
                                    }
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"Please use valid Cvsu email address", Toast.LENGTH_SHORT).show();
                                    user1.delete();
                                    mAuth.signOut();
                                    revokeAccess();
                                }
                            } else {
                                if(isTeacher){
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Teachers");
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot sec : snapshot.getChildren()) {
                                            FirebaseUser user1 = mAuth.getCurrentUser();
                                            if(snapshot.hasChild(user1.getUid())){
                                                Toast.makeText(MainActivity.this, "Log in Successfully.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(MainActivity.this, TeacherDashboard.class));
                                                Toast.makeText(MainActivity.this, "Welcome " +account.getGivenName(), Toast.LENGTH_SHORT).show();
                                                MainActivity.this.finish();
                                            } else {
                                                Toast.makeText(MainActivity.this, "The Email you entered is registered as a student, Logging you in as a student", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(MainActivity.this, StudentDashboard.class));
                                                MainActivity.this.finish();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });}
                                else if (isStudent){
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Students");
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot sec : snapshot.getChildren()) {
                                                FirebaseUser user1 = mAuth.getCurrentUser();
                                                if(snapshot.hasChild(user1.getUid())){
                                                    Toast.makeText(MainActivity.this, "Log in Successfully.", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(MainActivity.this, StudentDashboard.class));
                                                    Toast.makeText(MainActivity.this, "Welcome " + user1.getEmail(), Toast.LENGTH_SHORT).show();
                                                    MainActivity.this.finish();
                                                } else {
                                                    Toast.makeText(MainActivity.this, "The Email you entered is registered as a teacher, Logging you in as a teacher", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(MainActivity.this, TeacherDashboard.class));
                                                    MainActivity.this.finish();
                                                }}}
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Sign-in Failed", Toast.LENGTH_SHORT).show();
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
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Teachers");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot sec : snapshot.getChildren()) {
                        FirebaseUser user1 = mAuth.getCurrentUser();
                        if(snapshot.hasChild(user1.getUid())){
                            Toast.makeText(MainActivity.this, "The Email you entered is registered as a teacher, Logging you in as a teacher", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, TeacherDashboard.class));
                            MainActivity.this.finish();
                        } else {
                            Toast.makeText(MainActivity.this, "The Email you entered is registered as a student, Logging you in as a student", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, StudentDashboard.class));
                            MainActivity.this.finish();
                        }}}
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            revokeAccess();
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {MainActivity.super.onBackPressed();
                        finish();
                        finishAndRemoveTask();
                        finishAffinity();
                        System.exit(0);
                    }
                }).create().show();
    }
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}