package xyz.brassgoggledcoders.shadyskies.mathexpressions;

import java.util.Map;

public class EmptyExpression implements IExpression {
    public static final EmptyExpression INSTANCE = new EmptyExpression();

    @Override
    public Number apply(Map<String, Number> stringNumberMap) {
        return 0;
    }
}
