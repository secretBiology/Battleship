package com.secretbiology.battleship.setup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.secretbiology.battleship.R;
import com.secretbiology.battleship.common.GameDetails;
import com.secretbiology.helpers.general.General;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameHolder> {


    private List<GameDetails> gameDetails;
    private onGameClick gameClick;

    public GameListAdapter(List<GameDetails> gameDetails) {
        this.gameDetails = gameDetails;
    }

    @Override
    public GameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final GameHolder holder, int position) {

        GameDetails details = gameDetails.get(position);
        Context context = holder.itemView.getContext();

        holder.gameID.setText(details.getGameID());
        holder.timestamp.setText(details.getCreationTimestamp());
        holder.name.setText(context.getString(R.string.created_by, details.getPlayer1().getName()));

        if (details.isFull()) {
            holder.icon.setBackground(new ColorDrawable(General.getColor(context, R.color.red)));
        }else {
            holder.icon.setBackground(new ColorDrawable(General.getColor(context, R.color.green)));
        }

        if (details.isRunning()) {
            holder.icon.setBackground(new ColorDrawable(General.getColor(context, R.color.running)));
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClick.gameSelected(holder.getLayoutPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return gameDetails.size();
    }

    class GameHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        ImageView icon;
        TextView gameID, name, timestamp;

        GameHolder(View itemView) {
            super(itemView);
            layout = ButterKnife.findById(itemView, R.id.gl_layout);
            icon = ButterKnife.findById(itemView, R.id.gl_icon);
            gameID = ButterKnife.findById(itemView, R.id.gl_gameid);
            name = ButterKnife.findById(itemView, R.id.gl_creator);
            timestamp = ButterKnife.findById(itemView, R.id.gl_timestamp);
        }
    }

    void setOnGameClick(onGameClick click) {
        this.gameClick = click;
    }

    public interface onGameClick {
        void gameSelected(int pos);
    }
}