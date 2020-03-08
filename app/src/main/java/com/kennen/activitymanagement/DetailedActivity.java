package com.kennen.activitymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailedActivity extends AppCompatActivity
{
    TextView tvName, tvDate, tvLocation, tvDes;
    FloatingActionButton fab, rollCallFab, unRCFab, exportFab;
    RecyclerView student;
    boolean isShowFAB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        setReference(); //reference to layout

        Intent intent = getIntent();
        Activity temp = (Activity) intent.getSerializableExtra("activity"); //get 'activity' from MainActivity

        if(temp!=null)    //check if temp != null
        {
            tvName.setText(temp.getName());
            tvDate.setText(temp.getOrganizeDate().toString());
            tvLocation.setText(temp.getLocation());
            tvDes.setText(temp.getDescription());

            student = (RecyclerView)findViewById(R.id.rv_actStudent);
            RVAdapterStudent adapterStudent = new RVAdapterStudent(temp.getStudentsList(), this);
            student.setAdapter(adapterStudent);
            student.setLayoutManager(new LinearLayoutManager(this));
        }

        fab.setOnClickListener(v -> //use Lambda
        {
            if(!isShowFAB)
            {
                ShowFAB();
                isShowFAB = true;
            }
            else
            {
                HideFAB();
                isShowFAB = false;
            }
        });
    }

    private void RollCall()
    {
        Dialog rollCall = new Dialog(this);
        rollCall.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rollCall.setContentView(R.layout.rollcall_dialog);
        rollCall.show();

        TextView nameAct = (TextView)rollCall.findViewById(R.id.tv_name);
        EditText idStd = (EditText)rollCall.findViewById(R.id.edt_mssv);
        Button scan = (Button)rollCall.findViewById(R.id.btn_scan);
        Button rc = (Button)rollCall.findViewById(R.id.btn_rollcall);
    }

    private void setReference()
    {
        tvName = (TextView)findViewById(R.id.tv_nameAct);
        tvDate = (TextView)findViewById(R.id.tv_dateAct);
        tvLocation = (TextView)findViewById(R.id.tv_location);
        tvDes = (TextView)findViewById(R.id.tv_description);
        fab = (FloatingActionButton)findViewById(R.id.fab_expand);
        rollCallFab = (FloatingActionButton)findViewById(R.id.fab_rollCall);
        unRCFab = (FloatingActionButton)findViewById(R.id.fab_unRC);
        exportFab = (FloatingActionButton)findViewById(R.id.fab_export);
    }

    private void ShowFAB()
    {
        rollCallFab.show();
        unRCFab.show();
        exportFab.show();
    }

    private void HideFAB()
    {
        rollCallFab.hide();
        unRCFab.hide();;
        exportFab.hide();
    }

}
