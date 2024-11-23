package com.example.appbansach.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbansach.Adapter.ChiTietDonHangAdapter;
import com.example.appbansach.R;
import com.example.appbansach.api.OrderApi;
import com.example.appbansach.model.OrderDetail;
import com.example.appbansach.model.ResponseHistoryOrderModel;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietDonHangActivity extends AppCompatActivity {
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    TextView maDonHang, totalItems, totalCost, tvKH, tvDC, tvTrangThai;
    RecyclerView recyclerViewChiTietDonHang;
    Space spaceBtnHuy, spaceBtnDanhGia;
    AppCompatButton btnHuy, btnDanhGia;
    int maDonHangHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        setControl();
        innitData();
        setEvent();
    }

    private void setEvent() {
//        btnHuy.setOnClickListener(v ->
//                new AlertDialog.Builder(v.getContext())
//                        .setTitle("Xác nhận hủy đơn mua")
//                        .setMessage("Bạn có chắc muốn hủy đơn hàng này không?")
//                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                            RetrofitClient retrofitClient = new RetrofitClient();
//                            OrderApi orderApi = retrofitClient.getRetrofit().create(OrderApi.class);
//
//                            int orderId = getIntent().getIntExtra("orderId", -1);
//                            orderApi.cancelOrder(orderId).enqueue(new Callback<List<ResponseHistoryOrderModel>>() {
//                                @Override
//                                public void onResponse(Call<List<ResponseHistoryOrderModel>> call, Response<List<ResponseHistoryOrderModel>> response) {
//                                    if(response.isSuccessful()){
//                                        Toast.makeText(getApplicationContext(), "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(getApplicationContext(), LichSuDonHangActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }else {
//                                        Toast.makeText(getApplicationContext(),"Hủy đơn hàng thất bại",Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                                @Override
//                                public void onFailure(Call<List<ResponseHistoryOrderModel>> call, Throwable t) {
//
//                                }
//                            });
//
//                        })
//                        .setNegativeButton(android.R.string.no, null)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show()
//        );
        btnHuy.setOnClickListener(v ->
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Xác nhận hủy đơn mua")
                        .setMessage("Bạn có chắc muốn hủy đơn hàng này không?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            RetrofitClient retrofitClient = new RetrofitClient();
                            OrderApi orderApi = retrofitClient.getRetrofit().create(OrderApi.class);

                            int orderId = getIntent().getIntExtra("orderId", -1);
                            Log.d("ChiTietDonHang", "orderId: " + orderId);
                            orderApi.cancelOrder(orderId).enqueue(new Callback<ResponseHistoryOrderModel>() {
                                @Override
                                public void onResponse(Call<ResponseHistoryOrderModel> call, Response<ResponseHistoryOrderModel> response) {;
                                    if (response.isSuccessful()) {
                                        // Hiển thị thông báo hủy thành công
                                        Toast.makeText(getApplicationContext(), "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LichSuDonHangActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Hiển thị thông báo hủy thất bại nếu có vấn đề với phản hồi
                                        Toast.makeText(getApplicationContext(), "Hủy đơn hàng thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseHistoryOrderModel> call, Throwable t) {
                                    // Hiển thị thông báo lỗi khi kết nối gặp sự cố
                                    Toast.makeText(getApplicationContext(), "Lỗi khi hủy đơn hàng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()
        );

    }

    private void innitData() {
        Intent intent = getIntent();
        // Lấy dữ liệu từ Intent
        int orderId = intent.getIntExtra("orderId", -1);
        String totalPrice = intent.getStringExtra("totalPrice");
        int status = intent.getIntExtra("status", -1);
        String orderDetailsJson = intent.getStringExtra("orderDetails");
        List<OrderDetail> orderDetails = new ArrayList<>();

        Log.d("ChiTietDonHang", "orderId: " + orderId);
        Log.d("ChiTietDonHang", "totalPrice: " + totalPrice);
        Log.d("ChiTietDonHang", "status: " + status);
        Log.d("ChiTietDonHang", "orderDetailsJson: " + orderDetailsJson);
        Log.d("ChiTietDonHang", "orderDetails size: " + orderDetails.size());

        // Kiểm tra và khởi tạo danh sách chi tiết đơn hàng từ JSON
        if (orderDetailsJson != null && !orderDetailsJson.isEmpty()) {
            orderDetails = new Gson().fromJson(orderDetailsJson, new TypeToken<List<OrderDetail>>() {}.getType());
        }

        if (orderDetails == null) {
            orderDetails = new ArrayList<>(); // Đảm bảo danh sách không bị null
        }

        String fullName = intent.getStringExtra("fullName");
        String address = intent.getStringExtra("address");

        // Gán dữ liệu vào UI
        maDonHang.setText(String.format("Mã đơn hàng: %d", orderId));

        // Tính tổng số lượng sản phẩm trong danh sách
        int totalQuantity = 0;
        for (OrderDetail orderDetail : orderDetails) {
            totalQuantity += orderDetail.getQuantity();
        }
        totalItems.setText(String.format("%d sản phẩm", totalQuantity));
        totalCost.setText(String.format("Thành tiền: đ%s", totalPrice));
        tvKH.setText(String.format("Tên khách hàng: %s", fullName));
        tvDC.setText(String.format("Địa chỉ: %s", address));

        // Hiển thị trạng thái đơn hàng
        tvTrangThai.setText(status == 0 ? "Chờ xác nhận" : status == 1 ? "Đang giao" : status == 2 ? "Đã giao" : "Đã hủy");

        // Hiển thị danh sách chi tiết đơn hàng
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );

        ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(this, orderDetails);
        recyclerViewChiTietDonHang.setLayoutManager(layoutManager);
        recyclerViewChiTietDonHang.setAdapter(chiTietDonHangAdapter);

        // Xử lý hiển thị nút "Hủy" và "Đánh giá"
        if (status == 0) { // Nếu đơn hàng ở trạng thái chờ xác nhận
            btnHuy.setVisibility(View.VISIBLE);
            spaceBtnHuy.setVisibility(View.VISIBLE);
        }
    }


    private void setControl() {
        maDonHang = findViewById(R.id.maDonHang);
        totalItems = findViewById(R.id.totalItems);
        totalCost = findViewById(R.id.totalCost);
        tvKH = findViewById(R.id.tvKH);
        tvDC = findViewById(R.id.tvDC);
        tvTrangThai = findViewById(R.id.tvTrangThai);
        recyclerViewChiTietDonHang = findViewById(R.id.recyclerViewChiTietDonHang);

        btnHuy = findViewById(R.id.btnHuy);
        btnDanhGia = findViewById(R.id.btnDanhGia);

        spaceBtnHuy = findViewById(R.id.spaceBtnHuy);
        spaceBtnDanhGia = findViewById(R.id.spaceBtnDanhGia);
    }
}