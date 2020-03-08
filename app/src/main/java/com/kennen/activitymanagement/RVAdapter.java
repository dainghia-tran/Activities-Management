package com.kennen.activitymanagement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>
{
    private Context mContext;
    private List<Activity> activityList;

    public class ViewHolder extends RecyclerView.ViewHolder //ViewHolder
    {
        private TextView act_name, act_date;
        private Button btn_detailed;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            btn_detailed = (Button)itemView.findViewById(R.id.btn_detailed);
            act_name = (TextView)itemView.findViewById(R.id.activity_name);
            act_date = (TextView)itemView.findViewById(R.id.activity_date);
        }
    }

    public RVAdapter(Context mContext, List<Activity> activityList)
    {
        this.mContext = mContext;
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_information, parent, false);    //inflate layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.ViewHolder holder, int position)
    {
        Activity temp = activityList.get(position);
        holder.act_name.setText(temp.getName());
        holder.act_date.setText(temp.getOrganizeDate().toString());
        holder.btn_detailed.setOnClickListener(new View.OnClickListener()   //go to detailed 'Activity'
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, DetailedActivity.class);
                intent.putExtra("activity", temp);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return activityList.size();
    }
}
