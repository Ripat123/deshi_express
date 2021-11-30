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
import com.sbitbd.deshi_express.model.six_model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class shipment_adapter extends RecyclerView.Adapter<shipment_adapter.viewHolder>{

    private List<six_model> userList;
    private Context context;

    public shipment_adapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipment_lay, null);
        viewHolder userHolder = new viewHolder(inflate);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        six_model user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void Clear() {
        userList.clear();
        notifyDataSetChanged();
    }

    public void addUser(six_model user) {
        try {
            userList.add(user);
            //notifyDataSetChanged();
            int position = userList.indexOf(user);
            notifyItemInserted(position);
        } catch (Exception e) {
        }
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView id,date,customer,branch,charge,amount;
        final Context context;
        MaterialCardView ship_card;

        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            date = itemView.findViewById(R.id.date_t);
            customer = itemView.findViewById(R.id.customer_name);
            branch = itemView.findViewById(R.id.brance);
            charge = itemView.findViewById(R.id.cod_t);
            amount = itemView.findViewById(R.id.total_t);
            ship_card = itemView.findViewById(R.id.ship_card);
            context = itemView.getContext();
        }
        public void bind(six_model six_model){
            try {
                id.setText(six_model.getOne());
                date.setText(six_model.getTwo());
                customer.setText(six_model.getThree());
                branch.setText(six_model.getFour());
                charge.setText(six_model.getFive()+" TK");
                amount.setText(six_model.getSix()+" TK");
                ship_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context,details_one.class);
                        intent.putExtra("id",six_model.getOne());
                        context.startActivity(intent);
                    }
                });
            }catch (Exception e){
            }
        }
    }
}
