package org.example.shapes;

public class Square extends Shape{
    private final int width;

    public Square(SquareBuilder<?, ?> squareBuilder) {
        super(squareBuilder);
        this.width=squareBuilder.width;

    }
    public static SquareBuilder<?,?> getBuilder() {
        return new SquareBuilderImpl();
    }
    public static abstract class SquareBuilder<C extends Square, T extends ShapeBuilder<C,T>>  extends ShapeBuilder<C,T>{
        protected int width;

        public T setWidth(int width) {
            this.width = width;
            return self();
        }
    }
    private static class SquareBuilderImpl extends SquareBuilder<Square, SquareBuilderImpl> {

        @Override
        protected SquareBuilderImpl self() {
            return this;
        }

        @Override
        public Square build() {
            return new Square(this);
        }
    }
    @Override
    public void draw() {
      System.out.println("drawing Square");
    }

    public int getWidth() {
        return width;
    }
}
