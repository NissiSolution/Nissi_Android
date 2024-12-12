package com.nissisolution.nissibeta.Adapter.Approve;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Adapter.AdminMissedPunch.AdminMissedPunchHolder;
import com.nissisolution.nissibeta.Classes.AdminMissedPunch;
import com.nissisolution.nissibeta.R;

import java.util.List;

public class ApproveMissedPunchAdapter extends RecyclerView.Adapter<AdminMissedPunchHolder> {

    public Context context;
    public List<AdminMissedPunch> admin_missed_punch_list;
    public Button selection;
    public boolean isSelected;

    public ApproveMissedPunchAdapter(Context context, List<AdminMissedPunch> admin_missed_punch_list,
                                     Button selection, boolean isSelected) {
        this.context = context;
        this.admin_missed_punch_list = admin_missed_punch_list;
        this.isSelected = isSelected;
        this.selection = selection;
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
            if (admin_missed_punch.isChecked) {
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_selected));
            } else {
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
            }
            holder.status.setText("Request On-process");
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
            if (admin_missed_punch.status == 0) {
                if (admin_missed_punch.isChecked) {
                    admin_missed_punch.isChecked = false;
                    holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
                } else {
                    admin_missed_punch.isChecked = true;
                    holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_selected));
                }
                process();
            }
        });
    }

    private void process() {
        boolean ch = true;
        for (AdminMissedPunch h: admin_missed_punch_list) {
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

    @Override
    public int getItemCount() {
        return admin_missed_punch_list.size();
    }
}
