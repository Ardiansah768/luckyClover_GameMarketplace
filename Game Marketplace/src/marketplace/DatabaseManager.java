package marketplace;

import java.sql.*;
import java.util.*;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/gamemarket_db"
        + "?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection conn = null;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver tidak ditemukan: " + e.getMessage());
            }
        }
        return conn;
    }

    public static void initDB() {
        String[] sqls = {
            "CREATE TABLE IF NOT EXISTS games ("
            + "id INT AUTO_INCREMENT PRIMARY KEY,"
            + "name VARCHAR(255),"
            + "price DOUBLE,"
            + "category VARCHAR(100),"
            + "description TEXT,"
            + "image_url VARCHAR(500),"
            + "rating DOUBLE,"
            + "developer VARCHAR(255),"
            + "badge VARCHAR(100) DEFAULT '',"
            + "type VARCHAR(50) DEFAULT 'Game'"
            + ")",

            "CREATE TABLE IF NOT EXISTS transaksi ("
            + "id INT AUTO_INCREMENT PRIMARY KEY,"
            + "game_name VARCHAR(255),"
            + "price DOUBLE,"
            + "payment_method VARCHAR(100),"
            + "waktu TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ")",

            "CREATE TABLE IF NOT EXISTS wallet ("
            + "id INT AUTO_INCREMENT PRIMARY KEY,"
            + "name VARCHAR(100),"
            + "balance DOUBLE DEFAULT 0"
            + ")",

            "CREATE TABLE IF NOT EXISTS users ("
            + "username VARCHAR(50) PRIMARY KEY,"
            + "password VARCHAR(255) NOT NULL,"
            + "role VARCHAR(10) NOT NULL DEFAULT 'user'"
            + ")"
        };
        try (Statement st = getConnection().createStatement()) {
            for (String sql : sqls) st.execute(sql);
            initWallet();
            initAdmin();
        } catch (SQLException e) { System.err.println("initDB: " + e.getMessage()); }
    }

    private static void initWallet() throws SQLException {
        ResultSet rs = getConnection().createStatement()
            .executeQuery("SELECT COUNT(*) FROM wallet");
        rs.next();
        if (rs.getInt(1) == 0) {
            getConnection().createStatement().execute(
                "INSERT INTO wallet (name, balance) VALUES ('GoPay',150000),('OVO',200000),('Dana',100000)"
            );
        }
    }

    private static void initAdmin() throws SQLException {
        ResultSet rs = getConnection().createStatement()
            .executeQuery("SELECT COUNT(*) FROM users WHERE username='admin'");
        rs.next();
        if (rs.getInt(1) == 0) {
            getConnection().createStatement().execute(
                "INSERT INTO users (username, password, role) VALUES ('admin','admin123','admin')"
            );
        }
    }

    // ── AUTH ──────────────────────────────────────────────────

    public static String login(String username, String password) {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "SELECT role FROM users WHERE username=? AND password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("role");
        } catch (SQLException e) { System.err.println("login: " + e.getMessage()); }
        return null;
    }

    public static boolean isUsernameExist(String username) {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "SELECT COUNT(*) FROM users WHERE username=?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { System.err.println("isUsernameExist: " + e.getMessage()); }
        return false;
    }

    public static boolean registerUser(String username, String password) {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.err.println("registerUser: " + e.getMessage()); return false; }
    }

    // ── GAMES CRUD ─────────────────────────────────────────────

    public static List<AbstractProduct> getAllGames() {
        List<AbstractProduct> list = new ArrayList<>();
        try (ResultSet rs = getConnection().createStatement()
                .executeQuery("SELECT * FROM games ORDER BY id")) {
            while (rs.next()) {
                String type = rs.getString("type");
                if (type.equals("Bundle")) {
                    list.add(new BundleGame(
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("price"),
                        rs.getString("description"), rs.getString("image_url"),
                        rs.getString("category"), rs.getDouble("rating"),
                        rs.getString("developer"), new String[]{}, rs.getDouble("price") * 1.3
                    ));
                } else if (type.equals("Featured")) {
                    list.add(new FeaturedGame(
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("price"),
                        rs.getString("description"), rs.getString("image_url"),
                        rs.getString("category"), rs.getDouble("rating"),
                        rs.getString("developer"), "Top Pick"
                    ));
                } else {
                    list.add(new Game(
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("price"),
                        rs.getString("description"), rs.getString("image_url"),
                        rs.getString("category"), rs.getDouble("rating"),
                        rs.getString("developer")
                    ));
                }
            }
        } catch (SQLException e) { System.err.println("getAllGames: " + e.getMessage()); }
        return list;
    }

    // Ambil 1 game berdasarkan ID (untuk mengisi form saat baris tabel diklik)
    public static AbstractProduct getGameById(int id) {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "SELECT * FROM games WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                if (type.equals("Bundle")) {
                    return new BundleGame(
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("price"),
                        rs.getString("description"), rs.getString("image_url"),
                        rs.getString("category"), rs.getDouble("rating"),
                        rs.getString("developer"), new String[]{}, rs.getDouble("price") * 1.3
                    );
                } else if (type.equals("Featured")) {
                    return new FeaturedGame(
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("price"),
                        rs.getString("description"), rs.getString("image_url"),
                        rs.getString("category"), rs.getDouble("rating"),
                        rs.getString("developer"), "Top Pick"
                    );
                } else {
                    return new Game(
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("price"),
                        rs.getString("description"), rs.getString("image_url"),
                        rs.getString("category"), rs.getDouble("rating"),
                        rs.getString("developer")
                    );
                }
            }
        } catch (SQLException e) { System.err.println("getGameById: " + e.getMessage()); }
        return null;
    }

    public static List<AbstractProduct> getGamesByCategory(String category) {
        List<AbstractProduct> all = getAllGames();
        if (category.equals("Semua")) return all;
        List<AbstractProduct> filtered = new ArrayList<>();
        for (AbstractProduct p : all)
            if (p.getCategory().equals(category)) filtered.add(p);
        return filtered;
    }

    public static boolean insertGame(String name, double price, String category,
                                     String desc, String imageUrl, double rating,
                                     String developer, String type) {
        String sql = "INSERT INTO games (name,price,category,description,image_url,rating,developer,type) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, name); ps.setDouble(2, price); ps.setString(3, category);
            ps.setString(4, desc); ps.setString(5, imageUrl); ps.setDouble(6, rating);
            ps.setString(7, developer); ps.setString(8, type);
            ps.executeUpdate(); return true;
        } catch (SQLException e) { System.err.println("insertGame: " + e.getMessage()); return false; }
    }

    // Update semua field game (versi lengkap untuk Admin Dashboard)
    public static boolean updateGameFull(int id, String name, double price, String category,
                                         String desc, String imageUrl, double rating,
                                         String developer, String type) {
        String sql = "UPDATE games SET name=?, price=?, category=?, description=?, "
                   + "image_url=?, rating=?, developer=?, type=? WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, name);    ps.setDouble(2, price);
            ps.setString(3, category); ps.setString(4, desc);
            ps.setString(5, imageUrl); ps.setDouble(6, rating);
            ps.setString(7, developer); ps.setString(8, type);
            ps.setInt(9, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("updateGameFull: " + e.getMessage()); return false; }
    }

    public static boolean updateGame(int id, String name, double price, String desc) {
        String sql = "UPDATE games SET name=?, price=?, description=? WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, name); ps.setDouble(2, price);
            ps.setString(3, desc); ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("updateGame: " + e.getMessage()); return false; }
    }

    public static boolean deleteGame(int id) {
        try (PreparedStatement ps = getConnection().prepareStatement("DELETE FROM games WHERE id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("deleteGame: " + e.getMessage()); return false; }
    }

    // ── TRANSAKSI ─────────────────────────────────────────────

    public static void saveTransaction(String gameName, double price, String method) {
        String sql = "INSERT INTO transaksi (game_name, price, payment_method) VALUES (?,?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, gameName); ps.setDouble(2, price); ps.setString(3, method);
            ps.executeUpdate();
        } catch (SQLException e) { System.err.println("saveTransaction: " + e.getMessage()); }
    }

    public static List<String[]> getTransactions() {
        List<String[]> list = new ArrayList<>();
        try (ResultSet rs = getConnection().createStatement()
                .executeQuery("SELECT * FROM transaksi ORDER BY waktu DESC")) {
            while (rs.next())
                list.add(new String[]{
                    rs.getString("game_name"),
                    String.format("Rp %,.0f", rs.getDouble("price")),
                    rs.getString("payment_method"),
                    rs.getString("waktu")
                });
        } catch (SQLException e) { System.err.println("getTransactions: " + e.getMessage()); }
        return list;
    }

    // ── WALLET ────────────────────────────────────────────────

    public static double getWalletBalance(String name) {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "SELECT balance FROM wallet WHERE name=?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        } catch (SQLException e) { System.err.println("getWallet: " + e.getMessage()); }
        return 0;
    }

    public static void updateWalletBalance(String name, double newBalance) {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "UPDATE wallet SET balance=? WHERE name=?")) {
            ps.setDouble(1, newBalance); ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) { System.err.println("updateWallet: " + e.getMessage()); }
    }
}