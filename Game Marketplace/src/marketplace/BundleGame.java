package marketplace;

/** Subclass untuk paket bundle berisi beberapa game. */
public class BundleGame extends Game {

    private String[] includedGames;
    private double   originalPrice;

    public BundleGame(int id, String name, double price, String description,
                      String imageUrl, String category, double rating,
                      String developer, String[] includedGames, double originalPrice) {
        super(id, name, price, description, imageUrl, category, rating, developer);
        this.includedGames = includedGames;
        this.originalPrice = originalPrice;
    }

    @Override
    public String getBadge() {
        int disc = (int)(100 - (getPrice() / originalPrice * 100));
        return "🎁 BUNDLE -" + disc + "%";
    }

    @Override
    public String getSummary() {
        return super.getSummary() + " | Bundle: " + includedGames.length + " games";
    }

    public String[] getIncludedGames() { return includedGames; }
    public double   getOriginalPrice()  { return originalPrice; }
    public double   getSavings()        { return originalPrice - getPrice(); }
}
