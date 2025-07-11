package com.supermarket.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * CartItem represents a product and its quantity in the shopping cart
 */
public class CartItem {
    
    @JsonProperty("product")
    private Product product;
    
    @JsonProperty("quantity")
    private Integer quantity;
    
    @JsonProperty("subtotal")
    private Double subtotal;
    
    // Default constructor
    public CartItem() {}
    
    // Constructor
    public CartItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        calculateSubtotal();
    }
    
    // Getters and Setters
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
        calculateSubtotal();
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }
    
    public Double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
    
    // Business methods
    private void calculateSubtotal() {
        if (product != null && product.getPrice() != null && quantity != null) {
            this.subtotal = product.getPrice() * quantity;
        } else {
            this.subtotal = 0.0;
        }
    }
    
    public void addQuantity(int additionalQuantity) {
        if (this.quantity == null) {
            this.quantity = additionalQuantity;
        } else {
            this.quantity += additionalQuantity;
        }
        calculateSubtotal();
    }
    
    public boolean canFulfill() {
        return product != null && product.hasEnoughStock(quantity);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(product, cartItem.product);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + (product != null ? product.getName() : "null") +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }
}