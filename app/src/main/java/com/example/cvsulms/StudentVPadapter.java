package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StudentVPadapter extends FragmentPagerAdapter {

    public StudentVPadapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new TodoList();
            case 1:
                return new Subjects();
            case 2:
                return new StudentProfile();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 3;
    }
}
