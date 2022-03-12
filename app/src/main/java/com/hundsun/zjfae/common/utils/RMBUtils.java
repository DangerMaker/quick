package com.hundsun.zjfae.common.utils;

import java.math.BigDecimal;

public class RMBUtils {

    /********************************金额大小写转换开始**********************************************/

    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = {"0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9"};
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = {"分", "角", "元",
            "拾", "百", "千", "万", "拾", "百", "千", "亿", "拾", "百", "千", "万", "拾",
            "百", "千"};
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;


    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param money 输入的金额
     * @return 对应的汉语大写
     */
    public static String number2CNMontrayUnit(String money) {
        money = money.replaceAll(",", "");
        if (money.length() == 1 && money.equals(".")) {
            money = "0.";
        }
        money = money.trim();
        if (money.length() > 13) {
            return "超出计算范围";
        }
        BigDecimal numberOfMoney = new BigDecimal(money);
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }

    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param money 输入的金额
     * @return 对应的汉语大写
     */
    public static String numberCNMontrayUnit(String money) {
        money = money.replaceAll(",", "");
        if (money.length() == 1 && money.equals(".")) {
            money = "0.00";
        }
        money = money.trim();

        if (money.length() < 4) {

            return money;
        }

        if (money.length() > 13) {
            return "超出计算范围";
        }
        BigDecimal numberOfMoney = new BigDecimal(money);
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        return sb.toString();
    }

    /********************************金额大小写转换结束**********************************************/
    /**
     * <pre>
     * 数字格式化显示
     * 小于万默认显示 大于万以1.7万方式显示最大是9999.9万
     * 大于亿以1.1亿方式显示最大没有限制都是亿单位
     * make by dongxh 2017年12月28日上午10:05:22
     * </pre>
     *
     * @param num 格式化的数字
     * @return
     */
//    public static DecimalFormat fnum = new DecimalFormat("0.00000000");

    public static String formatWanNum(String num) {

        num = num.replaceAll(",", "");
        if (num.length() == 1 && num.equals(".")) {
            num = "0.00";
        }
        //如果小于等于1000使用上面那个方法
        if (!MoneyUtil.moneyComp(num, "1000")) {
            return numberCNMontrayUnit(num);
        }
        StringBuffer sb = new StringBuffer();
        BigDecimal b0 = new BigDecimal("1000");
        BigDecimal b1 = new BigDecimal("10000");
        BigDecimal b2 = new BigDecimal("100000000");
        BigDecimal b3 = new BigDecimal(num);

        String formatNumStr = "";
        String nuit = "";

        // 以千为单位处理
        //小于一万
        if (!MoneyUtil.moneyCompare(num, "10000")) {
            if (b3.compareTo(b0) == 0 || b3.compareTo(b0) == 1) {
                formatNumStr = b3.divide(b0).toString();
                nuit = "千";
            }
        } else {
            // 以万为单位处理
            if (b3.compareTo(b1) == -1) {
                sb.append(b3.toString());
            } else if ((b3.compareTo(b1) == 0 && b3.compareTo(b1) == 1)
                    || b3.compareTo(b2) == -1) {
                formatNumStr = b3.divide(b1).toString();
                nuit = "万";
            } else if (b3.compareTo(b2) == 0 || b3.compareTo(b2) == 1) {
                formatNumStr = b3.divide(b2).toString();
                nuit = "亿";
            }
        }
        BigDecimal bigDecimal = new BigDecimal(formatNumStr);
        formatNumStr = bigDecimal.stripTrailingZeros().toPlainString();
        sb.append(formatNumStr).append(nuit);
//        if (!"".equals(formatNumStr)) {
//            int i = formatNumStr.indexOf(".");
//            if (i == -1) {
//                sb.append(formatNumStr).append(nuit);
//            } else {
//                i = i + 1;
//                String v = formatNumStr.substring(i, i + 1);
//                if (!v.equals("0")) {
//                    sb.append(formatNumStr.substring(0, i + 1)).append(nuit);
//                } else {
//                    sb.append(formatNumStr.substring(0, i - 1)).append(nuit);
//                }
//            }
//        }
        if (sb.length() == 0)
            return "0";
        return sb.toString();
    }
}
