package com.secretbiology.battleship.arrange;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.secretbiology.battleship.GameConstants;
import com.secretbiology.battleship.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardView> implements GameConstants {

    private List<BoardItem> itemList;
    private OnItemClick click;
    private boolean isEnemy;


    public BoardAdapter(List<BoardItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public BoardView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardView(LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final BoardView holder, int position) {
        Context context = holder.itemView.getContext();
        holder.icon.setImageResource(resourceID(position));
        if (itemList.get(position).getType() == CELL_DAMAGED_SHIP) {
            if (isEnemy) {
                holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.green));
            } else {
                holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.red));
            }
        } else {
            holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.ship_background));
        }

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.selected(holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class BoardView extends RecyclerView.ViewHolder {

        ImageView icon;

        BoardView(View itemView) {
            super(itemView);
            icon = ButterKnife.findById(itemView, R.id.board_item);
        }
    }

    public void setOnItemClick(OnItemClick c) {
        click = c;
    }

    public interface OnItemClick {
        void selected(int position);
    }

    private int resourceID(int pos) {

        switch (itemList.get(pos).getType()) {
            case CELL_EMPTY:
                return R.drawable.icon_empty;
            case CELL_SHIP:
                if (isEnemy) {
                    return R.drawable.icon_empty;
                } else {
                    return R.drawable.icon_ship;
                }
            case CELL_MISSED:
                return R.drawable.icon_missed;
            case CELL_DAMAGED_SHIP:
                return R.drawable.icon_damaged;
            default:
                return R.drawable.icon_empty;
        }
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }
}
