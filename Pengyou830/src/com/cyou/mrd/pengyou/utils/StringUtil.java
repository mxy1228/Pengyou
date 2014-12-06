package com.cyou.mrd.pengyou.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class StringUtil {
    /**
     * 字符串类型转换为int类型
     * 
     * @param value
     * @return
     */
    public static int getIntValue(String value) {
        int intvalue = 0;
        if (TextUtils.isEmpty(value)) {
            return intvalue;
        }
        try {
            intvalue = Integer.parseInt(value);
        } catch (Exception e) {
            return intvalue;
        }
        return intvalue;
    }
    /**
     * 字符串类型转换为int类型
     * 
     * @param value
     * @return
     */
    public static int getIntValue(Object value) {
        int intvalue = 0;
        if (value==null) {
            return intvalue;
        }
        try {
            intvalue = Integer.parseInt(value.toString());
        } catch (Exception e) {
            return intvalue;
        }
        return intvalue;
    }
    public static String getString(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return text.trim();
    }

    /**
     * 去掉多个回车
     * @param str
     * @return
     */
     public static String replaceBlank(String str) {  

        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\n+");
          Matcher m = p.matcher(ToDBC(str));
            dest = m.replaceAll("\n");
        }
        return dest;
        }
     
     /**
 	 * 半角转换为全角
 	 * @param input
 	 * @return
 	 */
 	public static String ToDBC(String input) {
 		char[] c = input.toCharArray();
 		for (int i = 0; i < c.length; i++) {
 			if (c[i] == 12288) {
 				c[i] = (char) 32;
 				continue;
 			}
 			if (c[i] > 65280 && c[i] < 65375)
 				c[i] = (char) (c[i] - 65248);
 		}
 		return new String(c);
 	}
 	
     
     public static int countBlank(String s){
    	if(s == null) return 0;
		char c = '\n';
		int num = 0;
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (c == chars[i]) {
				num++;
			}
		}
		return num;
     }

}
