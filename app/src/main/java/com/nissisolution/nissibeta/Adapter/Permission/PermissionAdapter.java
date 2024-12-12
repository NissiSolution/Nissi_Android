package com.nissisolution.nissibeta.Adapter.Permission;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Adapter.GuestHouse.GuestHouseHolder;
import com.nissisolution.nissibeta.Classes.PermissionData;
import com.nissisolution.nissibeta.R;

import java.util.List;

public class PermissionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<PermissionData> permissionDataList;
    private final int KEY_HEADER = 1, KEY_DATA = 2;
    private int type = 0;

    public PermissionAdapter(Context context, List<PermissionData> permissionDataList) {
        this.context = context;
        this.permissionDataList = permissionDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == KEY_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
            return new HeaderHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_permission, parent, false);
            return new DataHolder(view);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PermissionData data = permissionDataList.get(position);
        if (holder.getItemViewType() == KEY_HEADER) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.setTitle(data.tagName);
        } else if (holder.getItemViewType() == KEY_DATA) {
            DataHolder dataHolder = (DataHolder) holder;
            dataHolder.title.setText(data.tagName);
            if (type == 0) {
                dataHolder.layout.setBackground(context.getDrawable(R.drawable.background_p1));
                type = 1;
            } else {
                dataHolder.layout.setBackground(context.getDrawable(R.drawable.background_p2));
                type = 0;
            }
            dataHolder.permission.setChecked(data.isChecked);

            dataHolder.permission.setOnCheckedChangeListener((buttonView, isChecked) -> {
                data.isChecked = isChecked;
            });

        }
    }

    @Override
    public int getItemCount() {
        return permissionDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (permissionDataList.get(position).id == 0) {
            return KEY_HEADER;
        } else {
            return KEY_DATA;
        }
    }
}
