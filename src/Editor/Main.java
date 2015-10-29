package Editor;

import BareBone.Debug;
import BareBone.Scope;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application
{
    private static final String[] KEYWORDS = new String[] {
            "clear", "incr", "decr", "while", "not", "do", "end"
    };

    private static final String KEYWORD_PATTERN   = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN    = "\"([^\"\\\\]|\\\\.)*\"";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")");

	private static Scope mainCodeScope;

	private static File currentFile;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
	    Menu fileMenu = new Menu("File");
	    MenuItem newMenuItem = new MenuItem("New");
	    MenuItem openMenuItem = new MenuItem("Open");
	    MenuItem saveMenuItem = new MenuItem("Save");
	    MenuItem saveAsMenuItem = new MenuItem("Save As");
	    MenuItem exitMenuItem = new MenuItem("Exit");
	    exitMenuItem.setOnAction(e -> System.exit(202));
	    fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem, exitMenuItem);

	    Menu codeSamplesMenu = new Menu("Code Samples");
	    MenuItem loopMenuItem = new MenuItem("Simple Loop");
	    MenuItem multiplyMenuItem = new MenuItem("Multiply X * Y");
	    codeSamplesMenu.getItems().addAll(loopMenuItem, multiplyMenuItem);

	    Menu runMenu = new Menu("Run");
	    MenuItem runMenuItem = new MenuItem("Run");
	    runMenu.getItems().addAll(runMenuItem);

	    MenuBar menuBar = new MenuBar();
	    menuBar.getMenus().addAll(fileMenu, codeSamplesMenu, runMenu);

        CodeArea codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        DebugArea debugArea = new DebugArea(codeArea);

	    codeArea.richChanges().subscribe(change -> {
		    // Code highlighting
		    codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
	    });

	    loopMenuItem.setOnAction(actionEvent -> {
			codeArea.replaceText(0, codeArea.getLength(), CodeSamples.simpleLoop);
		    runMenuItem.fire();
	    });

	    multiplyMenuItem.setOnAction(actionEvent -> {
		    codeArea.replaceText(0, codeArea.getLength(), CodeSamples.mutiplyXTimesY);
		    runMenuItem.fire();
	    });

	    newMenuItem.setOnAction(event -> {
		    codeArea.replaceText(0, codeArea.getLength(), "");
		    debugArea.replaceText(0, debugArea.getLength(), "");
//		    Process.exec(javaExecutable, "-classpath", urls.join(":"), CLASS_TO_BE_EXECUTED)
	    });

	    openMenuItem.setOnAction(event -> {
		    FileChooser fileChooser = new FileChooser();
		    File file = fileChooser.showOpenDialog(primaryStage);
		    if (file != null)
		    {
				codeArea.replaceText(0, codeArea.getLength(), readFile(file));
		    }
		    runMenuItem.fire();
	    });

	    saveMenuItem.setOnAction(event -> {
		    BufferedWriter writer = null;

		    try
		    {
			    writer = new BufferedWriter(new FileWriter(currentFile));
			    writer.write(codeArea.getText());
		    }
		    catch (Exception e) {}
		    finally
		    {
			    try
			    {
				    writer.close();
			    }
			    catch (Exception e) {}
		    }
	    });

	    saveAsMenuItem.setOnAction(event -> {
		    FileChooser fileChooser = new FileChooser();
		    currentFile = fileChooser.showSaveDialog(primaryStage);

		    saveMenuItem.fire();
	    });

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.85);
        splitPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
	    splitPane.getItems().addAll(codeArea, debugArea);

	    ToolBar toolBar = new ToolBar();
	    Label executionTimeToolbar = new Label();
	    toolBar.getItems().addAll(executionTimeToolbar);

	    runMenuItem.setOnAction(actionEvent -> {
		    // Code execute
		    try
		    {
			    long startTime = System.currentTimeMillis();

			    Debug.messages.clear();
			    mainCodeScope.code = codeArea.getText();
			    mainCodeScope.index = 0;
			    mainCodeScope.execute();

			    executionTimeToolbar.setText("Execution Time: " + (System.currentTimeMillis() - startTime) + "ms");

		    }
		    catch (Exception exception)
		    {
			    System.out.println(exception.getMessage());
		    }

		    // Code debug
		    System.out.println(Debug.messagesToString());
		    debugArea.replaceText(0, debugArea.getLength(), Debug.messagesToString());
	    });

	    VBox vbox = new VBox(5);
	    vbox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
	    vbox.getChildren().addAll(menuBar, splitPane, toolBar);
	    vbox.getStylesheets().add(getClass().getResource("bare-bone.css").toExternalForm());

	    VBox.setVgrow(splitPane, Priority.ALWAYS);

        primaryStage.setTitle("Bare Bone Editor");
        primaryStage.setScene(new Scene(vbox, 300, 275));
        primaryStage.show();
    }

	private static StyleSpans<Collection<String>> computeHighlighting(String text)
	{
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while(matcher.find()) {
			String styleClass =
					matcher.group("KEYWORD") != null ? "keyword" :
					matcher.group("SEMICOLON") != null ? "semicolon" :
					matcher.group("STRING") != null ? "string" :
					null; /* never happens */ assert styleClass != null;
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}

    public static void main(String[] args) throws Exception
    {
	    // Bare bone initialization
	    Scope.initialize();
	    mainCodeScope = new Scope();

	    // Launch
        launch(args);
    }

	private String readFile(File file)
	{
		StringBuilder stringBuffer = new StringBuilder();
		BufferedReader bufferedReader;
		try
		{
			bufferedReader = new BufferedReader(new FileReader(file));
			String text;
			while ((text = bufferedReader.readLine()) != null)
			{
				stringBuffer.append(text).append("\n");
			}
		}
		catch (Exception e) {}

		return stringBuffer.toString();
	}
}
