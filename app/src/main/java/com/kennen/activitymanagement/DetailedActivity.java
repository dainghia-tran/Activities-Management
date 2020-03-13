package com.kennen.activitymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kennen.activitymanagement.MainActivity.myReference;

public class DetailedActivity extends AppCompatActivity
{
    TextView tvName, tvDate, tvLocation, tvDes;
    FloatingActionButton fab, rollCallFab, deleteActFab, exportFab;
    RecyclerView student;
    RVAdapterStudent adapterStudent;
    boolean isShowFAB = false;
    Activity temp = null;
    EditText idStd;
    List<Student> studentList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        setReference(); //reference to layout

        Intent intent = getIntent();
        temp = (Activity) intent.getSerializableExtra("activity"); //get 'activity' from MainActivity

        if (temp != null)    //check if temp != null
        {
            studentList = temp.getStudentsList();
            tvName.setText(temp.getName());
            tvDate.setText(temp.getOrganizeDate());
            tvLocation.setText(temp.getLocation());
            tvDes.setText(temp.getDescription());


            if (temp.getStudentsList() !=null)
            {
                student = (RecyclerView) findViewById(R.id.rv_actStudent);
                adapterStudent = new RVAdapterStudent(temp.getStudentsList().stream().filter(std -> !std.isRollCall()).collect(Collectors.toList()), this);
                student.setAdapter(adapterStudent);
                student.setLayoutManager(new LinearLayoutManager(this));
            }
        }

        fab.setOnClickListener(v -> //use Lambda
        {
            if (!isShowFAB)
            {
                ShowFAB();
                isShowFAB = true;
            } else
            {
                HideFAB();
                isShowFAB = false;
            }
        });

        rollCallFab.setOnClickListener(v ->
        {
            RollCall();
        });

        exportFab.setOnClickListener(v ->
        {
            Dexter.withActivity(this).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener()
            {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report)
                {
                    ExportXLSX();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
                {
                    Toast.makeText(DetailedActivity.this, "Vui lòng cấp quyền xuất file lên bộ nhớ!", Toast.LENGTH_SHORT).show();
                }
            }).check();


            //Toast.makeText(this, "Đã xuất file " + temp.getDbChild() + ".xlsx" +" thành công!", Toast.LENGTH_SHORT).show();
        });

        deleteActFab.setOnClickListener(v->
        {
            DeleteActivity();
        });
    }

    private void DeleteActivity()
    {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có muốn xóa hoạt động này không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        myReference.child(temp.getDbChild()).removeValue();
                        finish();
                    }
                })
                .setNegativeButton("Không", null)
                .setIcon(R.drawable.round_delete_forever_black_18dp)
                .show();
    }

    private void ExportXLSX()
    {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("List");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setColor(IndexedColors.RED.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        String[] col = {"STT", "MSSV", "Họ và tên", "Số điện thoại", "Điểm danh", "Thời gian điểm danh"};
        for (int i = 0; i < col.length; i++)
        {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(col[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;
        for (Student elm : studentList)
        {
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(rowNum++);
            row.createCell(1).setCellValue(elm.getId());
            row.createCell(2).setCellValue(elm.getName());
            row.createCell(3).setCellValue(elm.getPhoneNumber());
            row.createCell(4).setCellValue(elm.isRollCall() ? "✓" : "");
            row.createCell(5).setCellValue(elm.getRollCallTime());
        }

        try
        {
            File path = new File(Environment.getExternalStorageDirectory() + "/Download", temp.getDbChild() + ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            Toast.makeText(this, "Đã xuất file " + temp.getDbChild() + ".xlsx" + " vào thư mục Download" + " thành công!", Toast.LENGTH_SHORT).show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void RollCall()
    {
        Dialog rollCall = new Dialog(this);
        rollCall.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rollCall.setContentView(R.layout.rollcall_dialog);
        rollCall.show();

        TextView nameAct = (TextView) rollCall.findViewById(R.id.tv_name);
        idStd = (EditText) rollCall.findViewById(R.id.edt_mssv);
        Button scan = (Button) rollCall.findViewById(R.id.btn_scan);
        Button rc = (Button) rollCall.findViewById(R.id.btn_rollcall);

        nameAct.setText(temp.getName());

        scan.setOnClickListener(v ->
        {
            Intent intent = new Intent(DetailedActivity.this, ScanActivity.class);
            startActivityForResult(intent, 1);
        });

        rc.setOnClickListener(v ->
        {
            Optional<Student> match = studentList.stream().filter(std -> std.getId().equals(idStd.getText().toString())).findFirst();
            Student rcStudent;
            int index;
            if (match.isPresent())
            {
                rcStudent = match.get();
                index = studentList.indexOf(rcStudent);
                Log.e(String.valueOf(index), rcStudent.getName());
                rcStudent.setRollCall(true);
                myReference.child(temp.getDbChild()).child("studentsList").child(String.valueOf(index)).child("rollCall").setValue(true);
                Calendar calendar = Calendar.getInstance();
                String rcTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                rcStudent.setRollCallTime(rcTime);
                myReference.child(temp.getDbChild()).child("studentsList").child(String.valueOf(index)).child("rollCallTime").setValue(rcTime);
                studentList.set(index, rcStudent);
                adapterStudent.notifyDataSetChanged();
                Toast.makeText(this, "Đã điểm danh " + rcStudent.getName(), Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Không tìm thấy sinh viên!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null)
        {
            idStd.setText(data.getStringExtra("ID"));
        }
    }

    private void setReference()
    {
        tvName = (TextView) findViewById(R.id.tv_nameAct);
        tvDate = (TextView) findViewById(R.id.tv_dateAct);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvDes = (TextView) findViewById(R.id.tv_description);
        fab = (FloatingActionButton) findViewById(R.id.fab_expand);
        rollCallFab = (FloatingActionButton) findViewById(R.id.fab_rollCall);
        exportFab = (FloatingActionButton) findViewById(R.id.fab_export);
        deleteActFab = (FloatingActionButton)findViewById(R.id.fab_deleteAct);
    }

    private void ShowFAB()
    {
        rollCallFab.show();
        exportFab.show();
        deleteActFab.show();
    }

    private void HideFAB()
    {
        rollCallFab.hide();
        exportFab.hide();
        deleteActFab.hide();
    }

}
