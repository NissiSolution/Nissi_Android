package com.nissisolution.nissibeta.Adapter.Approval;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class ApprovalHolder extends RecyclerView.ViewHolder {

    public TextView position, approver, ap_position, status;

    public ApprovalHolder(@NonNull View itemView) {
        super(itemView);
        position = itemView.findViewById(R.id.position);
        approver = itemView.findViewById(R.id.approver);
        ap_position = itemView.findViewById(R.id.ap_position);
        status = itemView.findViewById(R.id.status);
    }
}
