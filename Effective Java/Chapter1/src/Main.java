//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Person personByName=new Person("Nasim");
        Person personByFullName=new Person("Nasim","Maleki");

        Person personByNameStaticMethod= PersonStaticFactoryMethods.createYoung("nasim","Maleki");

        Person youngPerson= Person.createYoung("nasim","Maleki");

    }
}