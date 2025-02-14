public class PersonStaticFactoryMethods  extends Person{
    //you need to create all super factory class because subclass can't call
    public PersonStaticFactoryMethods(String name) {
        super(name);
    }

    public PersonStaticFactoryMethods(String name, String family) {
        super(name, family);
    }

    public PersonStaticFactoryMethods(String name, String family, int age) {
        super(name, family, age);
    }

    //method factory creator by name
    public static PersonStaticFactoryMethods createPersonByName(String name){
        return new PersonStaticFactoryMethods(name);
    }
    //method factory method for create by fullName
    public static PersonStaticFactoryMethods createPersonByFullName(String name, String family) {
        return new PersonStaticFactoryMethods(name, family);
    }
    //method factory method for create by Adult
    public static PersonStaticFactoryMethods createAdult(String name, String family) {
        return new PersonStaticFactoryMethods(name, family,30);
    }

    //method factory method for create by young
    public static PersonStaticFactoryMethods createYoung(String name, String family) {
        return new PersonStaticFactoryMethods(name, family,20);
    }


}
