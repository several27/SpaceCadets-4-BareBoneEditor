package sample;

import BareBone.Scope;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        CodeArea codeArea = new CodeArea();
	    codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
	    codeArea.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        DebugArea debugArea = new DebugArea(codeArea);

	    SplitPane splitPane = new SplitPane();
	    splitPane.setDividerPositions(0.8);
	    splitPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
	    splitPane.getItems().addAll(codeArea, debugArea);

	    VBox vbox = new VBox(5);
	    vbox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
	    vbox.getChildren().addAll(splitPane);

        primaryStage.setTitle("Bare Bone Editor");
        primaryStage.setScene(new Scene(vbox, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception
    {
//        launch(args);

//	    String code = String.join("\n", new String[] {
//			                              "clear Z;",
//			                              "while X not 0 do;",
//			                              "\tclear W;",
//			                              "\twhile Y not 0 do;",
//			                              "\t\tincr Z;",
//			                              "\t\tincr W;",
//			                              "\t\tdear Y;",
//			                              "\tend;",
//			                              "\twhile W not 0 do;",
//			                              "\t\tincr Y;",
//			                              "\t\tdecr W;",
//			                              "\tend;",
//			                              "\tdecr X;",
//			                              "end;"
//	                              });

//	    String code = String.join("\n", new String[] {"clear X;\n" +
//	                                                  "incr X;\n" +
//	                                                  "incr X;\n" +
//	                                                  "incr X;\n" +
//	                                                  "while X not 0 do;\n" +
//	                                                  "   decr X;\n" +
//	                                                  "end;"});

	    String code = String.join("\n", new String[] {"clear X;\n" +
	                                                  "incr X;\n" +
	                                                  "incr X;\n" +
	                                                  "clear Y;\n" +
	                                                  "incr Y;\n" +
	                                                  "incr Y;\n" +
	                                                  "incr Y;\n" +
	                                                  "clear Z;\n" +
	                                                  "while X not 0 do;\n" +
	                                                  "   clear W;\n" +
	                                                  "   while Y not 0 do;\n" +
	                                                  "      incr Z;\n" +
	                                                  "      incr W;\n" +
	                                                  "      decr Y;\n" +
	                                                  "   end;\n" +
	                                                  "   while W not 0 do;\n" +
	                                                  "      incr Y;\n" +
	                                                  "      decr W;\n" +
	                                                  "   end;\n" +
	                                                  "   decr X;\n" +
	                                                  "end;"});

	    Scope.initialize();

	    Scope mainScope = new Scope();
	    mainScope.code = code;
	    mainScope.index = 0;
	    mainScope.execute();
    }
}
