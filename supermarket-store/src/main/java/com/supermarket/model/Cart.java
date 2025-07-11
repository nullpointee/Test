package com.supermarket.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Cart represents the shopping cart containing multiple cart items
 */
public class Cart {
    
    @JsonProperty("items")
    private List<CartItem> items;
    
    @JsonProperty("totalItems")
    private Integer totalItems;
    
    @JsonProperty("totalPrice")
    private Double totalPrice;
    
    // Default constructor
    public Cart() {
        this.items = new ArrayList<>();
        recalculateTotals();
    }
    
    // Getters and Setters
    public List<CartItem> getItems() {
        return items;
    }
    
    public void setItems(List<CartItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        recalculateTotals();
    }
    
    public Integer getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
    
    public Double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    // Business methods
    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid product or quantity");
        }
        
        Optional<CartItem> existingItem = findItemByProduct(product);
        if (existingItem.isPresent()) {
            existingItem.get().addQuantity(quantity);
        } else {
            items.add(new CartItem(product, quantity));
        }
        recalculateTotals();
    }
    
    public boolean removeItem(Long productId) {
        boolean removed = items.removeIf(item -> 
            item.getProduct() != null && productId.equals(item.getProduct().getId()));
        if (removed) {
            recalculateTotals();
        }
        return removed;
    }
    
    public boolean updateItemQuantity(Long productId, int newQuantity) {
        if (newQuantity <= 0) {
            return removeItem(productId);
        }
        
        Optional<CartItem> item = findItemByProductId(productId);
        if (item.isPresent()) {
            item.get().setQuantity(newQuantity);
            recalculateTotals();
            return true;
        }
        return false;
    }
    
    public void clear() {
        items.clear();
        recalculateTotals();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public boolean hasItem(Long productId) {
        return findItemByProductId(productId).isPresent();
    }
    
    public boolean canFulfillAllItems() {
        return items.stream().allMatch(CartItem::canFulfill);
    }
    
    private Optional<CartItem> findItemByProduct(Product product) {
        return items.stream()
                .filter(item -> item.getProduct() != null && item.getProduct().equals(product))
                .findFirst();
    }
    
    private Optional<CartItem> findItemByProductId(Long productId) {
        return items.stream()
                .filter(item -> item.getProduct() != null && productId.equals(item.getProduct().getId()))
                .findFirst();
    }
    
    private void recalculateTotals() {
        this.totalItems = items.stream()
                .mapToInt(item -> item.getQuantity() != null ? item.getQuantity() : 0)
                .sum();
        
        this.totalPrice = items.stream()
                .mapToDouble(item -> item.getSubtotal() != null ? item.getSubtotal() : 0.0)
                .sum();
    }
    
    @Override
    public String toString() {
        return "Cart{" +
                "totalItems=" + totalItems +
                ", totalPrice=" + totalPrice +
                ", itemsCount=" + items.size() +
                '}';
    }
}