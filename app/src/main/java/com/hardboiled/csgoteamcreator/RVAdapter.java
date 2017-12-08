package com.hardboiled.csgoteamcreator;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jonah on 12/3/17.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.UserViewHolder> {

    private List<User> users;
    //private RecyclerView recyclerView;
    private View.OnClickListener onClickListener;

    //private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    public RVAdapter(View.OnClickListener mOnClickListener, List<User> users) {
        //this.recyclerView = recyclerView;
        this.onClickListener = mOnClickListener;
        this.users = users;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView username;
        ImageView rank;
        TextView role;
        TextView eseaRank;

        public UserViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            username = (TextView)itemView.findViewById(R.id.card_username);
            rank = (ImageView) itemView.findViewById(R.id.card_mm_rank);
            role = (TextView) itemView.findViewById(R.id.card_role_value);
            eseaRank = (TextView) itemView.findViewById(R.id.card_esea_rank_value);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_card, viewGroup, false);
        v.setOnClickListener(onClickListener);
        UserViewHolder userViewHolder = new UserViewHolder(v);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int i) {
        userViewHolder.username.setText(users.get(i).getUsername());
        userViewHolder.rank.setImageResource(users.get(i).getRankId());
        userViewHolder.role.setText(users.get(i).getRole());
        userViewHolder.eseaRank.setText(users.get(i).getEseaRank());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public User getUser(int index) {
        return users.get(index);
    }

//    private class MyOnClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            int itemPosition = recyclerView.getChildLayoutPosition(view);
//            User user = users.get(itemPosition);
//            Intent profileIntent = new Intent(recyclerView.getContext(), ProfileActivity.class);
//
//        }
//    }
}
