public enum PersonFactory {
    INSTANCE;

    public Person createPerson(String name, int age) {
        return new Person(name, age);
    }
}







