package com.supertreasure.util;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 * 
 * @author LWH
 *
 */
public class ValidationUtil {
	/**
	 * 文字内容是否超出长度
	 * (比较的是字符)
	 * @param str
	 * @param leng
	 * @return
	 */
	public static boolean isLengOut(String str, int leng) {
		return stringIsNull(str) ? false : str.trim().length() > leng;
	}

	/**
	 * 文字内容是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean stringIsNull(String str) {
		return str == null ? true : str.trim().length() <= 0;
	}
	
	 /**  
     * 判断字段是否为正浮点数正则表达式 >=0 符合返回ture 
     * @param str
     * @return boolean 
     */
    public static  boolean isDOUBLE_NEGATIVE(String str) {
        return Regular(str,"^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0$");
    }  
    
    /** 
     * 判断字段是否为正整数正则表达式 >=0 符合返回ture
     * @param str 
     * @return boolean 
     */
    public static  boolean isINTEGER_NEGATIVE(String str) {
        return Regular(str, "^[1-9]\\d*|0$");
    }
    
    /**
	 * 判断字符串是否为合法手机号 11位 13 14 15 18开头
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isMobile(String str) {
		return Regular(str, "^(13|14|15|17|18)\\d{9}$");
	}
    
	/**
	 * 判断日期是否符合指定格式
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static boolean isValidDate(String str, String pattern){
		boolean flag = true;
		SimpleDateFormat  sdf = new SimpleDateFormat( pattern );
		try{
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			sdf.setLenient(false);
			sdf.parse(str);
		}catch( Exception e ){
			flag = false;
		}
		return flag;
	}
    /** 
     * 匹配是否符合正则表达式pattern 匹配返回true 
     * @param str 匹配的字符串 
     * @param pattern 匹配模式 
     * @return boolean 
     */
    private static  boolean Regular(String str,String pattern){
        if(null == str || str.trim().length()<=0)
            return false;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    
    public static void main(String[] args) {
    	System.out.println( isMobile("15111111111") );
	}
}
