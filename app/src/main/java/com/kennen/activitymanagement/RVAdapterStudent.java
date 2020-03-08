package com.kennen.activitymanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterStudent extends RecyclerView.Adapter<RVAdapterStudent.ViewHolder>
{
    private List<Student> studentList = new ArrayList<>();
    private Context mContext;

    public RVAdapterStudent(List<Student> studentList, Context mContext)
    {
        this.studentList = studentList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View temp = LayoutInflater.from(mContext).inflate(R.layout.student_detail, parent, false);
        return new ViewHolder(temp);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Student temp = studentList.get(position);
        holder.tv_stdName.setText(temp.getName());
        holder.tv_stdTel.setText(temp.getPhoneNumber());
    }

    @Override
    public int getItemCount()
    {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_stdName, tv_stdTel;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_stdName = (TextView) itemView.findViewById(R.id.tv_stdName);
            tv_stdTel = (TextView) itemView.findViewById(R.id.tv_stdTel);
        }
    }
}
