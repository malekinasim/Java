package org.example.vehicle.secondSolution;



public class ElectricalCar<T extends ElectricalCar.Builder<T>> extends Car1<T>{
    private final String batteryType;

    public ElectricalCar(T builder) {
        super(builder);
        this.batteryType = builder.batteryType;
    }

    public static class Builder<T extends ElectricalCar.Builder<T>> extends Car1.Builder<T> {
        protected   String batteryType;
        public Builder(String fuelType) {
            super(fuelType);
        }

        public T batteryType(String batteryType) {
            this.batteryType = batteryType;
            return (T) this;
        }
        @Override
        public ElectricalCar build() {
            return new ElectricalCar(this);
        }

    }

    public String getBatteryType() {
        return batteryType;
    }
}
