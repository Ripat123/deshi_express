package com.sbitbd.deshi_express.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.home_model;

import java.util.ArrayList;
import java.util.List;

public class single_adapter extends RecyclerView.Adapter<single_adapter.userHolder>{

    private List<home_model> userList;
    private Context context;

    public single_adapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item,null);
        userHolder userHolder =new userHolder(inflate);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull userHolder holder, int position) {
        home_model user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public void addUser(home_model user){
        try {
            userList.add(user);
            //notifyDataSetChanged();
            int position = userList.indexOf(user);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }
    public void Clear(){
        try {
            userList.clear();
            notifyDataSetChanged();
        }catch (Exception e){
        }
    }

    class userHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public userHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView3);
        }
        public void bind(home_model home_model){
            try {
                textView.setText(home_model.getName());
            }catch (Exception e){
            }
        }

    }
}
