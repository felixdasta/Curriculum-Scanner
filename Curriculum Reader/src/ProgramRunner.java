import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class ProgramRunner {
	public static void main(String args[])throws FileNotFoundException{
		String fileName = (String) JOptionPane.showInputDialog(null, "Enter the name of the curriculum you wish to scan: ", "Curriculum Scanner", JOptionPane.PLAIN_MESSAGE,
				new ImageIcon("res/icon1.png"), null, "");
		new Window(new CurriculumData(fileName));
		
	}
}
