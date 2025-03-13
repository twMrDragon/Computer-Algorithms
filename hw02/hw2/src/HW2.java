 
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
		throw new Error("method extendedWith(int fruit) to be completed (Question 1)");
	}

	// return the list of all stable rows of width width
	static LinkedList<Row> allStableRows(int width) {
		throw new Error("method allStableRows(int width) to be completed (Question 1)");
	}


	// check if the row can be stacked with rows r1 and r2
	// without having three fruits of the same type adjacent
	boolean areStackable(Row r1, Row r2) {
		throw new Error("method areStackable(Row r1, Row r2) to be completed (Question 1)");
	}
}

// Naive counting
class CountConfigurationsNaive {  // counting of stable configurations

	// Question 2

	// returning the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		throw new Error(
				"method count(Row r1, Row r2, LinkedList<Row> rows, int height) of the class CountConfigurationsHashNaive to be completed (Question 2)");

	}

	// returning the number of grids with n lines and n columns
	static long count(int n) {
		throw new Error("method count(int n) of the class CountConfigurationsHashNaive to be completed (Question 2)");
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
		throw new Error("Constructor HashTable() to be completed (Question 3.1)");
	}

	// Question 3.2

	// return the hash code of the triplet (r1, r2, height)
	static int hashCode(Row r1, Row r2, int height) {
		throw new Error("method hashCode(Row r1, Row r2, int height) to be completed (Question 3.2)");
	}

	// return the bucket of the triplet (r1, r2, height)
	int bucket(Row r1, Row r2, int height) {
		throw new Error("method bucket(Row r1, Row r2, int height) to be completed (Question 3.2)");
	}

	// Question 3.3

	// add the quadruplet (r1, r2, height, result) in the bucket indicated by the
	// method bucket
	void add(Row r1, Row r2, int height, long result) {
		throw new Error("method add(Row r1, Row r2, int height, long result) to be completed (Question 3.3)");
	}

	// Question 3.4

	// search in the table an entry for the triplet (r1, r2, height)
	Long find(Row r1, Row r2, int height) {
		throw new Error("method Quadruple find(Row r1, Row r2, int height) to be completed (Question 3.4)");
	}

}

class CountConfigurationsHashTable { // counting of stable configurations using our hash table
	static HashTable memo = new HashTable();

	// Question 4

	// return the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	// using our hash table
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		throw new Error(
				"method count(Row r1, Row r2, LinkedList<Row> rows, int height) of the class CountConfigurationsHashTable to be completed (Question 4)");
	}

	// return the number of grids with n lines and n columns
	static long count(int n) {
		throw new Error("method count(int n) of the class CountConfigurationsHashTable to be completed (Question 4)");
	}
}

//Use of HashMap

class Triple { // triplet (r1, r2, height)
	// to be completed
}

class CountConfigurationsHashMap { // counting of stable configurations using the HashMap of java
	static HashMap<Triple, Long> memo = new HashMap<Triple, Long>();

	// Question 5

	// returning the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	// using the HashMap of java
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		throw new Error(
				"method count(Row r1, Row r2, LinkedList<Row> rows, int height) of the class CountConfigurationsHashMap to be completed (Question 5)");
	}

	// return the number of grids with n lines and n columns
	static long count(int n) {
		throw new Error("method count(int n) of the class CountConfigurationsHashMap to be completed (Question 5)");
	}
}
