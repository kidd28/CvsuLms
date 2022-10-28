package com.example.cvsulms;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherProfile extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView avatar;
    TextView name, email,dept,empnum,phonenum,address,birthday, role;
    Button Logout;
    FirebaseAuth auth;
    ImageView  IcEmail, IcDept, IcEmpnum, IcPhone,IcAdress,IcBirthday;
    GoogleSignInClient mGoogleSignInClient;
    Uri personPhoto;
    GoogleSignInAccount account;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeacherProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeacherProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static TeacherProfile newInstance(String param1, String param2) {
        TeacherProfile fragment = new TeacherProfile();
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
        View v = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        avatar = v.findViewById(R.id.Avatar);
        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        dept = v.findViewById(R.id.department);
        empnum = v.findViewById(R.id.EmployeeNumber);
        phonenum = v.findViewById(R.id.phone);
        address = v.findViewById(R.id.addresss);
        birthday = v.findViewById(R.id.birthday);
        Logout = v.findViewById(R.id.logout);
        role = v.findViewById(R.id.role);

        IcEmail = v.findViewById(R.id.IconEmail);
        IcDept = v.findViewById(R.id.IconDepartment);
        IcEmpnum = v.findViewById(R.id.IconEmpNum);
        IcPhone = v.findViewById(R.id.IconPhone);
        IcAdress = v.findViewById(R.id.IconAddress);
        IcBirthday = v.findViewById(R.id.IconBirthday);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Teachers");

        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null) {
            personPhoto = account.getPhotoUrl();
        }
        Glide
                .with(getActivity())
                .load(personPhoto)
                .centerCrop()
                .into(avatar);

        Glide
                .with(getActivity())
                .load(getDrawable(getActivity(),R.drawable.email))
                .centerCrop()
                .into(IcEmail);
        Glide
                .with(getActivity())
                .load(getDrawable(getActivity(),R.drawable.email))
                .centerCrop()
                .into(IcEmail);
        Glide
                .with(getActivity())
                .load(getDrawable(getActivity(),R.drawable.section))
                .centerCrop()
                .into(IcDept);
        Glide
                .with(getActivity())
                .load(getDrawable(getActivity(),R.drawable.idnumber))
                .centerCrop()
                .into(IcEmpnum);
        Glide
                .with(getActivity())
                .load(getDrawable(getActivity(),R.drawable.phone))
                .centerCrop()
                .into(IcPhone);
        Glide
                .with(getActivity())
                .load(getDrawable(getActivity(),R.drawable.loc))
                .centerCrop()
                .into(IcAdress);
        Glide
                .with(getActivity())
                .load(getDrawable(getActivity(),R.drawable.cake))
                .centerCrop()
                .into(IcBirthday);


        Query query = reference.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String StuName = "" + ds.child("name").getValue();
                    String StuEmail = "" + ds.child("email").getValue();
                    String Dept = "" + ds.child("Department").getValue();
                    String EmpNum = "" + ds.child("Employee Number").getValue();
                    String phoneNumber = "" + ds.child("PhoneNumber").getValue();
                    String Address = "" + ds.child("Address").getValue();
                    String Birthday = "" + ds.child("Birthday").getValue();
                    String Role = "" + ds.child("Role").getValue();

                    name.setText(StuName);
                    email.setText(StuEmail);
                    dept.setText(Dept);
                    empnum.setText(EmpNum);
                    phonenum.setText(phoneNumber);
                    address.setText(Address);
                    birthday.setText(Birthday);
                    role.setText(Role);

                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();

                    mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to Logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                auth.signOut();
                                revokeAccess();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }
                        }).create().show();
            }
        });


        return v;
    }
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }


}