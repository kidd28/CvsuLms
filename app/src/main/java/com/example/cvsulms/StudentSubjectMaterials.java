package com.example.cvsulms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentSubjectMaterials#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentSubjectMaterials extends Fragment {
    String secCode, subjCode,subj,teacherUid;

    RecyclerView recyclerView;
    ArrayList<MaterialModel> materialModels;
    MaterialAdapter materialAdapter;
    FirebaseUser user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentSubjectMaterials() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentSubjectMaterials.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentSubjectMaterials newInstance(String param1, String param2) {
        StudentSubjectMaterials fragment = new StudentSubjectMaterials();
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
        View v=  inflater.inflate(R.layout.fragment_student_subject_materials, container, false);

        secCode = getActivity().getIntent().getExtras().getString("secCode");
        subjCode = getActivity().getIntent().getExtras().getString("subjCode");
        subj = getActivity().getIntent().getExtras().getString("subj");
        teacherUid = getActivity().getIntent().getExtras().getString("teacherUid");

        recyclerView=v.findViewById(R.id.matrv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        materialModels = new ArrayList<>();

        loadMaterial();

        return v;

    }
    private void loadMaterial() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Materials");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                materialModels.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("SubjCode").getValue().equals(subjCode) && ds.child("SecCode").getValue().equals(secCode)){
                        MaterialModel model = ds.getValue(MaterialModel.class);
                        materialModels.add(model);
                    }
                }
                materialAdapter = new MaterialAdapter(getContext(), materialModels);
                recyclerView.setAdapter(materialAdapter);
                materialAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
}@Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction().remove((Fragment) StudentSubjectMaterials.this).commitAllowingStateLoss();
    }

}