package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appbansach.R;
import com.example.appbansach.api.CartApi;
import com.example.appbansach.model.Book;
import com.example.appbansach.model.Cart;
import com.example.appbansach.model.User;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    TextView tensp, giasp, mota, textViewNoReviews;
    Button btnthem;
    ImageView imgHinhAnh;
    Toolbar toolbar;
    Book book;
    int bookId;
    String bookName;
    String price;
    String image;
    String description;
    RecyclerView recycleViewDanhGia;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        setControl();
        ActionBar();
        loadCartFromDatabase();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();
            }
        });
    }


        private void themGioHang() {
            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            boolean productExistsInCart = false;
            for (Cart cartItem : Utils.mangGioHang) {
                if (cartItem.getBook().getBookId() == bookId) {
                    // Nếu sản phẩm đã tồn tại, cập nhật số lượng
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    productExistsInCart = true;
                    break;
                }
            }

            if (!productExistsInCart) {
                Book book1 = new Book();
                book1.setBookId(bookId);
                book1.setImage(image);
                BigDecimal priceBig = new BigDecimal(price);
                book1.setPrice(priceBig);
                book1.setDescription(description);
                // Nếu sản phẩm chưa có trong giỏ, tạo mới một đối tượng Cart và thêm vào giỏ hàng
                Cart newCartItem = new Cart();
//                newCartItem.setBookid(bookId);
                newCartItem.setBook(book1);
                newCartItem.setBook(book1);
                newCartItem.setUserid(Utils.user_current.getUserId());
                newCartItem.setQuantity(1); // Mặc định số lượng ban đầu là 1
                newCartItem.setPrice(new BigDecimal(price)); // Đặt giá cho sản phẩm

                // Thêm vào giỏ hàng
                Utils.mangGioHang.add(newCartItem);

            }
            Log.d("API_INPUT", "mangGioHang: " + Utils.mangGioHang.size());
            Log.d("API_INPUT", "Book ID: " + bookId);
            Log.d("API_INPUT", "User ID: " + Utils.user_current.getUserId());

            // Sau khi thêm hoặc cập nhật, gọi API để lưu giỏ hàng
            RetrofitClient retrofitClient = new RetrofitClient();
            CartApi cartApi = retrofitClient.getRetrofit().create(CartApi.class);

            cartApi.addCart(bookId, Utils.user_current.getUserId()).enqueue(new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    if (response.isSuccessful()) {
                        // Cập nhật giao diện và thông báo thành công
                        updateBadge();
                        Toast.makeText(getApplicationContext(), "Thêm giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        // Nếu thất bại
                        Toast.makeText(getApplicationContext(), "Thêm giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }




    private void loadCartFromDatabase() {
        // userId biến tĩnh
        RetrofitClient retrofitClient = new RetrofitClient();
        CartApi cartApi = retrofitClient.getRetrofit().create(CartApi.class);
        cartApi.getCart(Utils.user_current.getUserId()).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    Utils.mangGioHang = response.body();
                    updateBadge();
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                // Handle failure to load cart
            }
        });
    }

    private void updateBadge() {
        int totalItem = 0;
        for (Cart cart : Utils.mangGioHang) {
            totalItem += cart.getQuantity();
        }
        badge.setText(String.valueOf(totalItem));
    }
    private void initData() {
        // Lấy dữ liệu từ Intent
        bookId = getIntent().getIntExtra("bookId", -1);  // Giá trị mặc định là -1 nếu không tìm thấy
        Log.d("DEBUG", "Received Book ID: " + bookId); // Kiểm tra xem có nhận đúng ID không
        bookName = getIntent().getStringExtra("bookName");
        price = getIntent().getStringExtra("price");
        image = getIntent().getStringExtra("image");
        description = getIntent().getStringExtra("description");

        // Kiểm tra dữ liệu có hợp lệ không trước khi sử dụng
        if (bookId != -1 && bookName != null && price != null && image != null) {
            // Cập nhật giao diện
            tensp.setText(bookName != null ? bookName : "Tên sách không có");
            mota.setText(description != null ? description : "Mô tả không có");
            giasp.setText(price != null && !price.isEmpty() ? "$" + price : "$0");

            // Tải ảnh bằng Glide
            Glide.with(this).load(image != null ? image : R.drawable.detective).into(imgHinhAnh);
        } else {
            // Nếu có dữ liệu không hợp lệ, xử lý lỗi hoặc hiển thị thông báo
            Toast.makeText(this, "Dữ liệu sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }



    private void setControl() {
        tensp = findViewById(R.id.chi_tiet_sp_ten);
        giasp = findViewById(R.id.chi_tiet_sp_gia);
        mota = findViewById(R.id.txtmotachitiet);
        btnthem = findViewById(R.id.btnthemvaogiohang);
        imgHinhAnh = findViewById(R.id.imgChitiet);
        toolbar = findViewById(R.id.toolbarChitietSp);
        badge = findViewById(R.id.menu_giohang);
        recycleViewDanhGia = findViewById(R.id.recycleViewDanhGia);
        recycleViewDanhGia.setLayoutManager(new LinearLayoutManager(this));

        FrameLayout frameLayout = findViewById(R.id.framegiohang);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(giohang);
            }
        });
        if(Utils.mangGioHang != null){
            badge.setText(String.valueOf(Utils.mangGioHang.size()));
        }
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartFromDatabase();
    }
}
