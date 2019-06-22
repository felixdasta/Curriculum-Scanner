import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EventHandler implements ActionListener, ItemListener, ListSelectionListener{

	private Window window;
	
	public EventHandler(Window window){ this.window = window; }
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		int size = window.getCurriculum().getSemesters().get(window.getSelectedSemester().getSelectedItem()).size(); //the list size;
		window.getLeftList().setListData(window.getCurriculum().getSemesters().get(window.getSelectedSemester().getSelectedItem().toString()).toArray(new Course[size]));
		window.getRightList().setListData(window.getCurriculum().getTakenCourses().get("All Semesters").toArray(new Course[window.getCurriculum().getTakenCourses().get("All Semesters").size()]));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == window.getMarkCourse()){
			ArrayList<String>noPrerequisite = new ArrayList<String>();

			for(Course c: window.getLeftList().getSelectedValuesList()){
				if(window.getCurriculum().hasPrerequisite(c)) { window.getCurriculum().courseGrade(c); }
				else{ noPrerequisite.add(c.toString()); }
			}

			if(noPrerequisite.isEmpty()) {
				window.getTakenCreditsLabel().setText("Taken Credits: " + window.getCurriculum().getTakenCredits());
				window.getGpaLabel().setText(String.format("GPA: %.2f", window.getCurriculum().getGPA()));
			}
			else{ 
				JOptionPane.showMessageDialog(null, "The following courses doesn't have the pre-requisites: " + noPrerequisite.toString());
			}
			itemStateChanged((ItemEvent)window.getSelectedSemester().getAction());
		}
		else if (event.getSource() == window.getWriteCurriculumData()){
			try {
				
				PrintWriter curriculumProgress = new PrintWriter("My Curriculum Progress.txt");
				String name = JOptionPane.showInputDialog("Enter your name:");
				String id = JOptionPane.showInputDialog("Enter your student ID:");
				
				for(int i = 0; i < id.length() || id.isEmpty(); i++){
					if(id.length() != 11 || id.charAt(3) != '-' && id.charAt(6) != '-' || !Character.isDigit(id.charAt(i)) && i != 3 && i != 6){
						id = JOptionPane.showInputDialog("INVALID STUDENT ID - Make sure you type it correctly [Ex: 802-16-9999]:");
						i = -1;
					}
				}

				curriculumProgress.println("STUDENT NAME: " + name);
				curriculumProgress.println("STUDENT ID: " + id);
				curriculumProgress.println(String.format("GPA: %.2f", window.getCurriculum().getGPA()));
				curriculumProgress.println();
				curriculumProgress.println("COURSES TAKEN:");

				for(String currentSemester: window.getCurriculum().getTakenCourses().keySet()){
					if(currentSemester != "All Semesters"){
						curriculumProgress.println();
						curriculumProgress.println(currentSemester + ":");
						for(Course c: window.getCurriculum().getTakenCourses().get(currentSemester)){
							String line = "                                                                      ";

							//Used to replace the first few line blank spaces so the txt file lines matches:
							int length = c.getCourseName().length() + c.getCourseCode().length() + 3;

							line = "COURSE: " + c.getCourseName() + " - " + c.getCourseCode() + line.substring(length)
							+ "LETTER GRADE: " + Character.toString(window.getCurriculum().getGrades().get(c.getCourseCode())); 

							curriculumProgress.println(line);
						}
					}
				}

				curriculumProgress.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else if(event.getSource() == window.getResetCurriculumData()){
			try {
				int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset the current data? This cannot be undone.", "Reset Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if(option == JOptionPane.YES_OPTION){
					window.getCurriculum().setCurriculumData(window.getCurriculum().getTitle() + ".txt");
					window.getTotalCreditsLabel().setText("Total Credits: " + window.getCurriculum().getTotCredits());
					window.getTakenCreditsLabel().setText("Taken Credits: " + window.getCurriculum().getTakenCredits());
					window.getGpaLabel().setText("GPA: N/A");
					itemStateChanged((ItemEvent)window.getSelectedSemester().getAction());
				}

			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "ERROR: Make sure that the curriculum text file is still available.");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		List<Course>leftListSelectedValues = window.getLeftList().getSelectedValuesList();
		List<Course>rightListSelectedValues = window.getRightList().getSelectedValuesList();
		
		//Left list selected values tool tip text:
		if(!leftListSelectedValues.isEmpty()){
			window.getLeftList().setToolTipText("<html>");
			for(Course c: leftListSelectedValues){  window.getLeftList().setToolTipText(window.getLeftList().getToolTipText() + c.getCourseName() + "<br>"); }
			window.getLeftList().setToolTipText(window.getLeftList().getToolTipText() + "</html>");
		}
		//Right list selected values tool tip text:
		else if(!rightListSelectedValues.isEmpty()){
			window.getRightList().setToolTipText("<html>");
			for(Course c: rightListSelectedValues){  window.getRightList().setToolTipText(window.getRightList().getToolTipText() + c.getCourseName() + "<br>"); }
			window.getRightList().setToolTipText(window.getRightList().getToolTipText() + "</html>");
		}else{
			window.getLeftList().setToolTipText(null);
			window.getRightList().setToolTipText(null);
		}
	}
}