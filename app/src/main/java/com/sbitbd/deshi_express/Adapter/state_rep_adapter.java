package com.sbitbd.deshi_express.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.state_rep_model;

import java.util.ArrayList;
import java.util.List;

public class state_rep_adapter extends RecyclerView.Adapter<state_rep_adapter.viewHolder>{

    private List<state_rep_model> userList;
    private Context context;

    public state_rep_adapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card,null);
        viewHolder userHolder =new viewHolder(inflate);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        state_rep_model user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void addUser(state_rep_model user){
        try {
            userList.add(user);
            //notifyDataSetChanged();
            int position = userList.indexOf(user);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }
    public void updateUser(state_rep_model pro_model){
        try {
            int position = getPosition(pro_model);
            if(position!=-1){
                userList.set(position,pro_model);
                notifyItemChanged(position);
            }
        }catch (Exception e){
        }
    }

    public int getPosition(state_rep_model pro_model){
        try {
            for (state_rep_model x:userList){
                if(x.getTitle().equals(pro_model.getTitle())){
                    return userList.indexOf(x);
                }
            }
        }catch (Exception e){
        }
        return -1;
    }

    public void Clear(){
        try {
            userList.clear();
            notifyDataSetChanged();
        }catch (Exception e){
        }
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        MaterialTextView title,count;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.r_card_img);
            title = itemView.findViewById(R.id.r_card_text);
            count = itemView.findViewById(R.id.r_card_count);
        }

        public void bind(state_rep_model state_rep_model){
            imageView.setImageResource(state_rep_model.getImg());
            title.setText(state_rep_model.getTitle());
            count.setText(state_rep_model.getCount());
        }
    }
}
