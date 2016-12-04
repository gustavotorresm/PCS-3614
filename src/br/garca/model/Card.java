package br.garca.model;

import java.util.Stack;
import java.util.stream.IntStream;

public class Card {

    public static final int ACE = 1;
    public static final int JACK = 11;
    public static final int QUEEN = 12;
    public static final int KING = 13;

    private final int number;
    private final Suit suit;

    public Card(int number, Suit suit) {
        this.number = number;
        this.suit = suit;
    }

    public int getNumber() {
        return number;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        String numberText;
        switch (number) {
            case ACE:
                numberText = "Ace";
                break;
            case QUEEN:
                numberText = "Queen";
                break;
            case JACK:
                numberText = "Jack";
                break;
            case KING:
                numberText = "King";
                break;
            default:
                numberText = String.valueOf(number);
        }
        return numberText + " of " + suit;
    }
}

class CardFactory {

    public static Stack<Card> createPack() {
        Stack<Card> pack = new Stack<>();

        IntStream numberRange = IntStream.rangeClosed(4, 7);
        numberRange = IntStream.concat(numberRange, IntStream.of(Card.QUEEN, Card.JACK, Card.KING));
        numberRange = IntStream.concat(numberRange, IntStream.rangeClosed(Card.ACE, 3));

        numberRange.forEachOrdered(
                n -> {
                    for (Suit suit : Suit.values()) {
                        pack.add(new Card(n, suit));
                    }
                }
        );

        return pack;
    }
}
