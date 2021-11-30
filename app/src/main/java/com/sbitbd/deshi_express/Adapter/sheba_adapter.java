package com.sbitbd.deshi_express.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.card_details.details_card;
import com.sbitbd.deshi_express.model.home_model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class sheba_adapter extends RecyclerView.Adapter<sheba_adapter.userHolder>{
    private List<home_model> userList;
    private Context context;

    public sheba_adapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_card,null);
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

    public void ClearCategory(){
        userList.clear();
        notifyDataSetChanged();
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

    class userHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name,details;
        MaterialCardView cardView;
        final View cat_view;
        private final Context context1;

        public userHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.de_img);
            name = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details_t);
            cardView = itemView.findViewById(R.id.card);
            cat_view = itemView;
            context1 = itemView.getContext();
        }

        public void bind(home_model user){
            try {
                name.setText(user.getName());
                details.setText(user.getText());

                    Picasso.get().load(config.SERVICES_IMG + user.getImage())
                            .fit().centerCrop()
//                            .placeholder(R.drawable.water)
//                            .error(R.drawable.water)
                            .into(imageView);


//                    Picasso.get().load(config.SELLER_IMG_URL + user.getImage())
//                            .fit().centerCrop()
////                            .placeholder(R.drawable.water)
////                            .error(R.drawable.water)
//                            .into(imageView);
//                int[] androidColors = context1.getResources().getIntArray(R.array.androidcolors);
//                int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
//                cardView.setCardBackgroundColor(randomAndroidColor);
//                cardView.setCardBackgroundColor(getRandomColors());
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent;
                            intent = new Intent(context1, details_card.class);
                            intent.putExtra("title", user.getName());
                            intent.putExtra("details", user.getText());
                            intent.putExtra("img", user.getImage());
                            context1.startActivity(intent);

                    }
                });
            }catch (Exception e){
            }
        }

//        private int getRandomColors(){
//            try {
//                Random random = new Random();
//
//                return Color.argb(255, random.nextInt(256), random.nextInt(256),     random.nextInt(256));
//            }catch (Exception e){
//            }
//            return 0;
//        }
    }
}
