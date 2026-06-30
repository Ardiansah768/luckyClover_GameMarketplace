package marketplace;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MarketplaceApp extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(MarketplaceApp.class.getName());

    // ── Steam-style dark palette ──────────────────────────────
    static final Color C_BG_DARKEST  = new Color(0x0F, 0x11, 0x17);
    static final Color C_BG_DARK     = new Color(0x17, 0x1A, 0x21);
    static final Color C_BG_MID      = new Color(0x1E, 0x26, 0x32);
    static final Color C_BG_PANEL    = new Color(0x16, 0x1D, 0x27);
    static final Color C_BG_CARD     = new Color(0x1B, 0x24, 0x32);
    static final Color C_BG_CARD_HOV = new Color(0x22, 0x2E, 0x3F);
    static final Color C_BG_FIELD    = new Color(0x31, 0x3D, 0x4E);
    static final Color C_ACCENT      = new Color(0x1A, 0x9F, 0xFF);
    static final Color C_ACCENT_HOV  = new Color(0x45, 0xB5, 0xFF);
    static final Color C_GREEN       = new Color(0x4C, 0xDE, 0x80);
    static final Color C_GREEN_HOV   = new Color(0x66, 0xEA, 0x95);
    static final Color C_YELLOW      = new Color(0xFF, 0xC4, 0x00);
    static final Color C_RED         = new Color(0xFF, 0x4C, 0x4C);
    static final Color C_TEXT        = new Color(0xE8, 0xEE, 0xF5);
    static final Color C_TEXT_MUTED  = new Color(0x8A, 0x97, 0xA8);
    static final Color C_BORDER      = new Color(0x2A, 0x38, 0x4A);
    static final Color C_BORDER_LIGHT= new Color(0x3A, 0x4E, 0x62);
    static final Color C_NAV_ACTIVE  = new Color(0x1E, 0x3D, 0x5A);

    // ── State ─────────────────────────────────────────────────
    private final List<AbstractProduct> cart = new ArrayList<>();

    // ── UI components ─────────────────────────────────────────
    private JPanel beranda, pembayaran, profil, cartPanel;
    private JPanel berandaMain; // permanent reference to main beranda content
    private JPanel gameGridPanel;
    private JScrollPane gameScrollPane;
    private JLabel cartBadge;

    // Filter buttons
    private JButton btnSemua, btnRPG, btnAction, btnSandbox, btnSports, btnStrategy, btnSimulation;
    private JButton activeFilterBtn = null;

    private JTabbedPane mainTabs; // reference so logo can switch to Beranda tab
    private JTable cartTable;
    private JLabel cartTotalLabel;

    // Payment
    private JTextArea summaryArea;
    private JLabel totalLabel;
    private JRadioButton rbGopay, rbOvo, rbDana;
    private ButtonGroup paymentGroup;

    // Transaksi
    private JTable transaksiTable;

    public MarketplaceApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Game Marketplace");
        setMinimumSize(new Dimension(1100, 650));
        setSize(1200, 720);
        setLocationRelativeTo(null);

        applyDarkUIDefaults();
        buildUI();
        loadGames("Semua");
        setupCartTable();
        setupTransaksiTable();
        refreshPaymentSummary();
        refreshWallet();
    }

    private void applyDarkUIDefaults() {
        UIManager.put("TabbedPane.background",        C_BG_DARK);
        UIManager.put("TabbedPane.foreground",        C_TEXT);
        UIManager.put("TabbedPane.selected",          C_BG_MID);
        UIManager.put("TabbedPane.contentAreaColor",  C_BG_DARK);
        UIManager.put("TabbedPane.tabAreaBackground", C_BG_DARKEST);
        UIManager.put("Table.background",             C_BG_CARD);
        UIManager.put("Table.foreground",             C_TEXT);
        UIManager.put("Table.gridColor",              C_BORDER);
        UIManager.put("Table.selectionBackground",    C_NAV_ACTIVE);
        UIManager.put("Table.selectionForeground",    C_TEXT);
        UIManager.put("TableHeader.background",       C_BG_MID);
        UIManager.put("TableHeader.foreground",       C_TEXT_MUTED);
        UIManager.put("ScrollBar.background",         C_BG_DARK);
        UIManager.put("ScrollBar.thumb",              C_BORDER_LIGHT);
        UIManager.put("ScrollBar.track",              C_BG_DARK);
        UIManager.put("RadioButton.background",       C_BG_MID);
        UIManager.put("RadioButton.foreground",       C_TEXT);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(C_BG_DARKEST);

        // ── TOP NAVBAR ────────────────────────────────────────
        JPanel navbar = buildNavbar();
        root.add(navbar, BorderLayout.NORTH);

        // ── MAIN TAB AREA ─────────────────────────────────────
        JTabbedPane tabs = buildTabs();
        root.add(tabs, BorderLayout.CENTER);

        setContentPane(root);
    }

    // ════════════════════════════════════════════════════════════
    // NAVBAR
    // ════════════════════════════════════════════════════════════
    private JPanel buildNavbar() {
        JPanel nav = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(C_BG_DARKEST);
                g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(C_BORDER);
                g.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
            }
        };
        nav.setOpaque(false);
        nav.setPreferredSize(new Dimension(0, 52));
        nav.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));

        // Logo
        JLabel logo = new JLabel("  GAME MARKETPLACE");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logo.setForeground(C_TEXT);
        logo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logo.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){
                if (mainTabs != null) mainTabs.setSelectedIndex(0);
                restoreBeranda(null, null);
            }
        });

        // Wallet info (right side)
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLogout.setForeground(C_TEXT_MUTED);
        btnLogout.setBackground(new Color(0x6B,0x1A,0x1A));
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addMouseListener(new MouseAdapter(){
            @Override public void mouseEntered(MouseEvent e){btnLogout.setForeground(Color.WHITE);}
            @Override public void mouseExited(MouseEvent e){btnLogout.setForeground(C_TEXT_MUTED);}
        });
        btnLogout.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });

        nav.add(logo, BorderLayout.WEST);
        nav.add(btnLogout, BorderLayout.EAST);
        return nav;
    }

    // ════════════════════════════════════════════════════════════
    // TABS
    // ════════════════════════════════════════════════════════════
    private JTabbedPane buildTabs() {
        mainTabs = new JTabbedPane(JTabbedPane.TOP);
        mainTabs.setBackground(C_BG_DARK);
        mainTabs.setForeground(C_TEXT);
        mainTabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        beranda    = buildBerandaTab();
        pembayaran = buildPembayaranTab();
        profil     = buildProfilTab();
        cartPanel  = buildCartTab();

        mainTabs.addTab("  Beranda  ", beranda);
        mainTabs.addTab("  Pembayaran  ", pembayaran);
        mainTabs.addTab("  Profil  ", profil);
        mainTabs.addTab("  Keranjang  ", cartPanel);

        return mainTabs;
    }

    // ════════════════════════════════════════════════════════════
    // BERANDA TAB
    // ════════════════════════════════════════════════════════════
    private JPanel buildBerandaTab() {
        JPanel panel = new JPanel(new CardLayout());
        panel.setBackground(C_BG_DARK);

        berandaMain = buildBerandaMain();
        panel.add(berandaMain, "main");

        return panel;
    }

    private JPanel buildBerandaMain() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(C_BG_DARK);

        // Header text
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(C_BG_DARKEST);
        header.setBorder(BorderFactory.createEmptyBorder(16, 24, 12, 24));
        JLabel h1 = new JLabel("Game Terbaru & Terpopuler");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        h1.setForeground(C_TEXT);
        JLabel h2 = new JLabel("Temukan ribuan game dengan harga terjangkau!");
        h2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        h2.setForeground(C_TEXT_MUTED);
        JPanel hv = new JPanel();
        hv.setOpaque(false);
        hv.setLayout(new BoxLayout(hv, BoxLayout.Y_AXIS));
        hv.add(h1); hv.add(Box.createVerticalStrut(2)); hv.add(h2);
        header.add(hv, BorderLayout.WEST);

        // Filter bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        filterBar.setBackground(C_BG_DARKEST);
        filterBar.setBorder(BorderFactory.createMatteBorder(0,0,1,0,C_BORDER));

        String[] cats = {"Semua","RPG","Action","Sandbox","Sports","Strategy","Simulation"};
        JButton[] catBtns = new JButton[cats.length];
        for (int i=0; i<cats.length; i++) {
            String cat = cats[i];
            JButton b = filterChip(cat);
            if (i==0) { activateChip(b); activeFilterBtn = b; }
            b.addActionListener(e -> {
                if (activeFilterBtn != null) deactivateChip(activeFilterBtn);
                activateChip(b);
                activeFilterBtn = b;
                loadGames(cat);
            });
            filterBar.add(b);
            catBtns[i] = b;
        }
        // save refs
        btnSemua      = catBtns[0]; btnRPG      = catBtns[1];
        btnAction     = catBtns[2]; btnSandbox  = catBtns[3];
        btnSports     = catBtns[4]; btnStrategy = catBtns[5];
        btnSimulation = catBtns[6];

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(C_BG_DARKEST);
        topSection.add(header, BorderLayout.NORTH);
        topSection.add(filterBar, BorderLayout.SOUTH);

        // Game grid
        gameGridPanel = new JPanel();
        gameGridPanel.setBackground(C_BG_DARK);
        gameGridPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 12, 12));
        gameGridPanel.setBorder(BorderFactory.createEmptyBorder(4,12,12,12));

        gameScrollPane = new JScrollPane(gameGridPanel);
        gameScrollPane.setBackground(C_BG_DARK);
        gameScrollPane.getViewport().setBackground(C_BG_DARK);
        gameScrollPane.setBorder(BorderFactory.createEmptyBorder());
        gameScrollPane.getVerticalScrollBar().setUnitIncrement(18);
        gameScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        gameScrollPane.addComponentListener(new ComponentAdapter(){
            @Override public void componentResized(ComponentEvent e){
                if(gameScrollPane.getViewport().getWidth()>0){
                    gameGridPanel.setPreferredSize(null);
                    gameGridPanel.revalidate();
                    gameGridPanel.repaint();
                }
            }
        });

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(gameScrollPane, BorderLayout.CENTER);

        // Force proper layout when shown (e.g. after returning from detail)
        panel.addComponentListener(new ComponentAdapter(){
            @Override public void componentShown(ComponentEvent e){
                topSection.revalidate();
                gameGridPanel.setPreferredSize(null);
                panel.revalidate();
                panel.repaint();
            }
        });

        return panel;
    }

    private JButton filterChip(String label) {
        JButton b = new JButton(label);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorderPainted(true);
        deactivateChip(b);
        return b;
    }
    private void activateChip(JButton b) {
        b.setBackground(C_ACCENT);
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
    }
    private void deactivateChip(JButton b) {
        b.setBackground(C_BG_FIELD);
        b.setForeground(C_TEXT_MUTED);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER,1),
            BorderFactory.createEmptyBorder(4,13,4,13)));
    }

    // ════════════════════════════════════════════════════════════
    // GAME CARD
    // ════════════════════════════════════════════════════════════
    private void loadGames(String category) {
        gameGridPanel.removeAll();
        gameGridPanel.setPreferredSize(null);
        List<AbstractProduct> games = DatabaseManager.getGamesByCategory(category);
        for (AbstractProduct p : games) gameGridPanel.add(createGameCard(p));
        gameGridPanel.revalidate();
        gameGridPanel.repaint();
        SwingUtilities.invokeLater(() -> gameScrollPane.getVerticalScrollBar().setValue(0));
    }

    private JPanel createGameCard(AbstractProduct p) {
        // Card container
        JPanel card = new JPanel(new BorderLayout(0,0)) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter(){
                    @Override public void mouseEntered(MouseEvent e){ hovered=true; repaint(); }
                    @Override public void mouseExited(MouseEvent e){ hovered=false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? C_BG_CARD_HOV : C_BG_CARD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? C_ACCENT : C_BORDER);
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(180, 220));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){ showDetail(p); }
        });

        // Image
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(180, 112));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setBackground(C_BG_DARKEST);
        imgLabel.setOpaque(true);
        loadImageAsync(imgLabel, p.getImageUrl(), 180, 112);

        // Badge overlay on image (e.g. FEATURED, SALE)
        JLayeredPane imgLayer = new JLayeredPane();
        imgLayer.setPreferredSize(new Dimension(180, 112));
        imgLabel.setBounds(0,0,180,112);
        imgLayer.add(imgLabel, JLayeredPane.DEFAULT_LAYER);

        if (!p.getBadge().isEmpty()) {
            JLabel badge = new JLabel("  " + p.getBadge() + "  ");
            badge.setFont(new Font("Segoe UI", Font.BOLD, 9));
            badge.setOpaque(true);
            badge.setBackground(C_YELLOW);
            badge.setForeground(new Color(0x1A,0x1A,0x1A));
            badge.setBounds(6, 6, badge.getPreferredSize().width + 8, 18);
            imgLayer.add(badge, JLayeredPane.PALETTE_LAYER);
        }

        // Info section
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));

        JLabel nameLabel = new JLabel(p.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(C_TEXT);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel catLabel = new JLabel(p.getCategory() + "  *" + String.format("%.1f", p.getRating()));
        catLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        catLabel.setForeground(C_TEXT_MUTED);
        catLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel priceLabel = new JLabel(p.getPrice()==0 ? "GRATIS" : p.getPriceFormatted());
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        priceLabel.setForeground(p.getPrice()==0 ? C_ACCENT : C_GREEN);
        priceLabel.setAlignmentX(LEFT_ALIGNMENT);

        info.add(nameLabel);
        info.add(Box.createVerticalStrut(3));
        info.add(catLabel);
        info.add(Box.createVerticalStrut(5));
        info.add(priceLabel);

        card.add(imgLayer, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    // ════════════════════════════════════════════════════════════
    // DETAIL PAGE
    // ════════════════════════════════════════════════════════════
    private void showDetail(AbstractProduct p) {
        JPanel detailPanel = new JPanel(new BorderLayout(0,0));
        detailPanel.setBackground(C_BG_DARK);

        // ── BANNER (cover fit, tidak stretch) ──────────────────
        final java.awt.image.BufferedImage[] bannerImg = {null};
        JPanel bannerPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                int pw = getWidth(), ph = getHeight();
                if (bannerImg[0] != null) {
                    int iw = bannerImg[0].getWidth(), ih = bannerImg[0].getHeight();
                    double scale = Math.max((double)pw/iw, (double)ph/ih);
                    int sw = (int)(iw*scale), sh = (int)(ih*scale);
                    int dx = (pw-sw)/2, dy = (ph-sh)/2;
                    g2.drawImage(bannerImg[0], dx, dy, sw, sh, null);
                } else {
                    g2.setColor(C_BG_DARKEST);
                    g2.fillRect(0, 0, pw, ph);
                }
                // gradient bawah
                java.awt.GradientPaint grad = new java.awt.GradientPaint(
                    0, ph*0.5f, new Color(0,0,0,0),
                    0, ph, new Color(0x17,0x1A,0x21,220));
                g2.setPaint(grad);
                g2.fillRect(0, 0, pw, ph);
            }
        };
        bannerPanel.setPreferredSize(new Dimension(0, 200));
        bannerPanel.setBackground(C_BG_DARKEST);
        new Thread(() -> {
            java.awt.image.BufferedImage img = loadImageFile(p.getImageUrl());
            if (img != null) SwingUtilities.invokeLater(() -> { bannerImg[0] = img; bannerPanel.repaint(); });
        }).start();

        // ── BODY (scrollable) ──────────────────────────────────
        JPanel body = new JPanel(new BorderLayout(0, 0));
        body.setBackground(C_BG_DARK);

        // ── INFO (kiri) ────────────────────────────────────────
        JPanel infoPanel = new JPanel(new BorderLayout(0, 0));
        infoPanel.setBackground(C_BG_DARK);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 20));

        // Header info (nama, dev, rating, tipe)
        JPanel headerInfo = new JPanel();
        headerInfo.setLayout(new BoxLayout(headerInfo, BoxLayout.Y_AXIS));
        headerInfo.setOpaque(false);

        JLabel nameL = new JLabel(p.getName());
        nameL.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameL.setForeground(C_TEXT);
        nameL.setAlignmentX(LEFT_ALIGNMENT);

        String devStr = (p instanceof Game) ? ((Game)p).getDeveloper() : "-";
        JLabel devL = new JLabel(devStr);
        devL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        devL.setForeground(C_TEXT_MUTED);
        devL.setAlignmentX(LEFT_ALIGNMENT);

        JLabel catL = new JLabel(p.getCategory() + "   * " + String.format("%.1f", p.getRating()) + " / 5.0");
        catL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        catL.setForeground(C_YELLOW);
        catL.setAlignmentX(LEFT_ALIGNMENT);

        String tipe = (p instanceof FeaturedGame)?"[Featured]":(p instanceof BundleGame)?"[Bundle]":"[Standard]";
        JLabel tipeL = new JLabel(tipe);
        tipeL.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tipeL.setForeground(C_ACCENT);
        tipeL.setAlignmentX(LEFT_ALIGNMENT);

        headerInfo.add(nameL);
        headerInfo.add(Box.createVerticalStrut(4));
        headerInfo.add(devL);
        headerInfo.add(Box.createVerticalStrut(8));
        headerInfo.add(catL);
        headerInfo.add(Box.createVerticalStrut(4));
        headerInfo.add(tipeL);

        // Deskripsi (mengisi sisa ruang)
        JTextArea descArea = new JTextArea(p.getDescription());
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descArea.setForeground(new Color(0xCC,0xD6,0xE0));
        descArea.setBackground(C_BG_DARK);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        infoPanel.add(headerInfo, BorderLayout.NORTH);
        infoPanel.add(descArea, BorderLayout.CENTER);

        // Wrap body dalam scroll
        JScrollPane bodyScroll = new JScrollPane(infoPanel);
        bodyScroll.setBorder(BorderFactory.createEmptyBorder());
        bodyScroll.getViewport().setBackground(C_BG_DARK);
        bodyScroll.setBackground(C_BG_DARK);
        bodyScroll.getVerticalScrollBar().setUnitIncrement(16);
        bodyScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // ── SIDEBAR (kanan) ────────────────────────────────────
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(C_BG_PANEL);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,1,0,0,C_BORDER),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        sidebar.setPreferredSize(new Dimension(240, 0));

        JLabel priceL = new JLabel(p.getPrice()==0?"GRATIS":p.getPriceFormatted());
        priceL.setFont(new Font("Segoe UI", Font.BOLD, 24));
        priceL.setForeground(p.getPrice()==0?C_ACCENT:C_GREEN);
        priceL.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(priceL);
        sidebar.add(Box.createVerticalStrut(14));

        JButton btnCart = steamButton(p.getPrice()==0?"Get for Free":"Add to Cart",
            p.getPrice()==0?C_ACCENT:C_GREEN, p.getPrice()==0?C_ACCENT_HOV:C_GREEN_HOV);
        btnCart.setAlignmentX(LEFT_ALIGNMENT);
        btnCart.addActionListener(e -> { addToCart(p); restoreBeranda(null, null); });
        sidebar.add(btnCart);
        sidebar.add(Box.createVerticalStrut(8));

        JButton btnBack = steamButton("< Back to Store", C_BG_MID, C_BG_CARD_HOV);
        btnBack.setAlignmentX(LEFT_ALIGNMENT);
        btnBack.addActionListener(e -> restoreBeranda(null, null));
        sidebar.add(btnBack);

        body.add(bodyScroll, BorderLayout.CENTER);
        body.add(sidebar, BorderLayout.EAST);

        detailPanel.add(bannerPanel, BorderLayout.NORTH);
        detailPanel.add(body, BorderLayout.CENTER);

        // Use CardLayout to swap views cleanly
        beranda.add(detailPanel, "detail");
        ((CardLayout) beranda.getLayout()).show(beranda, "detail");
        beranda.revalidate(); beranda.repaint();
    }

    private void restoreBeranda(java.awt.LayoutManager layout, java.awt.Component[] comps) {
        // Show the preserved main panel - no component juggling needed
        ((CardLayout) beranda.getLayout()).show(beranda, "main");
        SwingUtilities.invokeLater(() -> {
            berandaMain.revalidate();
            berandaMain.repaint();
        });
    }

    private JButton steamButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text){
            private Color cur = bg;
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cur);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                super.paintComponent(g);
            }
            { addMouseListener(new MouseAdapter(){
                @Override public void mouseEntered(MouseEvent e){cur=hover;repaint();}
                @Override public void mouseExited(MouseEvent e){cur=bg;repaint();}
            });}
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setPreferredSize(new Dimension(200, 42));
        return btn;
    }

    // ════════════════════════════════════════════════════════════
    // CART TAB
    // ════════════════════════════════════════════════════════════
    private JPanel buildCartTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(C_BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel title = new JLabel("Shopping Cart");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(C_TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(0,0,16,0));
        panel.add(title, BorderLayout.NORTH);

        // Table
        cartTable = new JTable();
        cartTable.setRowHeight(30);
        cartTable.setShowGrid(false);
        cartTable.setIntercellSpacing(new Dimension(0,0));
        cartTable.setBackground(C_BG_CARD);
        cartTable.setForeground(C_TEXT);
        cartTable.setSelectionBackground(C_NAV_ACTIVE);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        cartTable.getTableHeader().setBackground(C_BG_MID);
        cartTable.getTableHeader().setForeground(C_TEXT_MUTED);

        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setBorder(BorderFactory.createLineBorder(C_BORDER,1));
        cartScroll.setBackground(C_BG_DARK);
        cartScroll.getViewport().setBackground(C_BG_CARD);
        panel.add(cartScroll, BorderLayout.CENTER);

        // Bottom action bar
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(12,0,0,0));

        cartTotalLabel = new JLabel("Total: Rp 0");
        cartTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cartTotalLabel.setForeground(C_GREEN);
        bottom.add(cartTotalLabel, BorderLayout.WEST);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);

        JButton btnRefresh  = actionBtn("Refresh",  C_BG_FIELD,  C_BG_CARD_HOV);
        JButton btnRemove   = actionBtn("Remove",   new Color(0x6B,0x1A,0x1A), new Color(0x8B,0x25,0x25));
        JButton btnClear    = actionBtn("Clear All",new Color(0x6B,0x1A,0x1A), new Color(0x8B,0x25,0x25));
        JButton btnCheckout = actionBtn("Checkout ->", C_GREEN, C_GREEN_HOV);
        btnCheckout.setForeground(new Color(0x0A,0x1A,0x0A));

        btnRefresh.addActionListener(e -> refreshCartTable());
        btnRemove.addActionListener(e -> removeSelectedFromCart());
        btnClear.addActionListener(e -> clearCart());
        btnCheckout.addActionListener(e -> {
            Container p = panel;
            while (p != null && !(p instanceof JTabbedPane)) p = p.getParent();
            if (p instanceof JTabbedPane) ((JTabbedPane)p).setSelectedIndex(1);
            refreshPaymentSummary();
        });

        btnRow.add(btnRefresh); btnRow.add(btnRemove);
        btnRow.add(btnClear);  btnRow.add(btnCheckout);
        bottom.add(btnRow, BorderLayout.EAST);

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JButton actionBtn(String label, Color bg, Color hover) {
        JButton b = new JButton(label){
            private Color cur=bg;
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cur);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                super.paintComponent(g);
            }
            { addMouseListener(new MouseAdapter(){
                @Override public void mouseEntered(MouseEvent e){cur=hover;repaint();}
                @Override public void mouseExited(MouseEvent e){cur=bg;repaint();}
            });}
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(Color.WHITE); b.setOpaque(false);
        b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(110, 34));
        return b;
    }

    // ════════════════════════════════════════════════════════════
    // PAYMENT TAB
    // ════════════════════════════════════════════════════════════
    private JPanel buildPembayaranTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(C_BG_DARK);

        JPanel inner = new JPanel(new BorderLayout(20, 0));
        inner.setBackground(C_BG_DARK);

        // Left: summary
        JPanel summaryCard = new JPanel();
        summaryCard.setLayout(new BoxLayout(summaryCard, BoxLayout.Y_AXIS));
        summaryCard.setBackground(C_BG_PANEL);
        summaryCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER,1),
            BorderFactory.createEmptyBorder(20,20,20,20)));

        JLabel sumTitle = new JLabel("Purchase Summary");
        sumTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sumTitle.setForeground(C_TEXT);
        sumTitle.setAlignmentX(LEFT_ALIGNMENT);
        summaryCard.add(sumTitle);
        summaryCard.add(Box.createVerticalStrut(14));

        summaryArea = new JTextArea();
        summaryArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        summaryArea.setForeground(C_TEXT);
        summaryArea.setBackground(C_BG_FIELD);
        summaryArea.setEditable(false);
        summaryArea.setRows(6);
        summaryArea.setBorder(BorderFactory.createEmptyBorder(10,12,10,12));
        summaryArea.setAlignmentX(LEFT_ALIGNMENT);
        JScrollPane ss = new JScrollPane(summaryArea);
        ss.setBorder(BorderFactory.createLineBorder(C_BORDER));
        ss.setAlignmentX(LEFT_ALIGNMENT);
        ss.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        summaryCard.add(ss);
        summaryCard.add(Box.createVerticalStrut(12));

        JButton btnRefresh = actionBtn("Refresh", C_BG_FIELD, C_BG_CARD_HOV);
        btnRefresh.setAlignmentX(LEFT_ALIGNMENT);
        btnRefresh.addActionListener(e -> refreshPaymentSummary());
        summaryCard.add(btnRefresh);
        summaryCard.add(Box.createVerticalStrut(12));

        totalLabel = new JLabel("Total: Rp 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(C_GREEN);
        totalLabel.setAlignmentX(LEFT_ALIGNMENT);
        summaryCard.add(totalLabel);

        // Right: payment method
        JPanel methodCard = new JPanel();
        methodCard.setLayout(new BoxLayout(methodCard, BoxLayout.Y_AXIS));
        methodCard.setBackground(C_BG_PANEL);
        methodCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER,1),
            BorderFactory.createEmptyBorder(20,20,20,20)));

        JLabel metTitle = new JLabel("Payment Method");
        metTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        metTitle.setForeground(C_TEXT);
        metTitle.setAlignmentX(LEFT_ALIGNMENT);
        methodCard.add(metTitle);
        methodCard.add(Box.createVerticalStrut(16));

        paymentGroup = new ButtonGroup();
        rbGopay = styledRadio("GoPay"); rbOvo = styledRadio("OVO"); rbDana = styledRadio("Dana");
        paymentGroup.add(rbGopay); paymentGroup.add(rbOvo); paymentGroup.add(rbDana);

        methodCard.add(rbGopay); methodCard.add(Box.createVerticalStrut(10));
        methodCard.add(rbOvo);   methodCard.add(Box.createVerticalStrut(10));
        methodCard.add(rbDana);  methodCard.add(Box.createVerticalGlue());
        methodCard.add(Box.createVerticalStrut(20));

        JButton btnBayar = steamButton("Pay Now ", C_GREEN, C_GREEN_HOV);
        btnBayar.setForeground(new Color(0x0A,0x1A,0x0A));
        btnBayar.setAlignmentX(LEFT_ALIGNMENT);
        btnBayar.addActionListener(e -> doPayment());
        methodCard.add(btnBayar);

        inner.add(summaryCard, BorderLayout.CENTER);
        inner.add(methodCard, BorderLayout.EAST);
        inner.setPreferredSize(new Dimension(700, 360));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        panel.add(inner, gbc);
        return panel;
    }

    private JRadioButton styledRadio(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rb.setForeground(C_TEXT);
        rb.setBackground(C_BG_PANEL);
        rb.setFocusPainted(false);
        rb.setAlignmentX(LEFT_ALIGNMENT);
        return rb;
    }

    // ════════════════════════════════════════════════════════════
    // PROFIL TAB
    // ════════════════════════════════════════════════════════════
    private JPanel buildProfilTab() {
        JPanel panel = new JPanel(new BorderLayout(0,0));
        panel.setBackground(C_BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel title = new JLabel("Profile & Transaction History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(C_TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(0,0,16,0));
        panel.add(title, BorderLayout.NORTH);

        transaksiTable = new JTable();
        transaksiTable.setRowHeight(30);
        transaksiTable.setShowGrid(false);
        transaksiTable.setBackground(C_BG_CARD);
        transaksiTable.setForeground(C_TEXT);
        transaksiTable.setSelectionBackground(C_NAV_ACTIVE);
        transaksiTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        transaksiTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        transaksiTable.getTableHeader().setBackground(C_BG_MID);
        transaksiTable.getTableHeader().setForeground(C_TEXT_MUTED);

        JScrollPane ts = new JScrollPane(transaksiTable);
        ts.setBorder(BorderFactory.createLineBorder(C_BORDER,1));
        ts.getViewport().setBackground(C_BG_CARD);
        panel.add(ts, BorderLayout.CENTER);

        JButton btnRefresh = actionBtn("Refresh History", C_BG_FIELD, C_BG_CARD_HOV);
        btnRefresh.addActionListener(e -> loadTransaksi());
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bot.setOpaque(false);
        bot.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        bot.add(btnRefresh);
        panel.add(bot, BorderLayout.SOUTH);
        return panel;
    }

    // ════════════════════════════════════════════════════════════
    // CART LOGIC
    // ════════════════════════════════════════════════════════════
    private void setupCartTable() {
        javax.swing.table.DefaultTableModel m = new javax.swing.table.DefaultTableModel(
            new String[]{"Game Name","Category","Price","Type"}, 0){
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        cartTable.setModel(m);
        cartTable.getTableHeader().setReorderingAllowed(false);
    }

    private void addToCart(AbstractProduct p) {
        cart.add(p);
        refreshCartTable();
        JOptionPane.showMessageDialog(this, p.getName()+" added to cart!",
            "Cart", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshCartTable() {
        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel)cartTable.getModel();
        m.setRowCount(0);
        double total=0;
        for (AbstractProduct p : cart) {
            m.addRow(new Object[]{p.getName(),p.getCategory(),p.getPriceFormatted(),
                p instanceof BundleGame?"Bundle":p instanceof FeaturedGame?"Featured":"Game"});
            total+=p.getPrice();
        }
        cartTotalLabel.setText("Total: "+String.format("Rp %,.0f", total));
    }

    private void removeSelectedFromCart() {
        int row = cartTable.getSelectedRow();
        if (row<0){ JOptionPane.showMessageDialog(this,"Select an item first!","Info",JOptionPane.INFORMATION_MESSAGE); return; }
        cart.remove(row); refreshCartTable();
    }
    private void clearCart(){ cart.clear(); refreshCartTable(); }

    // ════════════════════════════════════════════════════════════
    // PAYMENT LOGIC
    // ════════════════════════════════════════════════════════════
    private void refreshPaymentSummary() {
        if (cart.isEmpty()){ summaryArea.setText("No items in cart."); totalLabel.setText("Total: Rp 0"); return; }
        StringBuilder sb = new StringBuilder();
        double total=0;
        for (AbstractProduct p : cart){ sb.append("- ").append(p.getName()).append(" - ").append(p.getPriceFormatted()).append("\n"); total+=p.getPrice(); }
        summaryArea.setText(sb.toString());
        totalLabel.setText("Total: "+String.format("Rp %,.0f", total));
    }

    private void refreshWallet() {
        double gopay = DatabaseManager.getWalletBalance("GoPay");
        double ovo   = DatabaseManager.getWalletBalance("OVO");
        double dana  = DatabaseManager.getWalletBalance("Dana");
        rbGopay.setText(String.format("GoPay   -   Saldo: Rp %,.0f", gopay));
        rbOvo.setText  (String.format("OVO     -   Saldo: Rp %,.0f", ovo));
        rbDana.setText (String.format("Dana    -   Saldo: Rp %,.0f", dana));
    }

    private void doPayment() {
        if (cart.isEmpty()){ JOptionPane.showMessageDialog(this,"Cart is empty!","Info",JOptionPane.INFORMATION_MESSAGE); return; }
        String method = rbGopay.isSelected()?"GoPay":rbOvo.isSelected()?"OVO":rbDana.isSelected()?"Dana":null;
        if (method==null){ JOptionPane.showMessageDialog(this,"Select a payment method first!","Info",JOptionPane.INFORMATION_MESSAGE); return; }
        double total = cart.stream().mapToDouble(AbstractProduct::getPrice).sum();
        double balance = DatabaseManager.getWalletBalance(method);
        if (balance<total){ JOptionPane.showMessageDialog(this,"Insufficient "+method+" balance!\nBalance: "+String.format("Rp %,.0f",balance)+"\nTotal: "+String.format("Rp %,.0f",total),"Payment Failed",JOptionPane.ERROR_MESSAGE); return; }
        for (AbstractProduct p : cart) DatabaseManager.saveTransaction(p.getName(),p.getPrice(),method);
        DatabaseManager.updateWalletBalance(method,balance-total);
        JOptionPane.showMessageDialog(this,"Payment successful!\nTotal: "+String.format("Rp %,.0f",total)+"\nMethod: "+method,"Success",JOptionPane.INFORMATION_MESSAGE);
        clearCart(); refreshPaymentSummary(); refreshWallet(); loadTransaksi();
    }

    // ════════════════════════════════════════════════════════════
    // TRANSAKSI LOGIC
    // ════════════════════════════════════════════════════════════
    private void setupTransaksiTable() {
        javax.swing.table.DefaultTableModel m = new javax.swing.table.DefaultTableModel(
            new String[]{"Game","Price","Method","Time"}, 0){
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        transaksiTable.setModel(m);
        transaksiTable.getTableHeader().setReorderingAllowed(false);
        loadTransaksi();
    }
    private void loadTransaksi() {
        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel)transaksiTable.getModel();
        m.setRowCount(0);
        for (String[] row : DatabaseManager.getTransactions()) m.addRow(row);
    }

    // ════════════════════════════════════════════════════════════
    // IMAGE HELPERS
    // ════════════════════════════════════════════════════════════
    private java.awt.image.BufferedImage loadImageFile(String imageFile) {
        try {
            java.net.URL res = getClass().getResource("/GAMBAR/"+imageFile);
            if (res!=null){ java.awt.image.BufferedImage img=javax.imageio.ImageIO.read(res); if(img!=null)return img; }
            java.io.File f = new java.io.File("src/GAMBAR/"+imageFile);
            if (!f.exists()) f = new java.io.File("GAMBAR/"+imageFile);
            if (f.exists()) return javax.imageio.ImageIO.read(f);
        } catch (Exception ignored){}
        return null;
    }

    private void loadImageAsync(JLabel label, String imageFile, int w, int h) {
        new Thread(() -> {
            java.awt.image.BufferedImage img = loadImageFile(imageFile);
            if (img!=null){
                java.awt.Image scaled = img.getScaledInstance(w,h,java.awt.Image.SCALE_SMOOTH);
                SwingUtilities.invokeLater(() -> label.setIcon(new ImageIcon(scaled)));
            }
        }).start();
    }
}
