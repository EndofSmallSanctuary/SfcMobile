package com.example.smarttag.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.smarttag.MainActivity;
import com.example.smarttag.Models.UserInfo;
import com.example.smarttag.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class RegistationFragment extends Fragment {

    @BindView(R.id.Registration_Company_name)
    EditText company_name;
    @BindView(R.id.Registration_Person_name)
    EditText person_name;
    @BindView(R.id.Registration_Phone)
    EditText person_phone;
    @BindView(R.id.Registration_Submit)
    Button submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_registation, container, false);
       ButterKnife.bind(this,view);
       return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        submit.setOnClickListener(v -> {
            if(company_name.getText().toString().equals("")||
               person_name.getText().toString().equals("")||
               person_phone.getText().toString().equals("")){
                Toasty.error(getActivity(), getString(R.string.all_fields_must_be_not_empty),Toasty.LENGTH_SHORT).show();
            }
            else {
                if(person_phone.getText().toString().length()<10){
                    Toasty.error(getActivity(),getString(R.string.correct_phone_required),Toasty.LENGTH_SHORT).show();
                    return;
                }
                MainActivity activity = (MainActivity)getActivity();
                activity.registratesomebruh(new UserInfo(
                        person_name.getText().toString(),
                        company_name.getText().toString(),
                        person_phone.getText().toString()
                ));
            }
        });
    }
}