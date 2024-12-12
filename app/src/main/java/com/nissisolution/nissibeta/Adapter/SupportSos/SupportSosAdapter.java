package com.nissisolution.nissibeta.Adapter.SupportSos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Classes.SupportSos;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Activity.SosSupport.FullMessageActivity;
import com.nissisolution.nissibeta.R;

import java.util.List;

public class SupportSosAdapter extends RecyclerView.Adapter<SupportSosHolder> {

    public String title;
    public Context context;
    public List<SupportSos> support_sos;

    public SupportSosAdapter(String title, Context context, List<SupportSos> support_sos) {
        this.title = title;
        this.context = context;
        this.support_sos = support_sos;
    }

    @NonNull
    @Override
    public SupportSosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_support_sos, parent, false);
        return new SupportSosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupportSosHolder holder, int position) {
        SupportSos sos = support_sos.get(position);
        holder.title.setText(sos.title);
        holder.message.setText("     " + sos.content);
        holder.author.setText(sos.author);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, FullMessageActivity.class);
            intent.putExtra(Constants.KEY_TOOLBAR, title);
            intent.putExtra(Constants.KEY_TITLE, sos.title);
            intent.putExtra(Constants.KEY_AUTHOR, sos.author);
            intent.putExtra(Constants.KEY_MESSAGE, sos.content);
            intent.putExtra(Constants.KEY_TIMESTAMP, sos.timestamp);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return support_sos.size();
    }
}
