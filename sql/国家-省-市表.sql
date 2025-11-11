/*
 Navicat Premium Data Transfer

 Source Server         : 测试环境
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 192.168.1.135:3306
 Source Schema         : sntl-system

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 11/11/2025 08:58:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `iso` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ISO 二位国家代码',
  `state_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '省代码',
  `city_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '市代码',
  `name_cn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '市名 中文',
  `name_kr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市名 韩语',
  `name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市名 英语',
  `name_jp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市名 日语',
  `sort` smallint(6) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ids_country_state_city`(`iso`, `state_code`, `city_code`) USING BTREE,
  INDEX `idx_iso_state`(`iso`, `state_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '地区 城市 表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of city
-- ----------------------------
INSERT INTO `city` VALUES ('CN-110100', 'CN', '11', '110100', '北京市', NULL, NULL, NULL, 1);
INSERT INTO `city` VALUES ('CN-120100', 'CN', '12', '120100', '天津市', NULL, NULL, NULL, 2);
INSERT INTO `city` VALUES ('CN-130100', 'CN', '13', '130100', '石家庄市', NULL, NULL, NULL, 3);
INSERT INTO `city` VALUES ('CN-130200', 'CN', '13', '130200', ' 唐山市', NULL, NULL, NULL, 4);
INSERT INTO `city` VALUES ('CN-130300', 'CN', '13', '130300', ' 秦皇岛市', NULL, NULL, NULL, 5);
INSERT INTO `city` VALUES ('CN-130400', 'CN', '13', '130400', ' 邯郸市', NULL, NULL, NULL, 6);
INSERT INTO `city` VALUES ('CN-130500', 'CN', '13', '130500', ' 邢台市', NULL, NULL, NULL, 7);
INSERT INTO `city` VALUES ('CN-130600', 'CN', '13', '130600', ' 保定市', NULL, NULL, NULL, 8);
INSERT INTO `city` VALUES ('CN-130700', 'CN', '13', '130700', ' 张家口市', NULL, NULL, NULL, 9);
INSERT INTO `city` VALUES ('CN-130800', 'CN', '13', '130800', ' 承德市', NULL, NULL, NULL, 10);
INSERT INTO `city` VALUES ('CN-130900', 'CN', '13', '130900', ' 沧州市', NULL, NULL, NULL, 11);
INSERT INTO `city` VALUES ('CN-131000', 'CN', '13', '131000', ' 廊坊市', NULL, NULL, NULL, 12);
INSERT INTO `city` VALUES ('CN-131100', 'CN', '13', '131100', ' 衡水市', NULL, NULL, NULL, 13);
INSERT INTO `city` VALUES ('CN-140100', 'CN', '14', '140100', ' 太原市', NULL, NULL, NULL, 14);
INSERT INTO `city` VALUES ('CN-140200', 'CN', '14', '140200', ' 大同市', NULL, NULL, NULL, 15);
INSERT INTO `city` VALUES ('CN-140300', 'CN', '14', '140300', ' 阳泉市', NULL, NULL, NULL, 16);
INSERT INTO `city` VALUES ('CN-140400', 'CN', '14', '140400', ' 长治市', NULL, NULL, NULL, 17);
INSERT INTO `city` VALUES ('CN-140500', 'CN', '14', '140500', ' 晋城市', NULL, NULL, NULL, 18);
INSERT INTO `city` VALUES ('CN-140600', 'CN', '14', '140600', ' 朔州市', NULL, NULL, NULL, 19);
INSERT INTO `city` VALUES ('CN-140700', 'CN', '14', '140700', ' 晋中市', NULL, NULL, NULL, 20);
INSERT INTO `city` VALUES ('CN-140800', 'CN', '14', '140800', ' 运城市', NULL, NULL, NULL, 21);
INSERT INTO `city` VALUES ('CN-140900', 'CN', '14', '140900', ' 忻州市', NULL, NULL, NULL, 22);
INSERT INTO `city` VALUES ('CN-141000', 'CN', '14', '141000', ' 临汾市', NULL, NULL, NULL, 23);
INSERT INTO `city` VALUES ('CN-141100', 'CN', '14', '141100', ' 吕梁市', NULL, NULL, NULL, 24);
INSERT INTO `city` VALUES ('CN-150100', 'CN', '15', '150100', ' 呼和浩特市', NULL, NULL, NULL, 25);
INSERT INTO `city` VALUES ('CN-150200', 'CN', '15', '150200', ' 包头市', NULL, NULL, NULL, 26);
INSERT INTO `city` VALUES ('CN-150300', 'CN', '15', '150300', ' 乌海市', NULL, NULL, NULL, 27);
INSERT INTO `city` VALUES ('CN-150400', 'CN', '15', '150400', ' 赤峰市', NULL, NULL, NULL, 28);
INSERT INTO `city` VALUES ('CN-150500', 'CN', '15', '150500', ' 通辽市', NULL, NULL, NULL, 29);
INSERT INTO `city` VALUES ('CN-150600', 'CN', '15', '150600', ' 鄂尔多斯市', NULL, NULL, NULL, 30);
INSERT INTO `city` VALUES ('CN-150700', 'CN', '15', '150700', ' 呼伦贝尔市', NULL, NULL, NULL, 31);
INSERT INTO `city` VALUES ('CN-150800', 'CN', '15', '150800', ' 巴彦淖尔市', NULL, NULL, NULL, 32);
INSERT INTO `city` VALUES ('CN-150900', 'CN', '15', '150900', ' 乌兰察布市', NULL, NULL, NULL, 33);
INSERT INTO `city` VALUES ('CN-152200', 'CN', '15', '152200', ' 兴安盟', NULL, NULL, NULL, 34);
INSERT INTO `city` VALUES ('CN-152500', 'CN', '15', '152500', ' 锡林郭勒盟', NULL, NULL, NULL, 35);
INSERT INTO `city` VALUES ('CN-152900', 'CN', '15', '152900', ' 阿拉善盟', NULL, NULL, NULL, 36);
INSERT INTO `city` VALUES ('CN-210100', 'CN', '21', '210100', ' 沈阳市', NULL, NULL, NULL, 37);
INSERT INTO `city` VALUES ('CN-210200', 'CN', '21', '210200', ' 大连市', NULL, NULL, NULL, 38);
INSERT INTO `city` VALUES ('CN-210300', 'CN', '21', '210300', ' 鞍山市', NULL, NULL, NULL, 39);
INSERT INTO `city` VALUES ('CN-210400', 'CN', '21', '210400', ' 抚顺市', NULL, NULL, NULL, 40);
INSERT INTO `city` VALUES ('CN-210500', 'CN', '21', '210500', ' 本溪市', NULL, NULL, NULL, 41);
INSERT INTO `city` VALUES ('CN-210600', 'CN', '21', '210600', ' 丹东市', NULL, NULL, NULL, 42);
INSERT INTO `city` VALUES ('CN-210700', 'CN', '21', '210700', ' 锦州市', NULL, NULL, NULL, 43);
INSERT INTO `city` VALUES ('CN-210800', 'CN', '21', '210800', ' 营口市', NULL, NULL, NULL, 44);
INSERT INTO `city` VALUES ('CN-210900', 'CN', '21', '210900', ' 阜新市', NULL, NULL, NULL, 45);
INSERT INTO `city` VALUES ('CN-211000', 'CN', '21', '211000', ' 辽阳市', NULL, NULL, NULL, 46);
INSERT INTO `city` VALUES ('CN-211100', 'CN', '21', '211100', ' 盘锦市', NULL, NULL, NULL, 47);
INSERT INTO `city` VALUES ('CN-211200', 'CN', '21', '211200', ' 铁岭市', NULL, NULL, NULL, 48);
INSERT INTO `city` VALUES ('CN-211300', 'CN', '21', '211300', ' 朝阳市', NULL, NULL, NULL, 49);
INSERT INTO `city` VALUES ('CN-211400', 'CN', '21', '211400', ' 葫芦岛市', NULL, NULL, NULL, 50);
INSERT INTO `city` VALUES ('CN-220100', 'CN', '22', '220100', ' 长春市', NULL, NULL, NULL, 51);
INSERT INTO `city` VALUES ('CN-220200', 'CN', '22', '220200', ' 吉林市', NULL, NULL, NULL, 52);
INSERT INTO `city` VALUES ('CN-220300', 'CN', '22', '220300', ' 四平市', NULL, NULL, NULL, 53);
INSERT INTO `city` VALUES ('CN-220400', 'CN', '22', '220400', ' 辽源市', NULL, NULL, NULL, 54);
INSERT INTO `city` VALUES ('CN-220500', 'CN', '22', '220500', ' 通化市', NULL, NULL, NULL, 55);
INSERT INTO `city` VALUES ('CN-220600', 'CN', '22', '220600', ' 白山市', NULL, NULL, NULL, 56);
INSERT INTO `city` VALUES ('CN-220700', 'CN', '22', '220700', ' 松原市', NULL, NULL, NULL, 57);
INSERT INTO `city` VALUES ('CN-220800', 'CN', '22', '220800', ' 白城市', NULL, NULL, NULL, 58);
INSERT INTO `city` VALUES ('CN-222400', 'CN', '22', '222400', ' 延边朝鲜族自治州', NULL, NULL, NULL, 59);
INSERT INTO `city` VALUES ('CN-230100', 'CN', '23', '230100', ' 哈尔滨市', NULL, NULL, NULL, 60);
INSERT INTO `city` VALUES ('CN-230200', 'CN', '23', '230200', ' 齐齐哈尔市', NULL, NULL, NULL, 61);
INSERT INTO `city` VALUES ('CN-230300', 'CN', '23', '230300', ' 鸡西市', NULL, NULL, NULL, 62);
INSERT INTO `city` VALUES ('CN-230400', 'CN', '23', '230400', ' 鹤岗市', NULL, NULL, NULL, 63);
INSERT INTO `city` VALUES ('CN-230500', 'CN', '23', '230500', ' 双鸭山市', NULL, NULL, NULL, 64);
INSERT INTO `city` VALUES ('CN-230600', 'CN', '23', '230600', ' 大庆市', NULL, NULL, NULL, 65);
INSERT INTO `city` VALUES ('CN-230700', 'CN', '23', '230700', ' 伊春市', NULL, NULL, NULL, 66);
INSERT INTO `city` VALUES ('CN-230800', 'CN', '23', '230800', ' 佳木斯市', NULL, NULL, NULL, 67);
INSERT INTO `city` VALUES ('CN-230900', 'CN', '23', '230900', ' 七台河市', NULL, NULL, NULL, 68);
INSERT INTO `city` VALUES ('CN-231000', 'CN', '23', '231000', ' 牡丹江市', NULL, NULL, NULL, 69);
INSERT INTO `city` VALUES ('CN-231100', 'CN', '23', '231100', ' 黑河市', NULL, NULL, NULL, 70);
INSERT INTO `city` VALUES ('CN-231200', 'CN', '23', '231200', ' 绥化市', NULL, NULL, NULL, 71);
INSERT INTO `city` VALUES ('CN-232700', 'CN', '23', '232700', ' 大兴安岭地区', NULL, NULL, NULL, 72);
INSERT INTO `city` VALUES ('CN-310100', 'CN', '31', '310100', '上海市', NULL, NULL, NULL, 73);
INSERT INTO `city` VALUES ('CN-320100', 'CN', '32', '320100', ' 南京市', NULL, NULL, NULL, 74);
INSERT INTO `city` VALUES ('CN-320200', 'CN', '32', '320200', ' 无锡市', NULL, NULL, NULL, 75);
INSERT INTO `city` VALUES ('CN-320300', 'CN', '32', '320300', ' 徐州市', NULL, NULL, NULL, 76);
INSERT INTO `city` VALUES ('CN-320400', 'CN', '32', '320400', ' 常州市', NULL, NULL, NULL, 77);
INSERT INTO `city` VALUES ('CN-320500', 'CN', '32', '320500', ' 苏州市', NULL, NULL, NULL, 78);
INSERT INTO `city` VALUES ('CN-320600', 'CN', '32', '320600', ' 南通市', NULL, NULL, NULL, 79);
INSERT INTO `city` VALUES ('CN-320700', 'CN', '32', '320700', ' 连云港市', NULL, NULL, NULL, 80);
INSERT INTO `city` VALUES ('CN-320800', 'CN', '32', '320800', ' 淮安市', NULL, NULL, NULL, 81);
INSERT INTO `city` VALUES ('CN-320900', 'CN', '32', '320900', ' 盐城市', NULL, NULL, NULL, 82);
INSERT INTO `city` VALUES ('CN-321000', 'CN', '32', '321000', ' 扬州市', NULL, NULL, NULL, 83);
INSERT INTO `city` VALUES ('CN-321100', 'CN', '32', '321100', ' 镇江市', NULL, NULL, NULL, 84);
INSERT INTO `city` VALUES ('CN-321200', 'CN', '32', '321200', ' 泰州市', NULL, NULL, NULL, 85);
INSERT INTO `city` VALUES ('CN-321300', 'CN', '32', '321300', ' 宿迁市', NULL, NULL, NULL, 86);
INSERT INTO `city` VALUES ('CN-330100', 'CN', '33', '330100', ' 杭州市', NULL, NULL, NULL, 87);
INSERT INTO `city` VALUES ('CN-330200', 'CN', '33', '330200', ' 宁波市', NULL, NULL, NULL, 88);
INSERT INTO `city` VALUES ('CN-330300', 'CN', '33', '330300', ' 温州市', NULL, NULL, NULL, 89);
INSERT INTO `city` VALUES ('CN-330400', 'CN', '33', '330400', ' 嘉兴市', NULL, NULL, NULL, 90);
INSERT INTO `city` VALUES ('CN-330500', 'CN', '33', '330500', ' 湖州市', NULL, NULL, NULL, 91);
INSERT INTO `city` VALUES ('CN-330600', 'CN', '33', '330600', ' 绍兴市', NULL, NULL, NULL, 92);
INSERT INTO `city` VALUES ('CN-330700', 'CN', '33', '330700', ' 金华市', NULL, NULL, NULL, 93);
INSERT INTO `city` VALUES ('CN-330800', 'CN', '33', '330800', ' 衢州市', NULL, NULL, NULL, 94);
INSERT INTO `city` VALUES ('CN-330900', 'CN', '33', '330900', ' 舟山市', NULL, NULL, NULL, 95);
INSERT INTO `city` VALUES ('CN-331000', 'CN', '33', '331000', ' 台州市', NULL, NULL, NULL, 96);
INSERT INTO `city` VALUES ('CN-331100', 'CN', '33', '331100', ' 丽水市', NULL, NULL, NULL, 97);
INSERT INTO `city` VALUES ('CN-340100', 'CN', '34', '340100', ' 合肥市', NULL, NULL, NULL, 98);
INSERT INTO `city` VALUES ('CN-340200', 'CN', '34', '340200', ' 芜湖市', NULL, NULL, NULL, 99);
INSERT INTO `city` VALUES ('CN-340300', 'CN', '34', '340300', ' 蚌埠市', NULL, NULL, NULL, 100);
INSERT INTO `city` VALUES ('CN-340400', 'CN', '34', '340400', ' 淮南市', NULL, NULL, NULL, 101);
INSERT INTO `city` VALUES ('CN-340500', 'CN', '34', '340500', ' 马鞍山市', NULL, NULL, NULL, 102);
INSERT INTO `city` VALUES ('CN-340600', 'CN', '34', '340600', ' 淮北市', NULL, NULL, NULL, 103);
INSERT INTO `city` VALUES ('CN-340700', 'CN', '34', '340700', ' 铜陵市', NULL, NULL, NULL, 104);
INSERT INTO `city` VALUES ('CN-340800', 'CN', '34', '340800', ' 安庆市', NULL, NULL, NULL, 105);
INSERT INTO `city` VALUES ('CN-341000', 'CN', '34', '341000', ' 黄山市', NULL, NULL, NULL, 106);
INSERT INTO `city` VALUES ('CN-341100', 'CN', '34', '341100', ' 滁州市', NULL, NULL, NULL, 107);
INSERT INTO `city` VALUES ('CN-341200', 'CN', '34', '341200', ' 阜阳市', NULL, NULL, NULL, 108);
INSERT INTO `city` VALUES ('CN-341300', 'CN', '34', '341300', ' 宿州市', NULL, NULL, NULL, 109);
INSERT INTO `city` VALUES ('CN-341500', 'CN', '34', '341500', ' 六安市', NULL, NULL, NULL, 110);
INSERT INTO `city` VALUES ('CN-341600', 'CN', '34', '341600', ' 亳州市', NULL, NULL, NULL, 111);
INSERT INTO `city` VALUES ('CN-341700', 'CN', '34', '341700', ' 池州市', NULL, NULL, NULL, 112);
INSERT INTO `city` VALUES ('CN-341800', 'CN', '34', '341800', ' 宣城市', NULL, NULL, NULL, 113);
INSERT INTO `city` VALUES ('CN-350100', 'CN', '35', '350100', ' 福州市', NULL, NULL, NULL, 114);
INSERT INTO `city` VALUES ('CN-350200', 'CN', '35', '350200', ' 厦门市', NULL, NULL, NULL, 115);
INSERT INTO `city` VALUES ('CN-350300', 'CN', '35', '350300', ' 莆田市', NULL, NULL, NULL, 116);
INSERT INTO `city` VALUES ('CN-350400', 'CN', '35', '350400', ' 三明市', NULL, NULL, NULL, 117);
INSERT INTO `city` VALUES ('CN-350500', 'CN', '35', '350500', ' 泉州市', NULL, NULL, NULL, 118);
INSERT INTO `city` VALUES ('CN-350600', 'CN', '35', '350600', ' 漳州市', NULL, NULL, NULL, 119);
INSERT INTO `city` VALUES ('CN-350700', 'CN', '35', '350700', ' 南平市', NULL, NULL, NULL, 120);
INSERT INTO `city` VALUES ('CN-350800', 'CN', '35', '350800', ' 龙岩市', NULL, NULL, NULL, 121);
INSERT INTO `city` VALUES ('CN-350900', 'CN', '35', '350900', ' 宁德市', NULL, NULL, NULL, 122);
INSERT INTO `city` VALUES ('CN-360100', 'CN', '36', '360100', ' 南昌市', NULL, NULL, NULL, 123);
INSERT INTO `city` VALUES ('CN-360200', 'CN', '36', '360200', ' 景德镇市', NULL, NULL, NULL, 124);
INSERT INTO `city` VALUES ('CN-360300', 'CN', '36', '360300', ' 萍乡市', NULL, NULL, NULL, 125);
INSERT INTO `city` VALUES ('CN-360400', 'CN', '36', '360400', ' 九江市', NULL, NULL, NULL, 126);
INSERT INTO `city` VALUES ('CN-360500', 'CN', '36', '360500', ' 新余市', NULL, NULL, NULL, 127);
INSERT INTO `city` VALUES ('CN-360600', 'CN', '36', '360600', ' 鹰潭市', NULL, NULL, NULL, 128);
INSERT INTO `city` VALUES ('CN-360700', 'CN', '36', '360700', ' 赣州市', NULL, NULL, NULL, 129);
INSERT INTO `city` VALUES ('CN-360800', 'CN', '36', '360800', ' 吉安市', NULL, NULL, NULL, 130);
INSERT INTO `city` VALUES ('CN-360900', 'CN', '36', '360900', ' 宜春市', NULL, NULL, NULL, 131);
INSERT INTO `city` VALUES ('CN-361000', 'CN', '36', '361000', ' 抚州市', NULL, NULL, NULL, 132);
INSERT INTO `city` VALUES ('CN-361100', 'CN', '36', '361100', ' 上饶市', NULL, NULL, NULL, 133);
INSERT INTO `city` VALUES ('CN-370100', 'CN', '37', '370100', ' 济南市', NULL, NULL, NULL, 134);
INSERT INTO `city` VALUES ('CN-370200', 'CN', '37', '370200', ' 青岛市', NULL, NULL, NULL, 135);
INSERT INTO `city` VALUES ('CN-370300', 'CN', '37', '370300', ' 淄博市', NULL, NULL, NULL, 136);
INSERT INTO `city` VALUES ('CN-370400', 'CN', '37', '370400', ' 枣庄市', NULL, NULL, NULL, 137);
INSERT INTO `city` VALUES ('CN-370500', 'CN', '37', '370500', ' 东营市', NULL, NULL, NULL, 138);
INSERT INTO `city` VALUES ('CN-370600', 'CN', '37', '370600', ' 烟台市', NULL, NULL, NULL, 139);
INSERT INTO `city` VALUES ('CN-370700', 'CN', '37', '370700', ' 潍坊市', NULL, NULL, NULL, 140);
INSERT INTO `city` VALUES ('CN-370800', 'CN', '37', '370800', ' 济宁市', NULL, NULL, NULL, 141);
INSERT INTO `city` VALUES ('CN-370900', 'CN', '37', '370900', ' 泰安市', NULL, NULL, NULL, 142);
INSERT INTO `city` VALUES ('CN-371000', 'CN', '37', '371000', ' 威海市', NULL, NULL, NULL, 143);
INSERT INTO `city` VALUES ('CN-371100', 'CN', '37', '371100', ' 日照市', NULL, NULL, NULL, 144);
INSERT INTO `city` VALUES ('CN-371300', 'CN', '37', '371300', ' 临沂市', NULL, NULL, NULL, 145);
INSERT INTO `city` VALUES ('CN-371400', 'CN', '37', '371400', ' 德州市', NULL, NULL, NULL, 146);
INSERT INTO `city` VALUES ('CN-371500', 'CN', '37', '371500', ' 聊城市', NULL, NULL, NULL, 147);
INSERT INTO `city` VALUES ('CN-371600', 'CN', '37', '371600', ' 滨州市', NULL, NULL, NULL, 148);
INSERT INTO `city` VALUES ('CN-371700', 'CN', '37', '371700', ' 菏泽市', NULL, NULL, NULL, 149);
INSERT INTO `city` VALUES ('CN-410100', 'CN', '41', '410100', ' 郑州市', NULL, NULL, NULL, 150);
INSERT INTO `city` VALUES ('CN-410200', 'CN', '41', '410200', ' 开封市', NULL, NULL, NULL, 151);
INSERT INTO `city` VALUES ('CN-410300', 'CN', '41', '410300', ' 洛阳市', NULL, NULL, NULL, 152);
INSERT INTO `city` VALUES ('CN-410400', 'CN', '41', '410400', ' 平顶山市', NULL, NULL, NULL, 153);
INSERT INTO `city` VALUES ('CN-410500', 'CN', '41', '410500', ' 安阳市', NULL, NULL, NULL, 154);
INSERT INTO `city` VALUES ('CN-410600', 'CN', '41', '410600', ' 鹤壁市', NULL, NULL, NULL, 155);
INSERT INTO `city` VALUES ('CN-410700', 'CN', '41', '410700', ' 新乡市', NULL, NULL, NULL, 156);
INSERT INTO `city` VALUES ('CN-410800', 'CN', '41', '410800', ' 焦作市', NULL, NULL, NULL, 157);
INSERT INTO `city` VALUES ('CN-410900', 'CN', '41', '410900', ' 濮阳市', NULL, NULL, NULL, 158);
INSERT INTO `city` VALUES ('CN-411000', 'CN', '41', '411000', ' 许昌市', NULL, NULL, NULL, 159);
INSERT INTO `city` VALUES ('CN-411100', 'CN', '41', '411100', ' 漯河市', NULL, NULL, NULL, 160);
INSERT INTO `city` VALUES ('CN-411200', 'CN', '41', '411200', ' 三门峡市', NULL, NULL, NULL, 161);
INSERT INTO `city` VALUES ('CN-411300', 'CN', '41', '411300', ' 南阳市', NULL, NULL, NULL, 162);
INSERT INTO `city` VALUES ('CN-411400', 'CN', '41', '411400', ' 商丘市', NULL, NULL, NULL, 163);
INSERT INTO `city` VALUES ('CN-411500', 'CN', '41', '411500', ' 信阳市', NULL, NULL, NULL, 164);
INSERT INTO `city` VALUES ('CN-411600', 'CN', '41', '411600', ' 周口市', NULL, NULL, NULL, 165);
INSERT INTO `city` VALUES ('CN-411700', 'CN', '41', '411700', ' 驻马店市', NULL, NULL, NULL, 166);
INSERT INTO `city` VALUES ('CN-420100', 'CN', '42', '420100', ' 武汉市', NULL, NULL, NULL, 167);
INSERT INTO `city` VALUES ('CN-420200', 'CN', '42', '420200', ' 黄石市', NULL, NULL, NULL, 168);
INSERT INTO `city` VALUES ('CN-420300', 'CN', '42', '420300', ' 十堰市', NULL, NULL, NULL, 169);
INSERT INTO `city` VALUES ('CN-420500', 'CN', '42', '420500', ' 宜昌市', NULL, NULL, NULL, 170);
INSERT INTO `city` VALUES ('CN-420600', 'CN', '42', '420600', ' 襄阳市', NULL, NULL, NULL, 171);
INSERT INTO `city` VALUES ('CN-420700', 'CN', '42', '420700', ' 鄂州市', NULL, NULL, NULL, 172);
INSERT INTO `city` VALUES ('CN-420800', 'CN', '42', '420800', ' 荆门市', NULL, NULL, NULL, 173);
INSERT INTO `city` VALUES ('CN-420900', 'CN', '42', '420900', ' 孝感市', NULL, NULL, NULL, 174);
INSERT INTO `city` VALUES ('CN-421000', 'CN', '42', '421000', ' 荆州市', NULL, NULL, NULL, 175);
INSERT INTO `city` VALUES ('CN-421100', 'CN', '42', '421100', ' 黄冈市', NULL, NULL, NULL, 176);
INSERT INTO `city` VALUES ('CN-421200', 'CN', '42', '421200', ' 咸宁市', NULL, NULL, NULL, 177);
INSERT INTO `city` VALUES ('CN-421300', 'CN', '42', '421300', ' 随州市', NULL, NULL, NULL, 178);
INSERT INTO `city` VALUES ('CN-422800', 'CN', '42', '422800', ' 恩施土家族苗族自治州', NULL, NULL, NULL, 179);
INSERT INTO `city` VALUES ('CN-430100', 'CN', '43', '430100', ' 长沙市', NULL, NULL, NULL, 180);
INSERT INTO `city` VALUES ('CN-430200', 'CN', '43', '430200', ' 株洲市', NULL, NULL, NULL, 181);
INSERT INTO `city` VALUES ('CN-430300', 'CN', '43', '430300', ' 湘潭市', NULL, NULL, NULL, 182);
INSERT INTO `city` VALUES ('CN-430400', 'CN', '43', '430400', ' 衡阳市', NULL, NULL, NULL, 183);
INSERT INTO `city` VALUES ('CN-430500', 'CN', '43', '430500', ' 邵阳市', NULL, NULL, NULL, 184);
INSERT INTO `city` VALUES ('CN-430600', 'CN', '43', '430600', ' 岳阳市', NULL, NULL, NULL, 185);
INSERT INTO `city` VALUES ('CN-430700', 'CN', '43', '430700', ' 常德市', NULL, NULL, NULL, 186);
INSERT INTO `city` VALUES ('CN-430800', 'CN', '43', '430800', ' 张家界市', NULL, NULL, NULL, 187);
INSERT INTO `city` VALUES ('CN-430900', 'CN', '43', '430900', ' 益阳市', NULL, NULL, NULL, 188);
INSERT INTO `city` VALUES ('CN-431000', 'CN', '43', '431000', ' 郴州市', NULL, NULL, NULL, 189);
INSERT INTO `city` VALUES ('CN-431100', 'CN', '43', '431100', ' 永州市', NULL, NULL, NULL, 190);
INSERT INTO `city` VALUES ('CN-431200', 'CN', '43', '431200', ' 怀化市', NULL, NULL, NULL, 191);
INSERT INTO `city` VALUES ('CN-431300', 'CN', '43', '431300', ' 娄底市', NULL, NULL, NULL, 192);
INSERT INTO `city` VALUES ('CN-433100', 'CN', '43', '433100', ' 湘西土家族苗族自治州', NULL, NULL, NULL, 193);
INSERT INTO `city` VALUES ('CN-440100', 'CN', '44', '440100', ' 广州市', NULL, NULL, NULL, 194);
INSERT INTO `city` VALUES ('CN-440200', 'CN', '44', '440200', ' 韶关市', NULL, NULL, NULL, 195);
INSERT INTO `city` VALUES ('CN-440300', 'CN', '44', '440300', ' 深圳市', NULL, NULL, NULL, 196);
INSERT INTO `city` VALUES ('CN-440400', 'CN', '44', '440400', ' 珠海市', NULL, NULL, NULL, 197);
INSERT INTO `city` VALUES ('CN-440500', 'CN', '44', '440500', ' 汕头市', NULL, NULL, NULL, 198);
INSERT INTO `city` VALUES ('CN-440600', 'CN', '44', '440600', ' 佛山市', NULL, NULL, NULL, 199);
INSERT INTO `city` VALUES ('CN-440700', 'CN', '44', '440700', ' 江门市', NULL, NULL, NULL, 200);
INSERT INTO `city` VALUES ('CN-440800', 'CN', '44', '440800', ' 湛江市', NULL, NULL, NULL, 201);
INSERT INTO `city` VALUES ('CN-440900', 'CN', '44', '440900', ' 茂名市', NULL, NULL, NULL, 202);
INSERT INTO `city` VALUES ('CN-441200', 'CN', '44', '441200', ' 肇庆市', NULL, NULL, NULL, 203);
INSERT INTO `city` VALUES ('CN-441300', 'CN', '44', '441300', ' 惠州市', NULL, NULL, NULL, 204);
INSERT INTO `city` VALUES ('CN-441400', 'CN', '44', '441400', ' 梅州市', NULL, NULL, NULL, 205);
INSERT INTO `city` VALUES ('CN-441500', 'CN', '44', '441500', ' 汕尾市', NULL, NULL, NULL, 206);
INSERT INTO `city` VALUES ('CN-441600', 'CN', '44', '441600', ' 河源市', NULL, NULL, NULL, 207);
INSERT INTO `city` VALUES ('CN-441700', 'CN', '44', '441700', ' 阳江市', NULL, NULL, NULL, 208);
INSERT INTO `city` VALUES ('CN-441800', 'CN', '44', '441800', ' 清远市', NULL, NULL, NULL, 209);
INSERT INTO `city` VALUES ('CN-441900', 'CN', '44', '441900', ' 东莞市', NULL, NULL, NULL, 210);
INSERT INTO `city` VALUES ('CN-442000', 'CN', '44', '442000', ' 中山市', NULL, NULL, NULL, 211);
INSERT INTO `city` VALUES ('CN-445100', 'CN', '44', '445100', ' 潮州市', NULL, NULL, NULL, 212);
INSERT INTO `city` VALUES ('CN-445200', 'CN', '44', '445200', ' 揭阳市', NULL, NULL, NULL, 213);
INSERT INTO `city` VALUES ('CN-445300', 'CN', '44', '445300', ' 云浮市', NULL, NULL, NULL, 214);
INSERT INTO `city` VALUES ('CN-450100', 'CN', '45', '450100', ' 南宁市', NULL, NULL, NULL, 215);
INSERT INTO `city` VALUES ('CN-450200', 'CN', '45', '450200', ' 柳州市', NULL, NULL, NULL, 216);
INSERT INTO `city` VALUES ('CN-450300', 'CN', '45', '450300', ' 桂林市', NULL, NULL, NULL, 217);
INSERT INTO `city` VALUES ('CN-450400', 'CN', '45', '450400', ' 梧州市', NULL, NULL, NULL, 218);
INSERT INTO `city` VALUES ('CN-450500', 'CN', '45', '450500', ' 北海市', NULL, NULL, NULL, 219);
INSERT INTO `city` VALUES ('CN-450600', 'CN', '45', '450600', ' 防城港市', NULL, NULL, NULL, 220);
INSERT INTO `city` VALUES ('CN-450700', 'CN', '45', '450700', ' 钦州市', NULL, NULL, NULL, 221);
INSERT INTO `city` VALUES ('CN-450800', 'CN', '45', '450800', ' 贵港市', NULL, NULL, NULL, 222);
INSERT INTO `city` VALUES ('CN-450900', 'CN', '45', '450900', ' 玉林市', NULL, NULL, NULL, 223);
INSERT INTO `city` VALUES ('CN-451000', 'CN', '45', '451000', ' 百色市', NULL, NULL, NULL, 224);
INSERT INTO `city` VALUES ('CN-451100', 'CN', '45', '451100', ' 贺州市', NULL, NULL, NULL, 225);
INSERT INTO `city` VALUES ('CN-451200', 'CN', '45', '451200', ' 河池市', NULL, NULL, NULL, 226);
INSERT INTO `city` VALUES ('CN-451300', 'CN', '45', '451300', ' 来宾市', NULL, NULL, NULL, 227);
INSERT INTO `city` VALUES ('CN-451400', 'CN', '45', '451400', ' 崇左市', NULL, NULL, NULL, 228);
INSERT INTO `city` VALUES ('CN-460100', 'CN', '46', '460100', ' 海口市', NULL, NULL, NULL, 229);
INSERT INTO `city` VALUES ('CN-460200', 'CN', '46', '460200', ' 三亚市', NULL, NULL, NULL, 230);
INSERT INTO `city` VALUES ('CN-460300', 'CN', '46', '460300', ' 三沙市', NULL, NULL, NULL, 231);
INSERT INTO `city` VALUES ('CN-460400', 'CN', '46', '460400', ' 儋州市', NULL, NULL, NULL, 232);
INSERT INTO `city` VALUES ('CN-500100', 'CN', '50', '500100', '重庆市', NULL, NULL, NULL, 233);
INSERT INTO `city` VALUES ('CN-510100', 'CN', '51', '510100', ' 成都市', NULL, NULL, NULL, 234);
INSERT INTO `city` VALUES ('CN-510300', 'CN', '51', '510300', ' 自贡市', NULL, NULL, NULL, 235);
INSERT INTO `city` VALUES ('CN-510400', 'CN', '51', '510400', ' 攀枝花市', NULL, NULL, NULL, 236);
INSERT INTO `city` VALUES ('CN-510500', 'CN', '51', '510500', ' 泸州市', NULL, NULL, NULL, 237);
INSERT INTO `city` VALUES ('CN-510600', 'CN', '51', '510600', ' 德阳市', NULL, NULL, NULL, 238);
INSERT INTO `city` VALUES ('CN-510700', 'CN', '51', '510700', ' 绵阳市', NULL, NULL, NULL, 239);
INSERT INTO `city` VALUES ('CN-510800', 'CN', '51', '510800', ' 广元市', NULL, NULL, NULL, 240);
INSERT INTO `city` VALUES ('CN-510900', 'CN', '51', '510900', ' 遂宁市', NULL, NULL, NULL, 241);
INSERT INTO `city` VALUES ('CN-511000', 'CN', '51', '511000', ' 内江市', NULL, NULL, NULL, 242);
INSERT INTO `city` VALUES ('CN-511100', 'CN', '51', '511100', ' 乐山市', NULL, NULL, NULL, 243);
INSERT INTO `city` VALUES ('CN-511300', 'CN', '51', '511300', ' 南充市', NULL, NULL, NULL, 244);
INSERT INTO `city` VALUES ('CN-511400', 'CN', '51', '511400', ' 眉山市', NULL, NULL, NULL, 245);
INSERT INTO `city` VALUES ('CN-511500', 'CN', '51', '511500', ' 宜宾市', NULL, NULL, NULL, 246);
INSERT INTO `city` VALUES ('CN-511600', 'CN', '51', '511600', ' 广安市', NULL, NULL, NULL, 247);
INSERT INTO `city` VALUES ('CN-511700', 'CN', '51', '511700', ' 达州市', NULL, NULL, NULL, 248);
INSERT INTO `city` VALUES ('CN-511800', 'CN', '51', '511800', ' 雅安市', NULL, NULL, NULL, 249);
INSERT INTO `city` VALUES ('CN-511900', 'CN', '51', '511900', ' 巴中市', NULL, NULL, NULL, 250);
INSERT INTO `city` VALUES ('CN-512000', 'CN', '51', '512000', ' 资阳市', NULL, NULL, NULL, 251);
INSERT INTO `city` VALUES ('CN-513200', 'CN', '51', '513200', ' 阿坝藏族羌族自治州', NULL, NULL, NULL, 252);
INSERT INTO `city` VALUES ('CN-513300', 'CN', '51', '513300', ' 甘孜藏族自治州', NULL, NULL, NULL, 253);
INSERT INTO `city` VALUES ('CN-513400', 'CN', '51', '513400', ' 凉山彝族自治州', NULL, NULL, NULL, 254);
INSERT INTO `city` VALUES ('CN-520100', 'CN', '52', '520100', ' 贵阳市', NULL, NULL, NULL, 255);
INSERT INTO `city` VALUES ('CN-520200', 'CN', '52', '520200', ' 六盘水市', NULL, NULL, NULL, 256);
INSERT INTO `city` VALUES ('CN-520300', 'CN', '52', '520300', ' 遵义市', NULL, NULL, NULL, 257);
INSERT INTO `city` VALUES ('CN-520400', 'CN', '52', '520400', ' 安顺市', NULL, NULL, NULL, 258);
INSERT INTO `city` VALUES ('CN-520500', 'CN', '52', '520500', ' 毕节市', NULL, NULL, NULL, 259);
INSERT INTO `city` VALUES ('CN-520600', 'CN', '52', '520600', ' 铜仁市', NULL, NULL, NULL, 260);
INSERT INTO `city` VALUES ('CN-522300', 'CN', '52', '522300', ' 黔西南布依族苗族自治州', NULL, NULL, NULL, 261);
INSERT INTO `city` VALUES ('CN-522600', 'CN', '52', '522600', ' 黔东南苗族侗族自治州', NULL, NULL, NULL, 262);
INSERT INTO `city` VALUES ('CN-522700', 'CN', '52', '522700', ' 黔南布依族苗族自治州', NULL, NULL, NULL, 263);
INSERT INTO `city` VALUES ('CN-530100', 'CN', '53', '530100', ' 昆明市', NULL, NULL, NULL, 264);
INSERT INTO `city` VALUES ('CN-530300', 'CN', '53', '530300', ' 曲靖市', NULL, NULL, NULL, 265);
INSERT INTO `city` VALUES ('CN-530400', 'CN', '53', '530400', ' 玉溪市', NULL, NULL, NULL, 266);
INSERT INTO `city` VALUES ('CN-530500', 'CN', '53', '530500', ' 保山市', NULL, NULL, NULL, 267);
INSERT INTO `city` VALUES ('CN-530600', 'CN', '53', '530600', ' 昭通市', NULL, NULL, NULL, 268);
INSERT INTO `city` VALUES ('CN-530700', 'CN', '53', '530700', ' 丽江市', NULL, NULL, NULL, 269);
INSERT INTO `city` VALUES ('CN-530800', 'CN', '53', '530800', ' 普洱市', NULL, NULL, NULL, 270);
INSERT INTO `city` VALUES ('CN-530900', 'CN', '53', '530900', ' 临沧市', NULL, NULL, NULL, 271);
INSERT INTO `city` VALUES ('CN-532300', 'CN', '53', '532300', ' 楚雄彝族自治州', NULL, NULL, NULL, 272);
INSERT INTO `city` VALUES ('CN-532500', 'CN', '53', '532500', ' 红河哈尼族彝族自治州', NULL, NULL, NULL, 273);
INSERT INTO `city` VALUES ('CN-532600', 'CN', '53', '532600', ' 文山壮族苗族自治州', NULL, NULL, NULL, 274);
INSERT INTO `city` VALUES ('CN-532800', 'CN', '53', '532800', ' 西双版纳傣族自治州', NULL, NULL, NULL, 275);
INSERT INTO `city` VALUES ('CN-532900', 'CN', '53', '532900', ' 大理白族自治州', NULL, NULL, NULL, 276);
INSERT INTO `city` VALUES ('CN-533100', 'CN', '53', '533100', ' 德宏傣族景颇族自治州', NULL, NULL, NULL, 277);
INSERT INTO `city` VALUES ('CN-533300', 'CN', '53', '533300', ' 怒江傈僳族自治州', NULL, NULL, NULL, 278);
INSERT INTO `city` VALUES ('CN-533400', 'CN', '53', '533400', ' 迪庆藏族自治州', NULL, NULL, NULL, 279);
INSERT INTO `city` VALUES ('CN-540100', 'CN', '54', '540100', ' 拉萨市', NULL, NULL, NULL, 280);
INSERT INTO `city` VALUES ('CN-540200', 'CN', '54', '540200', ' 日喀则市', NULL, NULL, NULL, 281);
INSERT INTO `city` VALUES ('CN-540300', 'CN', '54', '540300', ' 昌都市', NULL, NULL, NULL, 282);
INSERT INTO `city` VALUES ('CN-540400', 'CN', '54', '540400', ' 林芝市', NULL, NULL, NULL, 283);
INSERT INTO `city` VALUES ('CN-540500', 'CN', '54', '540500', ' 山南市', NULL, NULL, NULL, 284);
INSERT INTO `city` VALUES ('CN-540600', 'CN', '54', '540600', ' 那曲市', NULL, NULL, NULL, 285);
INSERT INTO `city` VALUES ('CN-542500', 'CN', '54', '542500', ' 阿里地区', NULL, NULL, NULL, 286);
INSERT INTO `city` VALUES ('CN-610100', 'CN', '61', '610100', ' 西安市', NULL, NULL, NULL, 287);
INSERT INTO `city` VALUES ('CN-610200', 'CN', '61', '610200', ' 铜川市', NULL, NULL, NULL, 288);
INSERT INTO `city` VALUES ('CN-610300', 'CN', '61', '610300', ' 宝鸡市', NULL, NULL, NULL, 289);
INSERT INTO `city` VALUES ('CN-610400', 'CN', '61', '610400', ' 咸阳市', NULL, NULL, NULL, 290);
INSERT INTO `city` VALUES ('CN-610500', 'CN', '61', '610500', ' 渭南市', NULL, NULL, NULL, 291);
INSERT INTO `city` VALUES ('CN-610600', 'CN', '61', '610600', ' 延安市', NULL, NULL, NULL, 292);
INSERT INTO `city` VALUES ('CN-610700', 'CN', '61', '610700', ' 汉中市', NULL, NULL, NULL, 293);
INSERT INTO `city` VALUES ('CN-610800', 'CN', '61', '610800', ' 榆林市', NULL, NULL, NULL, 294);
INSERT INTO `city` VALUES ('CN-610900', 'CN', '61', '610900', ' 安康市', NULL, NULL, NULL, 295);
INSERT INTO `city` VALUES ('CN-611000', 'CN', '61', '611000', ' 商洛市', NULL, NULL, NULL, 296);
INSERT INTO `city` VALUES ('CN-620100', 'CN', '62', '620100', ' 兰州市', NULL, NULL, NULL, 297);
INSERT INTO `city` VALUES ('CN-620200', 'CN', '62', '620200', ' 嘉峪关市', NULL, NULL, NULL, 298);
INSERT INTO `city` VALUES ('CN-620300', 'CN', '62', '620300', ' 金昌市', NULL, NULL, NULL, 299);
INSERT INTO `city` VALUES ('CN-620400', 'CN', '62', '620400', ' 白银市', NULL, NULL, NULL, 300);
INSERT INTO `city` VALUES ('CN-620500', 'CN', '62', '620500', ' 天水市', NULL, NULL, NULL, 301);
INSERT INTO `city` VALUES ('CN-620600', 'CN', '62', '620600', ' 武威市', NULL, NULL, NULL, 302);
INSERT INTO `city` VALUES ('CN-620700', 'CN', '62', '620700', ' 张掖市', NULL, NULL, NULL, 303);
INSERT INTO `city` VALUES ('CN-620800', 'CN', '62', '620800', ' 平凉市', NULL, NULL, NULL, 304);
INSERT INTO `city` VALUES ('CN-620900', 'CN', '62', '620900', ' 酒泉市', NULL, NULL, NULL, 305);
INSERT INTO `city` VALUES ('CN-621000', 'CN', '62', '621000', ' 庆阳市', NULL, NULL, NULL, 306);
INSERT INTO `city` VALUES ('CN-621100', 'CN', '62', '621100', ' 定西市', NULL, NULL, NULL, 307);
INSERT INTO `city` VALUES ('CN-621200', 'CN', '62', '621200', ' 陇南市', NULL, NULL, NULL, 308);
INSERT INTO `city` VALUES ('CN-622900', 'CN', '62', '622900', ' 临夏回族自治州', NULL, NULL, NULL, 309);
INSERT INTO `city` VALUES ('CN-623000', 'CN', '62', '623000', ' 甘南藏族自治州', NULL, NULL, NULL, 310);
INSERT INTO `city` VALUES ('CN-630100', 'CN', '63', '630100', ' 西宁市', NULL, NULL, NULL, 311);
INSERT INTO `city` VALUES ('CN-630200', 'CN', '63', '630200', ' 海东市', NULL, NULL, NULL, 312);
INSERT INTO `city` VALUES ('CN-632200', 'CN', '63', '632200', ' 海北藏族自治州', NULL, NULL, NULL, 313);
INSERT INTO `city` VALUES ('CN-632300', 'CN', '63', '632300', ' 黄南藏族自治州', NULL, NULL, NULL, 314);
INSERT INTO `city` VALUES ('CN-632500', 'CN', '63', '632500', ' 海南藏族自治州', NULL, NULL, NULL, 315);
INSERT INTO `city` VALUES ('CN-632600', 'CN', '63', '632600', ' 果洛藏族自治州', NULL, NULL, NULL, 316);
INSERT INTO `city` VALUES ('CN-632700', 'CN', '63', '632700', ' 玉树藏族自治州', NULL, NULL, NULL, 317);
INSERT INTO `city` VALUES ('CN-632800', 'CN', '63', '632800', ' 海西蒙古族藏族自治州', NULL, NULL, NULL, 318);
INSERT INTO `city` VALUES ('CN-640100', 'CN', '64', '640100', ' 银川市', NULL, NULL, NULL, 319);
INSERT INTO `city` VALUES ('CN-640200', 'CN', '64', '640200', ' 石嘴山市', NULL, NULL, NULL, 320);
INSERT INTO `city` VALUES ('CN-640300', 'CN', '64', '640300', ' 吴忠市', NULL, NULL, NULL, 321);
INSERT INTO `city` VALUES ('CN-640400', 'CN', '64', '640400', ' 固原市', NULL, NULL, NULL, 322);
INSERT INTO `city` VALUES ('CN-640500', 'CN', '64', '640500', ' 中卫市', NULL, NULL, NULL, 323);
INSERT INTO `city` VALUES ('CN-650100', 'CN', '65', '650100', ' 乌鲁木齐市', NULL, NULL, NULL, 324);
INSERT INTO `city` VALUES ('CN-650200', 'CN', '65', '650200', ' 克拉玛依市', NULL, NULL, NULL, 325);
INSERT INTO `city` VALUES ('CN-650400', 'CN', '65', '650400', ' 吐鲁番市', NULL, NULL, NULL, 326);
INSERT INTO `city` VALUES ('CN-650500', 'CN', '65', '650500', ' 哈密市', NULL, NULL, NULL, 327);
INSERT INTO `city` VALUES ('CN-652300', 'CN', '65', '652300', ' 昌吉回族自治州', NULL, NULL, NULL, 328);
INSERT INTO `city` VALUES ('CN-652700', 'CN', '65', '652700', ' 博尔塔拉蒙古自治州', NULL, NULL, NULL, 329);
INSERT INTO `city` VALUES ('CN-652800', 'CN', '65', '652800', ' 巴音郭楞蒙古自治州', NULL, NULL, NULL, 330);
INSERT INTO `city` VALUES ('CN-652900', 'CN', '65', '652900', ' 阿克苏地区', NULL, NULL, NULL, 331);
INSERT INTO `city` VALUES ('CN-653000', 'CN', '65', '653000', ' 克孜勒苏柯尔克孜自治州', NULL, NULL, NULL, 332);
INSERT INTO `city` VALUES ('CN-653100', 'CN', '65', '653100', ' 喀什地区', NULL, NULL, NULL, 333);
INSERT INTO `city` VALUES ('CN-653200', 'CN', '65', '653200', ' 和田地区', NULL, NULL, NULL, 334);
INSERT INTO `city` VALUES ('CN-654000', 'CN', '65', '654000', ' 伊犁哈萨克自治州', NULL, NULL, NULL, 335);
INSERT INTO `city` VALUES ('CN-654200', 'CN', '65', '654200', ' 塔城地区', NULL, NULL, NULL, 336);
INSERT INTO `city` VALUES ('CN-654300', 'CN', '65', '654300', ' 阿勒泰地区', NULL, NULL, NULL, 337);
INSERT INTO `city` VALUES ('US-BOS', 'US', 'MA', 'BOS', '波士顿', NULL, 'Boston', NULL, 1);
INSERT INTO `city` VALUES ('US-BUF', 'US', 'NY', 'BUF', '布法罗', NULL, 'Buffalo', NULL, 2);
INSERT INTO `city` VALUES ('US-CHI', 'US', 'IL', 'CHI', '芝加哥', NULL, 'Chicago', NULL, 1);
INSERT INTO `city` VALUES ('US-DAL', 'US', 'TX', 'DAL', '达拉斯', NULL, 'Dallas', NULL, 2);
INSERT INTO `city` VALUES ('US-HOU', 'US', 'TX', 'HOU', '休斯顿', NULL, 'Houston', NULL, 1);
INSERT INTO `city` VALUES ('US-LA', 'US', 'CA', 'LA', '洛杉矶', NULL, 'Los Angeles', NULL, 1);
INSERT INTO `city` VALUES ('US-MIA', 'US', 'FL', 'MIA', '迈阿密', NULL, 'Miami', NULL, 1);
INSERT INTO `city` VALUES ('US-NYC', 'US', 'NY', 'NYC', '纽约市', NULL, 'New York', NULL, 1);
INSERT INTO `city` VALUES ('US-SD', 'US', 'CA', 'SD', '圣迭戈', NULL, 'San Diego', NULL, 3);
INSERT INTO `city` VALUES ('US-SFO', 'US', 'CA', 'SFO', '旧金山', NULL, 'San Francisco', NULL, 2);
INSERT INTO `city` VALUES ('US-WAS', 'US', 'DC', 'WAS', '华盛顿特区', NULL, 'Washington D.C.', NULL, 1);

-- ----------------------------
-- Table structure for country
-- ----------------------------
DROP TABLE IF EXISTS `country`;
CREATE TABLE `country`  (
  `iso` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ISO 两位国家代码',
  `iso_s` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '三位字母代码',
  `iso_n` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '三位数字代码',
  `name_cn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '国家名 中文',
  `name_kr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家名 韩语',
  `name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家名 英语',
  `name_jp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家名 日语',
  `sort` smallint(6) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`iso`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '国家表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of country
-- ----------------------------
INSERT INTO `country` VALUES ('AD', 'AND', '20', '安道尔', '안도라', 'Andorra', 'アンドラ', 1);
INSERT INTO `country` VALUES ('AE', 'ARE', '784', '阿拉伯联合酋长国', '아랍에미리트', 'United Arab Emirates', 'アラブ首長国連邦', 2);
INSERT INTO `country` VALUES ('AF', 'AFG', '4', '阿富汗', '아프가니스탄', 'Afghanistan', 'アフガニスタン', 3);
INSERT INTO `country` VALUES ('AG', 'ATG', '28', '安提瓜和巴布达', '앤티가 바부다', 'Antigua and Barbuda', 'アンティグア・バルブーダ', 4);
INSERT INTO `country` VALUES ('AI', 'AIA', '660', '安圭拉', '앵귈라', 'Anguilla', 'アンギラ', 5);
INSERT INTO `country` VALUES ('AL', 'ALB', '8', '阿尔巴尼亚', '알바니아', 'Albania', 'アルバニア', 6);
INSERT INTO `country` VALUES ('AM', 'ARM', '51', '亚美尼亚', '아르메니아', 'Armenia', 'アルメニア', 7);
INSERT INTO `country` VALUES ('AN', 'ANT', '530', '荷属安的列斯', '네덜란드령 안틸레스', 'Netherlands Antilles', 'オランダ領アンチル諸島', 8);
INSERT INTO `country` VALUES ('AO', 'AGO', '24', '安哥拉', '앙골라', 'Angola', 'アンゴラ', 9);
INSERT INTO `country` VALUES ('AQ', 'ATA', '10', '南极洲', '남극洲', 'Antarctica', '南極大陸', 10);
INSERT INTO `country` VALUES ('AR', 'ARG', '32', '阿根廷', '아르헨티나', 'Argentina', 'アルゼンチン', 11);
INSERT INTO `country` VALUES ('AS', 'ASM', '16', '美属萨摩亚', '미국령 사모아', 'American Samoa', 'アメリカ領サモア', 12);
INSERT INTO `country` VALUES ('AT', 'AUT', '40', '奥地利', '오스트리아', 'Austria', 'オーストリア', 13);
INSERT INTO `country` VALUES ('AU', 'AUS', '36', '澳大利亚', '호주', 'Australia', 'オーストラリア', 14);
INSERT INTO `country` VALUES ('AW', 'ABW', '533', '阿鲁巴', '아루바', 'Aruba', 'アルバ', 15);
INSERT INTO `country` VALUES ('AZ', 'AZE', '31', '阿塞拜疆', '아제르바이잔', 'Azerbaijan', 'アゼルバイジャン', 16);
INSERT INTO `country` VALUES ('BA', 'BIH', '70', '波斯尼亚和黑塞哥维那', '보스니아 헤르체고비나', 'Bosnia and Herzegovina', 'ボスニア・ヘルツェゴビナ', 17);
INSERT INTO `country` VALUES ('BB', 'BRB', '52', '巴巴多斯', '바바도스', 'Barbados', 'バルバドス', 18);
INSERT INTO `country` VALUES ('BD', 'BGD', '50', '孟加拉国', '방글라데시', 'Bangladesh', 'バングラデシュ', 19);
INSERT INTO `country` VALUES ('BE', 'BEL', '56', '比利时', '벨기에', 'Belgium', 'ベルギー', 20);
INSERT INTO `country` VALUES ('BF', 'BFA', '854', '布基纳法索', '부르키나파소', 'Burkina Faso', 'ブルキナファソ', 21);
INSERT INTO `country` VALUES ('BG', 'BGR', '100', '保加利亚', '불가리아', 'Bulgaria', 'ブルガリア', 22);
INSERT INTO `country` VALUES ('BH', 'BHR', '48', '巴林', '바레인', 'Bahrain', 'バーレーン', 23);
INSERT INTO `country` VALUES ('BI', 'BDI', '108', '布隆迪', '부룬디', 'Burundi', 'ブルンジ', 24);
INSERT INTO `country` VALUES ('BJ', 'BEN', '204', '贝宁', '베닌', 'Benin', 'ベニン', 25);
INSERT INTO `country` VALUES ('BM', 'BMU', '60', '百慕大', '버뮤다', 'Bermuda', 'バーミューダ', 26);
INSERT INTO `country` VALUES ('BN', 'BRN', '96', '文莱', '브루나이', 'Brunei Darussalam', 'ブルネイ・ダルサラム', 27);
INSERT INTO `country` VALUES ('BO', 'BOL', '68', '玻利维亚', '볼리비아', 'Bolivia', 'ボリビア', 28);
INSERT INTO `country` VALUES ('BR', 'BRA', '76', '巴西', '브라질', 'Brazil', 'ブラジル', 29);
INSERT INTO `country` VALUES ('BS', 'BHS', '44', '巴哈马', '바하마', 'Bahamas', 'バハマ', 30);
INSERT INTO `country` VALUES ('BT', 'BTN', '64', '不丹', '부탄', 'Bhutan', 'ブータン', 31);
INSERT INTO `country` VALUES ('BV', 'BVT', '74', '布维岛', '부베 섬', 'Bouvet Island', 'ブーベ島', 32);
INSERT INTO `country` VALUES ('BW', 'BWA', '72', '博茨瓦纳', '보츠와나', 'Botswana', 'ボツワナ', 33);
INSERT INTO `country` VALUES ('BY', 'BLR', '112', '白俄罗斯', '벨라루스', 'Belarus', 'ベラルーシ', 34);
INSERT INTO `country` VALUES ('BZ', 'BLZ', '84', '伯利兹', '벨리즈', 'Belize', 'ベリーズ', 35);
INSERT INTO `country` VALUES ('CA', 'CAN', '124', '加拿大', '캐나다', 'Canada', 'カナダ', 36);
INSERT INTO `country` VALUES ('CC', 'CCK', '166', '科科斯（基林）群岛', '코코스 제릴링 제도', 'Cocos (Keeling) Islands', 'ココス諸島', 37);
INSERT INTO `country` VALUES ('CF', 'CAF', '140', '中非共和国', '중앙아프리카 공화국', 'Central African Republic', '中央アフリカ共和国', 38);
INSERT INTO `country` VALUES ('CG', 'COG', '178', '刚果共和国', '콩고 공화국', 'Republic of the Congo', 'コンゴ共和国', 39);
INSERT INTO `country` VALUES ('CH', 'CHE', '756', '瑞士', '스위스', 'Switzerland', 'スイス', 40);
INSERT INTO `country` VALUES ('CI', 'CIV', '384', '科特迪瓦', '코트디부아르', 'Côte d\'Ivoire', 'コートジボワール', 41);
INSERT INTO `country` VALUES ('CK', 'COK', '184', '库克群岛', '쿡 제도', 'Cook Islands', 'クック諸島', 42);
INSERT INTO `country` VALUES ('CL', 'CHL', '152', '智利', '칠레', 'Chile', 'チリ', 43);
INSERT INTO `country` VALUES ('CM', 'CMR', '120', '喀麦隆', '카메룬', 'Cameroon', 'カメルーン', 44);
INSERT INTO `country` VALUES ('CN', 'CHN', '156', '中国', '중국', 'China', '中国', 45);
INSERT INTO `country` VALUES ('CO', 'COL', '170', '哥伦比亚', '콜롬비아', 'Colombia', 'コロンビア', 46);
INSERT INTO `country` VALUES ('CR', 'CRI', '188', '哥斯达黎加', '코스타리카', 'Costa Rica', 'コスタリカ', 47);
INSERT INTO `country` VALUES ('CS', 'CSR', '162', '圣诞岛', '크리스마스 섬', 'Christmas Island', 'クリスマス島', 48);
INSERT INTO `country` VALUES ('CU', 'CUB', '192', '古巴', '쿠바', 'Cuba', 'キューバ', 49);
INSERT INTO `country` VALUES ('CV', 'CPV', '132', '佛得角', '케이프버드', 'Cabo Verde', 'カーボベルデ', 50);
INSERT INTO `country` VALUES ('CY', 'CYP', '196', '塞浦路斯', '키프로스', 'Cyprus', 'キプロス', 51);
INSERT INTO `country` VALUES ('CZ', 'CZE', '203', '捷克', '체코', 'Czech Republic', 'チェコ共和国', 52);
INSERT INTO `country` VALUES ('DE', 'DEU', '276', '德国', '독일', 'Germany', 'ドイツ', 53);
INSERT INTO `country` VALUES ('DJ', 'DJI', '262', '吉布提', '지부티', 'Djibouti', 'ジブチ', 54);
INSERT INTO `country` VALUES ('DK', 'DNK', '208', '丹麦', '덴마크', 'Denmark', 'デンマーク', 55);
INSERT INTO `country` VALUES ('DM', 'DMA', '212', '多米尼克', '도미니카', 'Dominica', 'ドミニカ', 56);
INSERT INTO `country` VALUES ('DO', 'DOM', '214', '多米尼加共和国', '도미니카 공화국', 'Dominican Republic', 'ドミニカ共和国', 57);
INSERT INTO `country` VALUES ('DZ', 'DZA', '12', '阿尔及利亚', '알제리', 'Algeria', 'アルジェリア', 58);
INSERT INTO `country` VALUES ('EC', 'ECU', '218', '厄瓜多尔', '에콰도르', 'Ecuador', 'エクアドル', 59);
INSERT INTO `country` VALUES ('EE', 'EST', '233', '爱沙尼亚', '에스토니아', 'Estonia', 'エストニア', 60);
INSERT INTO `country` VALUES ('EG', 'EGY', '818', '埃及', '이집트', 'Egypt', 'エジプト', 61);
INSERT INTO `country` VALUES ('EH', 'ESH', '732', '西撒哈拉', '서사하라', 'Western Sahara', '西サハラ', 62);
INSERT INTO `country` VALUES ('ER', 'ERI', '232', '厄立特里亚', '에리트레아', 'Eritrea', 'エリトリア', 63);
INSERT INTO `country` VALUES ('ES', 'ESP', '724', '西班牙', '스페인', 'Spain', 'スペイン', 64);
INSERT INTO `country` VALUES ('ET', 'ETH', '231', '埃塞俄比亚', '에티오피아', 'Ethiopia', 'エチオピア', 65);
INSERT INTO `country` VALUES ('FI', 'FIN', '246', '芬兰', '핀란드', 'Finland', 'フィンランド', 66);
INSERT INTO `country` VALUES ('FJ', 'FJI', '242', '斐济', '피지', 'Fiji', 'フィジー', 67);
INSERT INTO `country` VALUES ('FK', 'FLK', '238', '马尔维纳斯群岛（福克兰群岛）', '말빈나스 제도 (포클랜드 제도)', 'Falkland Islands (Malvinas)', 'フォークランド諸島', 68);
INSERT INTO `country` VALUES ('FM', 'FSM', '583', '密克罗尼西亚联邦', '미크로네시아 연방', 'Federated States of Micronesia', 'ミクロネシア連邦', 69);
INSERT INTO `country` VALUES ('FO', 'FRO', '234', '法罗群岛', '페로 제도', 'Faroe Islands', 'フェロー諸島', 70);
INSERT INTO `country` VALUES ('FR', 'FRA', '250', '法国', '프랑스', 'France', 'フランス', 71);
INSERT INTO `country` VALUES ('GA', 'GAB', '266', '加蓬', '가봉', 'Gabon', 'ガボン', 72);
INSERT INTO `country` VALUES ('GB', 'GBR', '826', '英国', '영국', 'United Kingdom', 'イギリス', 73);
INSERT INTO `country` VALUES ('GD', 'GRD', '308', '格林纳达', '그레나다', 'Grenada', 'グレナダ', 74);
INSERT INTO `country` VALUES ('GE', 'GEO', '268', '格鲁吉亚', '그루지야', 'Georgia', 'ジョージア', 75);
INSERT INTO `country` VALUES ('GF', 'GUF', '254', '法属圭亚那', '프랑스령 기아나', 'French Guiana', 'フランス領ギアナ', 76);
INSERT INTO `country` VALUES ('GH', 'GHA', '288', '加纳', '가나', 'Ghana', 'ガーナ', 77);
INSERT INTO `country` VALUES ('GI', 'GIB', '292', '直布罗陀', '지브롤터', 'Gibraltar', 'ジブラルタル', 78);
INSERT INTO `country` VALUES ('GL', 'GRL', '304', '格陵兰', '그린란드', 'Greenland', 'グリーンランド', 79);
INSERT INTO `country` VALUES ('GM', 'GMB', '270', '冈比亚', '감비아', 'The Gambia', 'ガンビア', 80);
INSERT INTO `country` VALUES ('GN', 'GIN', '324', '几内亚', '기니', 'Guinea', 'ギニア', 81);
INSERT INTO `country` VALUES ('GP', 'GLP', '312', '瓜德罗普', '가드루프', 'Guadeloupe', 'グアドループ', 82);
INSERT INTO `country` VALUES ('GQ', 'GNQ', '226', '赤道几内亚', '적도 기니', 'Equatorial Guinea', '赤道ギニア', 83);
INSERT INTO `country` VALUES ('GR', 'GRC', '300', '希腊', '그리스', 'Greece', 'ギリシャ', 84);
INSERT INTO `country` VALUES ('GS', 'SGS', '239', '南乔治亚岛和南桑德韦奇岛', '사우스 조지아 제도 및 사우스 샌드위치 제도', 'South Georgia and the South Sandwich Islands', 'サウスジョージア島・サウスサンド위치諸島', 85);
INSERT INTO `country` VALUES ('GT', 'GTM', '320', '危地马拉', '과테말라', 'Guatemala', 'グアテマラ', 86);
INSERT INTO `country` VALUES ('GU', 'GUM', '316', '关岛', '괌', 'Guam', 'グアム', 87);
INSERT INTO `country` VALUES ('GW', 'GNB', '624', '几内亚比绍', '기니 비사우', 'Guinea-Bissau', 'ギニアビサウ', 88);
INSERT INTO `country` VALUES ('GY', 'GUY', '328', '圭亚那', '가이아나', 'Guyana', 'ガイアナ', 89);
INSERT INTO `country` VALUES ('HK', 'HKG', '344', '中国香港', '중국 홍콩', 'Hong Kong, China', '中国香港', 90);
INSERT INTO `country` VALUES ('HM', 'HMD', '334', '赫德岛和麦克唐纳岛', '허드 섬 및 맥도널드 섬', 'Heard Island and McDonald Islands', 'ハード島・マクドナルド島', 91);
INSERT INTO `country` VALUES ('HN', 'HND', '340', '洪都拉斯', '혼두라스', 'Honduras', 'ホンジュラス', 92);
INSERT INTO `country` VALUES ('HR', 'HRV', '191', '克罗地亚', '크로아티아', 'Croatia', 'クロアチア', 93);
INSERT INTO `country` VALUES ('HT', 'HTI', '332', '海地', '하이티', 'Haiti', 'ハイチ', 94);
INSERT INTO `country` VALUES ('HU', 'HUN', '348', '匈牙利', '헝가리', 'Hungary', 'ハンガリー', 95);
INSERT INTO `country` VALUES ('ID', 'IDN', '360', '印度尼西亚', '인도네시아', 'Indonesia', 'インドネシア', 96);
INSERT INTO `country` VALUES ('IE', 'IRL', '372', '爱尔兰', '아일랜드', 'Ireland', 'アイルランド', 97);
INSERT INTO `country` VALUES ('IL', 'ISR', '376', '以色列', '이스라엘', 'Israel', 'イスラエル', 98);
INSERT INTO `country` VALUES ('IN', 'IND', '356', '印度', '인도', 'India', 'インド', 99);
INSERT INTO `country` VALUES ('IO', 'IOT', '86', '英属印度洋领土', '영국령 인도양 지역', 'British Indian Ocean Territory', '英国領インド洋地域', 100);
INSERT INTO `country` VALUES ('IQ', 'IRQ', '368', '伊拉克', '이라크', 'Iraq', 'イラク', 101);
INSERT INTO `country` VALUES ('IR', 'IRN', '364', '伊朗', '이란', 'Iran', 'イラン', 102);
INSERT INTO `country` VALUES ('IS', 'ISL', '352', '冰岛', '아이슬란드', 'Iceland', 'アイスランド', 103);
INSERT INTO `country` VALUES ('IT', 'ITA', '380', '意大利', '이탈리아', 'Italy', 'イタリア', 104);
INSERT INTO `country` VALUES ('JM', 'JAM', '388', '牙买加', '자메이카', 'Jamaica', 'ジャマイカ', 105);
INSERT INTO `country` VALUES ('JO', 'JOR', '400', '约旦', '요르단', 'Jordan', 'ヨルダン', 106);
INSERT INTO `country` VALUES ('JP', 'JPN', '392', '日本', '일본', 'Japan', '日本', 107);
INSERT INTO `country` VALUES ('KE', 'KEN', '404', '肯尼亚', '케냐', 'Kenya', 'ケニア', 108);
INSERT INTO `country` VALUES ('KG', 'KGZ', '417', '吉尔吉斯斯坦', '키르기스스탄', 'Kyrgyzstan', 'キルギス共和国', 109);
INSERT INTO `country` VALUES ('KH', 'KHM', '116', '柬埔寨', '캄보디아', 'Cambodia', 'カンボジア', 110);
INSERT INTO `country` VALUES ('KI', 'KIR', '296', '基里巴斯', '키리바시', 'Kiribati', 'キリバス', 111);
INSERT INTO `country` VALUES ('KM', 'COM', '174', '科摩罗', '코모로', 'Comoros', 'コモロ', 112);
INSERT INTO `country` VALUES ('KN', 'KNA', '659', '圣基茨和尼维斯', '세인트 킷츠 네비스', 'Saint Kitts and Nevis', 'セントクリストファー・ネイビス', 113);
INSERT INTO `country` VALUES ('KP', 'PRK', '408', '朝鲜', '북한', 'Korea, Democratic People\'s Republic of', '朝鮮民主主義人民共和国', 114);
INSERT INTO `country` VALUES ('KR', 'KOR', '410', '韩国', '한국', 'Korea, Republic of', '大韓民国', 115);
INSERT INTO `country` VALUES ('KW', 'KWT', '414', '科威特', '쿠웨이트', 'Kuwait', 'クウェート', 116);
INSERT INTO `country` VALUES ('KY', 'CYM', '136', '开曼群岛', '케이맨 제도', 'Cayman Islands', 'ケイマン諸島', 117);
INSERT INTO `country` VALUES ('KZ', 'KAZ', '398', '哈萨克斯坦', '카자흐스탄', 'Kazakhstan', 'カザフスタン', 118);
INSERT INTO `country` VALUES ('LA', 'LAO', '418', '老挝', '라오스', 'Lao People\'s Democratic Republic', 'ラオス人民民主共和国', 119);
INSERT INTO `country` VALUES ('LB', 'LBN', '422', '黎巴嫩', '레바논', 'Lebanon', 'レバノン', 120);
INSERT INTO `country` VALUES ('LC', 'LCA', '662', '圣卢西亚', '세인트 루시아', 'Saint Lucia', 'セントルシア', 121);
INSERT INTO `country` VALUES ('LI', 'LIE', '438', '列支敦士登', '리히텐슈타인', 'Liechtenstein', 'リヒテンシュタイン', 122);
INSERT INTO `country` VALUES ('LK', 'LKA', '144', '斯里兰卡', '스리랑카', 'Sri Lanka', 'スリランカ', 123);
INSERT INTO `country` VALUES ('LR', 'LBR', '430', '利比里亚', '라이베리아', 'Liberia', 'ライベリア', 124);
INSERT INTO `country` VALUES ('LS', 'LSO', '426', '莱索托', '레소토', 'Lesotho', 'レソト', 125);
INSERT INTO `country` VALUES ('LT', 'LTU', '440', '立陶宛', '리투아니아', 'Lithuania', 'リトアニア', 126);
INSERT INTO `country` VALUES ('LU', 'LUX', '442', '卢森堡', '룩셈부르크', 'Luxembourg', 'ルクセンブルク', 127);
INSERT INTO `country` VALUES ('LV', 'LVA', '428', '拉脱维亚', '라트비아', 'Latvia', 'ラトビア', 128);
INSERT INTO `country` VALUES ('LY', 'LBY', '434', '利比亚', '리비아', 'Libya', 'リビア', 129);
INSERT INTO `country` VALUES ('MA', 'MAR', '504', '摩洛哥', '모로코', 'Morocco', 'モロッコ', 130);
INSERT INTO `country` VALUES ('MC', 'MCO', '492', '摩纳哥', '모나코', 'Monaco', 'モナコ', 131);
INSERT INTO `country` VALUES ('MD', 'MDA', '498', '摩尔多瓦', '몰도바', 'Moldova, Republic of', 'モルドバ共和国', 132);
INSERT INTO `country` VALUES ('MG', 'MDG', '450', '马达加斯加', '마다가스카르', 'Madagascar', 'マダガスカル', 133);
INSERT INTO `country` VALUES ('MH', 'MHL', '584', '马绍尔群岛', '마샬 제도', 'Marshall Islands', 'マーシャル諸島', 134);
INSERT INTO `country` VALUES ('MK', 'MKD', '807', '北马其顿', '북마케도니아', 'North Macedonia', '北マケドニア共和国', 135);
INSERT INTO `country` VALUES ('ML', 'MLI', '466', '马里', '말리', 'Mali', 'マリ', 136);
INSERT INTO `country` VALUES ('MM', 'MMR', '104', '缅甸', '미얀마', 'Myanmar', 'ミャンマー', 137);
INSERT INTO `country` VALUES ('MN', 'MNG', '496', '蒙古', '몽골', 'Mongolia', 'モンゴル', 138);
INSERT INTO `country` VALUES ('MO', 'MAC', '446', '中国澳门', '중국 마카오', 'Macao, China', '中国マカオ', 139);
INSERT INTO `country` VALUES ('MP', 'MNP', '585', '北马里亚纳群岛', '북마리아나 제도', 'Northern Mariana Islands', '北マリアナ諸島', 140);
INSERT INTO `country` VALUES ('MQ', 'MTQ', '474', '马提尼克', '마르티니크', 'Martinique', 'マルティニーク', 141);
INSERT INTO `country` VALUES ('MR', 'MRT', '478', '毛里塔尼亚', '모리타니아', 'Mauritania', 'モーリタニア', 142);
INSERT INTO `country` VALUES ('MS', 'MSR', '500', '蒙特塞拉特', '몬트세랫', 'Montserrat', 'モントセラト', 143);
INSERT INTO `country` VALUES ('MT', 'MLT', '470', '马耳他', '몰타', 'Malta', 'マルタ', 144);
INSERT INTO `country` VALUES ('MU', 'MUS', '480', '毛里求斯', '모리셔스', 'Mauritius', 'モーリシャス', 145);
INSERT INTO `country` VALUES ('MV', 'MDV', '462', '马尔代夫', '몰디브', 'Maldives', 'モルディブ', 146);
INSERT INTO `country` VALUES ('MW', 'MWI', '454', '马拉维', '말라위', 'Malawi', 'マラウイ', 147);
INSERT INTO `country` VALUES ('MX', 'MEX', '484', '墨西哥', '멕시코', 'Mexico', 'メキシコ', 148);
INSERT INTO `country` VALUES ('MY', 'MYS', '458', '马来西亚', '말레이시아', 'Malaysia', 'マレーシア', 149);
INSERT INTO `country` VALUES ('MZ', 'MOZ', '508', '莫桑比克', '모잠비크', 'Mozambique', 'モザンビーク', 150);
INSERT INTO `country` VALUES ('NA', 'NAM', '516', '纳米比亚', '나미비아', 'Namibia', 'ナミビア', 151);
INSERT INTO `country` VALUES ('NC', 'NCL', '540', '新喀里多尼亚', '뉴칼레도니아', 'New Caledonia', 'ニューカレドニア', 152);
INSERT INTO `country` VALUES ('NE', 'NER', '562', '尼日尔', '니제르', 'Niger', 'ニジェール', 153);
INSERT INTO `country` VALUES ('NF', 'NFK', '574', '诺福克岛', '노퍽 섬', 'Norfolk Island', 'ノーフォーク島', 154);
INSERT INTO `country` VALUES ('NG', 'NGA', '566', '尼日利亚', '나이지리아', 'Nigeria', 'ナイジェリア', 155);
INSERT INTO `country` VALUES ('NI', 'NIC', '558', '尼加拉瓜', '니카라과', 'Nicaragua', 'ニカラグア', 156);
INSERT INTO `country` VALUES ('NL', 'NLD', '528', '荷兰', '네덜란드', 'Netherlands', 'オランダ', 157);
INSERT INTO `country` VALUES ('NO', 'NOR', '578', '挪威', '노르웨이', 'Norway', 'ノルウェー', 158);
INSERT INTO `country` VALUES ('NP', 'NPL', '524', '尼泊尔', '네팔', 'Nepal', 'ネパール', 159);
INSERT INTO `country` VALUES ('NR', 'NRU', '520', '瑙鲁', '나우루', 'Nauru', 'ナウル', 160);
INSERT INTO `country` VALUES ('NU', 'NIU', '570', '纽埃', '니우에', 'Niue', 'ニウエ', 161);
INSERT INTO `country` VALUES ('NZ', 'NZL', '554', '新西兰', '뉴질랜드', 'New Zealand', 'ニュージーランド', 162);
INSERT INTO `country` VALUES ('OM', 'OMN', '512', '阿曼', '오만', 'Oman', 'オマーン', 163);
INSERT INTO `country` VALUES ('PA', 'PAN', '591', '巴拿马', '파나마', 'Panama', 'パナマ', 164);
INSERT INTO `country` VALUES ('PE', 'PER', '604', '秘鲁', '페루', 'Peru', 'ペルー', 165);
INSERT INTO `country` VALUES ('PF', 'PYF', '258', '法属波利尼西亚', '프랑스령 폴리네시아', 'French Polynesia', 'フランス領ポリネシア', 166);
INSERT INTO `country` VALUES ('PG', 'PNG', '598', '巴布亚新几内亚', '파푸아뉴기니', 'Papua New Guinea', 'パプアニューギニア', 167);
INSERT INTO `country` VALUES ('PH', 'PHL', '608', '菲律宾', '필리핀', 'Philippines', 'フィリピン', 168);
INSERT INTO `country` VALUES ('PK', 'PAK', '586', '巴基斯坦', '파키스탄', 'Pakistan', 'パキスタン', 169);
INSERT INTO `country` VALUES ('PL', 'POL', '616', '波兰', '폴란드', 'Poland', 'ポーランド', 170);
INSERT INTO `country` VALUES ('PM', 'SPM', '666', '圣皮埃尔和密克隆', '세인트 피에르 미클롱', 'Saint Pierre and Miquelon', 'サンピエール・ミクロン', 171);
INSERT INTO `country` VALUES ('PN', 'PCN', '612', '皮特凯恩群岛', '피트케인 제도', 'Pitcairn Islands Group', 'ピトケアン諸島', 172);
INSERT INTO `country` VALUES ('PR', 'PRI', '630', '波多黎各', '푸에르토리코', 'Puerto Rico', 'プエルトリコ', 173);
INSERT INTO `country` VALUES ('PS', 'PSE', '275', '巴勒斯坦', '팔레스타인', 'Palestine', 'パレスチナ', 174);
INSERT INTO `country` VALUES ('PT', 'PRT', '620', '葡萄牙', '포르투갈', 'Portugal', 'ポルトガル', 175);
INSERT INTO `country` VALUES ('PW', 'PLW', '585', '贝劳', '팔라우', 'Palau', 'パラオ', 176);
INSERT INTO `country` VALUES ('PY', 'PRY', '600', '巴拉圭', '파라과이', 'Paraguay', 'パラグアイ', 177);
INSERT INTO `country` VALUES ('QA', 'QAT', '634', '卡塔尔', '카타르', 'Qatar', 'カタール', 178);
INSERT INTO `country` VALUES ('RE', 'REU', '638', '留尼汪', '레위니옹', 'Reunion', 'レユニオン', 179);
INSERT INTO `country` VALUES ('RO', 'ROM', '642', '罗马尼亚', '루마니아', 'Romania', 'ルーマニア', 180);
INSERT INTO `country` VALUES ('RU', 'RUS', '643', '俄罗斯', '러시아', 'Russia', 'ロシア', 181);
INSERT INTO `country` VALUES ('RW', 'RWA', '646', '卢旺达', '루완다', 'Rwanda', 'ルワンダ', 182);
INSERT INTO `country` VALUES ('SA', 'SAU', '682', '沙特阿拉伯', '사우디아라비아', 'Saudi Arabia', 'サウジアラビア', 183);
INSERT INTO `country` VALUES ('SB', 'SLB', '90', '所罗门群岛', '솔로몬 제도', 'Solomon Islands', 'ソロモン諸島', 184);
INSERT INTO `country` VALUES ('SC', 'SYC', '690', '塞舌尔', '세이셸', 'Seychelles', 'セイシェル', 185);
INSERT INTO `country` VALUES ('SD', 'SDN', '729', '苏丹', '수단', 'Sudan', 'スーダン', 186);
INSERT INTO `country` VALUES ('SE', 'SWE', '752', '瑞典', '스웨덴', 'Sweden', 'スウェーデン', 187);
INSERT INTO `country` VALUES ('SG', 'SGP', '702', '新加坡', '싱가포르', 'Singapore', 'シンガポール', 188);
INSERT INTO `country` VALUES ('SH', 'SHN', '654', '圣赫勒拿', '세인트 헬레나', 'Saint Helena', 'セントヘレナ', 189);
INSERT INTO `country` VALUES ('SI', 'SVN', '705', '斯洛文尼亚', '슬로베니아', 'Slovenia', 'スロベニア', 190);
INSERT INTO `country` VALUES ('SJ', 'SJM', '744', '斯瓦尔巴群岛和扬马延岛', '스발바르 제도 및 얀 마이엔 섬', 'Svalbard and Jan Mayen Islands', 'スバルバル諸島・ヤンマイエン島', 191);
INSERT INTO `country` VALUES ('SK', 'SVK', '703', '斯洛伐克', '슬로바키아', 'Slovakia', 'スロバキア', 192);
INSERT INTO `country` VALUES ('SL', 'SLE', '694', '塞拉利昂', '시에라리온', 'Sierra Leone', 'シエラレオネ', 193);
INSERT INTO `country` VALUES ('SM', 'SMR', '674', '圣马力诺', '산 마리노', 'San Marino', 'サンマリノ', 194);
INSERT INTO `country` VALUES ('SN', 'SEN', '686', '塞内加尔', '세네갈', 'Senegal', 'セネガル', 195);
INSERT INTO `country` VALUES ('SO', 'SOM', '706', '索马里', '소말리아', 'Somalia', 'ソマリア', 196);
INSERT INTO `country` VALUES ('SR', 'SUR', '740', '苏里南', '수리남', 'Suriname', 'スリナム', 197);
INSERT INTO `country` VALUES ('ST', 'STP', '678', '圣多美和普林西比', '상토메 프린시페', 'Sao Tome and Principe', 'サントメ・プリンシペ', 198);
INSERT INTO `country` VALUES ('SV', 'SLV', '222', '萨尔瓦多', '엘살바도르', 'El Salvador', 'エルサルバドル', 199);
INSERT INTO `country` VALUES ('SY', 'SYR', '760', '叙利亚', '시리아', 'Syria', 'シリア', 200);
INSERT INTO `country` VALUES ('SZ', 'SWZ', '748', '斯威士兰', '스와질란드', 'Swaziland', 'スワジランド', 201);
INSERT INTO `country` VALUES ('TC', 'TCA', '796', '特克斯和凯科斯群岛', '터크스 케이커스 제도', 'Turks and Caicos Islands', 'タークス・ケイコス諸島', 202);
INSERT INTO `country` VALUES ('TD', 'TCD', '148', '乍得', '차드', 'Chad', 'チャド', 203);
INSERT INTO `country` VALUES ('TF', 'ATF', '260', '法属南部领土', '프랑스령 남부 지역', 'French Southern Territories', 'フランス領南部地域', 204);
INSERT INTO `country` VALUES ('TG', 'TGO', '768', '多哥', '토고', 'Togo', 'トーゴ', 205);
INSERT INTO `country` VALUES ('TH', 'THA', '764', '泰国', '태국', 'Thailand', 'タイ', 206);
INSERT INTO `country` VALUES ('TJ', 'TJK', '762', '塔吉克斯坦', '타지키스탄', 'Tajikistan', 'タジキスタン', 207);
INSERT INTO `country` VALUES ('TK', 'TKL', '772', '托克劳', '토클라우', 'Tokelau', 'トケラウ', 208);
INSERT INTO `country` VALUES ('TM', 'TKM', '795', '土库曼斯坦', '투르크메니스탄', 'Turkmenistan', 'トゥルクメニスタン', 209);
INSERT INTO `country` VALUES ('TN', 'TUN', '788', '突尼斯', '튀니지', 'Tunisia', 'チュニジア', 210);
INSERT INTO `country` VALUES ('TO', 'TON', '776', '汤加', '통가', 'Tonga', 'トンガ', 211);
INSERT INTO `country` VALUES ('TP', 'TMP', '626', '东帝汶', '동티모르', 'East Timor', '東ティモール', 212);
INSERT INTO `country` VALUES ('TR', 'TUR', '792', '土耳其', '터키', 'Turkey', 'トルコ', 213);
INSERT INTO `country` VALUES ('TT', 'TTO', '780', '特立尼达和多巴哥', '트리니다드 토바고', 'Trinidad and Tobago', 'トリニダード・ト바고', 214);
INSERT INTO `country` VALUES ('TV', 'TUV', '798', '图瓦卢', '투발루', 'Tuvalu', 'ツバル', 215);
INSERT INTO `country` VALUES ('TZ', 'TZA', '834', '坦桑尼亚', '탄자니아', 'Tanzania', 'タンザニア', 216);
INSERT INTO `country` VALUES ('UA', 'UKR', '804', '乌克兰', '우크라이나', 'Ukraine', 'ウクライナ', 217);
INSERT INTO `country` VALUES ('UG', 'UGA', '800', '乌干达', '우간다', 'Uganda', 'ウガンダ', 218);
INSERT INTO `country` VALUES ('UM', 'UMI', '581', '美属太平洋各群岛', '미국령 태평양 각 섬', 'United States Miscellaneous Pacific Islands', 'アメリカ合衆国領太平洋諸島', 219);
INSERT INTO `country` VALUES ('US', 'USA', '840', '美国', '미국', 'United States', 'アメリカ合衆国', 220);
INSERT INTO `country` VALUES ('UY', 'URY', '858', '乌拉圭', '우루과이', 'Uruguay', 'ウルグアイ', 221);
INSERT INTO `country` VALUES ('UZ', 'UZB', '860', '乌兹别克斯坦', '우즈베키스탄', 'Uzbekistan', 'ウズベキスタン', 222);
INSERT INTO `country` VALUES ('VA', 'VAT', '336', '梵蒂冈', '바티칸', 'Vatican', 'バチカン市国', 223);
INSERT INTO `country` VALUES ('VC', 'VCT', '670', '圣文森特和格林纳丁斯', '세인트 빈센트 그레나딘스', 'Saint Vincent and the Grenadines', 'セントヴィンセント・グレナディーンズ', 224);
INSERT INTO `country` VALUES ('VE', 'VEN', '862', '委内瑞拉', '베네수엘라', 'Venezuela', 'ベネズエラ', 225);
INSERT INTO `country` VALUES ('VG', 'VGB', '92', '英属维尔京群岛', '영국령 버진 제도', 'British Virgin Islands', 'イギリス領ヴァージン諸島', 226);
INSERT INTO `country` VALUES ('VI', 'VIR', '850', '美属维尔京群岛', '미국령 버진 제도', 'United States Virgin Islands', 'アメリカ領ヴァージン諸島', 227);
INSERT INTO `country` VALUES ('VN', 'VNM', '704', '越南', '베트남', 'Viet Nam', 'ベトナム', 228);
INSERT INTO `country` VALUES ('VU', 'VUT', '548', '瓦努阿图', '바누아투', 'Vanuatu', 'バヌアツ', 229);
INSERT INTO `country` VALUES ('WF', 'WLF', '876', '瓦利斯和富图纳群岛', '왈리스 푸투나 제도', 'Wallis and Futuna Islands', 'ワリス・フツナ諸島', 230);
INSERT INTO `country` VALUES ('WS', 'WSM', '882', '西萨摩亚', '서사모아', 'Western Samoa', '西サモア', 231);
INSERT INTO `country` VALUES ('YE', 'YEM', '887', '也门', '예멘', 'Yemen', 'イエメン', 232);
INSERT INTO `country` VALUES ('YT', 'MYT', '175', '马约特', '마요트', 'Mayotte', 'マヨット', 233);
INSERT INTO `country` VALUES ('YU', 'YUG', '891', '南斯拉夫', '유고슬라비아', 'Yugoslavia', 'ユーゴスラビア', 234);
INSERT INTO `country` VALUES ('ZA', 'ZAF', '710', '南非', '남아프리카', 'South Africa', '南アフリカ共和国', 235);
INSERT INTO `country` VALUES ('ZM', 'ZMB', '894', '赞比亚', '잠비아', 'Zambia', 'ザンビア', 236);
INSERT INTO `country` VALUES ('ZR', 'ZAR', '180', '扎伊尔', '자이르', 'Zaire', 'ザイール', 237);
INSERT INTO `country` VALUES ('ZW', 'ZWE', '716', '津巴布韦', '짐바브웨', 'Zimbabwe', 'ジンバブエ', 238);

-- ----------------------------
-- Table structure for province
-- ----------------------------
DROP TABLE IF EXISTS `province`;
CREATE TABLE `province`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `iso` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ISO 二位国家代码',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '省代码',
  `name_cn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '省名 中文',
  `name_kr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省名 韩语',
  `name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省名 英语',
  `name_jp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省名 日语',
  `sort` smallint(6) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_iso`(`iso`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '地区 省/州 表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of province
-- ----------------------------
INSERT INTO `province` VALUES ('CN-11', 'CN', '11', '北京市', '베이징시', 'Beijing', '北京市', 1);
INSERT INTO `province` VALUES ('CN-12', 'CN', '12', '天津市', '천진시', 'Tianjin', '天津市', 3);
INSERT INTO `province` VALUES ('CN-13', 'CN', '13', '河北省', '허베이성', 'Hebei Province', '河北省', 5);
INSERT INTO `province` VALUES ('CN-14', 'CN', '14', '山西省', '산시성', 'Shanxi Province', '山西省', 6);
INSERT INTO `province` VALUES ('CN-15', 'CN', '15', '内蒙古自治区', '내몽골자치구', 'Inner Mongolia Autonomous Region', '内モンゴル自治区', 28);
INSERT INTO `province` VALUES ('CN-21', 'CN', '21', '辽宁省', '료닝성', 'Liaoning Province', '遼寧省', 7);
INSERT INTO `province` VALUES ('CN-22', 'CN', '22', '吉林省', '길림성', 'Jilin Province', '吉林省', 8);
INSERT INTO `province` VALUES ('CN-23', 'CN', '23', '黑龙江省', '헤이룽장성', 'Heilongjiang Province', '黒竜江省', 9);
INSERT INTO `province` VALUES ('CN-31', 'CN', '31', '上海市', '상하이시', 'Shanghai', '上海市', 2);
INSERT INTO `province` VALUES ('CN-32', 'CN', '32', '江苏省', '장쑤성', 'Jiangsu Province', '江蘇省', 10);
INSERT INTO `province` VALUES ('CN-33', 'CN', '33', '浙江省', '절강성', 'Zhejiang Province', '浙江省', 11);
INSERT INTO `province` VALUES ('CN-34', 'CN', '34', '安徽省', '안후이성', 'Anhui Province', '安徽省', 12);
INSERT INTO `province` VALUES ('CN-35', 'CN', '35', '福建省', '푸젠성', 'Fujian Province', '福建省', 13);
INSERT INTO `province` VALUES ('CN-36', 'CN', '36', '江西省', '강서성', 'Jiangxi Province', '江西省', 14);
INSERT INTO `province` VALUES ('CN-37', 'CN', '37', '山东省', '산동성', 'Shandong Province', '山東省', 15);
INSERT INTO `province` VALUES ('CN-41', 'CN', '41', '河南省', '허난성', 'Henan Province', '河南省', 16);
INSERT INTO `province` VALUES ('CN-42', 'CN', '42', '湖北省', '후베이성', 'Hubei Province', '湖北省', 17);
INSERT INTO `province` VALUES ('CN-43', 'CN', '43', '湖南省', '후난성', 'Hunan Province', '湖南省', 18);
INSERT INTO `province` VALUES ('CN-44', 'CN', '44', '广东省', '광동성', 'Guangdong Province', '広東省', 19);
INSERT INTO `province` VALUES ('CN-45', 'CN', '45', '广西壮族自治区', '광서좡족자치구', 'Guangxi Zhuang Autonomous Region', '広西チワン族自治区', 29);
INSERT INTO `province` VALUES ('CN-46', 'CN', '46', '海南省', '하이난성', 'Hainan Province', '海南省', 20);
INSERT INTO `province` VALUES ('CN-50', 'CN', '50', '重庆市', '중경시', 'Chongqing', '重慶市', 4);
INSERT INTO `province` VALUES ('CN-51', 'CN', '51', '四川省', '사천성', 'Sichuan Province', '四川省', 21);
INSERT INTO `province` VALUES ('CN-52', 'CN', '52', '贵州省', '귀주성', 'Guizhou Province', '貴州省', 22);
INSERT INTO `province` VALUES ('CN-53', 'CN', '53', '云南省', '운남성', 'Yunnan Province', '雲南省', 23);
INSERT INTO `province` VALUES ('CN-54', 'CN', '54', '西藏自治区', '시장자치구', 'Tibet Autonomous Region', 'チベット自治区', 32);
INSERT INTO `province` VALUES ('CN-61', 'CN', '61', '陕西省', '산시성', 'Shaanxi Province', '陝西省', 24);
INSERT INTO `province` VALUES ('CN-62', 'CN', '62', '甘肃省', '간수성', 'Gansu Province', '甘粛省', 25);
INSERT INTO `province` VALUES ('CN-63', 'CN', '63', '青海省', '청하이성', 'Qinghai Province', '青海省', 26);
INSERT INTO `province` VALUES ('CN-64', 'CN', '64', '宁夏回族自治区', '닝샤 회족자치구', 'Ningxia Hui Autonomous Region', '寧夏回族自治区', 30);
INSERT INTO `province` VALUES ('CN-65', 'CN', '65', '新疆维吾尔自治区', '신장위구르자치구', 'Xinjiang Uygur Autonomous Region', '新疆ウイグル自治区', 31);
INSERT INTO `province` VALUES ('CN-71', 'CN', '71', '台湾省', '타이완성', 'Taiwan Province', '台湾省', 27);
INSERT INTO `province` VALUES ('CN-81', 'CN', '81', '香港特别行政区', '홍콩특별행정구', 'Hong Kong Special Administrative Region', '香港特別行政区', 33);
INSERT INTO `province` VALUES ('CN-82', 'CN', '82', '澳门特别行政区', '마카오특별행정구', 'Macao Special Administrative Region', '澳門特別行政区', 34);
INSERT INTO `province` VALUES ('US-AK', 'US', 'AK', '阿拉斯加州', '알래스카주', 'Alaska', 'アラスカ州', 2);
INSERT INTO `province` VALUES ('US-AL', 'US', 'AL', '亚拉巴马州', '알라바마주', 'Alabama', 'アラバマ州', 1);
INSERT INTO `province` VALUES ('US-AR', 'US', 'AR', '阿肯色州', '아칸소주', 'Arkansas', 'アーカンソー州', 4);
INSERT INTO `province` VALUES ('US-AZ', 'US', 'AZ', '亚利桑那州', '애리조나주', 'Arizona', 'アリゾナ州', 3);
INSERT INTO `province` VALUES ('US-CA', 'US', 'CA', '加利福尼亚州', '캘리포니아주', 'California', 'カリフォルニア州', 5);
INSERT INTO `province` VALUES ('US-CO', 'US', 'CO', '科罗拉多州', '콜로라도주', 'Colorado', 'コロラド州', 6);
INSERT INTO `province` VALUES ('US-CT', 'US', 'CT', '康涅狄格州', '커네티컷주', 'Connecticut', 'コネチカット州', 7);
INSERT INTO `province` VALUES ('US-DE', 'US', 'DE', '特拉华州', '델라웨어주', 'Delaware', 'デラウェア州', 8);
INSERT INTO `province` VALUES ('US-FL', 'US', 'FL', '佛罗里达州', '플로리다주', 'Florida', 'フロリダ州', 9);
INSERT INTO `province` VALUES ('US-GA', 'US', 'GA', '佐治亚州', '조지아주', 'Georgia', 'ジョージア州', 10);
INSERT INTO `province` VALUES ('US-HI', 'US', 'HI', '夏威夷州', '하와이주', 'Hawaii', 'ハワイ州', 11);
INSERT INTO `province` VALUES ('US-IA', 'US', 'IA', '艾奥瓦州', '아이오와주', 'Iowa', 'アイオワ州', 15);
INSERT INTO `province` VALUES ('US-ID', 'US', 'ID', '爱达荷州', '아이다호주', 'Idaho', 'アイダホ州', 12);
INSERT INTO `province` VALUES ('US-IL', 'US', 'IL', '伊利诺伊州', '일리노이주', 'Illinois', 'イリノイ州', 13);
INSERT INTO `province` VALUES ('US-IN', 'US', 'IN', '印第安纳州', '인디애나주', 'Indiana', 'インディアナ州', 14);
INSERT INTO `province` VALUES ('US-KS', 'US', 'KS', '堪萨斯州', '칸사스주', 'Kansas', 'カンザス州', 16);
INSERT INTO `province` VALUES ('US-KY', 'US', 'KY', '肯塔基州', '켄터키주', 'Kentucky', 'ケンタッキー州', 17);
INSERT INTO `province` VALUES ('US-LA', 'US', 'LA', '路易斯安那州', '루이지애나주', 'Louisiana', 'ルイジアナ州', 18);
INSERT INTO `province` VALUES ('US-MA', 'US', 'MA', '马萨诸塞州', '매사추세츠주', 'Massachusetts', 'マサチューセッツ州', 21);
INSERT INTO `province` VALUES ('US-MD', 'US', 'MD', '马里兰州', '메릴랜드주', 'Maryland', 'メリーランド州', 20);
INSERT INTO `province` VALUES ('US-ME', 'US', 'ME', '缅因州', '메인주', 'Maine', 'メイン州', 19);
INSERT INTO `province` VALUES ('US-MI', 'US', 'MI', '密歇根州', '미시간주', 'Michigan', 'ミシガン州', 22);
INSERT INTO `province` VALUES ('US-MN', 'US', 'MN', '明尼苏达州', '미네소타주', 'Minnesota', 'ミネソタ州', 23);
INSERT INTO `province` VALUES ('US-MO', 'US', 'MO', '密苏里州', '미주리주', 'Missouri', 'ミズーリー州', 25);
INSERT INTO `province` VALUES ('US-MS', 'US', 'MS', '密西西比州', '미시시피주', 'Mississippi', 'ミシシッピー州', 24);
INSERT INTO `province` VALUES ('US-MT', 'US', 'MT', '蒙大拿州', '몬타나주', 'Montana', 'モンタナ州', 26);
INSERT INTO `province` VALUES ('US-NC', 'US', 'NC', '北卡罗来纳州', '노스캐롤라이나주', 'North Carolina', 'ノースカロライナ州', 33);
INSERT INTO `province` VALUES ('US-ND', 'US', 'ND', '北达科他州', '노스다코타주', 'North Dakota', 'ノースダコタ州', 34);
INSERT INTO `province` VALUES ('US-NE', 'US', 'NE', '内布拉斯加州', '네브라스카주', 'Nebraska', 'ネブラスカ州', 27);
INSERT INTO `province` VALUES ('US-NH', 'US', 'NH', '新罕布什尔州', '뉴햄프셔주', 'New Hampshire', 'ニューハンプシャー州', 29);
INSERT INTO `province` VALUES ('US-NJ', 'US', 'NJ', '新泽西州', '뉴저지주', 'New Jersey', 'ニュージャージー州', 30);
INSERT INTO `province` VALUES ('US-NM', 'US', 'NM', '新墨西哥州', '뉴멕시코주', 'New Mexico', 'ニューメキシコ州', 31);
INSERT INTO `province` VALUES ('US-NV', 'US', 'NV', '内华达州', '네바다주', 'Nevada', 'ネバダ州', 28);
INSERT INTO `province` VALUES ('US-NY', 'US', 'NY', '纽约州', '뉴욕주', 'New York', 'ニューヨーク州', 32);
INSERT INTO `province` VALUES ('US-OH', 'US', 'OH', '俄亥俄州', '오하이오주', 'Ohio', 'オハイオ州', 35);
INSERT INTO `province` VALUES ('US-OK', 'US', 'OK', '俄克拉何马州', '오클라호마주', 'Oklahoma', 'オクラホマ州', 36);
INSERT INTO `province` VALUES ('US-OR', 'US', 'OR', '俄勒冈州', '오레곤주', 'Oregon', 'オレゴン州', 37);
INSERT INTO `province` VALUES ('US-PA', 'US', 'PA', '宾夕法尼亚州', '펜실베니아주', 'Pennsylvania', 'ペンシルベニア州', 38);
INSERT INTO `province` VALUES ('US-RI', 'US', 'RI', '罗得岛州', '로드아일랜드주', 'Rhode Island', 'ロードアイランド州', 39);
INSERT INTO `province` VALUES ('US-SC', 'US', 'SC', '南卡罗来纳州', '사우스캐롤라이나주', 'South Carolina', 'サウスカロライナ州', 40);
INSERT INTO `province` VALUES ('US-SD', 'US', 'SD', '南达科他州', '사우스다코타주', 'South Dakota', 'サウスダコタ州', 41);
INSERT INTO `province` VALUES ('US-TN', 'US', 'TN', '田纳西州', '테네시주', 'Tennessee', 'テネシー州', 42);
INSERT INTO `province` VALUES ('US-TX', 'US', 'TX', '德克萨斯州', '텍사스주', 'Texas', 'テキサス州', 43);
INSERT INTO `province` VALUES ('US-UT', 'US', 'UT', '犹他州', '유타주', 'Utah', 'ユタ州', 44);
INSERT INTO `province` VALUES ('US-VA', 'US', 'VA', '弗吉尼亚州', '버지니아주', 'Virginia', 'バージニア州', 46);
INSERT INTO `province` VALUES ('US-VT', 'US', 'VT', '佛蒙特州', '버몬트주', 'Vermont', 'バーモント州', 45);
INSERT INTO `province` VALUES ('US-WA', 'US', 'WA', '华盛顿州', '워싱턴주', 'Washington', 'ワシントン州', 47);
INSERT INTO `province` VALUES ('US-WI', 'US', 'WI', '威斯康星州', '위스콘신주', 'Wisconsin', 'ウィスコンシン州', 49);
INSERT INTO `province` VALUES ('US-WV', 'US', 'WV', '西弗吉尼亚州', '웨스트버지니아주', 'West Virginia', 'ウェストバージニア州', 48);
INSERT INTO `province` VALUES ('US-WY', 'US', 'WY', '怀俄明州', '와이오밍주', 'Wyoming', 'ワイオミング州', 50);

SET FOREIGN_KEY_CHECKS = 1;
