package BareBone;

import java.util.ArrayList;

public class Debug
{
	public static ArrayList<String> messages = new ArrayList<>();

	public static void addMessage(String message)
	{
		messages.add(message);
		System.out.println(message);
	}

	public static String messagesToString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (String message : messages)
		{
			stringBuilder.append(message + "\n");
		}

		return stringBuilder.toString();
	}
}
