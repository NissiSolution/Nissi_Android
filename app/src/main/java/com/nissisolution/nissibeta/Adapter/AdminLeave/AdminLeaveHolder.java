package com.nissisolution.nissibeta.Adapter.AdminLeave;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class AdminLeaveHolder extends RecyclerView.ViewHolder {

    public TextView position, staff_name, leave_type, period, duration, status;

    public AdminLeaveHolder(@NonNull View itemView) {
        super(itemView);

        position = itemView.findViewById(R.id.position);
        staff_name = itemView.findViewById(R.id.staff_name);
        leave_type = itemView.findViewById(R.id.leave_type);
        period = itemView.findViewById(R.id.period);
        duration = itemView.findViewById(R.id.duration);
        status = itemView.findViewById(R.id.status);

    }
}
