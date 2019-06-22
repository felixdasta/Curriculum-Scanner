import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class CustomFont extends Font{
	 
	 /** Simplified class to create a custom font **/
	
	private static final long serialVersionUID = 1L;

	public CustomFont(String location, int style, int size) throws FontFormatException, IOException{
		 super(createFont(location, style, size));
	 }
	 
	 private static Font createFont(String location, int style, int size) throws FontFormatException, IOException{
			Font font = Font.createFont(style, new File(location)).deriveFont((float)size);	
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(style, new File(location)));
		    return font;
	 }
}
