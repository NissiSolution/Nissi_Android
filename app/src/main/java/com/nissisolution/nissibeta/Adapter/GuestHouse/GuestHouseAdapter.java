package com.nissisolution.nissibeta.Adapter.GuestHouse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Activity.GuestHouse.GuestHouseDetailsActivity;
import com.nissisolution.nissibeta.Classes.GuestHouse;
import com.nissisolution.nissibeta.Classes.SingleGuestHouse;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GuestHouseAdapter extends RecyclerView.Adapter<GuestHouseHolder> {

    public Context context;
    public List<GuestHouse> guest_house;

    public GuestHouseAdapter(Context context, List<GuestHouse> guest_house) {
        this.context = context;
        this.guest_house = guest_house;
    }

    @NonNull
    @Override
    public GuestHouseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_guest_house, parent, false);
        return new GuestHouseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestHouseHolder holder, int position) {
        GuestHouse house = guest_house.get(position);
        String l_link, r_link;
        SingleGuestHouse left_guest_house = house.left_guest_house;
        SingleGuestHouse right_guest_house = house.right_guest_house;
        Picasso.get().load(Uri.parse(left_guest_house.image_url)).into(holder.l_image);
        holder.l_text.setText(left_guest_house.name);
        l_link = left_guest_house.map_url;
        holder.l_image.setOnClickListener(view -> openUrl(left_guest_house));
        holder.l_text.setOnClickListener(view -> openUrl(left_guest_house));

        if (house.count == 2) {
            Picasso.get().load(Uri.parse(right_guest_house.image_url)).into(holder.r_image);
            holder.r_text.setText(right_guest_house.name);
            r_link = right_guest_house.map_url;
            holder.r_image.setOnClickListener(view -> openUrl(right_guest_house));
            holder.r_text.setOnClickListener(view -> openUrl(right_guest_house));
            holder.r_image.setVisibility(View.VISIBLE);
            holder.r_text.setVisibility(View.VISIBLE);
        } else {
            holder.r_image.setVisibility(View.GONE);
            holder.r_text.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return guest_house.size();
    }

    private void openUrl(SingleGuestHouse guestHouse){
        Intent intent = new Intent(context, GuestHouseDetailsActivity.class);
        intent.putExtra(Constants.KEY_ID, guestHouse.id);
        intent.putExtra(Constants.KEY_POSITION, guestHouse.position);
        intent.putExtra(Constants.KEY_NAME, guestHouse.name);
        intent.putExtra(Constants.KEY_MAP_URL, guestHouse.map_url);
        intent.putExtra(Constants.KEY_IMAGE_URL, guestHouse.image_url);
        context.startActivity(intent);
    }
}
