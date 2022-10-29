package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherSectionUi extends AppCompatActivity {
    String secCode, subjCode,subj,teacherUid;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_section_ui);

        viewPager = findViewById(R.id.tvp);
        bottomNavigationView = findViewById(R.id.bottomNav);

        secCode =getIntent().getExtras().getString("secCode");
        subjCode = getIntent().getExtras().getString("subjCode");
        subj =getIntent().getExtras().getString("subj");
        teacherUid =getIntent().getExtras().getString("teacherUid");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(subjCode+" "+secCode);

        TeacherSectionVPAdapter teacherSectionVPAdapter= new TeacherSectionVPAdapter(getSupportFragmentManager());
        viewPager.setAdapter(teacherSectionVPAdapter);

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
                    case 2:
                        bottomNavigationView.getMenu().getItem(2).setChecked(true);
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
                    case R.id.Task:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.Materials:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.StudentList:
                        viewPager.setCurrentItem(2);
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
        startActivity( new Intent(TeacherSectionUi.this, TeacherDashboard.class));
        TeacherSectionUi.this.finish();
        finishAffinity();
        finishAndRemoveTask();
    }
}