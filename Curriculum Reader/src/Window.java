import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class Window extends JFrame{
	
	/** The main window that is seen in the program with all its components.**/
	
	private static final long serialVersionUID = 1L;
	private JComboBox <String>selectedSemester;
	private JList<Course> leftList;
	private JList<Course> rightList;
	private JMenuItem writeCurriculumData;
	private JMenuItem resetCurriculumData;
	private JLabel totalCreditsLabel;
	private JLabel takenCreditsLabel;
	private JLabel gpaLabel;
	private JButton markCourse;
	private Curriculum curriculum;

	public Window(Curriculum curriculum) throws FontFormatException, IOException{
		super(curriculum.getTitle());
		this.curriculum = curriculum;
		this.createWindow();
	}

	private void createWindow() throws FontFormatException, IOException{
		JPanel mainPanel = new JPanel();
		JPanel panelForm = new JPanel(new GridBagLayout());
		JMenuBar menuBar = new JMenuBar();

		mainPanel.add(panelForm);
		menuBar.add(new JMenu("File"));

		writeCurriculumData = menuBar.getMenu(0).add(new JMenuItem("Write Curriculum Data")); //First File Menu Item
		resetCurriculumData = menuBar.getMenu(0).add(new JMenuItem("Reset Curriculum Data")); //Second File Menu Item

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;

		this.setJMenuBar(menuBar);
		this.getContentPane().add(mainPanel);
		this.setIconImage(new ImageIcon("res/Images/icon2.png").getImage());
		this.setSize(700, 350);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		c.insets.set(10, 0, 0, 0);
		panelForm.add(new JLabel("Required Courses: "), c);

		c.gridy++;
		selectedSemester = new JComboBox<String>(curriculum.getSemesters().keySet().toArray(new String[curriculum.getSemesters().size()]));
		panelForm.add(selectedSemester, c);

		c.gridy++;
		leftList = new JList<Course>(curriculum.getSemesters().get("All Semesters").toArray(new Course[curriculum.getSemesters().get("All Semesters").size()]));
		leftList.setVisibleRowCount(4);
		leftList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		leftList.setFixedCellWidth(115);
		leftList.setFixedCellHeight(15);
		panelForm.add(new JScrollPane(leftList), c);

		c.gridx++;
		c.gridy = 0;
		c.insets.set(0, 0, 0, 0);
		JLabel title = new JLabel("Curricular Sequence");
		title.setFont(new CustomFont("res/Fonts/PRIMETIME.ttf", Font.PLAIN, 24));
		panelForm.add(title, c);

		c.gridy++;
		c.insets.set(10, 0, 0, 0);
		totalCreditsLabel = new JLabel("Total Credits: " + curriculum.getTotCredits());
		panelForm.add(totalCreditsLabel, c);

		c.gridy+=2;
		markCourse = new JButton("Mark as " + '"' + "Taken Courses" + '"');
		panelForm.add(markCourse, c);

		c.gridx++;
		c.gridy--;
		c.insets.set(10, 30, 0, 0);
		panelForm.add(new JLabel("Taken Courses: "), c);

		c.gridy++;
		rightList = new JList<Course>();
		rightList.setVisibleRowCount(4);
		rightList.setFixedCellWidth(115);
		rightList.setFixedCellHeight(15);
		panelForm.add(new JScrollPane(rightList), c);

		c.gridy++;
		c.gridx--;
		c.insets.set(0, 0, 0, 0);
		takenCreditsLabel = new JLabel("Taken Credits: " + curriculum.getTakenCredits());
		panelForm.add(takenCreditsLabel, c);

		c.gridy++;
		c.insets.set(10, 0, 0, 0);
		gpaLabel = new JLabel("GPA: N/A");
		panelForm.add(gpaLabel, c);

		EventHandler handler = new EventHandler(this);
		
		selectedSemester.addItemListener(handler);
		markCourse.addActionListener(handler);
		writeCurriculumData.addActionListener(handler);
		resetCurriculumData.addActionListener(handler);
		leftList.addListSelectionListener(handler);
		rightList.addListSelectionListener(handler);

		this.setVisible(true);
	}

	public JComboBox<String> getSelectedSemester() { return selectedSemester; }
	public JList<Course> getLeftList() { return leftList; }
	public JList<Course> getRightList() { return rightList; }
	public JMenuItem getWriteCurriculumData() { return writeCurriculumData; }
	public JMenuItem getResetCurriculumData() { return resetCurriculumData; }
	public JLabel getTotalCreditsLabel() { return totalCreditsLabel; }
	public JLabel getTakenCreditsLabel() { return takenCreditsLabel; }
	public JLabel getGpaLabel() { return gpaLabel; }
	public JButton getMarkCourse() { return markCourse; }
	public Curriculum getCurriculum() { return curriculum; }
}
