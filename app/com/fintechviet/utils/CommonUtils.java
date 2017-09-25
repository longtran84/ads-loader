package com.fintechviet.utils;

import java.util.List;

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
}
