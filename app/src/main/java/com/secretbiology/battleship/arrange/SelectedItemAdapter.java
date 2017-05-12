package com.secretbiology.battleship.arrange;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.secretbiology.battleship.R;

import butterknife.ButterKnife;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

class SelectedItemAdapter extends RecyclerView.Adapter<SelectedItemAdapter.SelectedView> {

    private int size;
    private OnDirectionChange change;

    public SelectedItemAdapter(int size) {
        this.size = size;
    }

    @Override
    public SelectedView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedView(LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_ship_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedView holder, int position) {
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change.changeDirection();
            }
        });
    }

    public void setSize(int size) {
        this.size = size;
    }

    void setOnDirectionChange(OnDirectionChange directionChange) {
        change = directionChange;
    }

    @Override
    public int getItemCount() {
        return size;
    }

    class SelectedView extends RecyclerView.ViewHolder {
        ImageView icon;

        SelectedView(View itemView) {
            super(itemView);
            icon = ButterKnife.findById(itemView, R.id.selected_ship_icon);
        }
    }

    interface OnDirectionChange {
        void changeDirection();
    }

}
