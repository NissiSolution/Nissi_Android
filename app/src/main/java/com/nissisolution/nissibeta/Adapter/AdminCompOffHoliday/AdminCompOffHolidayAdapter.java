package com.nissisolution.nissibeta.Adapter.AdminCompOffHoliday;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Activity.CompOffHoliday.AdminUniqueCompOffHolidayActivity;
import com.nissisolution.nissibeta.Activity.CompOffHoliday.MassApproveCompOffActivity;
import com.nissisolution.nissibeta.Activity.NightShift.MassApproveNightShiftActivity;
import com.nissisolution.nissibeta.Classes.AdminCompOffHoliday;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.util.List;

public class AdminCompOffHolidayAdapter extends RecyclerView.Adapter<AdminCompOffHolidayHolder> {

    public Context context;
    public List<AdminCompOffHoliday> admin_comp_off_holiday_list;
    public boolean isChecked;
    public String selected_month, selected_year;

    public AdminCompOffHolidayAdapter(Context context, List<AdminCompOffHoliday> admin_comp_off_holiday_list,
                                      boolean isChecked, String selected_month, String selected_year) {
        this.context = context;
        this.admin_comp_off_holiday_list = admin_comp_off_holiday_list;
        this.isChecked = isChecked;
        this.selected_month = selected_month;
        this.selected_year = selected_year;
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
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
            holder.itemView.setOnLongClickListener(v -> {
                isChecked = false;
                if (admin_comp_off_holiday.request_type.equals(Constants.KEY_3)) {
                    Intent intent = new Intent(context, MassApproveNightShiftActivity.class);
                    intent.putExtra(Constants.KEY_MONTH, selected_month);
                    intent.putExtra(Constants.KEY_YEAR, selected_year);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, MassApproveCompOffActivity.class);
                    intent.putExtra(Constants.KEY_MONTH, selected_month);
                    intent.putExtra(Constants.KEY_YEAR, selected_year);
                    context.startActivity(intent);
                }
                return false;
            });
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
            if (isChecked) {
                Intent intent = new Intent(context, AdminUniqueCompOffHolidayActivity.class);
                intent.putExtra(Constants.KEY_DATA, admin_comp_off_holiday.the_data);
                intent.putExtra(Constants.KEY_REQUEST_TYPE, admin_comp_off_holiday.request_type);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return admin_comp_off_holiday_list.size();
    }
}
