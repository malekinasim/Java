package org.example.vehicle.thirdsolution;

public class Vehicle3 {
    private final String make;
    private final String model;

    protected Vehicle3(VehicleBuilder3<?, ?> builder) {
        this.make = builder.make;
        this.model = builder.model;
    }

    public static VehicleBuilder3<?, ?> builder() {
        return new VehicleBuilder3Impl();
    }

    public static abstract class VehicleBuilder3<C extends Vehicle3, B extends VehicleBuilder3<C, B>> {
        private String make;
        private String model;

        protected abstract B self();

        public B make(String make) {
            this.make = make;
            return self();
        }

        public B model(String model) {
            this.model = model;
            return self();
        }

        public abstract C build();
    }

    private static class VehicleBuilder3Impl extends VehicleBuilder3<Vehicle3, VehicleBuilder3Impl> {
        @Override
        protected VehicleBuilder3Impl self() {
            return this;
        }

        @Override
        public Vehicle3 build() {
            return new Vehicle3(this);
        }
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }
}
