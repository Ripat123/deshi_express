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
import com.sbitbd.deshi_express.balance_details.balance_details;
import com.sbitbd.deshi_express.model.return_model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class pay_adapter extends RecyclerView.Adapter<pay_adapter.viewholder> {

    Context context;
    List<return_model> models;

    public pay_adapter(Context context) {
        this.context = context;
        this.models = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_card,null);
        viewholder userHolder =new viewholder(inflate);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewholder holder, int position) {
        return_model user = models.get(position);
        holder.bind(user);
    }

    public void addUser(return_model user){
        try {
            models.add(user);
            //notifyDataSetChanged();
            int position = models.indexOf(user);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        TextView return_date,invoice_id,customer_name;
        MaterialCardView cardView;
        final View view;
        final Context context1;

        public viewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            return_date = itemView.findViewById(R.id.return_date);
            invoice_id = itemView.findViewById(R.id.inv_id);
            customer_name = itemView.findViewById(R.id.cus_name);
            cardView = itemView.findViewById(R.id.pay_card);
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
//                        if (check == 1){
//
//                        }
                        Intent intent = new Intent(context1, balance_details.class);
                        intent.putExtra("id",return_model.getInvoice());
                        context1.startActivity(intent);
                    }
                });
            }catch (Exception e){
            }
        }
    }
}
