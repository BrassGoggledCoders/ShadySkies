package xyz.brassgoggledcoders.shadyskies.mathexpressions;

import java.util.Map;

public record ConstantExpression(
        Number number
) implements IExpression {
    @Override
    public Number apply(Map<String, Number> stringNumberMap) {
        return number;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
