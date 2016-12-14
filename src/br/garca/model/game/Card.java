package br.garca.model.game;

import java.util.Comparator;
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

class CardComparator implements Comparator<Card> {

    private int manilha;

    public static int getManilha(Card vira) {
        int manilha;

        int number = vira.getNumber();

        if (number == 7) {
            manilha = Card.QUEEN;
        } else if (number == Card.KING) {
            manilha = Card.ACE;
        } else if (number == 3) {
            manilha = 4;
        } else {
            manilha = number + 1;
        }

        return manilha;
    }

    public static boolean isManilha(Card card, Card vira) {
        return card.getNumber() == getManilha(vira);
    }


    public CardComparator(Card vira) {
        manilha = getManilha(vira);
    }

    @Override
    public int compare(Card c1, Card c2) {
        if (c1.getNumber() == manilha) {
            if (c2.getNumber() == manilha) {
                return c1.getSuit().compareTo(c2.getSuit());
            } else {
                return 100;
            }
        }

        return toComparatorValue(c1.getNumber()) - toComparatorValue(c2.getNumber());
    }

    private int toComparatorValue(int number) {
        if (4 <= number && number <= 7) {
            return number - 4;
        } else if (Card.QUEEN <= number && number <= Card.KING) {
            return number - Card.QUEEN + 4;
        } else if (Card.ACE <= number && number <= 3) {
            return number - Card.ACE + 7;
        }

        return -1;
    }
}
