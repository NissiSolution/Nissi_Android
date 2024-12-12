package com.nissisolution.nissibeta.Adapter.Approve;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Adapter.AdminLeave.AdminLeaveHolder;
import com.nissisolution.nissibeta.Classes.AdminLeave;
import com.nissisolution.nissibeta.R;

import java.util.Arrays;
import java.util.List;

public class ApproveLeaveAdapter extends RecyclerView.Adapter<AdminLeaveHolder> {

    public Context context;
    public List<AdminLeave> admin_leave_list;
    public Button selection;
    public boolean isSelected;

    public ApproveLeaveAdapter(Context context, List<AdminLeave> admin_leave_list,
                               Button selection, boolean isSelected) {
        this.context = context;
        this.admin_leave_list = admin_leave_list;
        this.selection = selection;
        this.isSelected = isSelected;
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
            holder.status.setText("Request On-process");
            if (admin_leave.isChecked) {
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_selected));
            } else {
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
            }
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
        if (admin_leave.start_date.equals(admin_leave.end_date)) {
            holder.period.setText("On " + get_date(list_1.get(0)));
            holder.duration.setText(admin_leave.duration + " day");
        } else {
            holder.period.setText("From " + get_date(list_1.get(0)) + " to " + get_date(list_2.get(0)));
            holder.duration.setText(admin_leave.duration + " days");
        }

        holder.itemView.setOnClickListener(view -> {
            if (admin_leave.status == 0) {
                if (admin_leave.isChecked) {
                    admin_leave.isChecked = false;
                    holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
                } else {
                    admin_leave.isChecked = true;
                    holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_selected));
                }
                process();
            }
        });

    }

    private void process() {
        boolean ch = true;
        for (AdminLeave h: admin_leave_list) {
            if (!h.isChecked) {
                ch = false;
                break;
            }
        }

        if (ch) {
            selection.setText(context.getResources().getText(R.string.de_select_all));
            isSelected = false;
        } else {
            selection.setText(context.getResources().getText(R.string.select_all));
            isSelected = true;
        }
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
