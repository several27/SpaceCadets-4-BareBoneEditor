package Editor;

import javafx.scene.layout.Region;
import org.fxmisc.richtext.CodeArea;

public class DebugArea extends CodeArea
{
	private CodeArea codeArea;

	public DebugArea(CodeArea codeArea)
	{
		this.codeArea = codeArea;
		setupCodeArea();

		setDisabled(true);

		setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
	}

	private void setupCodeArea()
	{
		codeArea.richChanges().subscribe(change -> {
			replaceText(0, 0, codeArea.getText());
		});
	}
}
