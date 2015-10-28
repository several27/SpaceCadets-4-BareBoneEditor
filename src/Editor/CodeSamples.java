package Editor;

public class CodeSamples
{
	public static final String simpleLoop = "clear X;\n" +
	                                        "incr X;\n" +
	                                        "incr X;\n" +
	                                        "incr X;\n" +
	                                        "while X not 0 do;\n" +
	                                        "\tdecr X;\n" +
	                                        "end;";

	public static final String mutiplyXTimesY = "clear X;\n" +
	                                            "incr X;\n" +
	                                            "incr X;\n" +
	                                            "clear Y;\n" +
	                                            "incr Y;\n" +
	                                            "incr Y;\n" +
	                                            "incr Y;\n" +
	                                            "clear Z;\n" +
	                                            "while X not 0 do;\n" +
	                                            "\tclear W;\n" +
	                                            "\twhile Y not 0 do;\n" +
	                                            "\t\tincr Z;\n" +
	                                            "\t\tincr W;\n" +
	                                            "\t\tdecr Y;\n" +
	                                            "\tend;\n" +
	                                            "\twhile W not 0 do;\n" +
	                                            "\t\tincr Y;\n" +
	                                            "\t\tdecr W;\n" +
	                                            "\tend;\n" +
	                                            "\tdecr X;\n" +
	                                            "end;";
}
