package xyz.brassgoggledcoders.shadyskies.mathexpressions;

import java.util.Map;
import java.util.function.BiFunction;

public record CalculatedExpression(
        IExpression left,
        IExpression right,
        BiFunction<Number, Number, Number> calculate
) implements IExpression {
    @Override
    public Number apply(Map<String, Number> stringNumberMap) {
        return calculate.apply(
                left().apply(stringNumberMap),
                right().apply(stringNumberMap)
        );
    }
}
