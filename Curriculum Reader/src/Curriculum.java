import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Curriculum{

	private String title; //the name of the text file
	private Map<String, LinkedList<Course>>takenCourses; //courses that the student has taken
	private Map<String, LinkedList<Course>>semesters; //used to select the semester in the jcombo box
	private Map<Course, ArrayList<String>> prerequisites; //used to determine if the course can be added as "taken courses"
	private Map<String, Character>grades; //used to save the grades that the student have obtained in the courses
	private int totCredits; //curriculum total credits
	private int takenCredits; //taken credits by the individual
	private int totHonorPoints;	//used to calculate GPA

	public Curriculum(String fileName)throws FileNotFoundException{
		this.title = fileName.substring(0, fileName.indexOf("."));
		setCurriculumData(fileName);
	}

	public void setCurriculumData(String fileName)throws FileNotFoundException{
		Scanner curriculumScan = new Scanner(new File(fileName));

		init();

		int sociocount = 0;
		int inglcount = 0;

		String lastAddedSemester = "";
		semesters.put("All Semesters", new LinkedList<Course>());
		takenCourses.put("All Semesters", new LinkedList<Course>());

		while(curriculumScan.hasNext()){
			String line = curriculumScan.nextLine();
			Course lastCourseAdded = null;

			if (line.toUpperCase().startsWith("First".toUpperCase()) || line.toUpperCase().startsWith("Second".toUpperCase()) 
					|| line.toUpperCase().startsWith("Third".toUpperCase()) || line.toUpperCase().startsWith("Fourth".toUpperCase()) 
					|| line.toUpperCase().startsWith("Fifth".toUpperCase())){
				lastAddedSemester = line.substring(0, line.length() - 1);
				semesters.put(lastAddedSemester, new LinkedList<Course>());
				takenCourses.put(lastAddedSemester, new LinkedList<Course>());
				continue;
			}
			else if(line.endsWith(":") || !line.contains(":")){
				continue;
			}
			else if (line.startsWith("SOCIO")){
				int pos1 = 0;
				int pos2 = line.indexOf(":");
				lastCourseAdded = new Course("SOCIOXXXX" + "[" + ++sociocount + "]", line.substring(pos1, pos2), 
						Integer.parseInt(line.substring(line.lastIndexOf("-") + 2, line.lastIndexOf("-") + 3)));
			}
			else if (line.startsWith("English")){
				int pos1 = 0;
				int pos2 = line.indexOf(":");
				lastCourseAdded = new Course("INGLXXXX" + "[" + ++inglcount + "]", line.substring(pos1, pos2), 
						Integer.parseInt(line.substring(line.indexOf("-") + 2, line.indexOf("-") + 3)));
			}
			else{
				int pos1 = line.indexOf(":") + 2;
				int pos2 = pos1 + 8;
				lastCourseAdded = new Course(line.substring(pos1, pos2), line.substring(0, pos1 - 2),
						Integer.parseInt(line.substring(line.indexOf("-") + 2, line.indexOf("-") + 3)));
			}

			semesters.get("All Semesters").add(lastCourseAdded);
			semesters.get(lastAddedSemester).add(lastCourseAdded);
			totCredits += lastCourseAdded.getCredits();

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
		this.takenCourses = new LinkedHashMap<String, LinkedList<Course>>();
		this.semesters = new LinkedHashMap<String, LinkedList<Course>>();
		this.prerequisites = new HashMap<Course, ArrayList<String>>();
		this.grades = new HashMap<String, Character>();
		this.totCredits = 0;
		this.takenCredits = 0;
		this.totHonorPoints = 0;
	}

	/** Returns true if the individual has the prerequisite of the course and false if the previous
	 *  statement is not true. **/
	public boolean hasPrerequisite(Course course){
		if(!prerequisites.containsKey(course)){
			return true;
		}
		else{
			for(String c: prerequisites.get(course)){
				//If the student hasn't taken the course or if the student took it and got an F, then he/she doesn't have the prerequisite
				if(!grades.containsKey(c) || grades.get(c) == 'F') return false;
			}return true;
		}
	}

	/**Saves the grade gotten in the course and also removes it from the list of courses to take. Course gets added
	 * to the list as taken courses. Returns the previous grade of the course if it was repeated or null if it's the
	 * first time that the individual is taking the course. **/
	public Character courseGrade(Course course){
		int honorPoints = -1;
		String letterGrade = "";

		while(!(honorPoints >= 0 && honorPoints <= 4)){
			letterGrade = (String) JOptionPane.showInputDialog(null, "Enter the grade you got in " + course + ':', "Grade", JOptionPane.PLAIN_MESSAGE,
					new ImageIcon("res/Images/icon3.png"), null, "");
			try{
				if(letterGrade.equals("A")) honorPoints = 4;
				else if(letterGrade.equals("B")) honorPoints = 3;
				else if(letterGrade.equals("C")) honorPoints = 2;
				else if(letterGrade.equals("D")) honorPoints = 1;
				else if(letterGrade.equals("F")) honorPoints = 0;
				else JOptionPane.showMessageDialog(null, "ERROR: Make sure you enter a valid grade (A, B, C, D or F)");
			}catch(Exception e){
				return '!';
			}
		}

		this.totHonorPoints += honorPoints * course.getCredits();
		this.takenCredits += course.getCredits();


		for(String currentSemester: this.getSemesters().keySet()){ 
			/*If a particular course has been removed from a semester (due to the fact that the course has been taken),
			then add the course as a taken course in the specific semester. */
			if(this.semesters.get(currentSemester).remove(course)){
				this.takenCourses.get(currentSemester).add(course);
			}
		}

		return grades.put(course.getCourseCode(), letterGrade.charAt(0));
	}

	//Getters:
	public double getGPA(){ return (double)totHonorPoints/(double)takenCredits; }
	public String getTitle() { return title; }
	public Map<String, LinkedList<Course>> getSemesters() { return semesters; }
	public Map<String, Character> getGrades() { return grades; }
	public int getTotCredits() { return totCredits; }
	public int getTakenCredits() { return takenCredits; }
	public Map<String, LinkedList<Course>> getTakenCourses() { return takenCourses; }
}
