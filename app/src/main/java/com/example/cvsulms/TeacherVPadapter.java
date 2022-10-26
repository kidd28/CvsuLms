package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TeacherVPadapter extends FragmentPagerAdapter {

    public TeacherVPadapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new TeacherWorklist();
            case 1:
                return new TeacherSection();
            case 2:
                return new TeacherProfile();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 3;
    }
}
