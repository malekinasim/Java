package org.example.CombiningBuilderByFlyweight;

public class Button {
    public enum ButtonType {APPROVE, REJECT, ACCEPT, ACKNOWLEDGE, DEFFER, OK}

    private final String text;         // Extrinsic state (varies)
    private final Runnable action;
    private final ButtonStyle buttonStyle;
    private final ButtonType type;

    private Button(ButtonBuilder buttonBuilder) {
        this.text = buttonBuilder.text;
        this.action = buttonBuilder.action;
        this.buttonStyle = buttonBuilder.buttonStyle;
        this.type = buttonBuilder.type;
    }

    public void click() {
        if (action != null) action.run();
        System.out.println("Button clicked: " + text);
    }

    public void render() {
        System.out.println("Rendering Button: [" + text + "] with " +
                buttonStyle.toString());
    }

    public static class ButtonBuilder {
        private String text;
        private final ButtonType type;
        private Runnable action;
        private ButtonStyle buttonStyle;

        public ButtonBuilder(String text, ButtonType type) {
            if (text == null || type == null) {
                throw new IllegalArgumentException("Text and type must not be null.");
            }

            this.text = text;
            this.type = type;
        }

        public ButtonBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public ButtonBuilder setAction(Runnable action) {
            this.action = action;
            return this;
        }

        public ButtonBuilder setButtonStyle(ButtonStyle buttonStyle) {
            this.buttonStyle = buttonStyle;
            return this;
        }

        public Button build() {
            return new Button(this);
        }
    }

    public String getText() {
        return text;
    }

    public Runnable getAction() {
        return action;
    }

    public ButtonStyle getButtonStyle() {
        return buttonStyle;
    }
}
