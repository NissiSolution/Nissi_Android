package com.nissisolution.nissibeta.Adapter.AdminCompOffHoliday;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class AdminCompOffHolidayHolder extends RecyclerView.ViewHolder {

    public TextView position, staff_name, date, request_type, status;
    public AdminCompOffHolidayHolder(@NonNull View itemView) {
        super(itemView);

        position = itemView.findViewById(R.id.position);
        staff_name = itemView.findViewById(R.id.staff_name);
        date = itemView.findViewById(R.id.date);
        request_type = itemView.findViewById(R.id.request_type);
        status = itemView.findViewById(R.id.status);

    }
}
