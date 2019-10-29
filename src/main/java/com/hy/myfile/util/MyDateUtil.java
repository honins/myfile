package com.hy.myfile.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hy on 2019/10/16 13:22
 */
public class MyDateUtil {

    public static String getDatePath(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(new Date());
    }

}
