package org.example.pizza;


import java.util.EnumSet;


public class Pizza {


    public enum Topping{ONION, GARLIC, OLIVE, HAM, MUSHROOM,MEET,CHICKEN,SALAMI, PEPPER, SAUSAGE}
    public enum Main {CHEESE, DOUGH, OIL,SALT,THYME}
    private EnumSet<Main> main=EnumSet.allOf(Main.class);
    private EnumSet<Topping> toppings=EnumSet.noneOf(Topping.class);
    private Pizza(PizzaBuilder builder) {
        this.toppings=builder.toppings;
        this.main=builder.main;
    }
    public static class PizzaBuilder{
        private final EnumSet<Topping> toppings=EnumSet.noneOf(Topping.class);
        private final EnumSet<Main> main=EnumSet.allOf(Main.class);
        public PizzaBuilder() {}
        public PizzaBuilder addTopping(EnumSet<Topping> toppings){
            this.toppings.addAll(toppings);
            return this;
        }
        public  Pizza Build(){
            return new Pizza(this);
        }
    }
    public EnumSet<Main> getMain() {
        return main;
    }

    public EnumSet<Topping> getToppings() {
        return toppings;
    }

    @Override
    public String toString() {
        return toppings.toString();
    }
}


