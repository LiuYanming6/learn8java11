package optional;

/**
 * @Author: 刘艳明
 * @Date: 19-5-17 上午8:57
 */
public class Godness {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //一定有的值不需要Optional
    private String name;

    @Override
    public String toString() {
        return "Godness{" +
                "name='" + name + '\'' +
                '}';
    }
}
