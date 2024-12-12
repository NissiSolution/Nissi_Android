package com.nissisolution.nissibeta.Adapter.AdminMissedPunch;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class AdminMissedPunchHolder extends RecyclerView.ViewHolder {

    public TextView position, staff_name, date, worked_hour, status;

    public AdminMissedPunchHolder(@NonNull View itemView) {
        super(itemView);

        position = itemView.findViewById(R.id.position);
        staff_name = itemView.findViewById(R.id.staff_name);
        date = itemView.findViewById(R.id.date);
        worked_hour = itemView.findViewById(R.id.working_hours);
        status = itemView.findViewById(R.id.status);

    }
}
