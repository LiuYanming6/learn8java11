package optional;

import org.junit.Test;

import java.util.Optional;

/**
 * @Author: 刘艳明
 * @Date: 19-5-17 上午8:24
 *
 * jdk8 引进的 Optional
 */
public class TestOp {

    //创建一个Optional实例
    // 之前发生空指针后,定位困难,需要debug追踪,使用了Optional.of,能够马上定位到
    @Test
    public void test1() {
//        Optional<String> op = Optional.of(new String());
        Optional<String> op = Optional.of(null);
        String a = op.get();
        System.out.println(a);
    }

    /*
    创建一个空的Optional实例
     */
    @Test
    public void test2() {
        Optional<Object> op = Optional.empty();
        System.out.println(op.get());
    }

    /*
    创建一个Optional实例, 如果o为null则创建空实例
     */
    @Test
    public void test3() {
//        Object o = new Object();
        Object o = null;
        Optional<Object> op = Optional.ofNullable(o);

        //1
//        System.out.println(op.get());//No value present而不是空指针异常

        //2
//        if(op.isPresent()){
//            System.out.println(op.get());
//        }

        //3
//        o = op.orElse(new Object());//如果空则创建
//        System.out.println(o);

        o = op.orElseGet(() -> new Object());
        System.out.println(o);
    }

    /*
    map(Function f)如果有值对其处理,并返回处理后的Optional,否则返回Optional.empty()
     */
    @Test
    public void test4(){
        Optional<Object> op = Optional.ofNullable(new Object());
//        Optional<String> str = op.map(e -> e.toString());
//        System.out.println(str.get());

        //进一步避免空指针异常
        Optional<String> str2 = op.flatMap(e -> Optional.of(e.toString()));
        System.out.println(str2.get());
    }


    /*
    获取一个nan心中女神的名字
     */
    @Test
    public void test5(){
//        Man man = new Man();
//        Godness n = man.getGodness();
//        System.out.println(n.getName());

        Godness g = new Godness();
        g.setName("M");

        NewMan man = new NewMan();
//        man.setGodness(Optional.ofNullable(g));
        Optional<Godness> n = man.getGodness();
        System.out.println(n.orElseGet(() -> {
            Godness godness = new Godness();
            godness.setName("S");
            return godness;
        }));
    }
}
