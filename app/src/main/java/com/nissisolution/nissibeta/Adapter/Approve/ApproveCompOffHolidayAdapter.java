package com.nissisolution.nissibeta.Adapter.Approve;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Adapter.AdminCompOffHoliday.AdminCompOffHolidayHolder;
import com.nissisolution.nissibeta.Classes.AdminCompOffHoliday;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.util.List;

public class ApproveCompOffHolidayAdapter extends RecyclerView.Adapter<AdminCompOffHolidayHolder> {

    public Context context;
    public List<AdminCompOffHoliday> admin_comp_off_holiday_list;
    public Button selection;
    public boolean isSelected;

    public ApproveCompOffHolidayAdapter(Context context, List<AdminCompOffHoliday> admin_comp_off_holiday_list,
                                        Button selection, boolean isSelected) {
        this.context = context;
        this.admin_comp_off_holiday_list = admin_comp_off_holiday_list;
        this.selection = selection;
        this.isSelected = isSelected;
    }

    @NonNull
    @Override
    public AdminCompOffHolidayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_admin_comp_off_holiday, parent, false);
        return new AdminCompOffHolidayHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull AdminCompOffHolidayHolder holder, int position) {
        AdminCompOffHoliday admin_comp_off_holiday = admin_comp_off_holiday_list.get(position);
        int index = position + 1;
        if (index < 10) {
            holder.position.setText("00" + index + ".");
        } else if (index < 100) {
            holder.position.setText("0" + index + ".");
        } else {
            holder.position.setText(index + ".");
        }
        if (admin_comp_off_holiday.status == 0) {
            holder.status.setText("Request On-process");
            if (admin_comp_off_holiday.isChecked) {
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_selected));
            } else {
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
            }
        } else if (admin_comp_off_holiday.status == 1) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_approved));
            holder.status.setText("Request Approved");
        } else if (admin_comp_off_holiday.status == 2) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_rejected));
            holder.status.setText("Request Rejected");
        }

        holder.date.setText(admin_comp_off_holiday.date);

        switch (admin_comp_off_holiday.request_type) {
            case Constants.KEY_1:
                holder.request_type.setText(Constants.KEY_COMP_OFF);
                break;
            case Constants.KEY_2:
                holder.request_type.setText(Constants.KEY_HOLIDAY);
                break;
            case Constants.KEY_3:
                holder.request_type.setText(Constants.KEY_NIGHT_SHIFTS);
                break;
            default:
                holder.request_type.setText(admin_comp_off_holiday.request_type);
                break;
        }

        holder.staff_name.setText(admin_comp_off_holiday.staff_name);

        holder.itemView.setOnClickListener(view -> {
            if (admin_comp_off_holiday.status == 0) {
                if (admin_comp_off_holiday.isChecked) {
                    admin_comp_off_holiday.isChecked = false;
                    holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
                } else {
                    admin_comp_off_holiday.isChecked = true;
                    holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_selected));
                }
                process();
            }
        });
    }

    @Override
    public int getItemCount() {
        return admin_comp_off_holiday_list.size();
    }

    private void process() {
        boolean ch = true;
        for (AdminCompOffHoliday h: admin_comp_off_holiday_list) {
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

}
