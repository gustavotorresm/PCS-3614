package br.garca.model.spring;

import br.garca.model.game.Suit;

public class CardJSON {

    private int number;
    private Suit suit;

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
