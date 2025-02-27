package org.example.CombiningBuilderByFlyweight;

public class ButtonStyle {
    private final String backgroundColor;
    private final String textColor;
    private final int borderRadius;

    private ButtonStyle(ButtonStyleBuilder builder) {
        this.backgroundColor = builder.backgroundColor;
        this.textColor = builder.textColor;
        this.borderRadius = builder.borderRadius;
    }

    protected static class ButtonStyleBuilder {
        private String backgroundColor;
        private String textColor;
        private int borderRadius;

        public ButtonStyleBuilder() {

        }

        public ButtonStyleBuilder setTextColor(String textColor) {
            this.textColor = textColor;
            return this;
        }

        public ButtonStyleBuilder setBorderRadius(int borderRadius) {
            this.borderRadius = borderRadius;
            return this;
        }

        public ButtonStyleBuilder setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        protected ButtonStyle build() {
            return new ButtonStyle(this);
        }

    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    @Override
    public String toString() {
        return String.format("TextColor: %s, BackgroundColor: %s, BorderRadius: %d", textColor, backgroundColor, borderRadius);

    }
}