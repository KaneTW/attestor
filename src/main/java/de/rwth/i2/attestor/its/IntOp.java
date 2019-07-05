package de.rwth.i2.attestor.its;

public enum IntOp {
    PLUS, MINUS, MUL, DIV, MOD;

    @Override
    public String toString() {
        switch(this) {
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MUL:
                return "*";
            case DIV:
                return "/";
            case MOD:
                return "%";
        }
    }
}
