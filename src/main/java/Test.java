import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/*
lambda
 */
public class Test {
    public static void main(String[] args) throws IOException {
        MyInterface i1 = (r) -> {};
        System.out.println(i1.getClass().getInterfaces()[0]); //

        MyInterface2 i2 = () -> {};
        System.out.println(i2.getClass().getInterfaces().length); //1

        List<String> list = Arrays.asList("hell", "wol", "eee");
        list.forEach((t) -> {
            System.out.println(t);
        });
//        System.out.println(list);
    }
}

@FunctionalInterface
interface MyInterface {
    void myMethod(int a);
}

@FunctionalInterface
interface MyInterface2 {
    void myMethod();
}