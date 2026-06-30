package marketplace;

/**
 * Superclass abstrak untuk semua produk di marketplace.
 */
public abstract class AbstractProduct implements Purchasable {

    private int    id;
    private String name;
    private double price;
    private String description;
    private String imageUrl;
    private String category;
    private double rating;

    public AbstractProduct(int id, String name, double price,
                           String description, String imageUrl,
                           String category, double rating) {
        this.id          = id;
        this.name        = name;
        this.price       = price;
        this.description = description;
        this.imageUrl    = imageUrl;
        this.category    = category;
        this.rating      = rating;
    }

    // Overloading constructor tanpa rating
    public AbstractProduct(int id, String name, double price,
                           String description, String imageUrl, String category) {
        this(id, name, price, description, imageUrl, category, 4.0);
    }

    public abstract String getBadge();

    public String getSummary() {
        return "[" + category + "] " + name + " - " + getPriceFormatted()
             + " ★" + String.format("%.1f", rating);
    }

    // Getters & Setters
    public int    getId()          { return id; }
    public String getName()        { return name; }
    public double getPrice()       { return price; }
    public String getDescription() { return description; }
    public String getImageUrl()    { return imageUrl; }
    public String getCategory()    { return category; }
    public double getRating()      { return rating; }
    public void   setPrice(double price)       { this.price = price; }
    public void   setDescription(String desc)  { this.description = desc; }
}
