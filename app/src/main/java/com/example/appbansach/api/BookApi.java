package com.example.appbansach.api;

import com.example.appbansach.model.Book;
import com.example.appbansach.model.Publisher;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookApi {
    @GET("book")
    Call<List<Book>> getAllBook();

    @GET("book/search")
    Call<List<Book>> searchBook(
            @Query("keyword") String keyword
        );

}
