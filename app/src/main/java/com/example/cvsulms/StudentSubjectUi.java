package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentSubjectUi extends AppCompatActivity {
    String secCode, subjCode,subj,teacherUid;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subject_ui);

        viewPager = findViewById(R.id.tvp);
        bottomNavigationView = findViewById(R.id.bottomNav);

        secCode =getIntent().getExtras().getString("secCode");
        subjCode = getIntent().getExtras().getString("subjCode");
        subj =getIntent().getExtras().getString("subj");
        teacherUid =getIntent().getExtras().getString("teacherUid");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(subjCode+" "+secCode);

        StudentSubjectVPadapter studentSubjectVPadapter= new StudentSubjectVPadapter(getSupportFragmentManager());
        viewPager.setAdapter(studentSubjectVPadapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().getItem(1).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.WorkList:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.Materials:
                        viewPager.setCurrentItem(1);
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        startActivity( new Intent(StudentSubjectUi.this, StudentDashboard.class));
        StudentSubjectUi.this.finish();
        finishAndRemoveTask();
    }
    }
