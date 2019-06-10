import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class CurriculumData{

	private String fileName; //the name of the text file
	private LinkedList<String>coursesCodes; //curriculum courses codes, one of the most valuable arrays that is going to be used for other arrays
	private LinkedList<Integer>coursesCredits; //used to save the credits value for each class
	private LinkedList<String>coursesNames; //used to save the class name
	private LinkedList<String>semesterSelection; //used to select the semester in the jcombo box
	private ArrayList<String>takenCourses; //courses that the individual has taken
	private ArrayList<LinkedList<String>>individualSemesters; //saves the courses codes by semester
	private Map<String, ArrayList<String>> prerequisites; //used to determine if the course can be added as "taken courses"
	private Map<String, Character>grades; //used to save the individual grades gotten in a course
	public int totCredits; //curriculum total credits
	public int takenCredits; //taken credits by the individual
	public int totHonorPoints;	//used to calculate gpa

	public CurriculumData(String fileName)throws FileNotFoundException{
		this.fileName = fileName;
		setCurriculumData(fileName);
	}

	public void setCurriculumData(String fileName)throws FileNotFoundException{
		Scanner curriculumScan = new Scanner(new File(fileName));
		LinkedList<Integer> coursePositionerList = new LinkedList<Integer>();

		init();

		int sociocount = 1;
		int inglcount = 1;
		int coursePositionerToAdd = 0;

		semesterSelection.add("All Semesters");

		while(curriculumScan.hasNext()){
			String line = curriculumScan.nextLine();
			String lastCourseAdded = "";
			if (line.toUpperCase().startsWith("First".toUpperCase()) || line.toUpperCase().startsWith("Second".toUpperCase()) 
					|| line.toUpperCase().startsWith("Third".toUpperCase()) || line.toUpperCase().startsWith("Fourth".toUpperCase()) 
					|| line.toUpperCase().startsWith("Fifth".toUpperCase())){
				semesterSelection.add(line.substring(0, line.length() - 1));
				coursePositionerList.add(coursePositionerToAdd);
				individualSemesters.add(new LinkedList<String>());
			}
			else if(line.lastIndexOf(":") == line.length()-1 || !line.contains(":")){
				continue;
			}
			else if (line.startsWith("SOCIO")){
				int pos1 = 0;
				int pos2 = line.indexOf(":");
				coursesNames.add(line.substring(pos1, pos2));
				coursesCodes.add("SOCIOXXXX" + "[" + sociocount + "]");
				coursesCredits.add(Integer.parseInt(line.substring(line.lastIndexOf("-") + 2, line.lastIndexOf("-") + 3)));
				totCredits += coursesCredits.get(coursesCredits.size() - 1);
				lastCourseAdded = "SOCIOXXXX" + "[" + sociocount + "]";
				sociocount++;
			}
			else if (line.startsWith("English")){
				int pos1 = 0;
				int pos2 = line.indexOf(":");
				coursesNames.add(line.substring(pos1, pos2));
				coursesCodes.add("INGLXXXX" + "[" + inglcount + "]");
				coursesCredits.add(Integer.parseInt(line.substring(line.indexOf("-") + 2, line.indexOf("-") + 3)));
				totCredits += coursesCredits.get(coursesCredits.size() - 1);
				lastCourseAdded = "INGLXXXX" + "[" + inglcount + "]";
				inglcount++;
			}
			else{
				int pos1 = line.indexOf(":") + 2;
				int pos2 = pos1 + 8;
				coursesNames.add(line.substring(0, pos1 - 2));
				coursesCodes.add(line.substring(pos1, pos2));
				coursesCredits.add(Integer.parseInt(line.substring(line.indexOf("-") + 2, line.indexOf("-") + 3)));
				totCredits += coursesCredits.get(coursesCredits.size() - 1);
				lastCourseAdded = line.substring(pos1, pos2);
			}
			coursePositionerToAdd++;

			if(!line.contains("NONE") && !lastCourseAdded.isEmpty()){
				prerequisites.put(lastCourseAdded, new ArrayList<String>());
				if(line.endsWith(")")){prerequisites.get(lastCourseAdded).add(line.substring(line.length()-22, line.length()-14));}
				else {prerequisites.get(lastCourseAdded).add(line.substring(line.length()-8));}
				while(line.contains(",") || line.contains("&")){
					if(line.lastIndexOf(",") > line.lastIndexOf("&")) line = line.substring(0, line.lastIndexOf(","));
					else line = line.substring(0, line.lastIndexOf("&") - 1); 
					
					if(line.endsWith(")")){prerequisites.get(lastCourseAdded).add(line.substring(line.length()-22, line.length()-14));}
					else {prerequisites.get(lastCourseAdded).add(line.substring(line.length()-8));}
				}
			}
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
		prerequisites = new HashMap<String, ArrayList<String>>();
		grades = new HashMap<String, Character>();
		totCredits = 0;
		takenCredits = 0;
		totHonorPoints = 0;
	}

	public boolean hasPrerequisite(String course){
		if(!prerequisites.containsKey(course)){
			return true;
		}
		else{
			for(String c: prerequisites.get(course)){
				if(!takenCourses.contains(c)) return false;
			}return true;
		}
	}

	public int courseGrade(String course){
		int honorPoints = -1;
		String letterGrade = "";
		while(!(honorPoints >= 0 && honorPoints <= 4)){
			letterGrade = JOptionPane.showInputDialog(null, "Enter the grade you got in " + course + ':', "Course Grade");
			if(letterGrade.equals("A")) honorPoints = 4;
			else if(letterGrade.equals("B")) honorPoints = 3;
			else if(letterGrade.equals("C")) honorPoints = 2;
			else if(letterGrade.equals("D")) honorPoints = 1;
			else if(letterGrade.equals("F")) honorPoints = 0;
			else JOptionPane.showMessageDialog(null, "ERROR: Make sure you enter a valid grade (A, B, C, D or F)");
		}
		grades.put(course, letterGrade.charAt(0));
		return honorPoints;
	}

	//Getters:
	public double getGPA(){ return (double)totHonorPoints/(double)takenCredits; }
	public String getFileName() { return fileName; }
	public LinkedList<String> getCoursesCodes() { return coursesCodes; }
	public LinkedList<Integer> getCoursesCredits() { return coursesCredits; }
	public LinkedList<String> getCoursesNames() { return coursesNames; }
	public LinkedList<String> getSemesterSelection() { return semesterSelection; }
	public Map<String, Character> getGrades() { return grades; }
	public ArrayList<String> getTakenCourses() { return takenCourses; }
	public ArrayList<LinkedList<String>> getIndividualSemesters() { return individualSemesters; }
}
