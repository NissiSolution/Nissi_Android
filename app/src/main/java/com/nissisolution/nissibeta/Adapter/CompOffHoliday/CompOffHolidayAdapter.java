package com.nissisolution.nissibeta.Adapter.CompOffHoliday;

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
import com.nissisolution.nissibeta.Activity.CompOffHoliday.MyUniqueCompOffHolidayActivity;
import com.nissisolution.nissibeta.Classes.CompOffHoliday;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompOffHolidayAdapter extends RecyclerView.Adapter<CompOffHolidayHolder> {

        private Context context;
        private List<CompOffHoliday> comp_off_holidays_list;
        private ProgressBar progressBar;

        public CompOffHolidayAdapter(Context context, List<CompOffHoliday> comp_off_holidays_list, ProgressBar progressBar) {
            this.context = context;
            this.comp_off_holidays_list = comp_off_holidays_list;
            this.progressBar = progressBar;
        }

        @NonNull
        @Override
        public CompOffHolidayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_my_comp_off_holiday, parent, false);
            return new CompOffHolidayHolder(view);
        }

        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
        @Override
        public void onBindViewHolder(@NonNull CompOffHolidayHolder holder, int position) {
            CompOffHoliday comp_off_holiday = comp_off_holidays_list.get(position);
            int index = position + 1;
            if (index < 10) {
                holder.position.setText("00" + index + ".");
            } else if (index < 100) {
                holder.position.setText("0" + index + ".");
            } else {
                holder.position.setText(index + ".");
            }
            if (comp_off_holiday.status == 0) {
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request));
                holder.status.setText("Request On-process");
                holder.delete.setOnClickListener(view -> {
                    progressBar.setVisibility(View.VISIBLE);
                    StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                            response -> {
                                if (response.equals(Constants.KEY_SUCCESS)) {
                                    progressBar.setVisibility(View.GONE);
                                    comp_off_holidays_list.remove(holder.getAdapterPosition());
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
                            params.put(Constants.KEY_ID, String.valueOf(comp_off_holiday.id));
                            params.put(Constants.KEY_TYPE, Constants.KEY_0);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(request);
                });
                holder.delete.setVisibility(View.VISIBLE);
            } else if (comp_off_holiday.status == 1) {
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_approved));
                holder.status.setText("Request Approved");
                holder.delete.setVisibility(View.GONE);
            } else if (comp_off_holiday.status == 2) {
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_leave_request_rejected));
                holder.status.setText("Request Rejected");
                holder.delete.setVisibility(View.GONE);
            }

            holder.date.setText(comp_off_holiday.date);

            switch (comp_off_holiday.request_type) {
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
                    holder.request_type.setText(comp_off_holiday.request_type);
                    break;
            }

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, MyUniqueCompOffHolidayActivity.class);
                intent.putExtra(Constants.KEY_DATA, comp_off_holiday.the_data);
                intent.putExtra(Constants.KEY_REQUEST_TYPE, comp_off_holiday.request_type);
                context.startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return comp_off_holidays_list.size();
        }

}
