package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StudentSubjectVPadapter extends FragmentPagerAdapter {
    public StudentSubjectVPadapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new StudentSubjectWorklist();
            case 1:
                return new StudentSubjectMaterials();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }
}
