package BareBone;

import java.math.BigInteger;

public class Variable
{
	private String name;
	private BigInteger value; // maybe it could store byte[] instead

	public Variable(String name, BigInteger value)
	{
		setName(name);
		setValue(value);
	}

	public String getName()
	{
		return name;
	}

	private void setName(String name)
	{
		this.name = name;
	}

	public BigInteger getValue()
	{
		return value;
	}

	public void setValue(BigInteger value)
	{
		this.value = value;
	}

	public String getBinaryValue()
	{
		return value.toString(2);
	}

	public void setBinaryValue(String binaryValue)
	{
		value = new BigInteger(binaryValue, 2);
	}
}

