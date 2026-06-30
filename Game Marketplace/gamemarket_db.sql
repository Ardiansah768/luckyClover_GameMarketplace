-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 27, 2026 at 10:17 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gamemarket_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `games`
--

CREATE TABLE `games` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` double NOT NULL DEFAULT 0,
  `category` varchar(100) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `rating` double DEFAULT 4,
  `developer` varchar(255) DEFAULT NULL,
  `badge` varchar(100) DEFAULT '',
  `type` varchar(50) DEFAULT 'Game'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `games`
--

INSERT INTO `games` (`id`, `name`, `price`, `category`, `description`, `image_url`, `rating`, `developer`, `badge`, `type`) VALUES
(16, 'Elden Ring', 459000, 'RPG', 'Open world action RPG dari FromSoftware. Jelajahi Lands Between dan taklukkan para Demigod.', 'elden ring.jpg', 4.9, 'FromSoftware', '', 'Featured'),
(17, 'Red Dead Redemption 2', 399000, 'Action', 'Petualangan epik di dunia koboi Amerika tahun 1899. Grafis memukau, cerita luar biasa.', 'rdr2.jpg', 4.8, 'Rockstar Games', '', 'Featured'),
(18, 'Minecraft', 189000, 'Sandbox', 'Bangun duniamu sendiri! Game sandbox terlaris sepanjang masa dengan kreativitas tanpa batas.', 'minicrft.jpg', 4.7, 'Mojang Studios', '', 'Game'),
(19, 'Cyberpunk 2077', 349000, 'RPG', 'RPG futuristik di kota Night City. Jadilah mercenary dan ungkap misteri immortality.', 'cp.jpg', 4.5, 'CD Projekt Red', '', 'Game'),
(20, 'FIFA 24', 299000, 'Sports', 'Simulasi sepak bola terealistis. Main dengan klub favoritmu di berbagai liga dunia.', 'fifa.jpg', 4.2, 'EA Sports', '', 'Game'),
(21, 'Counter-Strike 2', 0, 'Action', 'Game FPS kompetitif paling populer di dunia. Gratis dimainkan!', 'cs.jpg', 4.6, 'Valve', '', 'Game'),
(22, 'Stardew Valley', 89000, 'Simulation', 'Bangun pertanian impianmu, kenali penduduk desa, dan temukan misteri lembah.', 'SV.jpg', 4.9, 'ConcernedApe', '', 'Game'),
(23, 'Among Us', 49000, 'Strategy', 'Game deduksi sosial multiplayer. Temukan pengkhianat sebelum terlambat!', 'Among us.jpg', 4.3, 'Innersloth', '', 'Game'),
(24, 'The Witcher 3', 199000, 'RPG', 'RPG terbaik sepanjang masa. Mainkan Geralt of Rivia dalam perburuan anaknya.', 'header.jpg', 4.9, 'CD Projekt Red', '', 'Game'),
(25, 'GTA V', 149000, 'Action', 'Dunia open world terbesar. Mainkan tiga karakter berbeda dalam kisah kejahatan epik.', 'GTA.jpg', 4.7, 'Rockstar Games', '', 'Game'),
(26, 'Rockstar Bundle', 549000, 'Action', 'Paket lengkap: GTA V + Red Dead Redemption 2. Hemat 30%!', 'GTA.jpg', 4.8, 'Rockstar Games', '', 'Bundle'),
(27, 'Hollow Knight', 79000, 'Action', 'Metroidvania indah di dunia serangga bawah tanah. Tantangan tinggi, seni memukau.', 'header.jpg', 4.8, 'Team Cherry', '', 'Game'),
(28, 'Civilization VI', 259000, 'Strategy', 'Bangun peradaban dari nol hingga menaklukkan dunia. Strategi turn-based terbaik.', 'civilization.jpg', 4.5, 'Firaxis Games', '', 'Game'),
(29, 'Apex Legends', 0, 'Action', 'Battle royale gratis dengan hero unik. Tim 3 orang, siapa yang bertahan terakhir?', 'apex.jpg', 4.4, 'Respawn Entertainment', '', 'Game'),
(30, 'Hades', 149000, 'Action', 'Roguelike terbaik 2020. Kabur dari dunia bawah tanah dengan bantuan para dewa Olympus.', 'header.jpg', 4.9, 'Supergiant Games', '', 'Game');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id` int(11) NOT NULL,
  `game_name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `payment_method` varchar(100) DEFAULT NULL,
  `waktu` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id`, `game_name`, `price`, `payment_method`, `waktu`) VALUES
(1, 'Hollow Knight', 79000, 'GoPay', '2026-06-27 08:01:29');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(10) NOT NULL DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`, `role`) VALUES
('admin', 'admin123', 'admin'),
('naufalll', 'naufalll', 'user');

-- --------------------------------------------------------

--
-- Table structure for table `wallet`
--

CREATE TABLE `wallet` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `balance` double DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `wallet`
--

INSERT INTO `wallet` (`id`, `name`, `balance`) VALUES
(1, 'GoPay', 71000),
(2, 'OVO', 200000),
(3, 'Dana', 100000);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `games`
--
ALTER TABLE `games`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `wallet`
--
ALTER TABLE `wallet`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `games`
--
ALTER TABLE `games`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `wallet`
--
ALTER TABLE `wallet`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
