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

public class SanPhamMainAdapter extends RecyclerView.Adapter<SanPhamMainAdapter.Viewholder> {

    private List<Book> bookList;
    private Context context;
    public SanPhamMainAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_pop_list,parent,false);
        context = parent.getContext();
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamMainAdapter.Viewholder holder, int position) {
        Book book = bookList.get(position);
        holder.tenDssp.setText(bookList.get(position).getBookName());
        holder.giaDssp.setText("$" + bookList.get(position).getPrice());

        // Adjust the text size programmatically (optional)
        holder.tenDssp.setTextSize(10);  // Adjust this value as needed
        holder.giaDssp.setTextSize(10);  // Adjust this value as needed

        Glide.with(context).load(bookList.get(position).getImage()).into(holder.imgDssp);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    //click
                    Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
//                    intent.putExtra("chitietsp", book); // thay vi nguyên class book
                    Log.d("DEBUG", "Sending Book ID: " + book.getBookId());
                    intent.putExtra("bookId", book.getBookId());
                    intent.putExtra("bookName", book.getBookName());
                    intent.putExtra("price", book.getPrice().toString()); // BigDecimal -> String
                    intent.putExtra("image", book.getImage());
                    intent.putExtra("description", book.getDescription());
                    // Truyền các thuộc tính khác nếu cần
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class Viewholder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tenDssp,giaDssp;
        ImageView imgDssp;
        private ItemClickListener itemClickListener;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
          imgDssp = itemView.findViewById(R.id.btn_tt);
          tenDssp = itemView.findViewById(R.id.titleTxt);
          giaDssp = itemView.findViewById(R.id.feeTxt);
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
