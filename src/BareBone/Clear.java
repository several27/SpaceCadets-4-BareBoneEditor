package BareBone;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Clear implements Statement
{
	public static final String name         = "clear";
	public static final String regexPattern = "clear\\s*([a-zA-Z])\\s*";

	private Variable variable;

	// it should probably be moved to constructor (or maybe not because execute is from interface ?)
	public void execute(String code, Scope scope)
	{
		Matcher matcher = Pattern.compile(regexPattern).matcher(code);

		while (matcher.find())
		{
			scope.updateVariable(matcher.group(1), BigInteger.ZERO);

			System.out.println(matcher.group(1) + " cleared");
			Debug.addMessage(matcher.group(1) + " cleared");
		}
	}
}
