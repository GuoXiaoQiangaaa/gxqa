package com.pwc.common.utils;

import java.util.StringTokenizer;

public class FormatUtil {
    /**
     * 日期格式化
     * @param date
     * @return
     */
    public static Boolean formatDate(String date) {
        if (date.length() == 11) {
            boolean YYYYDate = date.substring(0, 4).matches("[0-9]+");
            boolean YYYYStr = "年".equals(date.substring(4, 5));
            boolean MMDate = date.substring(5, 7).matches("[0-9]+");
            boolean MMStr = "月".equals(date.substring(7, 8));
            boolean DDDate = date.substring(8, 10).matches("[0-9]+");
            boolean DDStr = "日".equals(date.substring(10, 11));
            if (YYYYDate && YYYYStr && MMDate && MMStr && DDDate && DDStr) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 金额格式化
     * @param price
     * @return
     */
    public static Boolean formatPrice(String price) {
        if (price.contains(".")) {//验证是否是float型
            if (price.indexOf('.') == price.lastIndexOf('.')) {
                StringTokenizer st = new StringTokenizer(price, ".");
                while (st.hasMoreElements()) {
                    String splitStr = st.nextToken();
                    for (int i = splitStr.length(); --i >= 0; ) {
                        if (!Character.isDigit(splitStr.charAt(i))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
