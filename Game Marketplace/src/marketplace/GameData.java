package marketplace;

import java.sql.*;

public class GameData {

    public static void seedIfEmpty() {
        try {
            ResultSet rs = DatabaseManager.getConnection().createStatement()
                .executeQuery("SELECT COUNT(*) FROM games");
            rs.next();
            if (rs.getInt(1) > 0) return;
        } catch (SQLException e) { return; }

        // {name, price, category, description, imageFile, rating, developer, type}
        // imageFile = nama file di folder GAMBAR/ (flat, tanpa subfolder)
        Object[][] games = {
            {"Elden Ring", 459000, "RPG",
             "Open world action RPG dari FromSoftware. Jelajahi Lands Between yang luas dan taklukkan para Demigod dalam pertempuran epik. Dunia yang kaya dengan lore mendalam dan tantangan yang memuaskan.",
             "elden ring.jpg", 4.9, "FromSoftware", "Featured"},

            {"Red Dead Redemption 2", 399000, "Action",
             "Petualangan epik di dunia koboi Amerika tahun 1899. Mainkan Arthur Morgan dalam kisah persahabatan, pengkhianatan, dan penebusan diri di era Wild West yang memukau dengan grafis luar biasa.",
             "rdr2.jpg", 4.8, "Rockstar Games", "Featured"},

            {"Cyberpunk 2077", 349000, "RPG",
             "RPG futuristik di kota Night City yang dystopian. Jadilah V, seorang mercenary yang mencari immortality di dunia penuh teknologi dan korupsi. Penuh pilihan cerita yang mempengaruhi dunia.",
             "cyberpunk 2077.jpg", 4.5, "CD Projekt Red", "Game"},

            {"Baldur's Gate 3", 499000, "RPG",
             "RPG turn-based terbaik berdasarkan Dungeons & Dragons. Buat karakter unikmu dan jalani petualangan epik bersama companions dengan ribuan pilihan dialog dan ending yang berbeda.",
             "baldurs gate.jpg", 4.9, "Larian Studios", "Featured"},

            {"Grand Theft Auto V", 149000, "Action",
             "Dunia open world terbesar dan paling detail. Mainkan tiga karakter berbeda dalam kisah kejahatan epik di Los Santos. Mode online dengan ratusan aktivitas bersama pemain lain di seluruh dunia.",
             "GTA.jpg", 4.7, "Rockstar Games", "Game"},

            {"Stardew Valley", 89000, "Simulation",
             "Bangun pertanian impianmu, kenali penduduk desa, dan temukan misteri lembah. Game farming simulator yang adiktif dengan konten yang terus diperbarui oleh pengembang tunggalnya.",
             "stardew valley.jpg", 4.9, "ConcernedApe", "Game"},

            {"Hades", 149000, "Action",
             "Roguelike terbaik 2020. Kabur dari dunia bawah tanah sebagai Zagreus dengan bantuan para dewa Olympus. Setiap run berbeda dengan senjata dan blessing yang beragam dan cerita yang berkembang.",
             "hades.jpg", 4.9, "Supergiant Games", "Game"},

            {"The Witcher 3: Wild Hunt", 199000, "RPG",
             "RPG terbaik sepanjang masa. Mainkan Geralt of Rivia dalam perburuan epik menemukan anaknya. Dunia terbuka penuh quest berkualitas dan pilihan moral yang kompleks.",
             "the witcher.jpg", 4.9, "CD Projekt Red", "Game"},

            {"Sekiro: Shadows Die Twice", 359000, "Action",
             "Action game dari FromSoftware dengan setting feudal Jepang. Mainkan shinobi muda yang mencari balas dendam. Sistem combat berbasis parry yang sangat memuaskan dan menantang.",
             "sekiro.jpg", 4.8, "FromSoftware", "Game"},

            {"God of War Ragnarok", 449000, "Action",
             "Kratos dan Atreus menghadapi Ragnarok, akhir dari segalanya. Petualangan epik melintasi sembilan alam Norse mythology dengan combat yang brutal dan cerita yang mengharukan.",
             "god of war.jpg", 4.9, "Santa Monica Studio", "Featured"},

            {"Ghost of Tsushima", 359000, "Action",
             "Mainkan samurai terakhir yang mempertahankan pulau Tsushima dari invasi Mongol. Open world Jepang feudal yang indah dengan combat katana yang sangat memuaskan dan cerita yang menyentuh.",
             "ghost of tsushima.jpg", 4.8, "Sucker Punch Productions", "Game"},

            {"Persona 5 Royal", 299000, "RPG",
             "JRPG terbaik dari Atlus. Mainkan Joker dan Phantom Thieves yang mencuri hati orang-orang jahat di Tokyo modern. Sistem social link, dungeon crawling, dan cerita yang menakjubkan.",
             "persona 5.jpg", 4.9, "Atlus", "Featured"},

            {"Mass Effect Legendary Edition", 299000, "RPG",
             "Trilogi space opera terbaik yang pernah ada. Mainkan Commander Shepard dalam misi menyelamatkan galaksi. Pilihan dialog yang mempengaruhi cerita di tiga game sekaligus dalam satu paket.",
             "mass effect.jpg", 4.8, "BioWare", "Bundle"},

            {"Monster Hunter World", 249000, "Action",
             "Berburu monster raksasa di ekosistem dunia yang hidup. Game action RPG yang memadukan hunting mechanics mendalam dengan crafting sistem senjata dan armor dari material monster.",
             "monster hunter.jpg", 4.7, "Capcom", "Game"},

            {"Resident Evil 4", 329000, "Action",
             "Remake masterpiece horror action Capcom. Leon S. Kennedy menyelamatkan putri presiden dari sekte misterius di Eropa. Gameplay yang disempurnakan dengan grafis memukau dan konten baru.",
             "resident evil 4.jpg", 4.8, "Capcom", "Game"},

            {"Devil May Cry 5", 249000, "Action",
             "Hack and slash terbaik dari Capcom. Mainkan Nero, Dante, dan V dalam misi menghentikan iblis. Sistem combo yang dalam dan sangat memuaskan untuk dikuasai.",
             "devil may cry.jpg", 4.8, "Capcom", "Game"},

            {"DOOM Eternal", 279000, "Action",
             "FPS tercepat dan paling brutal. Bunuh demon dengan cara paling kreatif sambil bergerak non-stop. Gameplay loop yang adiktif dengan sistem resource management yang unik.",
             "doom eternal.jpg", 4.7, "id Software", "Game"},

            {"Disco Elysium", 199000, "RPG",
             "RPG berbasis dialog paling unik yang pernah ada. Mainkan detektif yang kehilangan ingatannya dalam investigasi pembunuhan di kota fiksi Revachol. Penuh humor, filsafat, dan pilihan tak terduga.",
             "disco elysium.jpg", 4.8, "ZA/UM", "Game"},

            {"Terraria", 69000, "Sandbox",
             "Game sandbox 2D dengan konten tak terbatas. Gali, bangun, bertarung, dan eksplorasi dunia yang procedurally generated. Lebih dari 400 musuh, 20 boss, dan ribuan item untuk ditemukan.",
             "terraria.jpg", 4.9, "Re-Logic", "Game"},

            {"Rust", 249000, "Action",
             "Survival game multiplayer yang brutal. Bangun base, kumpulkan resource, dan pertahankan dirimu dari pemain lain. Dinamika sosial yang unik antara kerja sama dan pengkhianatan.",
             "rust.jpg", 4.3, "Facepunch Studios", "Game"},

            {"RimWorld", 279000, "Strategy",
             "Simulator koloni sci-fi yang dikendalikan oleh AI storyteller. Bangun koloni dari penyintas pesawat luar angkasa dan hadapi ancaman dari semua arah dengan sistem yang sangat mendalam.",
             "rimworld.jpg", 4.9, "Ludeon Studios", "Game"},

            {"Satisfactory", 199000, "Simulation",
             "Game factory building first-person yang adiktif. Bangun pabrik otomatis raksasa di planet alien untuk memenuhi tujuan korporasi. Eksplorasi, mining, dan conveyor belt tanpa akhir.",
             "satisfactory.jpg", 4.8, "Coffee Stain Studios", "Game"},

            {"Subnautica", 179000, "Sandbox",
             "Survival game bawah laut yang memukau dan mendebarkan. Eksplorasi lautan alien yang indah sekaligus berbahaya. Bangun base bawah air dan temukan misteri planet Ocean yang ditinggalkan.",
             "subnautica.jpg", 4.8, "Unknown Worlds Entertainment", "Game"},

            {"No Man's Sky", 229000, "Sandbox",
             "Eksplorasi galaksi tak terbatas dengan miliaran planet yang unik. Bangun base, terbang antar planet, dan temukan alien civilizations. Update terus-menerus yang selalu menambah konten baru.",
             "no mans sky.jpg", 4.5, "Hello Games", "Game"},

            {"Kerbal Space Program", 159000, "Simulation",
             "Bangun dan terbangkan roket ke luar angkasa dengan fisika realistis. Program ruang angkasa untuk alien hijau lucu. Edukatif sekaligus sangat menghibur dan menantang.",
             "kerbal space.jpg", 4.8, "Squad", "Game"},

            {"Microsoft Flight Simulator", 299000, "Simulation",
             "Simulator penerbangan paling realistis yang pernah ada. Terbangkan ratusan pesawat di atas replika Bumi yang detail menggunakan data satelit dan AI. Grafis yang benar-benar memukau.",
             "flight simulator.jpg", 4.7, "Asobo Studio", "Game"},

            {"Euro Truck Simulator 2", 119000, "Simulation",
             "Jadilah sopir truk profesional di Eropa. Kendarai berbagai truk melintasi jalan-jalan Eropa yang detail. Bangun perusahaan trukmu sendiri dan pekerjakan driver lain. Sangat relaxing.",
             "euro truck.jpg", 4.7, "SCS Software", "Game"},

            {"Farming Simulator 22", 199000, "Simulation",
             "Kelola pertanian modern dengan ratusan mesin berlisensi asli. Tanam, panen, dan jual hasil bumi. Mode multiplayer untuk bertani bersama teman.",
             "farming simulator.jpg", 4.3, "Giants Software", "Game"},

            {"House Flipper 2", 179000, "Simulation",
             "Beli rumah bobrok, renovasi total, dan jual dengan profit. Simulator renovasi rumah yang sangat memuaskan. Desain interior sesukamu dengan ratusan furnitur dan material pilihan.",
             "house flipper.jpg", 4.5, "Empyrean", "Game"},

            {"Teardown", 149000, "Sandbox",
             "Game heist dengan engine fisika voxel yang revolusioner. Hancurkan segalanya untuk merencanakan rute kabur sempurna. Setiap misi membutuhkan kreativitas dan perencanaan matang.",
             "teardown.jpg", 4.8, "Tuxedo Labs", "Game"},

            {"Garry's Mod", 49000, "Sandbox",
             "Sandbox game paling bebas di dunia. Tidak ada tujuan, hanya kreativitas tanpa batas. Ribuan mod komunitas untuk pengalaman yang selalu berbeda. Klasik yang tidak pernah mati.",
             "garrys mod.jpg", 4.7, "Facepunch Studios", "Game"},

            {"Cities: Skylines", 149000, "Strategy",
             "Simulator kota terbaik. Bangun kota impianmu dari nol dengan sistem traffic, utilitas, dan zoning yang realistis. Ribuan mod komunitas untuk konten tak terbatas.",
             "cities skylines.jpg", 4.7, "Colossal Order", "Game"},

            {"Sid Meier's Civilization VI", 259000, "Strategy",
             "Bangun peradaban dari zaman batu hingga era luar angkasa. Turn-based strategy terbaik dengan enam era sejarah, puluhan civs unik, dan endless replayability.",
             "civilization.jpg", 4.5, "Firaxis Games", "Game"},

            {"Stellaris", 229000, "Strategy",
             "Grand strategy 4X di luar angkasa. Bangun empire galaktik, temui alien civilizations, dan taklukkan bintang-bintang. Sistem random events yang membuat setiap playthrough berbeda.",
             "stellaris.jpg", 4.6, "Paradox Development Studio", "Game"},

            {"Crusader Kings III", 299000, "Strategy",
             "Grand strategy medieval. Kelola dinasti kerajaanmu selama berabad-abad dengan intrigue politik, perang, dan hubungan keluarga yang kompleks.",
             "crusader kings.jpg", 4.7, "Paradox Development Studio", "Game"},

            {"Total War: WARHAMMER III", 329000, "Strategy",
             "RTS taktis dengan skala pertempuran epik di dunia fantasy WARHAMMER. Kendalikan tentara raksasa dari berbagai ras dengan unit unik.",
             "total war.jpg", 4.5, "Creative Assembly", "Game"},

            {"XCOM 2", 149000, "Strategy",
             "Tactical turn-based strategy terbaik. Pimpin perlawanan manusia melawan invasi alien. Permanen death system membuat setiap keputusan terasa sangat berat.",
             "xcom 2.jpg", 4.7, "Firaxis Games", "Game"},

            {"Company of Heroes 3", 249000, "Strategy",
             "RTS World War II terbaik dengan sistem destruksi yang memukau. Rencanakan operasi di peta taktis, lalu pimpin pasukan dalam pertempuran real-time yang intens.",
             "company of heroes.jpg", 4.3, "Relic Entertainment", "Game"},

            {"Age of Empires IV", 299000, "Strategy",
             "RTS klasik yang diperbarui untuk era modern. Bangun peradaban dari Dark Age hingga Imperial Age. Delapan civs unik dengan mekanik berbeda dan kampanye berbasis sejarah asli.",
             "age of empires.jpg", 4.5, "World's Edge", "Game"},

            {"Anno 1800", 229000, "Strategy",
             "City builder dan ekonomi simulator di era revolusi industri. Bangun kota yang berkembang, kelola rantai produksi kompleks, dan eksplorasi Dunia Baru.",
             "anno 1800.jpg", 4.7, "Ubisoft Mainz", "Game"},

            {"EA SPORTS FC 26", 699000, "Sports",
             "Simulasi sepak bola terealistis dengan lisensi lengkap. Main dengan klub dan pemain favoritmu dari semua liga dunia. Mode Ultimate Team, Career Mode, dan Pro Clubs.",
             "ea fc 26.jpg", 4.2, "EA Sports", "Game"},

            {"F1 24", 399000, "Sports",
             "Simulator Formula 1 resmi dengan seluruh tim dan sirkuit musim 2024. Career mode mendalam, My Team mode, dan gameplay realistis untuk penggemar motorsport.",
             "f1 24.jpg", 4.3, "Codemasters", "Game"},

            {"NBA 2K26", 549000, "Sports",
             "Simulasi basket NBA terlengkap dengan grafis pemain yang ultra-realistis. Mode MyCareer, MyTeam, dan MyLeague dengan database pemain yang selalu diperbarui.",
             "nba 2k26.jpg", 4.1, "Visual Concepts", "Game"},

            {"WWE 2K24", 449000, "Sports",
             "Game gulat profesional WWE dengan roster terlengkap. Mainkan superstar favoritmu dalam berbagai match type dan MyRise career mode.",
             "wwe 2k24.jpg", 4.2, "Visual Concepts", "Game"},

            {"PGA TOUR 2K23", 249000, "Sports",
             "Simulator golf profesional dengan berbagai course berlisensi resmi. Career mode mendalam, online tournaments, dan course designer.",
             "pga tour.jpg", 4.1, "HB Studios", "Game"},

            {"Tony Hawk's Pro Skater 1+2", 249000, "Sports",
             "Remake klasik skateboarding terbaik. Dua game legendaris dalam satu paket dengan grafis modern namun gameplay yang sama adiktifnya.",
             "tony hawk.jpg", 4.7, "Vicarious Visions", "Game"},

            {"Assetto Corsa Competizione", 199000, "Sports",
             "Simulator balap GT paling realistis. Rasakan sensasi mengemudi mobil GT3 dan GT4 di sirkuit-sirkuit terkenal dunia dengan fisika yang sangat akurat.",
             "assetto corsa.jpg", 4.6, "Kunos Simulazioni", "Game"},

            {"Forza Horizon 6", 499000, "Sports",
             "Open world racing game terbaik dengan ratusan mobil berlisensi. Balapan bebas di dunia terbuka yang indah dengan cuaca dinamis dan siklus siang-malam.",
             "forza horizon.jpg", 4.8, "Playground Games", "Featured"},

            {"Football Manager 2026", 399000, "Sports",
             "Manajemen sepak bola paling detail di dunia. Kelola tim favoritmu dari taktik, transfer pemain, hingga hubungan dengan media. Database 800.000 pemain nyata.",
             "football manager.jpg", 4.5, "Sports Interactive", "Game"},

            {"eFootball", 0, "Sports",
             "Game sepak bola gratis dari Konami dengan gameplay yang fokus pada kontrol pemain individual. Lisensi resmi beberapa klub top Eropa.",
             "efootball.jpg", 3.8, "Konami", "Game"},

            {"The Elder Scrolls V: Skyrim", 149000, "RPG",
             "RPG open world legendaris yang tidak pernah mati. Jelajahi Skyrim yang luas sebagai Dragonborn, sang penakluk naga. Ratusan quest, dungeon, dan mod komunitas.",
             "skyrim.jpg", 4.7, "Bethesda Game Studios", "Game"},

            {"The Sims 4", 0, "Simulation",
             "Simulator kehidupan paling populer di dunia, kini gratis! Buat Sim unikmu, bangun rumah impian, dan jalani kehidupan sesukamu. Ratusan DLC untuk konten tak terbatas.",
             "sims 4.jpg", 4.0, "Maxis", "Game"},
        };

        for (Object[] g : games) {
            DatabaseManager.insertGame(
                (String) g[0],
                ((Number) g[1]).doubleValue(),
                (String) g[2],
                (String) g[3],
                (String) g[4],
                ((Number) g[5]).doubleValue(),
                (String) g[6],
                (String) g[7]
            );
        }
        System.out.println("52 game berhasil dimuat.");
    }
}
