package com.nissisolution.nissibeta.Adapter.CheckInOut;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nissisolution.nissibeta.databinding.CheckedInLayoutBinding;
import com.nissisolution.nissibeta.databinding.CheckedOutLayoutBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class CheckInOutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> data_list;
    public static final int TYPE_CHECKED_IN = 1;
    public static final int TYPE_CHECKED_OUT = 2;


    public CheckInOutAdapter(List<String> data_list) {
        this.data_list = data_list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CHECKED_IN){
            return new Checked_inViewHolder(
                    CheckedInLayoutBinding.inflate(
                            LayoutInflater.from(parent.getContext())
                            ,parent, false
                    )
            );
        }
        else if (viewType == TYPE_CHECKED_OUT){
            return new Checked_outViewHolder(
                    CheckedOutLayoutBinding.inflate(
                            LayoutInflater.from(parent.getContext())
                            ,parent, false
                    )
            );
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_CHECKED_IN){
            ((Checked_inViewHolder) holder).setData(data_list.get(position));
        }else if (getItemViewType(position) == TYPE_CHECKED_OUT){
            ((Checked_outViewHolder) holder).setData(data_list.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    @Override
    public int getItemViewType(int position) {
        List<String> new_data = Arrays.asList(data_list.get(position).split("--"));
        if (new_data.get(1).equals(Long.toString(1))) {
            return TYPE_CHECKED_IN;
        } else {
            return TYPE_CHECKED_OUT;
        }
    }

    static class Checked_inViewHolder extends RecyclerView.ViewHolder {

        private final CheckedInLayoutBinding binding;

        Checked_inViewHolder(CheckedInLayoutBinding bindings){
            super(bindings.getRoot());
            binding = bindings;
        }

        void setData(String data){
            List<String> display_data = Arrays.asList(data.split("--"));
            binding.time.setText(changeDateFormatFromAnother(display_data.get(0)));
        }

        public String changeDateFormatFromAnother(String date){
            @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("hh:mm a");
            String resultDate = "";
            try {
                resultDate = outputFormat.format(inputFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return resultDate;
        }

    }

    static class Checked_outViewHolder extends RecyclerView.ViewHolder {

        private final CheckedOutLayoutBinding binding;

        Checked_outViewHolder(CheckedOutLayoutBinding bindings){
            super(bindings.getRoot());
            binding = bindings;
        }

        void setData(String data){
            List<String> display_data = Arrays.asList(data.split("--"));
            binding.time.setText(changeDateFormatFromAnother(display_data.get(0)));
        }

        public String changeDateFormatFromAnother(String date){
            @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("hh:mm a");
            String resultDate = "";
            try {
                resultDate = outputFormat.format(inputFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return resultDate;
        }

    }

}

