package com.example.cvsulms;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TeacherSectionVPAdapter extends FragmentPagerAdapter {
    public TeacherSectionVPAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new TeacherSectionTask();
            case 1:
                return new TeacherSectionMaterials();
            case 2:
                return new TeacherSectionStuList();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 3;
    }
}
