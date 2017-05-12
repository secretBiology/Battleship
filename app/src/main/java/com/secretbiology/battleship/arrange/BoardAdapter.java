package com.secretbiology.battleship.arrange;

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

class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardView> implements GameConstants {

    private List<BoardItem> itemList;
    private OnItemClick click;

    BoardAdapter(List<BoardItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public BoardView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardView(LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final BoardView holder, int position) {

        holder.icon.setImageResource(resourceID(position));

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

    void setOnItemClick(OnItemClick c) {
        click = c;
    }

    interface OnItemClick {
        void selected(int position);
    }

    private int resourceID(int pos) {

        switch (itemList.get(pos).getType()) {
            case CELL_EMPTY:
                return R.drawable.icon_empty;
            case CELL_SHIP:
                return R.drawable.icon_ship;
            default:
                return R.drawable.icon_empty;
        }

    }
}
