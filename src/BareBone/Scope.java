package BareBone;

import javax.swing.plaf.nimbus.State;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scope
{
	private static final ArrayList<Class<? extends Statement>> availableStatements = new ArrayList<>();

	public static void initialize()
	{
		availableStatements.add(While.class);
		availableStatements.add(Clear.class);
		availableStatements.add(Increment.class);
		availableStatements.add(Decrement.class);
	}

	public  ArrayList<Variable>  variables  = new ArrayList<>();
	private Scope parentScope;

	public int    index;
	public String code;

	public Scope()
	{

	}

	public Scope(Scope parentScope)
	{
		this.parentScope = parentScope;
		this.code = parentScope.code;
		this.index = parentScope.index;
	}

	public Variable getVariable(String name)
	{
		// look for variable recursively
		// return method
		if (parentScope != null)
		{
			Variable variable = parentScope.getVariable(name);
			if (variable != null)
			{
				return variable;
			}
		}

		// look for variable in current scope
		for (Variable variable : variables)
		{
			if (variable.getName().equals(name))
			{
				return variable;
			}
		}

		return null;
	}

	public void updateVariable(String name, BigInteger value)
	{
		Variable variable = getVariable(name);
		if (variable == null)
		{
			variables.add(new Variable(name, value));
		}
		else
		{
			variable.setValue(value);
		}
	}

	public int execute() throws Exception
	{
		String statements[] = code.split(";");
//		System.out.println("Start: " + index);
		while (index < statements.length)
		{
			String statement = statements[index];

			boolean statementRecognized = false;

			for (Class<?extends Statement> availableStatement : availableStatements)
			{
				String regex = (String) availableStatement.getDeclaredField("regexPattern").get(null);
				Matcher subMatcher = Pattern.compile(regex).matcher(statement);

				int numberOfSubMatches = 0;
				while (subMatcher.find())
				{
					numberOfSubMatches++;
				}

				if (numberOfSubMatches > 0)
				{
					Statement statementInstance = availableStatement.newInstance();
					statementInstance.execute(statement, this);
					statementRecognized = true;
					break;
				}
			}

			// end scope
			if (statement.contains("end"))
			{
//				System.out.println("End: " + index);
				return index;
			}

			if (!statementRecognized)
				System.out.println("Statement not recognized");

			index++;
		}

		return statements.length;
	}
}
