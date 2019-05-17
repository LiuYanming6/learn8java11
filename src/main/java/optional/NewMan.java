package optional;

import java.util.Optional;

/**
 * @Author: 刘艳明
 * @Date: 19-5-17 上午9:03
 */
public class NewMan {
    public Optional<Godness> getGodness() {
        return godness;
    }

    public void setGodness(Optional<Godness> godness) {
        this.godness = godness;
    }

    //可能为空的属性用Optional
    private Optional<Godness> godness = Optional.empty();

}
