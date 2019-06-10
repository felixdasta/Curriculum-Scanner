import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class Window implements ActionListener, ItemListener{
	private JFrame frame;
	private JComboBox <String>selectedSemester;
	private JList<String> leftlist;
	private JList<String> rightlist;
	private JLabel creditsRepresentation;
	private JButton markCourse;
	private JButton writeFile;
	private JButton resetData;
	private CurriculumData curriculum;

	public Window(CurriculumData curriculum){
		this.curriculum = curriculum;

		frame = new JFrame(curriculum.getFileName().substring(0, curriculum.getFileName().indexOf(".")));
		frame.setIconImage(new ImageIcon("res/icon2.png").getImage());
		frame.setLayout(new FlowLayout());
		frame.setSize(900, 150);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JLabel("Required Courses: "));

		selectedSemester = new JComboBox<String>(curriculum.getSemesters().keySet().toArray(new String[curriculum.getSemesters().size()]));
		frame.add(selectedSemester);

		leftlist = new JList<String>(curriculum.getSemesters().get("All Semesters").toArray(new String[curriculum.getSemesters().get("All Semesters").size()]));
		leftlist.setVisibleRowCount(4);
		leftlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		leftlist.setFixedCellWidth(100);
		leftlist.setFixedCellHeight(15);
		frame.add(new JScrollPane(leftlist));

		markCourse = new JButton("Mark as " + '"' + "Taken Courses" + '"');
		frame.add(markCourse);

		frame.add(new JLabel("Taken Courses: "));

		rightlist = new JList<String>();
		rightlist.setVisibleRowCount(4);
		rightlist.setFixedCellWidth(100);
		rightlist.setFixedCellHeight(15);
		frame.add(new JScrollPane(rightlist));

		writeFile = new JButton("Write Curriculum Progress");
		frame.add(writeFile);

		resetData = new JButton("Reset Data");
		frame.add(resetData);

		creditsRepresentation = new JLabel(String.format("Curriculum Total Credits: %d \nTaken Credits: %d", curriculum.totCredits, curriculum.takenCredits));
		frame.add(creditsRepresentation);

		selectedSemester.addItemListener(this);
		markCourse.addActionListener(this);
		writeFile.addActionListener(this);
		resetData.addActionListener(this);

		frame.setVisible(true);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		int size = curriculum.getSemesters().get(selectedSemester.getSelectedItem().toString()).size(); //the list size;
		leftlist.setListData(curriculum.getSemesters().get(selectedSemester.getSelectedItem().toString()).toArray(new String[size]));
		rightlist.setListData(curriculum.getTakenCourses().toArray(new String[curriculum.getTakenCourses().size()]));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == markCourse){
			ArrayList<String>noPrerequisite = new ArrayList<String>();
			for(Object o: leftlist.getSelectedValues()){
				if(curriculum.hasPrerequisite(o.toString())) {
					curriculum.getTakenCourses().add(o.toString());
				}else{
					noPrerequisite.add(o.toString());
				}

			}
			for(String course: curriculum.getTakenCourses()){
				try{
					int pos = curriculum.getSemesters().get("All Semesters").indexOf(course);
					curriculum.getSemesters().get("All Semesters").remove(pos);
					curriculum.totHonorPoints += curriculum.courseGrade(course) * curriculum.getCoursesCredits().get(pos);
					for(LinkedList<String> currentSemester: curriculum.getSemesters().values()){
						if (currentSemester.contains(course)){
							currentSemester.remove(course);
						}
					}
					curriculum.takenCredits += curriculum.getCoursesCredits().remove(pos);
				}catch(Exception e){
					continue;
				}
			}
			if(noPrerequisite.isEmpty()) creditsRepresentation.setText(String.format("Curriculum Total Credits: %d Taken Credits: %d GPA: %.2f", curriculum.totCredits, curriculum.takenCredits, curriculum.getGPA()));
			else creditsRepresentation.setText("The following courses doesn't have the pre-requisites: " + noPrerequisite.toString());
			itemStateChanged((ItemEvent)selectedSemester.getAction());
		}
		else if (event.getSource() == writeFile){
			try {
				PrintWriter curriculumProgress = new PrintWriter("My Curriculum Progress.txt");
				String name = JOptionPane.showInputDialog("Enter your name:");
				String id = JOptionPane.showInputDialog("Enter your student ID:");

				while(id.length() != 11){
					id = JOptionPane.showInputDialog("INVALID STUDENT ID - Make sure you type it correctly:");
					try{
						int num = Integer.parseInt(id.replaceAll("-", ""));
					}catch(Exception e){
						continue;
					}
				}

				curriculumProgress.println("STUDENT NAME: " + name);
				curriculumProgress.println("STUDENT ID: " + id);
				curriculumProgress.println(String.format("GPA: %.2f", curriculum.getGPA()));
				curriculumProgress.println();
				curriculumProgress.write("Taken courses:");


				for(int i = 0; i < curriculum.getTakenCourses().size(); i++){
					curriculumProgress.println();
					curriculumProgress.write("\nCOURSE: " + curriculum.getTakenCourses().get(i));
					curriculumProgress.write("\tLETTER GRADE: " + Character.toString(curriculum.getGrades().get(curriculum.getTakenCourses().get(i))));
				}

				curriculumProgress.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else if(event.getSource() == resetData){
			try {
				curriculum.setCurriculumData(curriculum.getFileName());
				creditsRepresentation.setText(String.format("Curriculum Total Credits: %d Taken Credits: %d", curriculum.totCredits, curriculum.takenCredits));
				itemStateChanged((ItemEvent)selectedSemester.getAction());
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "ERROR: Make sure that the curriculum text file is still available.");
				e.printStackTrace();
			}
		}
	}
}
