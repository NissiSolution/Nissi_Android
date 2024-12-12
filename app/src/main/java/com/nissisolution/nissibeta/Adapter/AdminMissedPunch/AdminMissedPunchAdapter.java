package com.nissisolution.nissibeta.Adapter.AdminMissedPunch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Activity.MissedPunch.AdminUniqueMissedPunchActivity;
import com.nissisolution.nissibeta.Activity.MissedPunch.MassApproveMissedPunchActivity;
import com.nissisolution.nissibeta.Classes.AdminMissedPunch;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.util.List;

public class AdminMissedPunchAdapter extends RecyclerView.Adapter<AdminMissedPunchHolder> {

    public Context context;
    public List<AdminMissedPunch> admin_missed_punch_list;
    public boolean isChecked;
    public String selected_month, selected_year;

    public AdminMissedPunchAdapter(Context context, List<AdminMissedPunch> admin_missed_punch_list,
                                   boolean isChecked, String selected_month, String selected_year) {
        this.context = context;
        this.admin_missed_punch_list = admin_missed_punch_list;
        this.isChecked = isChecked;
        this.selected_month = selected_month;
        this.selected_year = selected_year;
    }

    @NonNull
    @Override
    public AdminMissedPunchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_admin_missed_punch, parent, false);
        return new AdminMissedPunchHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull AdminMissedPunchHolder holder, int position) {
        AdminMissedPunch admin_missed_punch = admin_missed_punch_list.get(position);
        int index = position + 1;
        if (index < 10) {
            holder.position.setText("00" + index + ".");
        } else if (index < 100) {
            holder.position.setText("0" + index + ".");
        } else {
            holder.position.setText(index + ".");
        }
        if (admin_missed_punch.status == 0) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
            holder.status.setText("Request On-process");
            holder.itemView.setOnLongClickListener(v -> {
                isChecked = false;
                Intent intent = new Intent(context, MassApproveMissedPunchActivity.class);
                intent.putExtra(Constants.KEY_MONTH, selected_month);
                intent.putExtra(Constants.KEY_YEAR, selected_year);
                context.startActivity(intent);
                return false;
            });
        } else if (admin_missed_punch.status == 1) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_approved));
            holder.status.setText("Request Approved");
        } else if (admin_missed_punch.status == 2) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_rejected));
            holder.status.setText("Request Rejected");
        }

        holder.date.setText(admin_missed_punch.date);

        holder.worked_hour.setText(admin_missed_punch.period);

        holder.staff_name.setText(admin_missed_punch.staff_name);

        holder.itemView.setOnClickListener(view -> {
            if (isChecked) {
                Intent intent = new Intent(context, AdminUniqueMissedPunchActivity.class);
                intent.putExtra(Constants.KEY_DATA, admin_missed_punch.the_data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return admin_missed_punch_list.size();
    }
}
