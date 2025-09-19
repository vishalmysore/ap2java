package io.github.vishalmysore.ap2java.domain;

import io.github.vishalmysore.ap2java.mandate.IntentMandate;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cart {

    private final String id;
    private final String userId;
    private final Map<String, CartItem> items;
    private double totalAmount;
    @Getter
    @Setter
    private IntentMandate intent;

    public Cart(String userId) {
        this.id = "cart-" + UUID.randomUUID().toString().substring(0, 8);
        this.userId = userId;
        this.items = new HashMap<>();
        this.totalAmount = 0.0;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, CartItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void addItem(String productId, String productName, double price, int quantity) {
        CartItem item = new CartItem(productId, productName, price, quantity);
        items.put(productId, item);
        recalculateTotal();
    }

    public void removeItem(String productId) {
        items.remove(productId);
        recalculateTotal();
    }

    private void recalculateTotal() {
        this.totalAmount = items.values().stream()
                .mapToDouble(item -> item.price * item.quantity)
                .sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cart{id='").append(id).append("', totalAmount=").append(String.format("%.2f", totalAmount)).append("}\n");

        for (CartItem item : items.values()) {
            sb.append("  - ").append(item.productName)
                    .append(" (").append(item.productId).append(")")
                    .append(": $").append(String.format("%.2f", item.price))
                    .append(" x ").append(item.quantity)
                    .append(" = $").append(String.format("%.2f", item.price * item.quantity))
                    .append("\n");
        }

        return sb.toString();
    }


    /**
     * Helper class for cart items
     */
    public static class CartItem {
        private final String productId;
        private final String productName;
        private final double price;
        private final int quantity;

        public CartItem(String productId, String productName, double price, int quantity) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
        }

        public String getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}