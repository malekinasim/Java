package org.example.shapes;

public class Rectangle extends Square{
    private int length;
    public Rectangle(SquareBuilder<?, ?> squareBuilder) {
        super(squareBuilder);
    }
    public static RectangleBuilder<?,?> getBuilder() {
        return new RectangleBuilderImpl();
    }

    public static  abstract class RectangleBuilder<C extends Rectangle,T extends RectangleBuilder<C,T>> extends SquareBuilder<C,T>{
        protected int length;

        public T setLength(int length) {
            this.length = length;
            return self();
        }
    }
    private static class RectangleBuilderImpl extends RectangleBuilder<Rectangle,RectangleBuilderImpl>{

        @Override
        protected RectangleBuilderImpl self() {
            return this;
        }

        @Override
        public Rectangle build() {
            return new Rectangle(this);
        }
    }

    public void draw() {
      System.out.println("Drawing a rectangle");
    }

    public int getLength() {
        return length;
    }
}
