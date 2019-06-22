import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Main {
	public static void main(String args[])throws Exception{
		String fileName = (String) JOptionPane.showInputDialog(null, "Enter the name of the curriculum you wish to scan: ", "Curriculum Scanner", JOptionPane.PLAIN_MESSAGE,
				new ImageIcon("res/Images/icon1.png"), null, "");
		new Window(new Curriculum(fileName));
	}
}
