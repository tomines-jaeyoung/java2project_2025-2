package w0910.listtest;
import java.util.List;

public class PrintPerson {
    public void print(List<Person> persons) {

        for (Person person : persons) {
            System.out.println("1. 성명 :" + person.name);
            System.out.println("2. 나이 :" + person.age);
            System.out.println("3. 성별 :" + person.gender);
            System.out.println("++++++++++++++++++++++++++++++++++");

        }
    }
}
