package info.btsland.app.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/11/6.
 */

public class ArrayUtils {


    public static List<String> remove(String[] array,String str){
        List<String> newStr=new ArrayList<>();
        for(int i=0;i<array.length;i++){
            if(array[i].equals(str)){

            }else {
                newStr.add(array[i]);
            }
        }
        return newStr;
    }

}
