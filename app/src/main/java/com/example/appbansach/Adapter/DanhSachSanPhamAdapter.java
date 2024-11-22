package com.example.appbansach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbansach.Activity.ChiTietSanPhamActivity;
import com.example.appbansach.Interface.ItemClickListener;
import com.example.appbansach.R;
import com.example.appbansach.model.Book;

import java.util.List;

public class DanhSachSanPhamAdapter extends RecyclerView.Adapter<DanhSachSanPhamAdapter.ViewHolder> {

    private List<Book> bookList;
    private Context context;
    public DanhSachSanPhamAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public DanhSachSanPhamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danh_sach_san_pham, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachSanPhamAdapter.ViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tendssp.setText(book.getBookName());
        holder.giadssp.setText("$" + book.getPrice());
        Glide.with(context).load(book.getImage()).into(holder.imgdssp);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick) {
                    // Handle click
                Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
//                     Pass data correctly to ChiTietSanPhamActivity
                    Log.d("DEBUG", "Sending Book ID: " + book.getBookId());
                intent.putExtra("bookId", book.getBookId());
                intent.putExtra("bookName", book.getBookName());
                intent.putExtra("price", book.getPrice().toString());
                intent.putExtra("image", book.getImage());
                intent.putExtra("description", book.getDescription());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
//       return bookList.size();
        return (bookList != null) ? bookList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView tendssp, giadssp;
        ImageView imgdssp;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tendssp = itemView.findViewById(R.id.item_danh_sach_san_pham_ten);
            giadssp = itemView.findViewById(R.id.item_danh_sach_san_pham_gia);
            imgdssp = itemView.findViewById(R.id.item_danh_sach_san_pham_image);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }
}
