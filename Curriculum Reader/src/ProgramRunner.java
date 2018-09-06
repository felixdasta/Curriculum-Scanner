import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

public class ProgramRunner {
	public static void main(String args[])throws FileNotFoundException{
		String fileName = JOptionPane.showInputDialog("Enter the name of the curriculum you wish to scan: ");
		CurriculumData curriculumRunner = new CurriculumData(fileName);
		
	}
}
