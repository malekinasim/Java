package singelton;

public class Person {
    private String name;
    private String family;
    private int age;


    public Person(String name) {
        this(name,null,0);
    }
    public Person(String name, String family) {
       this(name,family,0);
    }
    //method factory method for create by young
    public static PersonStaticFactoryMethods createYoung(String name, String family) {
        return new PersonStaticFactoryMethods(name, family,20);
    }
    public Person(String name, String family, int age) {
        this.name = name;
        this.family = family;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
