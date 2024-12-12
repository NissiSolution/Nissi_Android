package com.nissisolution.nissibeta.Adapter.Leave;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Activity.Leave.MyUniqueLeaveActivity;
import com.nissisolution.nissibeta.Classes.Leave;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveHolder> {

    public Context context;
    public List<Leave> leaveList;
    public ProgressBar progressBar;

    public LeaveAdapter(Context context, List<Leave> leaveList, ProgressBar progressBar) {
        this.context = context;
        this.leaveList = leaveList;
        this.progressBar = progressBar;
    }

    @NonNull
    @Override
    public LeaveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_my_leave, parent, false);
        return new LeaveHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull LeaveHolder holder, int position) {
        Leave leave = leaveList.get(position);
        int index = position + 1;
        if (index < 10) {
            holder.position.setText("00" + index + ".");
        } else if (index < 100) {
            holder.position.setText("0" + index + ".");
        } else {
            holder.position.setText(index + ".");
        }

        if (leave.status == 0) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
            holder.status.setText("Request On-process");
            holder.delete.setOnClickListener(view -> {
                progressBar.setVisibility(View.VISIBLE);
                StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                        response -> {
                            if (response.equals(Constants.KEY_SUCCESS)) {
                                progressBar.setVisibility(View.GONE);
                                leaveList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, Constants.KEY_SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, Constants.KEY_SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                ) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_DELETE_DATA);
                        params.put(Constants.KEY_ID, String.valueOf(leave.id));
                        params.put(Constants.KEY_TYPE, Constants.KEY_1);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request);
            });
            holder.delete.setVisibility(View.VISIBLE);
        } else if (leave.status == 1) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_approved));
            holder.status.setText("Request Approved");
            holder.delete.setVisibility(View.GONE);
        } else if (leave.status == 2) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_rejected));
            holder.status.setText("Request Rejected");
            holder.delete.setVisibility(View.GONE);
        }

        List<String> list1 = Arrays.asList(String.valueOf(leave.duration).split("\\."));
        if (leave.duration <= 1) {
            if (list1.get(1).equals(Constants.KEY_0)) {
                holder.duration.setText(list1.get(0) + " day");
            } else {
                holder.duration.setText(leave.duration + " day");
            }
        } else {
            if (list1.get(1).equals(Constants.KEY_0)) {
                holder.duration.setText(list1.get(0) + " days");
            } else {
                holder.duration.setText(leave.duration + " days");
            }
        }

        holder.leave_type.setText(leave.type);
        List<String> list_1 = Arrays.asList(leave.start_date.split(" "));
        List<String> list_2 = Arrays.asList(leave.end_date.split(" "));
        if (leave.start_date.equals(leave.end_date)) {
            holder.period.setText("On " + get_date(list_1.get(0)));
        } else {
            holder.period.setText("From " + get_date(list_1.get(0)) + " to " + get_date(list_2.get(0)));
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MyUniqueLeaveActivity.class);
            intent.putExtra(Constants.KEY_DATA, leave.the_data);
            intent.putExtra(Constants.KEY_REQUEST_TYPE, leave.type);
            context.startActivity(intent);
        });

    }

    private String get_date(String the_date) {
        List<String> list_1 = Arrays.asList(the_date.split("-"));
        return list_1.get(2) + "-" + list_1.get(1) + "-" + list_1.get(0);
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }
}
