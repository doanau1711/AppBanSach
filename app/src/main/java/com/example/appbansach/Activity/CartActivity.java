package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

//import com.example.appbansach.Adapter.GioHangAdapter;
import com.example.appbansach.Adapter.GioHangAdapter;
import com.example.appbansach.R;
import com.example.appbansach.api.CartApi;
import com.example.appbansach.model.Cart;
import com.example.appbansach.retrofit.ApiBanSach;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;
import com.example.appbansach.model.EventBus.TinhTongEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    TextView totalPrice, soLuongSp;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnMuahang;
    GioHangAdapter gioHangAdapter;
    List<Cart> cartList = new ArrayList<>();
    long tongtiensp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setControl();
//        getData();
        initControl();
        tinhTongTien();
    }

    private void tinhTongTien() {
        tongtiensp = 0;
        for(int i = 0 ; i < Utils.mangGioHang.size();i++){
            BigDecimal price = Utils.mangGioHang.get(i).getBook().getPrice();
            int quantity = Utils.mangGioHang.get(i).getQuantity();
            tongtiensp = tongtiensp + price.multiply(BigDecimal.valueOf(quantity)).longValue();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        totalPrice.setText(decimalFormat.format(tongtiensp));
    }

    private void initControl() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
            gioHangAdapter = new GioHangAdapter(getApplicationContext(),Utils.mangGioHang);
            recyclerView.setAdapter(gioHangAdapter);


        btnMuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PayMentActivity.class);
                intent.putExtra("tongtien",tongtiensp);
                startActivity(intent);
            }
        });

    }

    private void setControl() {
        totalPrice = findViewById(R.id.textView6);
        soLuongSp = findViewById(R.id.textView14);
        btnMuahang = findViewById(R.id.btnOrder);
        recyclerView = findViewById(R.id.cartView);
    }

//    private void getData() {
//        RetrofitClient retrofitClient = new RetrofitClient();
//        CartApi cartApi = retrofitClient.getRetrofit().create(CartApi.class);
//        cartApi.getCart(Utils.user_current.getUserId()).enqueue(new Callback<List<Cart>>() {
//            @Override
//            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
//                if (response.isSuccessful()) {
//                    cartList = response.body();
////                    Utils.mangGioHang.clear(); // Clear the old list
//                    Utils.mangGioHang.addAll(cartList); // Add the new list
//                    gioHangAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
//
//                    // Optionally log or display the data
//                    for (Cart cart : cartList) {
//                        Log.d("CartItem2", cart.toString());
//                    }
//                    tinhTongTien(); // Recalculate the total price after updating the cart
//                } else {
//                    Log.e("CartActivity", "Failed to load cart data");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Cart>> call, Throwable t) {
//                Log.e("CartActivity", "Error: " + t.getMessage());
//            }
//        });
//    }



    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event){
        if(event != null){
            tinhTongTien();
        }
    }
}