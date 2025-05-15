/* 
 * HW6. In place merge sort and generics
 * this file contains 5 classes:
 * 	- Singly<E> : generic linked lists,
 * 	- MergeSortString : merge-sort algorithm for (linked) lists of strings,
 * 	- Occurrence : word counting of a text,
 *  - MergeSort : generic merge-sort algorithm (we replace the type «String» by the generic type «E»),
 *  - Median : calculation of the median of a set of numerical values
 */

/* 
 * Remark: only the constructors and methods whose visibility cannot be reduced are declared "public",
 * here toString and compareTo.
 */

// SINGLY 

class Singly<E> {
	E element;
	Singly<E> next;

	// we choose to represent the empty list by null, the two constructors that
	// follow cannot
	// therefore build an empty list.

	// create a list with one element.

	public Singly(E element, Singly<E> next) {
		this.element = element;
		this.next = next;
	}

	// create a list from a non-empty array.

	public Singly(E[] data) {
		assert (data.length > 0) : "\nThe constructor Singly(E[] data) cannot be used with an empty array"
				+ "\nbecause we cannot build a non-empty list without data.";
		this.element = data[0];
		this.next = null;
		Singly<E> cursor = this;
		for (int i = 1; i < data.length; i++) {
			cursor.next = new Singly<E>(data[i], null);
			cursor = cursor.next;
		}
		;
	}

	// physical copy of a list (for testing only)

	static <E> Singly<E> copy(Singly<E> l) {
		if (l == null)
			return null;
		Singly<E> res = new Singly<E>(l.element, l.next);
		Singly<E> cursor = res;
		while (l.next != null) {
			l = l.next;
			cursor.next = new Singly<E>(l.element, l.next);
			cursor = cursor.next;
		}
		return res;
	}

	// test of equality of two lists

	static <E> boolean areEqual(Singly<E> chain1, Singly<E> chain2) {
		while (chain1 != null && chain2 != null) {
			if (!chain1.element.equals(chain2.element))
				return false;
			chain1 = chain1.next;
			chain2 = chain2.next;
		}
		return chain1 == chain2;
	}

	// create a string from a linked list (necessary for display).

	public String toString() {
		Singly<E> cursor = this;
		String answer = "[ ";
		while (cursor != null) {
			answer = answer + (cursor.element).toString() + " ";
			cursor = cursor.next;
		}
		answer = answer + "]";
		return answer;
	}

	// Question 1
	// Length of a list. Iterative implementation to avoid stack overflow.

	static <E> int length(Singly<E> l) {
		int count = 0;
		while (l != null) {
			count++;
			l = l.next;
		}
		return count;
	}

	// Question 1
	// Cutte the second half of the list passed as an argument,
	// the removed part is returned.
	// The split method therefore modifies the list passed as an argument.

	static <E> Singly<E> split(Singly<E> l) {
		if (l == null)
			return null;
		Singly<E> slow = l;
		Singly<E> fast = l;
		Singly<E> prev = null;
		while (fast != null && fast.next != null) {
			prev = slow;
			slow = slow.next;
			fast = fast.next.next;
		}

		if (fast == null) {
			prev.next = null;
			return slow;
		}

		Singly<E> temp = slow.next;
		slow.next = null;
		return temp;
	}
}

/* MERGE_SORT_STRING */

class MergeSortString {

	// Question 2.2
	// Realizes the merge of the two lists passed as arguments, returns the merged
	// list.
	// The two lists passed as arguments are destroyed since the operation
	// is done "in place".

	static Singly<String> merge(Singly<String> l1, Singly<String> l2) {
		if (l1 == null)
			return l2;
		if (l2 == null)
			return l1;

		Singly<String> result;
		Singly<String> last;
		if (l1.element.compareTo(l2.element) < 0) {
			result = l1;
			l1 = l1.next;
		} else {
			result = l2;
			l2 = l2.next;
		}
		last = result;

		while (l1 != null && l2 != null) {
			if (l1.element.compareTo(l2.element) < 0) {
				last.next = l1;
				l1 = l1.next;
			} else {
				last.next = l2;
				l2 = l2.next;
			}
			last = last.next;
		}

		last.next = (l1 == null) ? l2 : l1;

		return result;
	}

	// Question 2.2
	// Sort (recursively) the list passed as an argument by sorting each of its two
	// halves separately before merging the two sorted halves.
	// The list passed as an argument is destroyed during the operation.

	static Singly<String> sort(Singly<String> l) {
		if (l == null || l.next == null)
			return l;

		Singly<String> l1 = l;
		Singly<String> l2 = Singly.split(l1);
		l1 = MergeSortString.sort(l1);
		l2 = MergeSortString.sort(l2);
		return MergeSortString.merge(l1, l2);
	}

}

/* OCCURRENCE */

class Occurrence implements Comparable<Occurrence> {
	String word;
	int count;

	Occurrence(String word, int count) {
		this.word = word;
		this.count = count;
	}

	public String toString() {
		return word;
	}

	// Question 2.3 :
	// Return a list whose each link contains a word present
	// in the list of words passed as an argument, with its multiplicity.
	// The list passed as an argument can be destroyed.

	static Singly<Occurrence> count(Singly<String> l) {
		if (l == null)
			return null;

		l = MergeSortString.sort(l);
		Singly<Occurrence> result = null;
		Occurrence current = new Occurrence(l.element, 0);
		while (l != null) {
			if (current.word.equals(l.element)) {
				current.count++;
			} else {
				result = new Singly<Occurrence>(current, result);
				current = new Occurrence(l.element, 1);
			}
			l = l.next;
		}
		result = new Singly<Occurrence>(current, result);

		return result;
	}

	// Question 3.2
	// Method of comparison necessary for the use of the sorting algorithm

	public int compareTo(Occurrence that) {
		if (this.count != that.count)
			return that.count - this.count;
		return this.word.compareTo(that.word);
	}

	// Question 3.2
	// Identical to the count(Singly<String> l) method except that the returned list
	// is sorted in descending order of multiplicity.

	static Singly<Occurrence> sortedCount(Singly<String> l) {
		return MergeSort.sort(Occurrence.count(l));
	}
}

/* MERGE_SORT */

// Generic version of MergeSortString
// We replace the type "String" with the generic type "E" in the implementation
// of MergeSort

class MergeSort {

	// Question 3.1
	// Identical to merge(Singly<String> l1, Singly<String> l2) with "E" instead of
	// "String"

	static <E extends Comparable<E>> Singly<E> merge(Singly<E> l1, Singly<E> l2) {
		if (l1 == null)
			return l2;
		if (l2 == null)
			return l1;

		Singly<E> result;
		Singly<E> last;
		if (l1.element.compareTo(l2.element) < 0) {
			result = l1;
			l1 = l1.next;
		} else {
			result = l2;
			l2 = l2.next;
		}
		last = result;

		while (l1 != null && l2 != null) {
			if (l1.element.compareTo(l2.element) < 0) {
				last.next = l1;
				l1 = l1.next;
			} else {
				last.next = l2;
				l2 = l2.next;
			}
			last = last.next;
		}

		last.next = (l1 == null) ? l2 : l1;

		return result;
	}

	// Question 3.1
	// Identical to sort(Singly<String> l) with "E" instead of "String"

	static <E extends Comparable<E>> Singly<E> sort(Singly<E> l) {
		if (l == null || l.next == null)
			return l;

		Singly<E> l1 = l;
		Singly<E> l2 = Singly.split(l1);
		l1 = MergeSort.sort(l1);
		l2 = MergeSort.sort(l2);
		return MergeSort.merge(l1, l2);
	}

}

/* MEDIAN */

class Median {

	// Question 3.3
	// Returns a median of the set of numerical values passed as an argument
	// in the form of a linked list.

	static Pair<Double> median(Singly<Double> data) {
		if (data == null)
			return new Pair<>(Double.NaN, Double.NaN);

		data = MergeSort.sort(data);
		int n = Singly.length(data);

		Singly<Double> l1 = data;
		Singly<Double> l2 = Singly.split(l1);

		Singly<Double> p = l1;
		while (p.next != null)
			p = p.next;

		if (n % 2 == 1) {
			return new Pair<Double>(p.element, p.element);
		} else {
			return new Pair<Double>(p.element, l2.element);
		}
	}
}
