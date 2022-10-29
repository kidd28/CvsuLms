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
 * Use the {@link TodoList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoList extends Fragment {
    String secCode, subjCode,subj,teacherUid,CourSection;
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

    public TodoList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodoList.
     */
    // TODO: Rename and change types and number of parameters
    public static TodoList newInstance(String param1, String param2) {
        TodoList fragment = new TodoList();
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
        View v= inflater.inflate(R.layout.fragment_todo_list, container, false);

        recyclerView=v.findViewById(R.id.taskrv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        TaskModel = new ArrayList<>();

        loadTask();

        return v;
    }
    private void loadTask(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Students");
        Query query = ref1.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CourSection = "" + ds.child("secCode").getValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TaskModel.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("SecCode").getValue().equals(CourSection)){
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
        getFragmentManager().beginTransaction().remove((Fragment) TodoList.this).commitAllowingStateLoss();
    }
}