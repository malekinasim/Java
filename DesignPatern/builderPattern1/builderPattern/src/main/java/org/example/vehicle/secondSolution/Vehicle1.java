package org.example.vehicle.secondSolution;

import org.example.vehicle.firstSolution.Vehicle;

public class Vehicle1<T extends Vehicle1.Builder<T>>{
    private final String fuelType;
    private final float milePerSecond;
    private final String material;
    private final String colour;
    public Vehicle1(T builder) {
        this.fuelType = builder.fuelType;
        this.milePerSecond = builder.milePerSecond;
        this.material = builder.material;
        this.colour = builder.colour;
    }
    
    public static class Builder<T extends Vehicle1.Builder<T> > {
        protected  final String fuelType;
        protected  float milePerSecond;
        protected  String material;
        protected  String colour;

        public Builder(String fuelType) {
            this.fuelType = fuelType;
        }
        T self() {
            return (T) this;
        }

        public T milePerSecond(float milePerSecond) {
            this.milePerSecond = milePerSecond;
            return  self();
        }

        public T material(String material) {
            this.material = material;
            return self();
        }

        public T colour(String colour) {
            this.colour = colour;
            return self();
        }


        public Vehicle1 build(){
            return new Vehicle1(this);

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
