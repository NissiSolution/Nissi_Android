package com.nissisolution.nissibeta.Adapter.Missed_Punch;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Activity.MissedPunch.MyUniqueMissedPunchActivity;
import com.nissisolution.nissibeta.Classes.MissedPunch;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissedPunchAdapter extends RecyclerView.Adapter<MissedPunchHolder> {

    private Context context;
    private List<MissedPunch> missed_punch_list;
    private ProgressBar progressBar;

    public MissedPunchAdapter(Context context, List<MissedPunch> missed_punch_list, ProgressBar progressBar) {
        this.context = context;
        this.missed_punch_list = missed_punch_list;
        this.progressBar = progressBar;
    }

    @NonNull
    @Override
    public MissedPunchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_my_missed_punch, parent, false);
        return new MissedPunchHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull MissedPunchHolder holder, int position) {
        MissedPunch missed_punch = missed_punch_list.get(position);
        int index = position + 1;
        if (index < 10) {
            holder.position.setText("00" + index + ".");
        } else if (index < 100) {
            holder.position.setText("0" + index + ".");
        } else {
            holder.position.setText(index + ".");
        }
        if (missed_punch.status == 0) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
            holder.status.setText("Request On-process");
            holder.delete.setOnClickListener(view -> {
                progressBar.setVisibility(View.VISIBLE);
                StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                        response -> {
                            if (response.equals(Constants.KEY_SUCCESS)) {
                                progressBar.setVisibility(View.GONE);
                                missed_punch_list.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, Constants.KEY_SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            Toast.makeText(context, Constants.KEY_SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                ) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_DELETE_DATA);
                        params.put(Constants.KEY_ID, String.valueOf(missed_punch.id));
                        params.put(Constants.KEY_TYPE, Constants.KEY_0);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request);
            });
            holder.delete.setVisibility(View.VISIBLE);
        } else if (missed_punch.status == 1) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_approved));
            holder.status.setText("Request Approved");
            holder.delete.setVisibility(View.GONE);
        } else if (missed_punch.status == 2) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_rejected));
            holder.status.setText("Request Rejected");
            holder.delete.setVisibility(View.GONE);
        }

        holder.date.setText(missed_punch.date);
        holder.working_hours.setText(missed_punch.worked_hour);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MyUniqueMissedPunchActivity.class);
            intent.putExtra(Constants.KEY_DATA, missed_punch.the_data);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return missed_punch_list.size();
    }
}
