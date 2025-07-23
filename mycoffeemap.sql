-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mycoffeemap
-- ------------------------------------------------------
-- Server version	8.0.40

CREATE DATABASE IF NOT EXISTS mycoffeemap;
USE mycoffeemap;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bean_preference`
--

DROP TABLE IF EXISTS `bean_preference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bean_preference` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `roast_level` enum('DARK','LIGHT','MEDIUM') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK11n8nrd7hqvkn3pv49429name` (`user_id`),
  CONSTRAINT `FK11n8nrd7hqvkn3pv49429name` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bean_preference`
--

LOCK TABLES `bean_preference` WRITE;
/*!40000 ALTER TABLE `bean_preference` DISABLE KEYS */;
/*!40000 ALTER TABLE `bean_preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bean_preference_flavor`
--

DROP TABLE IF EXISTS `bean_preference_flavor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bean_preference_flavor` (
  `bean_preference_id` bigint NOT NULL,
  `flavor` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  KEY `FK5qm03god0pyxo9a2ugmy0p9vi` (`bean_preference_id`),
  CONSTRAINT `FK5qm03god0pyxo9a2ugmy0p9vi` FOREIGN KEY (`bean_preference_id`) REFERENCES `bean_preference` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bean_preference_flavor`
--

LOCK TABLES `bean_preference_flavor` WRITE;
/*!40000 ALTER TABLE `bean_preference_flavor` DISABLE KEYS */;
/*!40000 ALTER TABLE `bean_preference_flavor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `beans`
--

DROP TABLE IF EXISTS `beans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beans` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `flavor_notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `origin` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `roast_level` enum('DARK','LIGHT','MEDIUM') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnitishruixdgrvm6h5nhjbftx` (`user_id`),
  CONSTRAINT `FKnitishruixdgrvm6h5nhjbftx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beans`
--

LOCK TABLES `beans` WRITE;
/*!40000 ALTER TABLE `beans` DISABLE KEYS */;
INSERT INTO `beans` VALUES (1,'華やかな香りと明るい酸味が調和した豆です。','Floral, Fruity, Chocolate','/images/beans/yirgacheffe.png','エチオピア イルガチェフェ','Ethiopia','LIGHT',NULL),(2,'バランスの取れたボディとナッツのような風味が特徴です。','Nutty, Chocolate','/images/beans/colombia.png','コロンビア スプレモ','Colombia','MEDIUM',NULL),(3,'重厚なボディと深みのある風味でエスプレッソに最適です。','Earthy, Caramel, Smoky','/images/beans/brazil.png','ブラジル サントス','Brazil','DARK',NULL),(4,'豊かな香りと深い甘みが絶妙に調和しています。','Spicy, Chocolate, Earthy','/images/beans/guatemala.png','グアテマラ アンティグア','Guatemala','MEDIUM',NULL),(5,'しっかりとしたボディとユニークなハーブの香りが魅力です。','Earthy, Spicy, Herbal','/images/beans/mandheling.png','インドネシア マンデリン','Indonesia','DARK',NULL),(6,'明るい果実の香りとキャラメルの甘みが特徴の豆です。','Caramel, Fruity, Sweet','/images/beans/costarica.png','コスタリカ タラズ','Costa Rica','LIGHT',NULL),(7,'ナッツの香ばしさと穏やかな甘みが感じられる柔らかい味わい。','Nutty, Chocolate, Earthy','/images/beans/mexico.png','メキシコ アルトゥーラ','Mexico','MEDIUM',NULL),(8,'有機栽培ならではのクリアな花の香りと優しい甘みが魅力です。','Floral, Sweet, Herbal','/images/beans/peru.png','ペルー オーガニック','Peru','LIGHT',NULL),(9,'ジューシーでベリー系の酸味とワインのようなコクが特徴です。','Fruity, Bright, Winey','/images/beans/kenya.png','ケニア AA','Kenya','LIGHT',NULL),(10,'柑橘系の爽やかな酸味とスパイシーな後味が印象的な豆です。','Citrus, Bright, Spicy','/images/beans/tanzania.png','タンザニア キリマンジャロ','Tanzania','MEDIUM',NULL),(11,'紅茶のような繊細な香りと複雑で上品なフレーバーが楽しめます。','Floral, Tea-like, Complex','/images/beans/geisha.png','パナマ ゲイシャ','Panama','LIGHT',NULL),(12,'まろやかで優しい口当たり。ナッツのような甘みが特徴です。','Nutty, Smooth, Mild','/images/beans/kona.png','ハワイ コナ','USA (Hawaii)','MEDIUM',NULL),(13,'透明感のある味わいと穏やかな果実の甘みが魅力の豆です。','Fruity, Clean, Sweet','/images/beans/rwanda.png','ルワンダ ブルボン','Rwanda','MEDIUM',NULL);
/*!40000 ALTER TABLE `beans` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_likes`
--

DROP TABLE IF EXISTS `board_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_likes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `liked_at` datetime(6) DEFAULT NULL,
  `board_id` bigint NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9aqa1yn5n4xcg2t5exv1lwgfd` (`user_id`,`board_id`),
  KEY `FKq0j8j4h4bhcsgsndjaoqr0cmy` (`board_id`),
  CONSTRAINT `FKn6r8psm1l68teg04e86e19lev` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKq0j8j4h4bhcsgsndjaoqr0cmy` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_likes`
--

LOCK TABLES `board_likes` WRITE;
/*!40000 ALTER TABLE `board_likes` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `boards`
--

DROP TABLE IF EXISTS `boards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `boards` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `bean_id` bigint DEFAULT NULL,
  `cafe_id` bigint DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8cc7g3f0radmng7eldmg7nf7o` (`bean_id`),
  KEY `FK3ycx13iti0cnpioqaiuatfyd8` (`cafe_id`),
  KEY `FK7kt8hby5livgmjj15f79e9t6v` (`user_id`),
  CONSTRAINT `FK3ycx13iti0cnpioqaiuatfyd8` FOREIGN KEY (`cafe_id`) REFERENCES `cafes` (`id`),
  CONSTRAINT `FK7kt8hby5livgmjj15f79e9t6v` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK8cc7g3f0radmng7eldmg7nf7o` FOREIGN KEY (`bean_id`) REFERENCES `beans` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `boards`
--

LOCK TABLES `boards` WRITE;
/*!40000 ALTER TABLE `boards` DISABLE KEYS */;
INSERT INTO `boards` VALUES (1,'フルーティーで酸味のある味わいが最高でした。浅煎り好きにおすすめです。','2025-07-21 11:41:27.000000','sakura.png',NULL,NULL,5,'エチオピアの魅力再発見','2025-07-21 11:41:27.000000',1,1,1),(2,'ナッツのような香ばしさが印象的で、毎朝の一杯にぴったりです。','2025-07-21 11:41:27.000000','yume.png',NULL,NULL,4,'コロンビア豆でほっと一息','2025-07-21 11:41:27.000000',2,2,2),(3,'酸味と苦味のバランスが取れた味で、誰にでも勧められます。','2025-07-21 11:41:27.000000','midorinoki.png',NULL,NULL,4,'毎日飲みたいグアテマラ','2025-07-21 11:41:27.000000',3,1,3),(4,'深煎りで香りが強く、エスプレッソにも合う万能豆です。','2025-07-21 11:41:27.000000','hibiya.png',NULL,NULL,5,'エスプレッソに最適なブレンド','2025-07-21 11:41:27.000000',4,3,4),(5,'カフェの雰囲気がとても良く、豆との相性も抜群でした！','2025-07-21 11:41:27.000000','slow.png',NULL,NULL,5,'お気に入りのカフェ発見','2025-07-21 11:41:27.000000',2,4,5);
/*!40000 ALTER TABLE `boards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cafe_beans`
--

DROP TABLE IF EXISTS `cafe_beans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cafe_beans` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `linked_at` datetime(6) DEFAULT NULL,
  `use_type` enum('대표원두','시즌원두','한정판') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bean_id` bigint DEFAULT NULL,
  `cafe_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8l2fs8ewirl1v5ufc7uwbx7p1` (`bean_id`),
  KEY `FKk1i289ljdaew4cvo898q6tfrr` (`cafe_id`),
  CONSTRAINT `FK8l2fs8ewirl1v5ufc7uwbx7p1` FOREIGN KEY (`bean_id`) REFERENCES `beans` (`id`),
  CONSTRAINT `FKk1i289ljdaew4cvo898q6tfrr` FOREIGN KEY (`cafe_id`) REFERENCES `cafes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cafe_beans`
--

LOCK TABLES `cafe_beans` WRITE;
/*!40000 ALTER TABLE `cafe_beans` DISABLE KEYS */;
INSERT INTO `cafe_beans` VALUES (1,NULL,NULL,1,1),(2,NULL,NULL,2,2),(3,NULL,NULL,3,3),(4,NULL,NULL,4,4),(5,NULL,NULL,5,5),(6,NULL,NULL,6,6),(7,NULL,NULL,1,7),(8,NULL,NULL,2,8),(9,NULL,NULL,3,9),(10,NULL,NULL,4,10),(11,NULL,NULL,9,11),(12,NULL,NULL,10,12),(13,NULL,NULL,11,13),(14,NULL,NULL,12,14),(15,NULL,NULL,13,15);
/*!40000 ALTER TABLE `cafe_beans` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cafes`
--

DROP TABLE IF EXISTS `cafes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cafes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4jxkfavkvrmuik1r0h70c4wh4` (`user_id`),
  CONSTRAINT `FK4jxkfavkvrmuik1r0h70c4wh4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cafes`
--

LOCK TABLES `cafes` WRITE;
/*!40000 ALTER TABLE `cafes` DISABLE KEYS */;
INSERT INTO `cafes` VALUES (1,'東京都 渋谷区','2025-07-21 11:41:26.000000','渋谷の路地裏にある隠れ家的ロースターカフェ。フローラル系の豆をハンドドリップで提供します。','/images/cafe/sakura.png','カフェさくら',NULL),(2,'東京都 吉祥寺','2025-07-21 11:41:26.000000','自然をモチーフにした落ち着いた空間。ミディアムローストのナッツ系豆を使用しています。','/images/cafe/quiet_tree.png','静かな木',NULL),(3,'東京都 港区','2025-07-21 11:41:26.000000','コーヒー実験室をコンセプトにしたモダンな空間。酸味のあるフルーティーな豆で多彩な抽出を提供。','/images/cafe/cafelab.png','トーキョーカフェラボ',NULL),(4,'東京都 中目黒','2025-07-21 11:41:26.000000','ヴィンテージ感のある静かなカフェ。ダークロースト豆で深いボディとスモーキーな香りを楽しめます。','/images/cafe/yume.png','カフェ夢',NULL),(5,'東京都 品川区','2025-07-21 11:41:26.000000','通勤途中のビジネスマンに人気のテイクアウト専門店。キャラメル風味の豆とまろやかなラテが好評。','/images/cafe/haruharu.png','ハルハルコーヒー',NULL),(6,'東京都 池袋','2025-07-21 11:41:26.000000','植物に囲まれてくつろげる小さなカフェ。アーシーな風味のブレンドを提供しています。','/images/cafe/midorinoki.png','みどりの木',NULL),(7,'東京都 銀座','2025-07-21 11:41:26.000000','高級感あるインテリアとデザートが人気の銀座を代表するカフェ。チョコレートの香りの豆を使用。','/images/cafe/momoyama.png','カフェ桃山',NULL),(8,'東京都 日比谷','2025-07-21 11:41:26.000000','コーヒーとクラフトビールの融合カフェ。スパイシーな風味が特徴です。','/images/cafe/hibiya.png','日比谷ブルワーズ',NULL),(9,'東京都 浅草','2025-07-21 11:41:26.000000','伝統とモダンが融合する街で、クラシックなブレンド豆を提供しています。','/images/cafe/asakusa.png','浅草ブレンド',NULL),(10,'東京都 恵比寿','2025-07-21 11:41:26.000000','忙しい東京でひと息つける空間。アーシーとナッツのブレンドを楽しめます。','/images/cafe/slow.png','東京スロー',NULL),(11,'東京都 代々木','2025-07-21 11:41:26.000000','ケニアAA豆を使ったジューシーなドリップが特徴のシングルオリジン専門店。','/images/cafe/kenya_house.png','ケニアハウス',NULL),(12,'東京都 立川','2025-07-21 11:41:26.000000','タンザニア産のスパイシーなキリマンジャロを使用。すっきりとした味わいが魅力。','/images/cafe/kilimanjaro.png','キリマン珈琲舎',NULL),(13,'東京都 中央区','2025-07-21 11:41:26.000000','ゲイシャ豆の上品な香りを味わえる、スペシャルティ専門のコーヒースタンド。','/images/cafe/geisha.png','ゲイシャスタンド',NULL),(14,'東京都 大田区','2025-07-21 11:41:26.000000','ハワイ・コナのやわらかな甘みとナッツの風味が香るリラックスカフェ。','/images/cafe/kona.png','コナベイク',NULL),(15,'東京都 文京区','2025-07-21 11:41:26.000000','ルワンダ産ブルボン豆を使用した、クリーンで甘みあるブレンドが自慢のカフェ。','/images/cafe/rwanda.png','ブルボンヒルズ',NULL);
/*!40000 ALTER TABLE `cafes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `board_id` bigint NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo7l08y12wlmjbt81ey5wysk2g` (`board_id`),
  KEY `FK8omq0tc18jd43bu5tjh6jvraq` (`user_id`),
  CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKo7l08y12wlmjbt81ey5wysk2g` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nick` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `pass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `join_date` datetime(6) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `token_expiry` datetime(6) DEFAULT NULL,
  `verification_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deleted_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'user1@test.com','user1','$2a$10$dnveZoce7hsHuuPK9XMdu.Fl//ukiCB3aLQzBqHdXz5L/qLi5spB6','3627e6f8-4251-4b80-9db4-e35106a5d444.PNG','2025-07-08 09:51:18.179018',_binary '',NULL,NULL,_binary '\0',NULL),(2,'user2@test.com','user2','$2a$10$eRDgxsr8/w8clVeblCEJ.O9WeQqojShgBujaMV0dN1ePEOZZEw21S',NULL,'2025-07-08 09:52:22.542872',_binary '',NULL,NULL,_binary '\0',NULL),(3,'user3@test.com','user3','$2a$10$nLcF7utC4ai.99h1x./1yOCiVJgoQ69hyqw/mfH5Wr6sX1jhS1Hn2','d38efabf-90a3-4321-9013-e335d409cd43.PNG','2025-07-08 09:54:27.433382',_binary '',NULL,NULL,_binary '\0',NULL),(4,'user4@test.com','user4','$2a$10$/DOrE9prOKOpsOh3Lnz/jeNCUbjjGv2NAfhSwVl4/OmpTcUili/RW','4688aba5-8d7e-42c7-80ce-9057badfbae9.PNG','2025-07-08 09:55:58.981585',_binary '',NULL,NULL,_binary '\0',NULL),(5,'user5@test.com','user5','$2a$10$HMAAyCHcJNrOXBT5FXwvCORXuouao6iLtf58IJOiIsWODdhHmt1LG','1f1d4bed-621c-402f-9e5b-7b64817773cf.PNG','2025-07-08 09:57:05.391717',_binary '',NULL,NULL,_binary '\0',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-21 11:49:24
