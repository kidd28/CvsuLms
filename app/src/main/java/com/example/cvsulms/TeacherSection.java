package com.example.cvsulms;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherSection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherSection extends Fragment {
    Button CreateSection;
    RecyclerView recyclerView;
    ArrayList<TeacherSectionModel> teacherSectionModels;
    TeacherSectionAdapter teacherSectionAdapter;
    FirebaseUser user;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeacherSection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeacherSection.
     */
    // TODO: Rename and change types and number of parameters
    public static TeacherSection newInstance(String param1, String param2) {
        TeacherSection fragment = new TeacherSection();
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
        View v = inflater.inflate(R.layout.fragment_teacher_section, container, false);

        CreateSection = v.findViewById(R.id.CreateSec);
        recyclerView=v.findViewById(R.id.secrv);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        teacherSectionModels = new ArrayList<>();


        loadSection();

        CreateSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateSection.class));
                getActivity().finish();
            }
        });
        return v;
    }


    private void loadSection() {
        user =FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sections");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherSectionModels.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("TeacherUid").getValue().equals(user.getUid())){
                        TeacherSectionModel model = ds.getValue(TeacherSectionModel.class);
                        teacherSectionModels.add(model);
                    }
                }
                teacherSectionAdapter = new TeacherSectionAdapter(getContext(), teacherSectionModels);
                recyclerView.setAdapter(teacherSectionAdapter);
                teacherSectionAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction().remove((Fragment) TeacherSection.this).commitAllowingStateLoss();
    }
}