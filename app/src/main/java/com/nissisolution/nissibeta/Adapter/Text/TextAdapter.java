package com.nissisolution.nissibeta.Adapter.Text;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.util.Arrays;
import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextHolder> {

    public Context context;
    public List<String> text_list;

    public TextAdapter(Context context, List<String> text_list) {
        this.context = context;
        this.text_list = text_list;
    }

    @NonNull
    @Override
    public TextHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_remaining_leave, parent, false);
        return new TextHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextHolder holder, int position) {
        List<String> list_1 = Arrays.asList(text_list.get(position).split(Constants.KEY_SPLITTER_2));
        int index = position;
        String pos;
        if (index < 10) {
            pos = "0" + (index+1) + ".";
        } else {
            pos = (index+1) + ".";
        }
        holder.position.setText(pos);
        holder.leave_type.setText(list_1.get(0));
        holder.remaining_days.setText(list_1.get(2));
        holder.total_days.setText(list_1.get(1));
    }

    @Override
    public int getItemCount() {
        return text_list.size();
    }

}
