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

	private ArrayList<Statement> statements = new ArrayList<>();
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

	public void updateVariable(String name, BigInteger value)
	{
		// look for variable in parent parent scope
		// return method
		if (parentScope != null && parentScope.parentScope != null)
		{
			for (Variable variable : parentScope.parentScope.variables)
			{
				if (variable.getName().equals(name))
				{
					variable.setValue(value);
					return;
				}
			}
		}

		// if variable already exists in parent scope
		// return method
		if (parentScope != null)
		{
			for (Variable variable : parentScope.variables)
			{
				if (variable.getName().equals(name))
				{
					variable.setValue(value);
					return;
				}
			}
		}

		// if variable already exists in current scope
		// return method
		for (Variable variable : variables)
		{
			if (variable.getName().equals(name))
			{
				variable.setValue(value);
				return;
			}
		}

		// if variable doesn't exist
		variables.add(new Variable(name, value));
	}

	public Variable getVariable(String name)
	{
		// look for variable in parent parent scope
		// return method
		if (parentScope != null && parentScope.parentScope != null)
		{
			for (Variable variable : parentScope.parentScope.variables)
			{
				if (variable.getName().equals(name))
				{
					return variable;
				}
			}
		}

		// look for variable in parent scope
		// return method
		if (parentScope != null)
		{
			for (Variable variable : parentScope.variables)
			{
				if (variable.getName().equals(name))
				{
					return variable;
				}
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

	public int execute() throws Exception
	{
		String statements[] = code.split(";");
//		System.out.println("Start: " + index);
		while (index < statements.length)
		{
			String statement = statements[index];

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
					break;
				}
			}

			// end scope
			if (statement.contains("end"))
			{
//				System.out.println("End: " + index);
				return index;
			}

			index++;
		}

		return statements.length;
	}
}
