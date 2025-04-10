import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class KDTree {
	int depth;
	double[] point;
	KDTree left;
	KDTree right;

	KDTree(double[] point, int depth) {
		this.point = point;
		this.depth = depth;
	}

	boolean compare(double[] a) {
		int ra = depth % a.length;
		int rp = depth % this.point.length;
		return a[ra] >= this.point[rp];
	}

	static KDTree insert(KDTree tree, double[] p) {
		if (tree == null) {
			return new KDTree(p, 0);
		}
		boolean isLeft = !tree.compare(p);
		if (isLeft) {
			if (tree.left == null) {
				tree.left = new KDTree(p, tree.depth + 1);
			} else {
				insert(tree.left, p);
			}
		} else {
			if (tree.right == null) {
				tree.right = new KDTree(p, tree.depth + 1);
			} else {
				insert(tree.right, p);
			}
		}
		return tree;
	}

	static double sqDist(double[] a, double[] b) {
		double res = 0;
		for (int i = 0; i < a.length; i++) {
			double d = a[i] - b[i];
			res += d * d;
		}
		return res;
	}

	static double[] closestNaive(KDTree tree, double[] a, double[] champion) {
		if (tree == null) {
			return champion;
		}

		if (champion == null || sqDist(tree.point, a) < sqDist(champion, a)) {
			champion = tree.point;
		}

		champion = closestNaive(tree.left, a, champion);
		champion = closestNaive(tree.right, a, champion);

		return champion;
	}

	static double[] closestNaive(KDTree tree, double[] a) {
		return closestNaive(tree, a, null);
	}

	static double[] closest(KDTree tree, double[] a, double[] champion) {
		if (tree == null)
			return champion;

		InteractiveClosest.trace(tree.point, champion);

		if (champion == null || sqDist(tree.point, a) < sqDist(champion, a)) {
			champion = tree.point;
		}

		int axis = tree.depth % a.length;

		boolean goLeft = a[axis] < tree.point[axis];
		KDTree first = goLeft ? tree.left : tree.right;
		KDTree second = goLeft ? tree.right : tree.left;

		champion = closest(first, a, champion);

		if (Math.abs(a[axis] - tree.point[axis]) < Math.sqrt(sqDist(champion, a))) {
			champion = closest(second, a, champion);
		}

		return champion;
	}

	static double[] closest(KDTree tree, double[] a) {
		return closest(tree, a, null);
	}

	static double[] farthest(KDTree tree, double[] a, double[] champion) {
		if (tree == null)
			return champion;

		InteractiveClosest.trace(tree.point, champion);

		if (champion == null || sqDist(tree.point, a) > sqDist(champion, a)) {
			champion = tree.point;
		}

		int axis = tree.depth % a.length;

		boolean goLeft = a[axis] < tree.point[axis];
		KDTree first = goLeft ? tree.left : tree.right;
		KDTree second = goLeft ? tree.right : tree.left;

		champion = farthest(first, a, champion);

		if (Math.abs(a[axis] - tree.point[axis]) > Math.sqrt(sqDist(champion, a))) {
			champion = farthest(second, a, champion);
		}

		return champion;
	}

	static double[] farthest(KDTree tree, double[] a) {
		return farthest(tree, a, null);
	}

	static int size(KDTree tree) {
		if (tree == null) {
			return 0;
		}
		return 1 + size(tree.left) + size(tree.right);
	}

	static void sum(KDTree tree, double[] acc) {
		if (tree == null) {
			return;
		}
		for (int i = 0; i < acc.length; i++) {
			acc[i] += tree.point[i];
		}
		sum(tree.left, acc);
		sum(tree.right, acc);
	}

	static double[] average(KDTree tree) {
		if (tree == null) {
			return null;
		}
		double[] acc = new double[tree.point.length];
		sum(tree, acc);
		for (int i = 0; i < acc.length; i++) {
			acc[i] /= size(tree);
		}
		return acc;
	}

	static void traversalTree(KDTree tree, Vector<double[]> points) {
		if (tree == null)
			return;
		points.add(tree.point);
		traversalTree(tree.left, points);
		traversalTree(tree.right, points);
	}

	static Vector<double[]> palette(KDTree tree, int maxpoints) {
		// Farthest Point Sampling
		Vector<double[]> palette = new Vector<>();
		if (tree == null || maxpoints <= 0)
			return palette;

		Vector<double[]> allPoints = new Vector<>();
		traversalTree(tree, allPoints);

		if (allPoints.isEmpty())
			return palette;

		int total = allPoints.size();
		double[] minDists = new double[total];

		Random rand = new Random();
		int firstIndex = rand.nextInt(total);
		double[] first = allPoints.get(firstIndex);
		palette.add(first);

		for (int i = 0; i < total; i++) {
			minDists[i] = sqDist(allPoints.get(i), first);
		}

		while (palette.size() < maxpoints) {
			double maxDist = -1;
			int farthestIndex = -1;

			for (int i = 0; i < total; i++) {
				if (minDists[i] > maxDist) {
					maxDist = minDists[i];
					farthestIndex = i;
				}
			}

			double[] next = allPoints.get(farthestIndex);
			palette.add(next);

			for (int i = 0; i < total; i++) {
				double d = sqDist(allPoints.get(i), next);
				if (d < minDists[i]) {
					minDists[i] = d;
				}
			}
		}

		return palette;
	}

	public String pointToString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (this.point.length > 0)
			sb.append(this.point[0]);
		for (int i = 1; i < this.point.length; i++)
			sb.append("," + this.point[i]);
		sb.append("]");
		return sb.toString();
	}

}
