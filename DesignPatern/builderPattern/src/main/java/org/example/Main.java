package org.example;

import org.example.CombiningBuilderByFlyweight.Button;
import org.example.CombiningBuilderByFlyweight.ButtonFactory;
import org.example.CombiningBuilderByFlyweight.ButtonStyle;
import org.example.CombiningBuilderByFlyweight.ButtonStyleFactory;
import org.example.combiningBuilderbySingelton.Configuration;
import org.example.pizza.Pizza;
import org.example.shapes.Point;
import org.example.shapes.Rectangle;
import org.example.shapes.Square;
import org.example.vehicle.firstSolution.Car;
import org.example.vehicle.secondSolution.Car1;
import org.example.vehicle.secondSolution.ElectricalCar;
import org.example.vehicle.thirdsolution.Car3;

import java.util.EnumSet;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Pizza pizza = (new Pizza.PizzaBuilder().addTopping(
                EnumSet.of(Pizza.Topping.CHICKEN, Pizza.Topping.ONION, Pizza.Topping.HAM,
                        Pizza.Topping.PEPPER)

        ).Build());
        System.out.println(pizza);

//        Car car = (new Car.CarBuilder("GAS")).make("ford").model("x")
//                .colour("red").material("GOLD").build();
//
//        Car car1= (new Car.CarBuilder("Petrol")).colour("red").make("Ford")
//                .model("F")
//                .build();

//        Car car2 = (new Car.CarBuilder("Petrol")).make("Ford")
//                .colour("red")
//                .model("F")
//                .build();


        Car1.Builder<?> carBuilder = new Car1.Builder<>("Petro");
        Car1 car = carBuilder.colour("red")
                .make("Ford")
                .model("F")
                .build();

        ElectricalCar.Builder<?> ElectricCarBuilder = new ElectricalCar.Builder<>("Electric");
        ElectricalCar eCar = ElectricCarBuilder.make("Mercedes")
                .colour("White")
                .model("G")
                .batteryType("Lithium")
                .build();


        Car3 car3 = Car3.builder()
                .seatingCapacity(3)
                .make("Ford")
                .model("F")
                .build();

        Rectangle rectangle = Rectangle.getBuilder().setLength(10)
                .setWidth(20)
                .setColor("red")
                .setMiddlePoint(new Point(5, 5))
                .build();
        rectangle.draw();


        Square square = Square.getBuilder()
                .setWidth(10)
                .setColor("red")
                .setMiddlePoint(new Point(2, 2))
                .build();
        square.draw();

        // Building the configuration
        Configuration.ConfigurationBuilder builder = new Configuration.ConfigurationBuilder();
        builder.setDbUrl("jdbc:mysql://localhost:3306")
                .setDbUser("user")
                .setDbPassword("password")
                .setDbName("mydb")
                .setDbSchema("public")
                .setTableName("users")
                .setTableSchema("schema")
                .setMaxConnections(10);

// Initialize the singleton instance
        Configuration.build(builder);

// Retrieve the configuration instance
        Configuration config = Configuration.getInstance();
        try {
            Configuration.build(builder);
        } catch (Exception e) {
            e.printStackTrace();
        }

// Access the configuration values
        System.out.println(config.getDbUrl()); // jdbc:mysql://localhost:3306
        System.out.println(config.getDbUser()); // user
        ButtonStyle b = ButtonStyleFactory.getStyle("red", "click me", 5);
        System.out.println(b);

        ButtonStyle b1 = ButtonStyleFactory.getStyle("red", "click me", 5);
        System.out.println(b1);
        System.out.println(b1.hashCode()+" : "+b.hashCode());
        Button button1= ButtonFactory.getButton(Button.ButtonType.OK,"OK",null,b1);
        Button button2= ButtonFactory.getButton(Button.ButtonType.OK,"OK",null,b1);
        Button button3= ButtonFactory.getButton(Button.ButtonType.ACCEPT,"ACCEPT",null,b1);
        System.out.println(button1.hashCode()+" : "+button2.hashCode()+" : "+button3.hashCode());

    }
}





