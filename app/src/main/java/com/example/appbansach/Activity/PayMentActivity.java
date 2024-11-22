package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbansach.R;
import com.example.appbansach.api.OrderApi;
import com.example.appbansach.model.RequestOrderModel;
import com.example.appbansach.model.ResponseOrderModel;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;

import java.text.DecimalFormat;
import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayMentActivity extends AppCompatActivity {

    TextView txtTongtien, txtAdress;
    long tongtien;
    AppCompatButton btnOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ment);
        setControl();
        initControl();
        

    }

    private void initControl() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien",0);
        txtTongtien.setText(decimalFormat.format(tongtien));
        txtAdress.setText(Utils.user_current.getAddress());
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RetrofitClient retrofitClient = new RetrofitClient();
                OrderApi orderApi = retrofitClient.getRetrofit().create(OrderApi.class);
                RequestOrderModel requestOrderModel = new RequestOrderModel();
//                requestOrderModel.setOrderDate("2024-11-17");
                requestOrderModel.setAddress(Utils.user_current.getAddress());
                requestOrderModel.setEmail(Utils.user_current.getEmail());
                requestOrderModel.setFullName(Utils.user_current.getFullName());
                requestOrderModel.setStatus(1);
                requestOrderModel.setUserId(Utils.user_current.getUserId());
                Log.d("RequestOrderModel2", "Request Data: " + requestOrderModel.toString());
                orderApi.createOrders(requestOrderModel).enqueue(new Callback<ResponseOrderModel>() {
                    @Override
                    public void onResponse(Call<ResponseOrderModel> call, Response<ResponseOrderModel> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Tạo đơn hàng thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Tạo đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseOrderModel> call, Throwable t) {

                    }
                });
            }
        });

    }


    private void setControl() {
        txtTongtien = findViewById(R.id.tvTotalAmount);
        txtAdress = findViewById(R.id.tvDeliveryAddress);
        btnOrder = findViewById(R.id.btnProceedPayment);
    }
}