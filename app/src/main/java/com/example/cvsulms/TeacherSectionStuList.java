package com.example.cvsulms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherSectionStuList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherSectionStuList extends Fragment {
    String secCode, subjCode,subj,teacherUid;

    RecyclerView recyclerView;
    ArrayList<StudentListModel> studentListModels;
    StudentListAdapter studentListAdapter;
    FirebaseUser user;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeacherSectionStuList() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeacherSectionStuList.
     */
    // TODO: Rename and change types and number of parameters
    public static TeacherSectionStuList newInstance(String param1, String param2) {
        TeacherSectionStuList fragment = new TeacherSectionStuList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_teacher_section_stu_list, container, false);

        recyclerView=v.findViewById(R.id.sturv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        studentListModels = new ArrayList<>();

        secCode = getActivity().getIntent().getExtras().getString("secCode");
        subjCode = getActivity().getIntent().getExtras().getString("subjCode");
        subj = getActivity().getIntent().getExtras().getString("subj");
        teacherUid = getActivity().getIntent().getExtras().getString("teacherUid");

        loadstudents();

        return v;
    }
    private void loadstudents() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentListModels.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    if(Objects.equals(ds.child("SecCode").getValue(), secCode)){
                        StudentListModel model = ds.getValue(StudentListModel.class);
                        studentListModels.add(model);
                    }
                }
                studentListAdapter = new StudentListAdapter(getContext(), studentListModels);
                recyclerView.setAdapter(studentListAdapter);
                studentListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction().remove((Fragment) TeacherSectionStuList.this).commitAllowingStateLoss();
    }
}