package br.garca.model.game;

public class GameException extends RuntimeException {
    private Code code = null;

    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Code code) {
        super(message);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }
}
