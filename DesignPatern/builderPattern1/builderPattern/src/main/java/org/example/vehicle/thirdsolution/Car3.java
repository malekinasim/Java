package org.example.vehicle.thirdsolution;


public class Car3 extends Vehicle3 {
    private final int seatingCapacity;

    protected Car3(Car3Builder<?, ?> builder) {
        super(builder);
        this.seatingCapacity = builder.seatingCapacity;
    }

    public static Car3Builder<?, ?> builder() {
        return new Car3BuilderImpl();
    }

    public static abstract class Car3Builder<C extends Car3, B extends Car3Builder<C, B>>
            extends VehicleBuilder3<C, B> {
        private int seatingCapacity;

        public B seatingCapacity(int seatingCapacity) {
            this.seatingCapacity = seatingCapacity;
            return self();
        }
    }

    private static class Car3BuilderImpl extends Car3Builder<Car3, Car3BuilderImpl> {
        @Override
        protected Car3BuilderImpl self() {
            return this;
        }

        @Override
        public Car3 build() {
            return new Car3(this);
        }
    }
}

