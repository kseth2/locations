package com.bmw.location.app.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bmw.location.app.R;
import com.bmw.location.app.model.LocationData;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {

    private List<LocationData> mDataSet;
    private OnRecyclerClickListener mListener;

    public LocationsAdapter(OnRecyclerClickListener listener, RealmResults<LocationData> dataSet) {
        mListener = listener;
        mDataSet = new ArrayList<>(dataSet);
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, final int position) {
        final LocationData locationData = mDataSet.get(position);
        holder.getLocation().setText(locationData.getName());
        holder.getAddress().setText(locationData.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLocationClick(locationData.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return mDataSet.get(position).getId();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView location;
        private TextView address;

        LocationViewHolder(View view) {
            super(view);
            location = (TextView) view.findViewById(R.id.text_location);
            address = (TextView) view.findViewById(R.id.text_address);
        }

        TextView getLocation() {
            return location;
        }

        TextView getAddress() {
            return address;
        }
    }

    public interface OnRecyclerClickListener {
        void onLocationClick(long id);
    }
}
