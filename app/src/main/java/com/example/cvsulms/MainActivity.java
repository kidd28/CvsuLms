package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button student,teacher;
    ImageView background;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        student = findViewById(R.id.student);
        teacher = findViewById(R.id.teacher);
        background = findViewById(R.id.bg);
        mAuth = FirebaseAuth.getInstance();

        Glide
                .with(MainActivity.this)
                .load(getDrawable(R.drawable.cvsu_bg))
                .centerCrop()
                .into(background);


        student.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StudentLogin.class));
                MainActivity.this.finish();
            }
        });

        teacher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TeacherLogin.class));
                MainActivity.this.finish();
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
                            Toast.makeText(MainActivity.this, "The Email saved is registered as a teacher", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "Logging you in as a teacher", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, TeacherDashboard.class));
                            MainActivity.this.finish();
                        } else {
                            Toast.makeText(MainActivity.this, "The Email saved is registered as a student", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "Logging you in as a student", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, StudentDashboard.class));
                            MainActivity.this.finish();
                        }}}
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}