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

public class CurriculumData {
	
	private Scanner curriculumScan;
	private String fileToScan;
	private LinkedList<String>coursesCodes;
	private LinkedList<Integer>coursePositionerList;
	private LinkedList<Integer>coursesCredits;
	private LinkedList<String>coursesNames;
	private LinkedList<String>y1sm1;
	private LinkedList<String>y1sm2;
	private LinkedList<String>y2sm1;
	private LinkedList<String>y2sm2;
	private LinkedList<String>y3sm1;
	private LinkedList<String>y3sm2;
	private LinkedList<String>y4sm1;
	private LinkedList<String>y4sm2;
	private LinkedList<String>y5sm1;
	private LinkedList<String>y5sm2; 
	private LinkedList<String>semesterSelection;
	private ArrayList<String>takenCourses;
	private JFrame frame = new JFrame();
	private JComboBox currentSemester;
	private JList leftlist;
	private JList rightlist;
	private JLabel lltitle;
	private JLabel rltitle;
	private JLabel creditsRepresentation;
	private JButton markCourse;
	private JButton writeFile;
	private JButton resetData;
	private PrintWriter curriculumProgress;
	private int totCredits;
	private int takenCredits;
	private int coursePositionerToAdd;
	private int pos1;
	private int pos2;
	private int sociocount;
	private int inglcount;
	
	public CurriculumData(String fileName)throws FileNotFoundException{
		
		fileToScan = fileName;
		
		setCurriculumData(fileToScan);
		
		frame.setTitle(fileName.substring(0, fileName.indexOf(".")));
		frame.setLayout(new FlowLayout());
		frame.setSize(900, 150);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		lltitle = new JLabel("Required Courses: ");
		frame.add(lltitle);
		
		currentSemester = new JComboBox(semesterSelection.toArray());
		frame.add(currentSemester);
		
		leftlist = new JList(coursesCodes.toArray());
		leftlist.setVisibleRowCount(4);
		leftlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		leftlist.setFixedCellWidth(100);
		leftlist.setFixedCellHeight(15);
		frame.add(new JScrollPane(leftlist));
		
		markCourse = new JButton("Mark as " + '"' + "Taken Courses" + '"');
		frame.add(markCourse);
		
		rltitle = new JLabel("Taken Courses: ");
		frame.add(rltitle);
		
		rightlist = new JList();
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
		
		currentSemester.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent event){
				currentSemesterStateChange();
			}
		}
		);
		courseHandling handler = new courseHandling();
		markCourse.addActionListener(handler);
		writeFile.addActionListener(handler);
		resetData.addActionListener(handler);
		
		frame.setVisible(true);

	}
	private void currentSemesterStateChange(){
		if (currentSemester.getSelectedItem() == semesterSelection.toArray()[0]){
			leftlist.setListData(coursesCodes.toArray());
		}
		else if (currentSemester.getSelectedItem() == semesterSelection.toArray()[1]){
			leftlist.setListData(y1sm1.toArray());
		}
		else if (currentSemester.getSelectedItem() == semesterSelection.toArray()[2]){
			leftlist.setListData(y1sm2.toArray());
		}
		else if (currentSemester.getSelectedItem() == semesterSelection.toArray()[3]){
			leftlist.setListData(y2sm1.toArray());
		}
		else if (currentSemester.getSelectedItem() == semesterSelection.toArray()[4]){
			leftlist.setListData(y2sm2.toArray());
		}
		else if (currentSemester.getSelectedItem() == semesterSelection.toArray()[5]){
			leftlist.setListData(y3sm1.toArray());
		}
		else if (currentSemester.getSelectedItem() == semesterSelection.toArray()[6]){
			leftlist.setListData(y3sm2.toArray());
		}
		else if (currentSemester.getSelectedItem() == semesterSelection.toArray()[7]){
			leftlist.setListData(y4sm1.toArray());
		}
		else if (currentSemester.getSelectedItem() == semesterSelection.toArray()[8]){
			leftlist.setListData(y4sm2.toArray());
		}
		else if (currentSemester.getSelectedItem() == semesterSelection.toArray()[9]){
			leftlist.setListData(y5sm1.toArray());
		}
		else if (currentSemester.getSelectedItem() == semesterSelection.toArray()[10]){
			leftlist.setListData(y5sm2.toArray());
		}
		rightlist.setListData(takenCourses.toArray());
	}
	private class courseHandling implements ActionListener{
			public void actionPerformed(ActionEvent event){
				if(event.getSource() == markCourse){
					for(Object o: leftlist.getSelectedValues()){
						takenCourses.add(o.toString());
					}
					rightlist.setListData(takenCourses.toArray());
					for(String course: takenCourses){
						try{
						int pos = coursesCodes.indexOf(course);
						coursesCodes.remove(pos);
						if (y1sm1.indexOf(course) != -1){
							y1sm1.remove(y1sm1.indexOf(course));
						}
						else if (y1sm2.indexOf(course) != -1){
							y1sm2.remove(y1sm2.indexOf(course));
						}
						else if (y2sm1.indexOf(course) != -1){
							y2sm1.remove(y2sm1.indexOf(course));
						}
						else if (y2sm2.indexOf(course) != -1){
							y2sm2.remove(y2sm2.indexOf(course));
						}
						else if (y3sm1.indexOf(course) != -1){
							y3sm1.remove(y3sm1.indexOf(course));
						}
						else if (y3sm2.indexOf(course) != -1){
							y3sm2.remove(y3sm2.indexOf(course));
						}
						else if (y4sm1.indexOf(course) != -1){
							y4sm1.remove(y4sm1.indexOf(course));
						}
						else if (y4sm2.indexOf(course) != -1){
							y4sm2.remove(y4sm2.indexOf(course));
						}
						else if (y5sm1.indexOf(course) != -1){
							y5sm1.remove(y5sm1.indexOf(course));
						}
						else{
							y5sm2.remove(y5sm2.indexOf(course));
						}
						takenCredits += coursesCredits.get(pos);
						coursesCredits.remove(pos);
						}catch(Exception e){
							continue;
					}
				}
				creditsRepresentation.setText(String.format("Curriculum Total Credits: %d Taken Credits: %d", totCredits, takenCredits));
				currentSemesterStateChange();
				}
				else if (event.getSource() == writeFile){
					try {
						curriculumProgress = new PrintWriter("My Curriculum Progress.txt");
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(event.getSource() == resetData){
					try {
						setCurriculumData(fileToScan);
						creditsRepresentation.setText(String.format("Curriculum Total Credits: %d Taken Credits: %d", totCredits, takenCredits));
						currentSemesterStateChange();
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(null, "ERROR: Make sure that the curriculum text file is still available.");
						e.printStackTrace();
					}

				}
			}
		}
	private void setCurriculumData(String fileName)throws FileNotFoundException{
		curriculumScan = new Scanner(new File(fileName));
		
		semesterSelection = new LinkedList<>();
		takenCourses = new ArrayList<>();
		coursesCodes = new LinkedList<>();
		coursePositionerList = new LinkedList<>();
		coursesCredits = new LinkedList<>();
		coursesNames = new LinkedList<>();
		y1sm1 = new LinkedList<>();
		y1sm2 = new LinkedList<>();
		y2sm1 = new LinkedList<>();
		y2sm2 = new LinkedList<>();
		y3sm1 = new LinkedList<>();
		y3sm2 = new LinkedList<>();
		y4sm1 = new LinkedList<>();
		y4sm2 = new LinkedList<>();
		y5sm1 = new LinkedList<>();
		y5sm2 = new LinkedList<>();
		totCredits = 0;
		takenCredits = 0;
		coursePositionerToAdd = 0;
		sociocount = 1;
		inglcount = 1;
		
		semesterSelection.add("All Semesters");
		
		while(curriculumScan.hasNext()){
			String line = curriculumScan.nextLine();
			if (line.toUpperCase().startsWith("First".toUpperCase()) || line.toUpperCase().startsWith("Second".toUpperCase()) 
					|| line.toUpperCase().startsWith("Third".toUpperCase()) || line.toUpperCase().startsWith("Fourth".toUpperCase()) 
					|| line.toUpperCase().startsWith("Fifth".toUpperCase())){
				semesterSelection.add(line.substring(0, line.length() - 1));
				coursePositionerList.add(coursePositionerToAdd);
			}
			else if(line.lastIndexOf(":") == line.length()-1){
				continue;
			}
			else if (line.indexOf(":") == -1){
				continue;
			}
			else if (line.startsWith("SOCIO")){
				pos1 = 0;
				pos2 = line.indexOf(":");
				coursesNames.add(line.substring(pos1, pos2));
				coursesCodes.add("SOCIOXXXX" + "[" + sociocount + "]");
				coursesCredits.add(Integer.parseInt(line.substring(line.lastIndexOf("-") + 2, line.lastIndexOf("-") + 3)));
				totCredits += coursesCredits.get(coursesCredits.size() - 1);
				sociocount += 1;
			}
			else if (line.startsWith("English")){
				pos1 = 0;
				pos2 = line.indexOf(":");
				coursesNames.add(line.substring(pos1, pos2));
				coursesCodes.add("INGLXXXX" + "[" + inglcount + "]");
				coursesCredits.add(Integer.parseInt(line.substring(line.indexOf("-") + 2, line.indexOf("-") + 3)));
				totCredits += coursesCredits.get(coursesCredits.size() - 1);
				inglcount += 1;
			}
			else{
				pos1 = line.indexOf(":") + 2;
				pos2 = pos1 + 8;
				coursesNames.add(line.substring(0, pos1 - 2));
				coursesCodes.add(line.substring(pos1, pos2));
				coursesCredits.add(Integer.parseInt(line.substring(line.indexOf("-") + 2, line.indexOf("-") + 3)));
				totCredits += coursesCredits.get(coursesCredits.size() - 1);
			}
			coursePositionerToAdd += 1;
		}
		
		curriculumScan.close();
		
		//Year I: Semester I Courses
			for(Object o: coursesCodes.subList(coursePositionerList.get(0), coursePositionerList.get(1) - 1).toArray()){
				y1sm1.add(o.toString());
			}
		//Year I: Semester II Courses
			for(Object o: coursesCodes.subList(coursePositionerList.get(1) - 1, coursePositionerList.get(2) - 2).toArray()){
				y1sm2.add(o.toString());
			}
		//Year II: Semester I Courses
			for(Object o: coursesCodes.subList(coursePositionerList.get(2) - 2, coursePositionerList.get(3) - 3).toArray()){
				y2sm1.add(o.toString());
			}
		//Year II: Semester II Courses
			for(Object o: coursesCodes.subList(coursePositionerList.get(3) - 3, coursePositionerList.get(4) - 4).toArray()){
				y2sm2.add(o.toString());
			}
		//Year III: Semester I Courses
			for(Object o: coursesCodes.subList(coursePositionerList.get(4) - 4, coursePositionerList.get(5) - 5).toArray()){
				y3sm1.add(o.toString());
			}
		//Year III: Semester II Courses
			for(Object o: coursesCodes.subList(coursePositionerList.get(5) - 5, coursePositionerList.get(6) - 6).toArray()){
				y3sm2.add(o.toString());
			}
		//Year IV: Semester I Courses
			for(Object o: coursesCodes.subList(coursePositionerList.get(6) - 6, coursePositionerList.get(7) - 7).toArray()){
				y4sm1.add(o.toString());
			}
		//Year IV: Semester II Courses
			for(Object o: coursesCodes.subList(coursePositionerList.get(7) - 7, coursePositionerList.get(8) - 8).toArray()){
				y4sm2.add(o.toString());
			}
		//Year V: Semester I Courses
			for(Object o: coursesCodes.subList(coursePositionerList.get(8) - 8, coursePositionerList.get(9) - 9).toArray()){
				y5sm1.add(o.toString());
			}
		//Year V: Semester II Courses
			for(Object o: coursesCodes.subList(coursePositionerList.get(9) - 9, coursesCodes.size()).toArray()){
				y5sm2.add(o.toString());
			}
	}
	}
