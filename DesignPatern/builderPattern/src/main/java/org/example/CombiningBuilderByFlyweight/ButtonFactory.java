package org.example.CombiningBuilderByFlyweight;

import java.util.Map;
import java.util.WeakHashMap;

public class ButtonFactory {
    private static Map<Button.ButtonType, Button> buttons = new WeakHashMap<>();

    public static Button getButton(Button.ButtonType buttonType, String text,
                                   Runnable action, ButtonStyle buttonStyle) {
        return buttons.computeIfAbsent(buttonType, (key) ->
                (new Button.ButtonBuilder(text, buttonType))
                        .setAction(action)
                        .setButtonStyle(buttonStyle).build()
        );
    }

    public static Button getButton(Button.ButtonType buttonType, String text,
                                   Runnable action, String backgroundColor, String textColor, int borderRadius) {
        return buttons.computeIfAbsent(buttonType, (key) ->
                (new Button.ButtonBuilder(text, buttonType))
                        .setAction(action)
                        .setButtonStyle(
                                ButtonStyleFactory.getStyle(backgroundColor, textColor, borderRadius)
                        ).build()
        );
    }
}
