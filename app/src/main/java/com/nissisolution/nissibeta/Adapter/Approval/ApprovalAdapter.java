package com.nissisolution.nissibeta.Adapter.Approval;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Classes.Approval;
import com.nissisolution.nissibeta.Classes.Department;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalHolder> {

    private Context context;
    private List<Approval> approval_list;
    private List<Department> department_list;

    public ApprovalAdapter(Context context, List<Approval> approval_list, List<Department> department_list) {
        this.context = context;
        this.approval_list = approval_list;
        this.department_list = department_list;
    }

    @NonNull
    @Override
    public ApprovalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_approval, parent, false);
        return new ApprovalHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull ApprovalHolder holder, int position) {
        Approval approval = approval_list.get(position);

        int index = position + 1;
        if (index < 10) {
            holder.position.setText("00" + index + ".");
        } else if (index < 100) {
            holder.position.setText("0" + index + ".");
        } else {
            holder.position.setText(index + ".");
        }
        if (approval.status == 0) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
            holder.status.setText("Request On-process");
        } else if (approval.status == 1) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_approved));
            holder.status.setText("Request Approved on " + get_date(approval.timestamp));
        } else if (approval.status == 2) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_rejected));
            holder.status.setText("Request Rejected on " + get_date(approval.timestamp));
        }
        for (int i = 0; i < department_list.size(); i++) {
            if (department_list.get(i).staff_id == approval.staff_id) {
                holder.approver.setText(department_list.get(i).manager_name);
                break;
            }
        }
        StringBuilder ap_position = new StringBuilder(Constants.KEY_NULL);
        int a = 0;
        for (int i = 0; i < department_list.size(); i++) {
            if (department_list.get(i).staff_id == approval.staff_id) {
                if (ap_position.toString().equals(Constants.KEY_NULL)) {
                    ap_position = new StringBuilder(department_list.get(i).position);
                } else {
                    if (a != 1) {
                        ap_position.append(", ").append(department_list.get(i).position);
                    } else {
                        if (!department_list.get(i).position.equals(Constants.KEY_HUMAN_RESOURCES)) {
                            ap_position.append(", ").append(department_list.get(i).position);
                        }
                    }
                }
                if (department_list.get(i).position.equals(Constants.KEY_HUMAN_RESOURCES_MANAGER)) {
                    a = 1;
                }
            }
        }
        holder.ap_position.setText(ap_position.toString());
    }

    @Override
    public int getItemCount() {
        return approval_list.size();
    }

    @SuppressLint("SimpleDateFormat")
    private String get_date(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        return format.format(new Date(timestamp));
    }

}
