
/* HW2. Fruits and hash tables
 * This file contains 7 classes:
 * 		- Row represents a row of fruits,
 * 		- CountConfigurationsNaive counts stable configurations naively,
 * 		- Quadruple manipulates quadruplets,
 * 		- HashTable builds a hash table,
 * 		- CountConfigurationsHashTable counts stable configurations using our hash table,
 * 		- Triple manipulates triplets,
 * 		- CountConfigurationsHashMap counts stable configurations using the HashMap of java.
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

class Row { // represent a row of fruits
	private final int[] fruits;

	// empty row constructor
	Row() {
		this.fruits = new int[0];
	}

	// constructor from the field fruits
	Row(int[] fruits) {
		this.fruits = fruits;
	}

	// equals method to compare the row to an object o
	@Override
	public boolean equals(Object o) {
		// we start by transforming the object o into an object of the class Row
		// here we suppose that o will always be of the class Row
		Row that = (Row) o;
		// we check if the two rows have the same length
		if (this.fruits.length != that.fruits.length)
			return false;
		// we check if the i-th fruits of the two rows coincide
		for (int i = 0; i < this.fruits.length; ++i) {
			if (this.fruits[i] != that.fruits[i])
				return false;
		}
		// we have the equality of the two rows
		return true;
	}

	// hash code of the row
	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = 0; i < fruits.length; ++i) {
			hash = 2 * hash + fruits[i];
		}
		return hash;
	}

	// string representing the row
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < fruits.length; ++i)
			s.append(fruits[i]);
		return s.toString();
	}

	// Question 1

	// returns a new row by adding fruit to the end of the row
	Row extendedWith(int fruit) {
		int newFruits[] = new int[fruits.length + 1];
		System.arraycopy(fruits, 0, newFruits, 0, fruits.length);
		newFruits[fruits.length] = fruit;
		return new Row(newFruits);
	}

	// return the list of all stable rows of width width
	static LinkedList<Row> allStableRows(int width) {
		LinkedList<Row> rows = new LinkedList<Row>();
		rows.add(new Row());
		for (int i = 0; i < width; i++) {
			int n = rows.size();
			for (int j = 0; j < n; j++) {
				Row head = rows.removeFirst();
				if (i < 2) {
					rows.add(head.extendedWith(0));
					rows.add(head.extendedWith(1));
				} else if (head.fruits[i - 1] != head.fruits[i - 2]) {
					rows.add(head.extendedWith(0));
					rows.add(head.extendedWith(1));
				} else if (head.fruits[i - 1] == 0) {
					rows.add(head.extendedWith(1));
				} else if (head.fruits[i - 1] == 1) {
					rows.add(head.extendedWith(0));
				}
			}
		}
		return rows;
	}

	// check if the row can be stacked with rows r1 and r2
	// without having three fruits of the same type adjacent
	boolean areStackable(Row r1, Row r2) {
		if (fruits.length != r1.fruits.length || fruits.length != r2.fruits.length)
			return false;
		for (int i = 0; i < fruits.length; i++) {
			if (fruits[i] == r1.fruits[i] && fruits[i] == r2.fruits[i])
				return false;
		}
		return true;
	}
}

// Naive counting
class CountConfigurationsNaive { // counting of stable configurations

	// Question 2

	// returning the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		if (height <= 1)
			return 0;
		if (height == 2)
			return 1;

		long result = 0;
		for (Row r3 : rows) {
			if (r3.areStackable(r1, r2)) {
				result += count(r2, r3, rows, height - 1);
			}
		}
		return result;
	}

	// returning the number of grids with n lines and n columns
	static long count(int n) {
		if (n == 0)
			return 1;
		if (n == 1)
			return 2;
		LinkedList<Row> rows = Row.allStableRows(n);
		long result = 0;
		for (Row r1 : rows) {
			for (Row r2 : rows) {
				result += count(r1, r2, rows, n);
			}
		}
		return result;
	}
}

// Construction and use of a hash table

class Quadruple { // quadruplet (r1, r2, height, result)
	Row r1;
	Row r2;
	int height;
	long result;

	Quadruple(Row r1, Row r2, int height, long result) {
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
		this.result = result;
	}
}

class HashTable { // hash table
	final static int M = 50000;
	Vector<LinkedList<Quadruple>> buckets;

	// Question 3.1

	// constructor
	HashTable() {
		buckets = new Vector<LinkedList<Quadruple>>(M);
		for (int i = 0; i < M; i++) {
			buckets.add(new LinkedList<Quadruple>());
		}
	}

	// Question 3.2

	// return the hash code of the triplet (r1, r2, height)
	static int hashCode(Row r1, Row r2, int height) {
		final int prime = 31;
		int result = 1;
		result = prime * result + r1.hashCode();
		result = prime * result + r2.hashCode();
		result = prime * result + height;
		return result;
	}

	// return the bucket of the triplet (r1, r2, height)
	int bucket(Row r1, Row r2, int height) {
		return (HashTable.hashCode(r1, r2, height) & 0x7fffffff) % M;
	}

	// Question 3.3

	// add the quadruplet (r1, r2, height, result) in the bucket indicated by the
	// method bucket
	void add(Row r1, Row r2, int height, long result) {
		int key = this.bucket(r1, r2, height);
		Quadruple q = new Quadruple(r1, r2, height, result);
		buckets.get(key).add(q);
	}

	// Question 3.4

	// search in the table an entry for the triplet (r1, r2, height)
	Long find(Row r1, Row r2, int height) {
		int key = this.bucket(r1, r2, height);
		LinkedList<Quadruple> list = buckets.get(key);
		for (Quadruple q : list) {
			if (q.r1.equals(r1) && q.r2.equals(r2) && q.height == height) {
				return Long.valueOf(q.result);
			}
		}
		return null;
	}

}

class CountConfigurationsHashTable { // counting of stable configurations using our hash table
	static HashTable memo = new HashTable();

	// Question 4

	// return the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	// using our hash table
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		if (height <= 1)
			return 0;
		if (height == 2)
			return 1;

		Long result = memo.find(r1, r2, height);
		if (result != null) {
			return result;
		}

		result = Long.valueOf(0);
		for (Row r3 : rows) {
			if (r3.areStackable(r1, r2)) {
				result += count(r2, r3, rows, height - 1);
			}
		}
		memo.add(r1, r2, height, result);
		return result;
	}

	// return the number of grids with n lines and n columns
	static long count(int n) {
		if (n == 0)
			return 1;
		if (n == 1)
			return 2;
		LinkedList<Row> rows = Row.allStableRows(n);
		long result = 0;
		for (Row r1 : rows) {
			for (Row r2 : rows) {
				result += count(r1, r2, rows, n);
			}
		}
		return result;
	}
}

// Use of HashMap

class Triple { // triplet (r1, r2, height)
	// to be completed
	Row r1;
	Row r2;
	int height;

	Triple(Row r1, Row r2, int height) {
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
	}

	@Override
	public boolean equals(Object o) {
		Triple that = (Triple) o;
		return this.r1.equals(that.r1) && this.r2.equals(that.r2) && this.height == that.height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + r1.hashCode();
		result = prime * result + r2.hashCode();
		result = prime * result + height;
		return result;
	}
}

class CountConfigurationsHashMap { // counting of stable configurations using the HashMap of java
	static HashMap<Triple, Long> memo = new HashMap<Triple, Long>();

	// Question 5

	// returning the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	// using the HashMap of java
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		if (height <= 1)
			return 0;
		if (height == 2)
			return 1;

		Triple t = new Triple(r1, r2, height);
		Long result = memo.get(t);
		if (result != null) {
			return result;
		}

		result = Long.valueOf(0);
		for (Row r3 : rows) {
			if (r3.areStackable(r1, r2)) {
				result += count(r2, r3, rows, height - 1);
			}
		}
		memo.put(t, result);
		return result;
	}

	// return the number of grids with n lines and n columns
	static long count(int n) {
		if (n == 0)
			return 1;
		if (n == 1)
			return 2;
		LinkedList<Row> rows = Row.allStableRows(n);
		long result = 0;
		for (Row r1 : rows) {
			for (Row r2 : rows) {
				result += count(r1, r2, rows, n);
			}
		}
		return result;
	}
}
