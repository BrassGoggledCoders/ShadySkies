package xyz.brassgoggledcoders.shadyskies.mathexpressions;

import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.functions.ThrowingBiFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class ExpressionParser {

    @NotNull
    public static IExpression parse(String expression) throws ParseException {
        int currentStart = 0;
        AtomicInteger depth = new AtomicInteger();
        AtomicReference<ParsingType> type = new AtomicReference<>(null);
        List<Object> parsedSymbols = new ArrayList<>();
        expression = expression.trim();
        char[] characters = expression.toCharArray();
        for (int position = 0; position < expression.length(); position++) {
            char character = characters[position];
            if (Character.isWhitespace(character)) {
                if (addParsedType(parsedSymbols, expression.substring(currentStart, position), type.get())) {
                    depth.set(0);
                    currentStart = position + 1;
                }
                if (type.get() != ParsingType.EXPRESSION) {
                    type.set(null);
                }
            } else {
                if (type.get() == null) {
                    type.set(findType(character));
                }

                Object parsed = parseType(type, parsedSymbols, character, expression.substring(currentStart, position), depth);

                if (parsed != null) {
                    parsedSymbols.add(parsed);
                    if (type.get() == ParsingType.NUMBER || type.get() == ParsingType.VARIABLE) {
                        type.set(findType(character));
                        parsed = parseType(type, parsedSymbols, character, expression.substring(currentStart, position), depth);

                        if (parsed != null) {
                            parsedSymbols.add(parsed);
                            type.set(null);
                        }
                    }
                    currentStart = position + 1;
                }
            }
        }

        if (currentStart != expression.length() && type.get() != null) {
            addParsedType(parsedSymbols, expression.substring(currentStart), type.get());
        }

        if (parsedSymbols.isEmpty()) {
            return EmptyExpression.INSTANCE;
        } else if (parsedSymbols.size() == 1) {
            return parseSymbol(parsedSymbols.get(0));
        } else {
            combineSymbols(
                    parsedSymbols,
                    object -> object.equals(ParsingType.EXPONENT),
                    handleOperation(Math::pow)
            );

            combineSymbols(
                    parsedSymbols,
                    object -> object.equals(Operator.MULTIPLY),
                    handleOperation((left, right) -> left * right)
            );

            combineSymbols(
                    parsedSymbols,
                    object -> object.equals(Operator.DIVIDE),
                    handleOperation((left, right) -> left / right)
            );

            combineSymbols(
                    parsedSymbols,
                    object -> object.equals(Operator.PLUS),
                    handleOperation(Double::sum)
            );


            combineSymbols(
                    parsedSymbols,
                    object -> object.equals(Operator.MINUS),
                    handleOperation((left, right) -> left - right)
            );

            if (parsedSymbols.size() == 1) {
                return parseSymbol(parsedSymbols.get(0));
            } else {
                throw new ParseException("No expression formed");
            }
        }
    }

    private static ThrowingBiFunction<Object, Object, Object, ParseException> handleOperation(
            BiFunction<Double, Double, Double> operate
    ) {
        return (left, right) -> {
            if (left instanceof IExpression expression && expression.isConstant()) {
                left = expression.get();
            }
            if (right instanceof IExpression expression && expression.isConstant()) {
                right = expression.get();
            }
            if (left instanceof Number leftNumber && right instanceof Number rightNumber) {
                return operate.apply(leftNumber.doubleValue(), rightNumber.doubleValue());
            } else {
                IExpression leftExpression = parseSymbol(left);
                IExpression rightExpression = parseSymbol(right);

                if (leftExpression != null && rightExpression != null) {
                    return new CalculatedExpression(
                            leftExpression,
                            rightExpression,
                            (calcLeft, calcRight) -> operate.apply(
                                    calcLeft.doubleValue(),
                                    calcRight.doubleValue()
                            )
                    );
                }
                throw new ParseException("Failed to create Expression for %s & %s".formatted(left, right));
            }
        };
    }

    private static Object parseType(AtomicReference<ParsingType> type, List<Object> parsedSymbols, char character,
                                    String expressionSubString, AtomicInteger depth) throws ParseException {
        return switch (type.get()) {
            case DASH -> {
                if (parsedSymbols.isEmpty()) {
                    type.set(ParsingType.NUMBER);
                    yield null;
                } else if (!(parsedSymbols.get(parsedSymbols.size() - 1) instanceof Operator)) {
                    yield Operator.MINUS;
                }
                yield null;
            }
            case NUMBER -> {
                if (!Character.isDigit(character) && character != '.') {
                    try {
                        yield Double.parseDouble(expressionSubString);
                    } catch (NumberFormatException e) {
                        throw new ParseException(expressionSubString + " is not a valid number");
                    }
                }
                yield null;
            }
            case EXPRESSION -> {
                if (character == '(') {
                    depth.incrementAndGet();
                    yield null;
                } else if (character == ')') {

                    if (depth.decrementAndGet() == 0) {
                        yield ExpressionParser.parse(expressionSubString.substring(1));
                    }
                }
                yield null;
            }
            case VARIABLE -> {
                if (!Character.isAlphabetic(character)) {
                    yield expressionSubString;
                } else {
                    yield null;
                }
            }
            case EXPONENT -> ParsingType.EXPONENT;
            default -> null;
        };
    }

    private static ParsingType findType(char character) throws ParseException {
        ParsingType type;
        if (Character.isDigit(character) || character == '.') {
            type = ParsingType.NUMBER;
        } else if (Character.isAlphabetic(character)) {
            type = ParsingType.VARIABLE;
        } else if (character == '(') {
            type = ParsingType.EXPRESSION;
        } else if (character == '^') {
            type = ParsingType.EXPONENT;
        } else if (character == '*' || character == '/' || character == '+') {
            type = ParsingType.OPERATOR;
        } else if (character == '-') {
            type = ParsingType.DASH;
        } else {
            throw new ParseException(character + " is not valid for expression");
        }
        return type;
    }

    private static void combineSymbols(List<Object> parsedSymbols, Predicate<Object> searchedSymbol,
                                       ThrowingBiFunction<Object, Object, Object, ParseException> combine) throws ParseException {
        int currentPos = 0;
        while (currentPos < parsedSymbols.size()) {
            if (searchedSymbol.test(parsedSymbols.get(currentPos))) {
                Object left = null;
                Object right = null;

                if (currentPos > 0) {
                    left = parsedSymbols.get(currentPos - 1);
                }
                if (currentPos + 1 < parsedSymbols.size()) {
                    right = parsedSymbols.get(currentPos + 1);
                }

                Object newSymbol = combine.apply(left, right);
                parsedSymbols.set(currentPos, newSymbol);
                if (right != null) {
                    parsedSymbols.remove(currentPos + 1);
                }
                if (left != null) {
                    parsedSymbols.remove(currentPos - 1);
                }
            }

            currentPos++;
        }
    }

    private static boolean addParsedType(List<Object> parsedSymbols, String parsing, ParsingType type) throws ParseException {
        if (type != null) {
            return switch (type) {
                case NUMBER -> {
                    try {
                        parsedSymbols.add(Double.parseDouble(parsing));
                        yield true;
                    } catch (NumberFormatException e) {
                        throw new ParseException(parsing + " is not a valid number");
                    }
                }
                case VARIABLE -> {
                    parsedSymbols.add(parsing);
                    yield true;
                }
                case DASH -> {
                    parsedSymbols.add(Operator.MINUS);
                    yield true;
                }
                case OPERATOR -> {
                    parsedSymbols.add(Operator.by(parsing));
                    yield true;
                }
                default -> false;
            };
        }

        return false;
    }

    private static IExpression parseSymbol(Object o) throws ParseException {
        if (o instanceof Number number) {
            return new ConstantExpression(number);
        } else if (o instanceof IExpression expression) {
            return expression;
        } else if (o instanceof String string) {
            return new VariableExpression(string);
        }

        throw new ParseException(o.toString() + " cannot be parsed yet");
    }
}
