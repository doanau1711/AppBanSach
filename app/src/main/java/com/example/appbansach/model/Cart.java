package com.example.appbansach.model;

import java.math.BigDecimal;

public class Cart {
    private int cartId;
    private int quantity;
    private BigDecimal price;
    private Book book; // chú ý bên spring Book book (Join column bookid)
//    test api post men trc đi
    private int userid;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", book=" + book.toString() +
                ", userid=" + userid +
                '}';
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
