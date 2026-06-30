package marketplace;

/** Subclass untuk game biasa. */
public class Game extends AbstractProduct {

    private String developer;

    public Game(int id, String name, double price, String description,
                String imageUrl, String category, double rating, String developer) {
        super(id, name, price, description, imageUrl, category, rating);
        this.developer = developer;
    }

    @Override
    public String getBadge() { return ""; }

    @Override
    public String getSummary() {
        return super.getSummary() + " | Dev: " + developer;
    }

    public String getDeveloper() { return developer; }
}
