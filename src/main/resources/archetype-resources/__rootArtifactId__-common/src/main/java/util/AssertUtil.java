package ${package}.util;

public class AssertUtil {

    private AssertUtil() {
    }

    public static void isTrue(boolean expression, RuntimeException exception) {
        if (!expression) {
            throw exception;
        }
    }

}
