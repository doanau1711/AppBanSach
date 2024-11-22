package com.example.appbansach.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbansach.Interface.IImageClickListenner;
import com.example.appbansach.R;
import com.example.appbansach.api.BookApi;
import com.example.appbansach.api.CartApi;
import com.example.appbansach.model.Book;
import com.example.appbansach.model.Cart;
import com.example.appbansach.model.EventBus.TinhTongEvent;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHodler> {

    Context context;
    List<Cart> cartList;

    public GioHangAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }


    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_cart,parent,false);
        return new ViewHodler(view);
    }
    // nó không cập nhật được số lượng tự động mà thoát ra vào lại mới oke
//    @Override
//    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
//        RetrofitClient retrofitClient = new RetrofitClient();
//        CartApi cartApi = retrofitClient.getRetrofit().create(CartApi.class);
//        Cart cart = cartList.get(position);
//        holder.ten_sp_cart.setText(cart.getBook().getBookName());
//        holder.sl_sp_cart.setText(String.valueOf(cart.getQuantity()));
//        Glide.with(context).load(cart.getBook().getImage()).into(holder.img_cart);
//        holder.item_cart_giasp1.setText("$" + cart.getBook().getPrice().toString());
//        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//        holder.item_cart_giasp1.setText(decimalFormat.format((cart.getBook().getPrice())));
//        BigDecimal quantity = BigDecimal.valueOf(cart.getQuantity());
//        BigDecimal price = cart.getBook().getPrice();
//        BigDecimal total = price.multiply(quantity);
//        holder.item_cart_giasp2.setText(decimalFormat.format(total));
//
//
//        // suwj kien onlick -, +: Nút trừ giatri = 1, nút công giatri = 2, khi số lượng = 1 bấm thì xóa
//        holder.setListenner(new IImageClickListenner() {
//            @Override
//            public void onImageClick(View view, int pos, int giatri) {
//                if(giatri == 1){
//                    cartApi.removeCart(cartList.get(position).getCartId()).enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//                            if(response.isSuccessful()){
//                                Toast.makeText(context,"Cập nhật giỏ hàng thành công", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(context,"Cập nhật giỏ hàng thất bại",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//
//                        }
//                    });
//
//                    holder.sl_sp_cart.setText(cartList.get(pos).getQuantity() + " ");
//                    long gia = cart.getQuantity() * cart.getBook().getPrice().longValue();
//                    holder.item_cart_giasp2.setText(decimalFormat.format(gia));
//                    // Thêm mà chưa tính tổng tiền mà tổng tiền bên giohangactivity gửi sự kieenj qua giohangactivity để tính tổng tiền
//                    EventBus.getDefault().postSticky(new TinhTongEvent());
//
//                }else if(giatri == 2){
//                    // userId biến tĩnh
//                    cartApi.addCart(cartList.get(position).getBook().getBookId(),Utils.user_current.getUserId()).enqueue(new Callback<Cart>() {
//                        @Override
//                        public void onResponse(Call<Cart> call, Response<Cart> response) {
//                            if(response.isSuccessful()){
//                                Toast.makeText(context,"Cập nhật giỏ hàng thành công", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(context,"Cập nhật giỏ hàng thất bại",Toast.LENGTH_SHORT).show();
//                            }
//                        // chú yes tính tổng tiền
//                            holder.sl_sp_cart.setText(cartList.get(pos).getQuantity() + " ");
//                            long gia = cart.getQuantity() * cart.getBook().getPrice().longValue();
//                            holder.item_cart_giasp2.setText(decimalFormat.format(gia));
//                            // Thêm mà chưa tính tổng tiền mà tổng tiền bên giohangactivity gửi sự kieenj qua giohangactivity để tính tổng tiền
//                            EventBus.getDefault().postSticky(new TinhTongEvent());
//
//                        }
//                        @Override
//                        public void onFailure(Call<Cart> call, Throwable t) {
//
//                        }
//                    });
//                }
//            }
//        });
//
//    }
    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        RetrofitClient retrofitClient = new RetrofitClient();
        CartApi cartApi = retrofitClient.getRetrofit().create(CartApi.class);
        Cart cart = cartList.get(position);

        holder.ten_sp_cart.setText(cart.getBook().getBookName());
        holder.sl_sp_cart.setText(String.valueOf(cart.getQuantity()));
        Glide.with(context).load(cart.getBook().getImage()).into(holder.img_cart);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_cart_giasp1.setText(decimalFormat.format(cart.getBook().getPrice()));

        BigDecimal quantity = BigDecimal.valueOf(cart.getQuantity());
        BigDecimal price = cart.getBook().getPrice();
        BigDecimal total = price.multiply(quantity);
        holder.item_cart_giasp2.setText(decimalFormat.format(total));

        // Event listener for quantity changes
        holder.setListenner(new IImageClickListenner() {
            // gia tri ==1 la dau -, gia tri =1 la dau +
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1) {
                    // Decrease quantity or remove item
                    if (cart.getQuantity() > 1) {
                        // Decrease quantity
                        cart.setQuantity(cart.getQuantity() - 1);
                        cartApi.removeCart(cartList.get(position).getCartId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Cập nhật giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                    holder.sl_sp_cart.setText(String.valueOf(cart.getQuantity()));
                                    BigDecimal newTotal = price.multiply(BigDecimal.valueOf(cart.getQuantity()));
                                    holder.item_cart_giasp2.setText(decimalFormat.format(newTotal));
                                    EventBus.getDefault().postSticky(new TinhTongEvent());
                                } else {
                                    Toast.makeText(context, "Cập nhật giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Remove product from cart
                        cartApi.removeCart(cartList.get(position).getCartId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Sản phẩm đã được xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                                    cartList.remove(position);
                                    notifyItemRemoved(position);
                                    EventBus.getDefault().postSticky(new TinhTongEvent());
                                } else {
                                    Toast.makeText(context, "Cập nhật giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else if (giatri == 2) {
                    // Increase quantity
                    cart.setQuantity(cart.getQuantity() + 1);
                    cartApi.addCart(cartList.get(position).getBook().getBookId(), Utils.user_current.getUserId()).enqueue(new Callback<Cart>() {
                        @Override
                        public void onResponse(Call<Cart> call, Response<Cart> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Cập nhật giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                holder.sl_sp_cart.setText(String.valueOf(cart.getQuantity()));
                                BigDecimal newTotal = price.multiply(BigDecimal.valueOf(cart.getQuantity()));
                                holder.item_cart_giasp2.setText(decimalFormat.format(newTotal));
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            } else {
                                Toast.makeText(context, "Cập nhật giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Cart> call, Throwable t) {
                            Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView item_cart_tru, item_cart_cong,ten_sp_cart, sl_sp_cart,item_cart_giasp1, item_cart_giasp2;
        ImageView img_cart;

        IImageClickListenner listenner;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            item_cart_tru = itemView.findViewById(R.id.item_tru);
            item_cart_cong = itemView.findViewById(R.id.item_cong);
            ten_sp_cart = itemView.findViewById(R.id.tvBookTitle);
            item_cart_giasp1 = itemView.findViewById(R.id.tvPrice);
            item_cart_giasp2 = itemView.findViewById(R.id.tvTotalPrice);
            sl_sp_cart = itemView.findViewById(R.id.tvQuantity);
            img_cart = itemView.findViewById(R.id.bookImg);

            // event click
            item_cart_tru.setOnClickListener(this);
            item_cart_cong.setOnClickListener(this);
        }

        public void setListenner(IImageClickListenner listenner) {
            this.listenner = listenner;
        }

        @Override
        public void onClick(View v) {

            if(v == item_cart_tru){
                // 1 tru
                listenner.onImageClick(v,getAdapterPosition(),1);

            }else if(v == item_cart_cong){
                // 2 cong
                listenner.onImageClick(v,getAdapterPosition(),2);
            }
        }
    }
}
