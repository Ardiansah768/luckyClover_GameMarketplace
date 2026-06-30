package marketplace;

/** Abstract superclass untuk semua metode pembayaran. */
public abstract class PaymentMethod {

    private String name;
    private double balance;

    public PaymentMethod(String name, double balance) {
        this.name    = name;
        this.balance = balance;
    }

    public abstract String getType();
    public abstract String getIcon();

    // Overloading: bayar dengan total saja
    public boolean pay(double total) {
        if (balance < total) return false;
        balance -= total;
        return true;
    }

    // Overloading: bayar dengan total + kode promo
    public boolean pay(double total, String promoCode) {
        double disc = promoCode.equals("LUCKY10") ? 0.10 : 0;
        double finalTotal = total - (total * disc);
        return pay(finalTotal);
    }

    public void topUp(double amount) { this.balance += amount; }

    public String getName()              { return name; }
    public double getBalance()           { return balance; }
    public String getBalanceFormatted()  { return String.format("Rp %,.0f", balance); }
}

// ── Subclass: Digital Wallet ──────────────────────────────────
class DigitalWallet extends PaymentMethod {

    private String phoneNumber;

    public DigitalWallet(String name, double balance, String phoneNumber) {
        super(name, balance);
        this.phoneNumber = phoneNumber;
    }

    @Override public String getType() { return "Digital Wallet"; }

    @Override public String getIcon() {
        switch (getName()) {
            case "GoPay": return "💚";
            case "OVO":   return "💜";
            case "Dana":  return "💙";
            default:      return "💰";
        }
    }

    public String getPhoneNumber() { return phoneNumber; }
}

// ── Subclass: QRIS ────────────────────────────────────────────
class QRISPayment extends PaymentMethod {

    private String merchantId;

    public QRISPayment(String merchantId) {
        super("QRIS", Double.MAX_VALUE); // QRIS tidak perlu cek saldo di sisi app
        this.merchantId = merchantId;
    }

    @Override public String getType() { return "QRIS"; }
    @Override public String getIcon() { return "📱"; }

    @Override
    public boolean pay(double total) {
        // QRIS selalu bisa bayar (diasumsikan user scan dari app bank)
        return true;
    }

    public String getMerchantId() { return merchantId; }
}
