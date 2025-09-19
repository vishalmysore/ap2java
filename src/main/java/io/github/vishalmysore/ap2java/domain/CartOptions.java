package io.github.vishalmysore.ap2java.domain;

public class CartOptions {
    private boolean allowModifications;
    private String currency;
    private int maxItems;
    private int minItems;

    public CartOptions() {
    }

    public CartOptions(boolean allowModifications, String currency, int maxItems, int minItems) {
        this.allowModifications = allowModifications;
        this.currency = currency;
        this.maxItems = maxItems;
        this.minItems = minItems;
    }

    public boolean isAllowModifications() {
        return allowModifications;
    }

    public void setAllowModifications(boolean allowModifications) {
        this.allowModifications = allowModifications;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public int getMinItems() {
        return minItems;
    }

    public void setMinItems(int minItems) {
        this.minItems = minItems;
    }
}
