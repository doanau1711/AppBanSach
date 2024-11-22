package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appbansach.Adapter.SanPhamMainAdapter;
import com.example.appbansach.R;
import com.example.appbansach.api.BookApi;
import com.example.appbansach.model.Book;
import com.example.appbansach.retrofit.ApiBanSach;
import com.example.appbansach.retrofit.RetrofitClient;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPopular;
    List<Book> bookList = new ArrayList<>();
    SanPhamMainAdapter danhSachSanPhamAdapter;
    RecyclerView RecyclerViewPopular;
    TextView textView;
    EditText editText;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanSach apiBanSach;
    ImageView btnGH, btnTT, btnLSDH, btnHome;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
        RetrofitClient retrofitClient = new RetrofitClient();
        BookApi bookApi = retrofitClient.getRetrofit().create(BookApi.class);

        bookApi.getAllBook().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                bookList = response.body();
                danhSachSanPhamAdapter = new SanPhamMainAdapter(bookList, getApplicationContext());
                recyclerViewPopular.setAdapter(danhSachSanPhamAdapter);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
            }
        });
    }

    private void setEvent() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DanhSachSanPham.class));
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
        btnTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProFileActivity.class));
            }
        });
        btnGH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
        btnLSDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LichSuDonHangActivity.class));
            }
        });

    }


    private void setControl() {
        recyclerViewPopular = findViewById(R.id.view1);
        editText = findViewById(R.id.editText2);
        textView = findViewById(R.id.textView4);
        btnGH = findViewById(R.id.imageView62);
        btnHome = findViewById(R.id.imageView6);
        btnLSDH = findViewById(R.id.imageView61);
        btnTT = findViewById(R.id.imageView63);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopular.setLayoutManager(layoutManager);
    }



}