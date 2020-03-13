package com.kennen.activitymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity
{
    private Button btnComplete;
    private ImageView img_addStudent;
    private TextView tvDate;
    private EditText edtName, edtLocation, edtDes;
    private RecyclerView rvStudentList;
    private List<Student> students = new ArrayList<>();
    private Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setReference();

        rvStudentList = findViewById(R.id.rv_student);

        Intent intent = getIntent();
        Activity temp = (Activity) intent.getSerializableExtra("activity");
        if (temp != null)   //edit available activity
        {
            edtName.setText(temp.getName());
            tvDate.setText(temp.getOrganizeDate());
            edtLocation.setText(temp.getLocation());
            edtDes.setText(temp.getDescription());
            students = temp.getStudentsList();
        } else
        {
            activity = new Activity();

            img_addStudent.setOnClickListener(v ->
            {
                addStudentDialog();
            });
        }
        RVAdapterStudent rvAStudent = new RVAdapterStudent(students, this);
        rvStudentList.setAdapter(rvAStudent);
        rvStudentList.setLayoutManager(new LinearLayoutManager(this));

        tvDate.setOnClickListener(v->
        {
            PickDate();
        });

        btnComplete.setOnClickListener(v ->
        {
            activity = new Activity(edtName.getText().toString(), tvDate.getText().toString(), edtLocation.getText().toString(), edtDes.getText().toString(), students);
            DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
            mRef.child(activity.getDbChild()).setValue(activity);
            finish();;
        });
    }

    private void setReference()
    {
        img_addStudent = (ImageView) findViewById(R.id.img_addStudent);
        btnComplete = (Button) findViewById(R.id.btn_complete);
        edtName = (EditText) findViewById(R.id.tv_actName);
        tvDate = (TextView) findViewById(R.id.tv_actDate);
        edtLocation = (EditText) findViewById(R.id.tv_actLocation);
        edtDes = (EditText) findViewById(R.id.tv_actDescription);
    }

    private void addStudentDialog()
    {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_student_dialog);
        dialog.show();

        EditText name = (EditText) dialog.findViewById(R.id.edt_stdName);
        EditText id = (EditText) dialog.findViewById(R.id.edt_stdId);
        EditText tel = (EditText) dialog.findViewById(R.id.edt_stdTel);
        Button confirm = (Button) dialog.findViewById(R.id.btn_confirm);
        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        Student temp = new Student();
        confirm.setOnClickListener(v ->
        {
            if (!name.getText().toString().equals("") && !id.getText().toString().equals("") && !tel.getText().toString().equals(""))
            {
                temp.setName(name.getText().toString());
                temp.setId(id.getText().toString());
                temp.setRollCallTime("0");
                temp.setRollCall(false);
                temp.setPhoneNumber(tel.getText().toString());
                students.add(temp);
                dialog.dismiss();
            } else
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        });

        cancel.setOnClickListener(v ->
        {
            dialog.dismiss();
        });
    }

    private void PickDate()
    {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
                tvDate.setText(simpleDateFormat.format(calendar.getTime()));
                PickTime();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        dialog.show();
    }

    private void PickTime()
    {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, (view, hourOfDay, minute) ->
        {
            calendar.set(0, 0, 0, hourOfDay, minute);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", new Locale("vi", "VN"));
            String temp = tvDate.getText().toString();
            tvDate.setText(String.format("%s - %s", temp, simpleDateFormat.format(calendar.getTime())));
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }
}
