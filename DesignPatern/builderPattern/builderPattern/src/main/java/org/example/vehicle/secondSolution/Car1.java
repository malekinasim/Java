package org.example.vehicle.secondSolution;



public class Car1<T extends Car1.Builder<T>> extends Vehicle1<T>{

private final String make;
private final String model;
public Car1(T builder) {
    super(builder);
    this.make = builder.make;
    this.model = builder.model;

}
    public static class Builder<T extends Car1.Builder<T>> extends Vehicle1.Builder<T> {
        protected String make;
        protected String model;

        public Builder(String fuelType) {
            super(fuelType);
        }

        public T make(String make) {
            this.make = make;
            return (T) this;
        }

        public T model(String model) {
            this.model = model;
            return (T) this;
        }

        @Override
        public Car1 build() {
            return new Car1(this);
        }
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }
}
