package org.example.shapes;

import java.util.List;

public class Polygon extends Shape {
    private final List<Line> sideLines;


    public Polygon(PolygonBuilder<?, ?> polygonBuilder) {
        super(polygonBuilder);
        this.sideLines = polygonBuilder.sideLines;

    }

    @Override
    public void draw() {
     System.out.println("Drawing Polygon");
    }

    public static PolygonBuilder<?,?> getBuilder() {
      return new PolygonBuilderImpl();
    }

    public abstract static class PolygonBuilder<C extends Polygon, T extends PolygonBuilder<C, T>> extends ShapeBuilder<C, T> {
        protected List<Line> sideLines;

        public T setSideSize(List<Line> sideLines) {
            this.sideLines = sideLines;
            return self();
        }
    }

    private static class PolygonBuilderImpl extends PolygonBuilder<Polygon, PolygonBuilderImpl> {

        @Override
        protected PolygonBuilderImpl self() {
            return this;
        }

        @Override
        public Polygon build() {
            return new Polygon(this);
        }
    }



  public List<Line> getSideSize() {
    return sideLines;
  }
}
