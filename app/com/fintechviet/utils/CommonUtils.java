package com.fintechviet.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by tungn on 9/25/2017.
 */
public class CommonUtils {
    public static String convertListToString(List<String> list) {
        StringBuilder commaSepValueBuilder = new StringBuilder();

        //Looping through the list
        for ( int i = 0; i< list.size(); i++){
            //append the value into the builder
            commaSepValueBuilder.append(list.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if ( i != list.size()-1){
                commaSepValueBuilder.append(", ");
            }
        }
        return commaSepValueBuilder.toString();
    }

    public static String convertLongToString(long value) {
        BigDecimal bd = new BigDecimal(value);
        NumberFormat formatter = NumberFormat.getInstance(new Locale("pt", "BR"));
        return formatter.format(bd.longValue());
    }
}
