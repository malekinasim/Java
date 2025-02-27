package org.example.vehicle.firstSolution;



public class Car extends Vehicle {


    private String make;
    private String model;
    public Car(CarBuilder carBuilder) {
        super(carBuilder);
        this.make = carBuilder.make;
        this.model = carBuilder.model;

    }
    public static class CarBuilder extends VehicleBuilder {
        private  String make;
        private  String model;

        public CarBuilder(String fuelType) {
            super(fuelType);
        }

        public CarBuilder make(String make){
            this.make = make;
            return this;
        }
        public CarBuilder model(String model){
            this.model=model;
            return this;
        }

        @Override
        public CarBuilder milePerSecond(float milePerSecond) {
            return (CarBuilder) super.milePerSecond(milePerSecond);
        }

        @Override
        public CarBuilder material(String material) {
            return (CarBuilder) super.material(material);

        }

        @Override
        public CarBuilder colour(String colour) {
          return (CarBuilder) super.colour(colour);
            //return this;
        }
        public Car build() {
            return new Car(this);
        }

    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }
}
