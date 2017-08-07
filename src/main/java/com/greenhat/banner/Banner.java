package com.greenhat.banner;

import com.greenhat.ConfigNames;

/**
 * Created by jiacheng on 2017/7/24.
 */
public final class Banner {
    private static String[] banner3D ={
            "                                     __                __      " ,
            "                                    /\\ \\              /\\ \\__   " ,
            "   __   _ __    __     __    ___    \\ \\ \\___      __  \\ \\ ,_\\  " ,
            " /'_ `\\/\\`'__\\/'__`\\ /'__`\\/' _ `\\   \\ \\  _ `\\  /'__`\\ \\ \\ \\/  " ,
            "/\\ \\L\\ \\ \\ \\//\\  __//\\  __//\\ \\/\\ \\   \\ \\ \\ \\ \\/\\ \\L\\.\\_\\ \\ \\_ " ,
            "\\ \\____ \\ \\_\\\\ \\____\\ \\____\\ \\_\\ \\_\\   \\ \\_\\ \\_\\ \\__/.\\_\\\\ \\__\\" ,
            " \\/___L\\ \\/_/ \\/____/\\/____/\\/_/\\/_/    \\/_/\\/_/\\/__/\\/_/ \\/__/" ,
            "   /\\____/",
            "   \\_/__/       "
    };
    public static void startBanner(){
        StringBuilder builder = new StringBuilder();
        for (String text:banner3D){
            builder.append("\r\n\t\t");
            builder.append(text);
        }
        builder.append("==================:: v").append(ConfigNames.VERSION).append(" ::==================");
        builder.append("\r\n\t");
        System.out.println(builder.toString());
    }

}
