package xyz.brassgoggledcoders.shadyskies.mathexpressions;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IExpression extends Function<Map<String, Number>, Number>, Supplier<Number> {

    @Override
    default Number get() {
        return this.apply(Collections.emptyMap());
    }

    default boolean isConstant() {
        return false;
    }
}
