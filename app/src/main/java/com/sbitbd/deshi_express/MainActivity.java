package com.sbitbd.deshi_express;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;
import com.sbitbd.deshi_express.Adapter.sheba_adapter;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.about.about_us;
import com.sbitbd.deshi_express.login.login;
import com.sbitbd.deshi_express.register.register;
import com.sbitbd.deshi_express.service.service_charge;
import com.sbitbd.deshi_express.suggest.suggest;
import com.sbitbd.deshi_express.terms.terms;

public class MainActivity extends AppCompatActivity {

    ImageView imageView, arrow;
    MaterialCardView cardView;
    ConstraintLayout constraintLayout;
    private Button about, service, terms, suggestions, login, reg;
    private RecyclerView sheba_rec, why_rec, blog_rec;
    private sheba_adapter sheba_adapter, why_adapter, blog_adapter;
    private main_controller main_controller = new main_controller();
    private config configer = new config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        config();
    }

    private void initView() {
        try {
            imageView = findViewById(R.id.top_img);
            arrow = findViewById(R.id.arrow);
            constraintLayout = findViewById(R.id.expend);
            cardView = findViewById(R.id.menu_id);
            about = findViewById(R.id.about_us_btn);
            service = findViewById(R.id.service_btn);
            terms = findViewById(R.id.terms_btn);
            suggestions = findViewById(R.id.suggest);
            sheba_rec = findViewById(R.id.sheba_rec);
            why_rec = findViewById(R.id.why_rec);
            blog_rec = findViewById(R.id.blog_rec);
            login = findViewById(R.id.login_btn);
            reg = findViewById(R.id.reg_btn);
        } catch (Exception e) {
        }
    }

    private void config() {
        try {
            //menu settings
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (constraintLayout.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        constraintLayout.setVisibility(View.VISIBLE);
                        arrow.setImageResource(R.drawable.ic_round_keyboard_arrow_up_24);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        constraintLayout.setVisibility(View.GONE);
                        arrow.setImageResource(R.drawable.ic_round_keyboard_arrow_down_24);
                    }
                }
            });
            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, about_us.class));
                }
            });

            service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, service_charge.class));
                }
            });
            terms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, terms.class));
                }
            });
            suggestions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, suggest.class));
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (configer.checkUser(MainActivity.this)){
                        startActivity(new Intent(MainActivity.this, Main_dashboard.class));
                    }
                    else {
                        startActivity(new Intent(MainActivity.this, login.class));
                    }
                    finish();
                }
            });
            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, register.class));
                }
            });

            GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 1);
            sheba_rec.setLayoutManager(manager);
            sheba_rec.setNestedScrollingEnabled(false);
            sheba_adapter = new sheba_adapter(MainActivity.this);
            main_controller.inithome(MainActivity.this, sheba_adapter, sheba_rec,
                    "SELECT description,title,image,id FROM `services`");

            GridLayoutManager manager1 = new GridLayoutManager(MainActivity.this, 1);
            why_rec.setLayoutManager(manager1);
            why_rec.setNestedScrollingEnabled(false);
            why_adapter = new sheba_adapter(MainActivity.this);
            main_controller.inithome(MainActivity.this, why_adapter, why_rec,
                    "SELECT description,title,image,id FROM `whycouriers`");

            GridLayoutManager manager2 = new GridLayoutManager(MainActivity.this, 1);
            blog_rec.setLayoutManager(manager2);
            blog_rec.setNestedScrollingEnabled(false);
            blog_adapter = new sheba_adapter(MainActivity.this);
            main_controller.inithome(MainActivity.this, blog_adapter, blog_rec,
                    "SELECT description,title,image,id FROM `blogs`");
        } catch (Exception e) {
        }
    }

}