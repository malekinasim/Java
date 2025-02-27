package org.example.vehicle.firstSolution;

public class Vehicle {
    private final String fuelType;
    private final float milePerSecond;
    private final String material;
    private final String colour;
    public Vehicle(VehicleBuilder vehicleBuilder) {
        this.fuelType = vehicleBuilder.fuelType;
        this.milePerSecond = vehicleBuilder.milePerSecond;
        this.material = vehicleBuilder.material;
        this.colour = vehicleBuilder.colour;
    }
    public static class VehicleBuilder{
        private   final String fuelType;
        private  float milePerSecond;
        private  String material;
        private  String colour;

        public VehicleBuilder(String fuelType) {
            this.fuelType = fuelType;
        }

        public VehicleBuilder milePerSecond(float milePerSecond) {
            this.milePerSecond = milePerSecond;
            return this;
        }

        public VehicleBuilder material(String material) {
            this.material = material;
            return this;
        }

        public VehicleBuilder colour(String colour) {
            this.colour = colour;
            return this;
        }


        public Vehicle build(){
         return new Vehicle(this);

        }



    }

    public String getFuelType() {
        return fuelType;
    }

    public float getMilePerSecond() {
        return milePerSecond;
    }

    public String getMaterial() {
        return material;
    }

    public String getColour() {
        return colour;
    }
}
