package org.example.CombiningBuilderByFlyweight;

import java.util.Map;
import java.util.WeakHashMap;

public class ButtonStyleFactory {
    // Use WeakHashMap to allow garbage collection of unused styles
    private final static Map<String, ButtonStyle> buttonStyles=new WeakHashMap<>();
    public static ButtonStyle getStyle(String backgroundColor, String textColor, int borderRadius) {

        String key = String.format("%s-%s-%d", backgroundColor, textColor, borderRadius);
       return  buttonStyles.computeIfAbsent(key,k->
            (new ButtonStyle.ButtonStyleBuilder())
                    .setTextColor(textColor)
                   .setBackgroundColor(backgroundColor)
                   .setBorderRadius(borderRadius).build());
    }
}
