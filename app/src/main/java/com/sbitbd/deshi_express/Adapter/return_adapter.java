package com.sbitbd.deshi_express.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.details.details_one;
import com.sbitbd.deshi_express.model.return_model;

import java.util.ArrayList;
import java.util.List;

public class return_adapter extends RecyclerView.Adapter<return_adapter.viewHolder>{
    private List<return_model> userList;
    private Context context;
    private int check;

    public return_adapter(Context context,int check) {
        this.context = context;
        this.userList = new ArrayList<>();
        this.check = check;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.return_card,null);
        viewHolder userHolder =new viewHolder(inflate);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        return_model user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void addUser(return_model user){
        try {
            userList.add(user);
            //notifyDataSetChanged();
            int position = userList.indexOf(user);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView return_date,invoice_id,customer_name;
        MaterialCardView cardView;
        final View view;
        final Context context1;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            return_date = itemView.findViewById(R.id.return_date);
            invoice_id = itemView.findViewById(R.id.inv_id);
            customer_name = itemView.findViewById(R.id.cus_name);
            cardView = itemView.findViewById(R.id.return_card);
            view = itemView;
            context1 = itemView.getContext();
        }
        public void bind(return_model return_model){
            try {
                return_date.setText(return_model.getReturn_date());
                invoice_id.setText(return_model.getInvoice());
                customer_name.setText(return_model.getCustomer_name());
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context1.startActivity(new Intent(context1, details_one.class));
//                        if (check == 1){
//
//                        }
                    }
                });
            }catch (Exception e){
            }
        }
    }
}
