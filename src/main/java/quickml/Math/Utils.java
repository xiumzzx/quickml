package quickml.math;

/**
 * Created by alexanderhawk on 10/12/15.
 */
public class Utils {
    public static double sigmoid(double z) {
        return 1 / (1 + Math.exp(-z));
    }

    public static double logBase2WithMaxError(double z, double minZ) {
        double x = Math.max(z, minZ);
        return Math.log(x)/Math.log(2);
    }
}
