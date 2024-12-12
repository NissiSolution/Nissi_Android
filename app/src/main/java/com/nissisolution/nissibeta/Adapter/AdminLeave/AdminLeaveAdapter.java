package com.nissisolution.nissibeta.Adapter.AdminLeave;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Activity.Leave.AdminUniqueLeaveActivity;
import com.nissisolution.nissibeta.Activity.Leave.MassApproveLeaveActivity;
import com.nissisolution.nissibeta.Classes.AdminLeave;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.util.Arrays;
import java.util.List;

public class AdminLeaveAdapter extends RecyclerView.Adapter<AdminLeaveHolder> {

    public Context context;
    public List<AdminLeave> admin_leave_list;
    public String selected_month, selected_year;
    public boolean isChecked;

    public AdminLeaveAdapter(Context context, List<AdminLeave> admin_leave_list,
                             String selected_month, String selected_year, boolean isChecked) {
        this.context = context;
        this.admin_leave_list = admin_leave_list;
        this.selected_month = selected_month;
        this.selected_year = selected_year;
        this.isChecked = isChecked;
    }

    @NonNull
    @Override
    public AdminLeaveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_admin_leave, parent, false);
        return new AdminLeaveHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull AdminLeaveHolder holder, int position) {
        AdminLeave admin_leave = admin_leave_list.get(position);
        int index = position + 1;
        if (index < 10) {
            holder.position.setText("00" + index + ".");
        } else if (index < 100) {
            holder.position.setText("0" + index + ".");
        } else {
            holder.position.setText(index + ".");
        }
        if (admin_leave.status == 0) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
            holder.status.setText("Request On-process");
            holder.itemView.setOnLongClickListener(v -> {
                isChecked = false;
                Intent intent = new Intent(context, MassApproveLeaveActivity.class);
                intent.putExtra(Constants.KEY_MONTH, selected_month);
                intent.putExtra(Constants.KEY_YEAR, selected_year);
                context.startActivity(intent);
                return false;
            });
        } else if (admin_leave.status == 1) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_approved));
            holder.status.setText("Request Approved");
        } else if (admin_leave.status == 2) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_rejected));
            holder.status.setText("Request Rejected");
        }

        holder.staff_name.setText(admin_leave.staff_name);
        holder.leave_type.setText(admin_leave.leave_type);

        List<String> list_1 = Arrays.asList(admin_leave.start_date.split(" "));
        List<String> list_2 = Arrays.asList(admin_leave.end_date.split(" "));
        List<String> list3 = Arrays.asList(String.valueOf(admin_leave.duration).split("\\."));
        if (admin_leave.start_date.equals(admin_leave.end_date)) {
            holder.period.setText("On " + get_date(list_1.get(0)));
            if (list3.get(1).equals(Constants.KEY_0)) {
                holder.duration.setText(list3.get(0) + " day");
            } else {
                holder.duration.setText(admin_leave.duration + " day");
            }
        } else {
            holder.period.setText("From " + get_date(list_1.get(0)) + " to " + get_date(list_2.get(0)));
            if (list3.get(1).equals(Constants.KEY_0)) {
                holder.duration.setText(list3.get(0) + " days");
            } else {
                holder.duration.setText(admin_leave.duration + " days");
            }
        }

        holder.itemView.setOnClickListener(view -> {
            if (isChecked) {
                Intent intent = new Intent(context, AdminUniqueLeaveActivity.class);
                intent.putExtra(Constants.KEY_DATA, admin_leave.the_data);
                intent.putExtra(Constants.KEY_REQUEST_TYPE, admin_leave.leave_type);
                context.startActivity(intent);
            }
        });

    }

    private String get_date(String the_date) {
        List<String> list_1 = Arrays.asList(the_date.split("-"));
        return list_1.get(2) + "-" + list_1.get(1) + "-" + list_1.get(0);
    }

    @Override
    public int getItemCount() {
        return admin_leave_list.size();
    }

}
