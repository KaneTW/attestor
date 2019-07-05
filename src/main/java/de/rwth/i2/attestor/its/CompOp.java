package de.rwth.i2.attestor.its;

public enum CompOp {
    Less,
    LessEqual,
    Equal,
    NotEqual,
    GreaterEqual,
    Greater;

    @Override
    public String toString() {
        switch(this) {
            case Less:
                return "<";
            case LessEqual:
                return "<=";
            case Equal:
                return "==";
            case NotEqual:
                return "!=";
            case GreaterEqual:
                return ">=";
            case Greater:
                return ">";
        }
    }
}
