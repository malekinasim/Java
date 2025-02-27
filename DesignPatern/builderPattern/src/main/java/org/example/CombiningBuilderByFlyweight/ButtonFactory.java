package org.example.CombiningBuilderByFlyweight;

import java.util.Map;
import java.util.WeakHashMap;

public class ButtonFactory {
    private static final Map<String, Button> buttons = new WeakHashMap<>();

    public static Button getButton(Button.ButtonType buttonType, String text,
                                   Runnable action, ButtonStyle buttonStyle) {
        if (buttonType == null || text == null || buttonStyle == null) {
            throw new IllegalArgumentException("ButtonType, text, and ButtonStyle cannot be null.");
        }
        String k = String.format("%s-%s-%s", buttonType.name() ,text , buttonStyle.hashCode());
        return buttons.computeIfAbsent(k, (key) ->
                (new Button.ButtonBuilder(text, buttonType))
                        .setAction(action)
                        .setButtonStyle(buttonStyle).build()
        );
    }

    public static Button getButton(Button.ButtonType buttonType, String text,
                                   Runnable action, String backgroundColor, String textColor, int borderRadius) {
        if (buttonType == null || text == null ) {
            throw new IllegalArgumentException("ButtonType and text cannot be null.");
        }

        ButtonStyle buttonStyle= ButtonStyleFactory.getStyle(backgroundColor, textColor, borderRadius);
        String k = String.format("%s-%s-%s", buttonType.name() ,text , buttonStyle.hashCode());
       return   getButton(buttonType, text, action, buttonStyle);
    }
}
