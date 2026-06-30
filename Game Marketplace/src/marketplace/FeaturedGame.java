package marketplace;

/** Subclass game unggulan dengan badge FEATURED. */
public class FeaturedGame extends Game {

    private String featuredReason;

    public FeaturedGame(int id, String name, double price, String description,
                        String imageUrl, String category, double rating,
                        String developer, String featuredReason) {
        super(id, name, price, description, imageUrl, category, rating, developer);
        this.featuredReason = featuredReason;
    }

    @Override
    public String getBadge() { return "⭐ FEATURED"; }

    @Override
    public String getSummary() {
        return super.getSummary() + " [" + featuredReason + "]";
    }

    public String getFeaturedReason() { return featuredReason; }
}
