package com.example.cvsulms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Use the {@link Subjects#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Subjects extends Fragment {
    RecyclerView recyclerView;
    ArrayList<StudentSubjectModel> studentSubjectModels;
    StudentSubjectAdapter studentSubjectAdapter;
    FirebaseUser user;

    String CourSection;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Subjects() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Subjects.
     */
    // TODO: Rename and change types and number of parameters
    public static Subjects newInstance(String param1, String param2) {
        Subjects fragment = new Subjects();
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
         View v =inflater.inflate(R.layout.fragment_subjects, container, false);

        recyclerView=v.findViewById(R.id.rv);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        studentSubjectModels = new ArrayList<>();
        recyclerView.setAdapter(studentSubjectAdapter);

        loadSubject();

   
        return v;
    }

    private void loadSubject() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Students");
        Query query = ref1.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CourSection = "" + ds.child("Cour&Sec").getValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Subjects");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentSubjectModels.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    System.out.println(ds.child("SecCode").getValue());
                    if(ds.child("SecCode").getValue().equals(CourSection)){
                        StudentSubjectModel model = ds.getValue(StudentSubjectModel.class);
                        studentSubjectModels.add(model);
                    }
                }
                studentSubjectAdapter = new StudentSubjectAdapter(getContext(), studentSubjectModels);
                recyclerView.setAdapter(studentSubjectAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}