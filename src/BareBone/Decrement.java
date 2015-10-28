package BareBone;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Decrement implements Statement
{
	public static final String name         = "decr";
	public static final String regexPattern = "decr\\s*([a-zA-Z])";

	private Variable variable;

	public void execute(String code, Scope scope)
	{
		Matcher matcher = Pattern.compile(regexPattern).matcher(code);

		while (matcher.find())
		{
			variable = scope.getVariable(matcher.group(1));
			variable.setValue(variable.getValue().subtract(BigInteger.ONE));

			System.out.println(variable.getName() + " decremented to " + variable.getValue());
		}
	}
}
