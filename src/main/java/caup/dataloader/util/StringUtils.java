package caup.dataloader.util;

import java.util.regex.Pattern;

/**
 * Created by Richard on 2015/03/26 .
 *
 */
public  class StringUtils {

    public static  final String pattern = "&|[\uFE30-\uFFA0]|‘’|“”";

    /**
      *
     * Used to format the strings in the excel
     * We exclude the chinese symbols and characters to make sure the data can be formatted correctly
     */
    public  static String replaceSpecialtyStr(String str,String pattern,String replace){
        if(isBlankOrNull(pattern))
            pattern="\\s*|\t|\r|\n";//去除字符串中空格、换行、制表
        if(isBlankOrNull(replace))
            replace="";
        return Pattern.compile(pattern).matcher(str).replaceAll(replace);

    }

    private  static boolean isBlankOrNull(String str){
        if(null==str)return true;
        //return str.length()==0?true:false;
        return str.length()==0;
    }
    /**清除数字和空格*/
    public   String cleanBlankOrDigit(String str){
        if(isBlankOrNull(str))return "null";
        return Pattern.compile("\\d|\\s").matcher(str).replaceAll("");
    }

    public static String yearbookIndexPreprocess(String index){
        return (index.indexOf("_") >= 0)? index.substring(index.indexOf("_") + 1, index.length()): index;
    }
}
