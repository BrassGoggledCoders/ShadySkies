package xyz.brassgoggledcoders.shadyskies.mathexpressions;

public enum Operator {
    MULTIPLY,
    DIVIDE,
    PLUS,
    MINUS;

    public static Operator by(String substring) throws ParseException {
        if (substring.length() != 1) {
            throw new ParseException(substring + " is not a valid operator");
        }
        return switch (substring.charAt(0)) {
            case '*' -> MULTIPLY;
            case '/' -> DIVIDE;
            case '+' -> PLUS;
            case '-' -> MINUS;
            default -> throw new ParseException(substring + " is not a valid operator");
        };
    }
}
