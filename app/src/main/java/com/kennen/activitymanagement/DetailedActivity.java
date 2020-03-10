package com.kennen.activitymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Optional;

import static com.kennen.activitymanagement.MainActivity.myReference;

public class DetailedActivity extends AppCompatActivity
{
    TextView tvName, tvDate, tvLocation, tvDes;
    FloatingActionButton fab, rollCallFab, unRCFab, exportFab;
    RecyclerView student;
    boolean isShowFAB = false;
    Activity temp = null;
    EditText idStd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        setReference(); //reference to layout

        Intent intent = getIntent();
        temp = (Activity) intent.getSerializableExtra("activity"); //get 'activity' from MainActivity

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

        rollCallFab.setOnClickListener(v->
        {
            RollCall();
        });
    }

    private void RollCall()
    {
        Dialog rollCall = new Dialog(this);
        rollCall.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rollCall.setContentView(R.layout.rollcall_dialog);
        rollCall.show();

        TextView nameAct = (TextView)rollCall.findViewById(R.id.tv_name);
        idStd = (EditText)rollCall.findViewById(R.id.edt_mssv);
        Button scan = (Button)rollCall.findViewById(R.id.btn_scan);
        Button rc = (Button)rollCall.findViewById(R.id.btn_rollcall);

        nameAct.setText(temp.getName());

        scan.setOnClickListener(v->
        {
            Intent intent = new Intent(DetailedActivity.this, ScanActivity.class);
            startActivityForResult(intent, 1);
        });

        rc.setOnClickListener(v->
        {
            Optional<Student> match = temp.getStudentsList().stream().filter(std -> std.getId().equals(idStd.getText().toString())).findFirst();
            Student rcStudent;
            int index;
            if(match.isPresent())
            {
                rcStudent = match.get();
                index = temp.getStudentsList().indexOf(rcStudent);
                Log.e(String.valueOf(index), rcStudent.getName());
                myReference.child(temp.getDbChild()).child("studentsList").child(String.valueOf(index)).child("rollCall").setValue(true);
                Toast.makeText(this, "Đã điểm danh " + rcStudent.getName(), Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Không tìm thấy sinh viên!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data != null)
        {
            idStd.setText(data.getStringExtra("ID"));
        }
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
