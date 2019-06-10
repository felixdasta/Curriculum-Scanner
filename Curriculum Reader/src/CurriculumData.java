import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class CurriculumData{

	private String fileName; //the name of the text file
	private LinkedList<Integer>coursesCredits; //used to save the credits value for each class
	private LinkedList<String>coursesNames; //used to save the class name
	private ArrayList<String>takenCourses; //courses that the individual has taken
	private Map<String, LinkedList<String>>semesters; //used to select the semester in the jcombo box
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

		init();

		int sociocount = 1;
		int inglcount = 1;

		String lastAddedSemester = "";
		semesters.put("All Semesters", new LinkedList<String>());

		while(curriculumScan.hasNext()){
			String line = curriculumScan.nextLine();
			String lastCourseAdded = "";
			
			if (line.toUpperCase().startsWith("First".toUpperCase()) || line.toUpperCase().startsWith("Second".toUpperCase()) 
					|| line.toUpperCase().startsWith("Third".toUpperCase()) || line.toUpperCase().startsWith("Fourth".toUpperCase()) 
					|| line.toUpperCase().startsWith("Fifth".toUpperCase())){
				lastAddedSemester = line.substring(0, line.length() - 1);
				semesters.put(lastAddedSemester, new LinkedList<String>());
				continue;
			}
			else if(line.endsWith(":") || !line.contains(":")){
				continue;
			}
			else if (line.startsWith("SOCIO")){
				int pos1 = 0;
				int pos2 = line.indexOf(":");
				lastCourseAdded = "SOCIOXXXX" + "[" + sociocount + "]";
				coursesNames.add(line.substring(pos1, pos2));
				coursesCredits.add(Integer.parseInt(line.substring(line.lastIndexOf("-") + 2, line.lastIndexOf("-") + 3)));
				sociocount++;
			}
			else if (line.startsWith("English")){
				int pos1 = 0;
				int pos2 = line.indexOf(":");
				lastCourseAdded = "INGLXXXX" + "[" + inglcount + "]";
				coursesNames.add(line.substring(pos1, pos2));
				coursesCredits.add(Integer.parseInt(line.substring(line.indexOf("-") + 2, line.indexOf("-") + 3)));
				inglcount++;
			}
			else{
				int pos1 = line.indexOf(":") + 2;
				int pos2 = pos1 + 8;
				lastCourseAdded = line.substring(pos1, pos2);
				coursesNames.add(line.substring(0, pos1 - 2));
				coursesCredits.add(Integer.parseInt(line.substring(line.indexOf("-") + 2, line.indexOf("-") + 3)));
			}
			
			semesters.get("All Semesters").add(lastCourseAdded);
			semesters.get(lastAddedSemester).add(lastCourseAdded);
			totCredits += coursesCredits.get(coursesCredits.size() - 1);

			if(!line.contains("NONE")){
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
	}

	private void init(){
		takenCourses = new ArrayList<>();
		coursesCredits = new LinkedList<>();
		coursesNames = new LinkedList<>();
		semesters = new LinkedHashMap<String, LinkedList<String>>();
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
				if(!grades.containsKey(c) || grades.get(c) == 'F') return false;
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
	public LinkedList<Integer> getCoursesCredits() { return coursesCredits; }
	public LinkedList<String> getCoursesNames() { return coursesNames; }
	public Map<String, LinkedList<String>> getSemesters() { return semesters; }
	public Map<String, Character> getGrades() { return grades; }
	public ArrayList<String> getTakenCourses() { return takenCourses; }
}
