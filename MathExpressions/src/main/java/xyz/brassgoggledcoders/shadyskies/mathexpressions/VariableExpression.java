package xyz.brassgoggledcoders.shadyskies.mathexpressions;

import java.util.Map;
import java.util.Objects;

public record VariableExpression(
        String variable
) implements IExpression {
    @Override
    public Number apply(Map<String, Number> stringNumberMap) {
        Number number = stringNumberMap.get(variable);
        return Objects.requireNonNullElse(number, 0);
    }
}
