import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class CurriculumData implements ActionListener, ItemListener{

	private String fileName;
	private LinkedList<String>coursesCodes;
	private LinkedList<Integer>coursesCredits;
	private LinkedList<String>coursesNames;
	private LinkedList<String>semesterSelection;
	private ArrayList<String>takenCourses;
	private ArrayList<LinkedList<String>>individualSemesters;
	private JFrame frame;
	private JComboBox <String>selectedSemester;
	private JList<String> leftlist;
	private JList<String> rightlist;
	private JLabel creditsRepresentation;
	private JButton markCourse;
	private JButton writeFile;
	private JButton resetData;
	private PrintWriter curriculumProgress;
	private int totCredits;
	private int takenCredits;

	public CurriculumData(String fileName)throws FileNotFoundException{

		this.fileName = fileName;

		setCurriculumData(fileName);

		frame = new JFrame(fileName.substring(0, fileName.indexOf(".")));
		frame.setLayout(new FlowLayout());
		frame.setSize(900, 150);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JLabel("Required Courses: "));
		
		selectedSemester = new JComboBox<String>(semesterSelection.toArray(new String[semesterSelection.size()]));
		frame.add(selectedSemester);

		leftlist = new JList<String>(coursesCodes.toArray(new String[coursesCodes.size()]));
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

		creditsRepresentation = new JLabel(String.format("Curriculum Total Credits: %d \nTaken Credits: %d", totCredits, takenCredits));
		frame.add(creditsRepresentation);

		selectedSemester.addItemListener(this);
		markCourse.addActionListener(this);
		writeFile.addActionListener(this);
		resetData.addActionListener(this);

		frame.setVisible(true);
	}

	private void setCurriculumData(String fileName)throws FileNotFoundException{
		Scanner curriculumScan = new Scanner(new File(fileName));
		LinkedList<Integer> coursePositionerList = new LinkedList<Integer>();

		init();

		int sociocount = 1;
		int inglcount = 1;
		int coursePositionerToAdd = 0;

		semesterSelection.add("All Semesters");

		while(curriculumScan.hasNext()){
			String line = curriculumScan.nextLine();
			if (line.toUpperCase().startsWith("First".toUpperCase()) || line.toUpperCase().startsWith("Second".toUpperCase()) 
					|| line.toUpperCase().startsWith("Third".toUpperCase()) || line.toUpperCase().startsWith("Fourth".toUpperCase()) 
					|| line.toUpperCase().startsWith("Fifth".toUpperCase())){
				semesterSelection.add(line.substring(0, line.length() - 1));
				coursePositionerList.add(coursePositionerToAdd);
				individualSemesters.add(new LinkedList<String>());
			}
			else if(line.lastIndexOf(":") == line.length()-1){
				continue;
			}
			else if (line.indexOf(":") == -1){
				continue;
			}
			else if (line.startsWith("SOCIO")){
				int pos1 = 0;
				int pos2 = line.indexOf(":");
				coursesNames.add(line.substring(pos1, pos2));
				coursesCodes.add("SOCIOXXXX" + "[" + sociocount + "]");
				coursesCredits.add(Integer.parseInt(line.substring(line.lastIndexOf("-") + 2, line.lastIndexOf("-") + 3)));
				totCredits += coursesCredits.get(coursesCredits.size() - 1);
				sociocount++;
			}
			else if (line.startsWith("English")){
				int pos1 = 0;
				int pos2 = line.indexOf(":");
				coursesNames.add(line.substring(pos1, pos2));
				coursesCodes.add("INGLXXXX" + "[" + inglcount + "]");
				coursesCredits.add(Integer.parseInt(line.substring(line.indexOf("-") + 2, line.indexOf("-") + 3)));
				totCredits += coursesCredits.get(coursesCredits.size() - 1);
				inglcount++;
			}
			else{
				int pos1 = line.indexOf(":") + 2;
				int pos2 = pos1 + 8;
				coursesNames.add(line.substring(0, pos1 - 2));
				coursesCodes.add(line.substring(pos1, pos2));
				coursesCredits.add(Integer.parseInt(line.substring(line.indexOf("-") + 2, line.indexOf("-") + 3)));
				totCredits += coursesCredits.get(coursesCredits.size() - 1);
			}
			coursePositionerToAdd++;
		}

		curriculumScan.close();
		
		//WARNING: Watch the first GitHub Commit to understand better what is happening on the following lines of code
		for(LinkedList<String> currentSemester: individualSemesters){
			int posNumber;

			if(individualSemesters.indexOf(currentSemester) != individualSemesters.size()-1)  posNumber = coursePositionerList.get(individualSemesters.indexOf(currentSemester) + 1) - (individualSemesters.indexOf(currentSemester) + 1);
			else posNumber = coursesCodes.size();

			for(Object o: coursesCodes.subList(coursePositionerList.get(individualSemesters.indexOf(currentSemester)) - individualSemesters.indexOf(currentSemester), posNumber).toArray()){
				currentSemester.add(o.toString());
			}
		}
	}

	private void init(){
		semesterSelection = new LinkedList<>();
		takenCourses = new ArrayList<>();
		coursesCodes = new LinkedList<>();
		coursesCredits = new LinkedList<>();
		coursesNames = new LinkedList<>();
		individualSemesters = new ArrayList<>();
		totCredits = 0;
		takenCredits = 0;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event){
		if(event.getSource() == markCourse){
			for(Object o: leftlist.getSelectedValues()){
				takenCourses.add(o.toString());
			}
			for(String course: takenCourses){
				try{
					int pos = coursesCodes.indexOf(course);
					coursesCodes.remove(pos);
					for(LinkedList<String> currentSemester: individualSemesters){
						if (currentSemester.contains(course)){
							currentSemester.remove(course);
						}
					}
					takenCredits += coursesCredits.get(pos);
					coursesCredits.remove(pos);
				}catch(Exception e){
					continue;
				}
			}
			creditsRepresentation.setText(String.format("Curriculum Total Credits: %d Taken Credits: %d", totCredits, takenCredits));
			itemStateChanged((ItemEvent)selectedSemester.getAction());
		}
		else if (event.getSource() == writeFile){
			try {
				curriculumProgress = new PrintWriter("My Curriculum Progress.txt");
				curriculumProgress.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else if(event.getSource() == resetData){
			try {
				setCurriculumData(fileName);
				creditsRepresentation.setText(String.format("Curriculum Total Credits: %d Taken Credits: %d", totCredits, takenCredits));
				itemStateChanged((ItemEvent)selectedSemester.getAction());
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "ERROR: Make sure that the curriculum text file is still available.");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(selectedSemester.getSelectedItem() != semesterSelection.get(0))
			leftlist.setListData(individualSemesters.get(semesterSelection.indexOf(selectedSemester.getSelectedItem())-1).toArray(new String[individualSemesters.get(semesterSelection.indexOf(selectedSemester.getSelectedItem())-1).size()]));
		else
			leftlist.setListData(coursesCodes.toArray(new String[coursesCodes.size()]));
		rightlist.setListData(takenCourses.toArray(new String[takenCourses.size()]));
	}
}
