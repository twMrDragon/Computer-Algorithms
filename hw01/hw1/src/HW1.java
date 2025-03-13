/* HW1. Battle
 * This file contains two classes :
 * 		- Deck represents a pack of cards,
 * 		- Battle represents a battle game.
 */

import java.util.LinkedList;

class Deck { // represents a pack of cards

	LinkedList<Integer> cards;
	// The methods toString, hashCode, equals, and copy are used for
	// display and testing, you should not modify them.

	@Override
	public String toString() {
		return cards.toString();
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		Deck d = (Deck) o;
		return cards.equals(d.cards);
	}

	Deck copy() {
		Deck d = new Deck();
		for (Integer card : this.cards)
			d.cards.addLast(card);
		return d;
	}

	// constructor of an empty deck
	Deck() {
		cards = new LinkedList<Integer>();
	}

	// constructor from field
	Deck(LinkedList<Integer> cards) {
		this.cards = cards;
	}

	// constructor of a complete sorted deck of cards with nbVals values
	Deck(int nbVals) {
		cards = new LinkedList<Integer>();
		for (int j = 1; j <= nbVals; j++)
			for (int i = 0; i < 4; i++)
				cards.add(j);
	}

	// Question 1

	// takes a card from deck d to put it at the end of the current packet
	int pick(Deck d) {
		// throw new Error("Method pick(Deck d) to complete (Question 1)");
		if (!d.cards.isEmpty()) {
			int x = d.cards.removeFirst();
			cards.addLast(x);
			return x;
		} else {
			return -1;
		}
	}

	// takes all the cards from deck d to put them at the end of the current deck
	void pickAll(Deck d) {
		// throw new Error("Method pickAll(Deck d) to complete (Question 1)");
		while (!d.cards.isEmpty()) {
			pick(d);
		}
	}

	// checks if the current packet is valid
	boolean isValid(int nbVals) {
		// throw new Error("Method isValid(int nbVals) to complete (Question 1)");
		int[] numbers = new int[nbVals];
		for (Integer x : cards) {
			if (x < 1 || x > nbVals || numbers[x - 1] > 3)
				return false;
			numbers[x - 1]++;
		}
		return true;
	}

	// Question 2.1

	// chooses a position for the cut
	int cut() {
		int count = 0;
		for (int i = 0; i < this.cards.size(); i++) {
			if (Math.random() < 0.5) {
				count++;
			}
		}
		return count;
	}

	// cuts the current packet in two at the position given by cut()
	Deck split() {
		int c = cut();
		Deck d = new Deck();
		for (int i = 0; i < c; i++) {
			d.cards.add(this.cards.removeFirst());
		}
		return d;
	}

	// Question 2.2

	// mixes the current deck and the deck d
	void riffleWith(Deck d) {
		Deck f = new Deck();
		while (!this.cards.isEmpty() && !d.cards.isEmpty()) {
			int a = d.cards.size();
			int b = this.cards.size();
			double p = 1.0 * a / (a + b);
			if (Math.random() < p) {
				f.pick(d);
			} else {
				f.pick(this);
			}
		}
		if (!this.cards.isEmpty()) {
			f.pickAll(this);
		} else {
			f.pickAll(d);
		}
		this.pickAll(f);
	}

	// Question 2.3

	// shuffles the current deck using the riffle shuffle
	void riffleShuffle(int m) {
		for (int i = 0; i < m; i++) {
			Deck d = this.split();
			this.riffleWith(d);
		}
	}
}

class Battle { // represents a battle game

	Deck player1;
	Deck player2;
	Deck trick;
	boolean turn = true;
	boolean ableToTrun = false;

	// constructor of a battle without cards
	Battle() {
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();
	}

	// constructor from fields
	Battle(Deck player1, Deck player2, Deck trick) {
		this.player1 = player1;
		this.player2 = player2;
		this.trick = trick;
	}

	// copy the battle
	Battle copy() {
		Battle r = new Battle();
		r.player1 = this.player1.copy();
		r.player2 = this.player2.copy();
		r.trick = this.trick.copy();
		r.turn = this.turn;
		r.ableToTrun = this.ableToTrun;
		return r;
	}

	// string representing the battle
	@Override
	public String toString() {
		return "Player 1 : " + player1.toString() + "\n" + "Player 2 : " + player2.toString() + "\nPli "
				+ trick.toString();
	}

	// Question 3.1

	// constructor of a battle with a deck of cards of nbVals values
	Battle(int nbVals) {
		Deck d = new Deck(nbVals);
		d.riffleShuffle(7);
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();
		while (!d.cards.isEmpty()) {
			player1.pick(d);
			player2.pick(d);
		}
	}

	// Question 3.2

	// test if the game is over
	boolean isOver() {
		if (player1.cards.isEmpty() || player2.cards.isEmpty()) {
			return true;
		}
		return false;
	}

	// effectue un tour de jeu
	boolean oneRound() {
		if (isOver())
			return false;
		int card1 = player1.cards.removeFirst();
		int card2 = player2.cards.removeFirst();
		if (turn) {
			trick.cards.add(card1);
			trick.cards.add(card2);
		} else {
			trick.cards.add(card2);
			trick.cards.add(card1);
		}
		if(ableToTrun)
			turn = !turn;

		int l = trick.cards.size();
		if (card1 > card2) {
			player1.pickAll(trick);
			return true;
		} else if (card1 < card2) {
			player2.pickAll(trick);
			return true;
		} else {
			if (isOver())
				return false;
			if (turn) {
				trick.pick(player1);
				trick.pick(player2);
			} else {
				trick.pick(player2);
				trick.pick(player1);
			}
			if(ableToTrun)
				turn = !turn;
			return oneRound();
		}
	}

	// Question 3.3

	// returns the winner
	int winner() {
		if (player1.cards.size() > player2.cards.size()) {
			return 1;
		} else if (player1.cards.size() < player2.cards.size()) {
			return 2;
		} else {
			return 0;
		}
	}

	// plays a game with a fixed maximum number of moves
	int game(int turns) {
		for (int i = 0; i < turns; i++) {
			if (!oneRound())
				break;
		}
		return winner();
	}

	// Question 4.1

	// plays a game without limit of moves, but with detection of infinite games
	int game() {
		Battle b1 = this.copy();
		Battle b2 = this.copy();
		while (true) {
			b1.game(1);
			b2.game(2);
			if (b1.isOver())
				return b1.winner();
			if (b2.isOver())
				return b2.winner();
			if (b1.toString().equals(b2.toString()))
				return 3;
		}
	}

	// Question 4.2

	// performs statistics on the number of infinite games
	static void stats(int nbVals, int nbGames) {
		int p1 = 0;
		int p2 = 0;
		int inf = 0;
		int draw = 0;
		for (int i = 0; i < nbGames; i++) {
			Battle b = new Battle(nbVals);
			b.ableToTrun = true;
			int r = b.game();
			if (r == 0)
				draw++;
			else if (r == 1)
				p1++;
			else if (r == 2)
				p2++;
			else
				inf++;
		}
		System.out.println("Player 1 wins : " + p1);
		System.out.println("Player 2 wins : " + p2);
		System.out.println("Draw : " + draw);
		System.out.println("Infinite games : " + inf);
	}
}
