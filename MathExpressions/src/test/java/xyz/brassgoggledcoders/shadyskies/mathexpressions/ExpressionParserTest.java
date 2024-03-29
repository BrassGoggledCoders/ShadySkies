package xyz.brassgoggledcoders.shadyskies.mathexpressions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ExpressionParserTest {

    @Test
    void singleNumberProducesConstant() throws ParseException {
        IExpression expression = ExpressionParser.parse("2");

        Assertions.assertThat(expression)
                .isInstanceOf(ConstantExpression.class);

        Assertions.assertThat(expression.get().intValue())
                .isEqualTo(2);
    }

    @Test
    void emptyStringProducesEmptiness() throws ParseException {
        IExpression expression = ExpressionParser.parse("   ");

        Assertions.assertThat(expression)
                .isSameAs(EmptyExpression.INSTANCE);
    }

    @Test
    void singleNumberInParenthesesProducesConstant() throws ParseException {
        IExpression expression = ExpressionParser.parse("(2)");

        Assertions.assertThat(expression)
                .isInstanceOf(ConstantExpression.class);

        Assertions.assertThat(expression.get().intValue())
                .isEqualTo(2);
    }

    @Test
    void simpleExponentNoSpaceProducesConstant() throws ParseException {
        IExpression expression = ExpressionParser.parse("(2^2)");

        Assertions.assertThat(expression)
                .isInstanceOf(ConstantExpression.class);

        Assertions.assertThat(expression.get().intValue())
                .isEqualTo(4);
    }

    @Test
    void simpleExponentWithSpaceProducesConstant() throws ParseException {
        IExpression expression = ExpressionParser.parse("(2 ^ 2)");

        Assertions.assertThat(expression)
                .isInstanceOf(ConstantExpression.class);

        Assertions.assertThat(expression.get().intValue())
                .isEqualTo(4);
    }

    @Test
    void parenthesesRunBeforeOperations() throws ParseException {
        IExpression expression = ExpressionParser.parse("4 * (2 + 2)");

        Assertions.assertThat(expression)
                .isInstanceOf(ConstantExpression.class);

        Assertions.assertThat(expression.get().intValue())
                .isEqualTo(16);
    }

    @Test
    void singleVariableExpression() throws ParseException {
        IExpression expression = ExpressionParser.parse("test");

        Assertions.assertThat(expression)
                .isInstanceOf(VariableExpression.class);

        Assertions.assertThat(expression.apply(Map.of("test", 2)).intValue())
                .isEqualTo(2);
    }

    @Test
    void operatedSingleVariableExpression() throws ParseException {
        IExpression expression = ExpressionParser.parse("2 * test");

        Assertions.assertThat(expression)
                .isInstanceOf(CalculatedExpression.class);

        Assertions.assertThat(expression.apply(Map.of("test", 2)).intValue())
                .isEqualTo(4);
    }


    @Test
    void complexVariableExpression() throws ParseException {
        IExpression expression = ExpressionParser.parse("2 * test + (3^tester) / 3");

        Assertions.assertThat(expression)
                .isInstanceOf(CalculatedExpression.class);

        Assertions.assertThat(expression.apply(Map.of("test", 2, "tester", 4)).intValue())
                .isEqualTo(85);
    }
}
