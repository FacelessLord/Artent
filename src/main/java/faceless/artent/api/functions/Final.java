/* @author FacelessLord@github.com
 *  Provided by GNU/CC
 *
 */
package faceless.artent.api.functions;

public class Final<T> {
    private T value;
    private boolean defined;

    public boolean isDefined() {
        return defined;
    }

    public void setValue(T value) {
        if (!defined) {
            this.value = value;
            defined = true;
        } else {
            throw new UnsupportedOperationException("Can't redefine final value");
        }
    }

    public T getValue() {
        return value;
    }

    public T getOrDefault(T defaultT) {
        if (defined)
            return value;
        return defaultT;
    }
}
