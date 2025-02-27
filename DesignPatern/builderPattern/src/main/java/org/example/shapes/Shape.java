package org.example.shapes;

public abstract class Shape {
    private final Point middlePoint;
    private final String color;

    public Shape(ShapeBuilder<?,?> shapeBuilder) {
        this.middlePoint = shapeBuilder.middlePoint;
         this.color=shapeBuilder.color;
    }

    public static abstract class ShapeBuilder<C extends Shape,T extends ShapeBuilder<C,T>>{
        protected Point middlePoint;
        protected String color;
        public ShapeBuilder(){

        }
        protected abstract T self();
        public abstract C build();

        public T setMiddlePoint(Point middlePoint) {
            this.middlePoint = middlePoint;
            return self();
        }

        public T setColor(String color) {
            this.color = color;
            return self();
        }
    }

    public Point getMiddlePoint() {
        return middlePoint;
    }

    public String getColor() {
        return color;
    }

    public abstract void draw();
}
