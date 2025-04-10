public class TestSqDist {
    public static void main(String[] args) {

        if (!TestSqDist.class.desiredAssertionStatus()) {
            System.err.println("You must pass the -ea option to the Java Virtual Machine.");
            System.exit(1);
        }

        System.out.println("-- Test of method sqDist ...");

        // Test case 1: 1D
        assert KDTree.sqDist(new double[]{1}, new double[]{4}) == 9.0 :
            "Distance between [1] and [4] should be 9";

        // Test case 2: 2D
        assert KDTree.sqDist(new double[]{-1, 1}, new double[]{1, -1}) == 8.0 :
            "Distance between [-1,1] and [1,-1] should be 8";

        // Test case 3: 3D
        assert KDTree.sqDist(new double[]{0, 0, 0}, new double[]{1, 2, 2}) == 9.0 :
            "Distance between [0,0,0] and [1,2,2] should be 9";

        // Test case 4: Same points
        assert KDTree.sqDist(new double[]{1, 2, 3}, new double[]{1, 2, 3}) == 0.0 :
            "Distance between same points should be 0";

        // Test case 5: High dimension
        double[] a = new double[1000];
        double[] b = new double[1000];
        for (int i = 0; i < 1000; i++) {
            a[i] = 1;
            b[i] = 0;
        }
        assert KDTree.sqDist(a, b) == 1000.0 :
            "Distance between [1,...,1] and [0,...,0] in 1000D should be 1000";

        System.out.println("[OK]");
    }
}
