package com.example.cvsulms;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherSectionTask#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherSectionTask extends Fragment {
    String secCode, subjCode,subj,teacherUid;
    Button createtask;

    RecyclerView recyclerView;
    ArrayList<TaskModel> TaskModel;
    TaskAdapter TaskAdapter;
    FirebaseUser user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeacherSectionTask() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeacherSectionTask.
     */
    // TODO: Rename and change types and number of parameters
    public static TeacherSectionTask newInstance(String param1, String param2) {
        TeacherSectionTask fragment = new TeacherSectionTask();
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
        View v= inflater.inflate(R.layout.fragment_teacher_section_task, container, false);

        secCode = getActivity().getIntent().getExtras().getString("secCode");
        subjCode = getActivity().getIntent().getExtras().getString("subjCode");
        subj = getActivity().getIntent().getExtras().getString("subj");
        teacherUid = getActivity().getIntent().getExtras().getString("teacherUid");

        createtask = v.findViewById(R.id.CreateTask);
        recyclerView=v.findViewById(R.id.taskrv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        TaskModel = new ArrayList<>();

        loadTask();

        createtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TaskId =""+ System.currentTimeMillis();
                Intent intent = new Intent (getActivity(), CreateTask.class);
                intent.putExtra("secCode",secCode );
                intent.putExtra("subjCode",subjCode );
                intent.putExtra("subj",subj );
                intent.putExtra("teacherUid",teacherUid );
                intent.putExtra("TaskId",TaskId );

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("TaskId", TaskId);
                reference.child(TaskId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getActivity().startActivity(intent);
                    }
                });

            }
        });
        return  v;


    }

    private void loadTask(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TaskModel.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    if(Objects.equals(ds.child("TeacherUid").getValue(), user.getUid()) && Objects.equals(ds.child("SecCode").getValue(), secCode)){
                        TaskModel model = ds.getValue(TaskModel.class);
                        TaskModel.add(model);
                    }
                }
                TaskAdapter = new TaskAdapter(getContext(), TaskModel);
                recyclerView.setAdapter(TaskAdapter);
                TaskAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction().remove((Fragment) TeacherSectionTask.this).commitAllowingStateLoss();
    }
}