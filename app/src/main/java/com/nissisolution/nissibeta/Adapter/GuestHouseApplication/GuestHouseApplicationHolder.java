package com.nissisolution.nissibeta.Adapter.GuestHouseApplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class GuestHouseApplicationHolder extends RecyclerView.ViewHolder {

    public TextView position, employeeName, employeeId, requestedOn, period;

    public GuestHouseApplicationHolder(@NonNull View itemView) {
        super(itemView);

        position = itemView.findViewById(R.id.position);
        employeeName = itemView.findViewById(R.id.employee_name);
        employeeId = itemView.findViewById(R.id.employee_id);
        requestedOn = itemView.findViewById(R.id.requested_on);
        period = itemView.findViewById(R.id.period);
    }
}
