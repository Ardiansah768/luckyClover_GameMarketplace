package marketplace;

/**
 * Interface utama untuk semua produk yang bisa dibeli.
 * Berisi method abstrak, static, dan default.
 */
public interface Purchasable {

    double getPrice();           // abstract
    String getCategory();        // abstract
    String getName();            // abstract
    String getImageUrl();        // abstract

    static double applyDiscount(double price, double pct) { // static
        return price - (price * pct / 100);
    }

    default String getPriceFormatted() { // default
        return String.format("Rp %,.0f", getPrice());
    }

    default boolean isAffordable(double budget) { // default
        return budget >= getPrice();
    }
}
