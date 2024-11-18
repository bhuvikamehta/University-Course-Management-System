import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class Course {
    private int semester;
    private String code;
    private String title;
    private Professor professor;
    private int credits;
    private List<Course> prerequisites;
    private String schedule;
    private List<Student> enrolledStudents;
    private String syllabus;
    private int enrollmentLimit;
    private String officeHours;
    private List<Feedback<?>> feedbackList;
    private LocalDate addDropDeadline;

    public Course(int semester, String code, String title, int credits, String schedule) {
        this.code = code;
        this.semester=semester;
        this.title = title;
        this.credits = credits;
        this.schedule = schedule;
        this.prerequisites = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.enrollmentLimit = 50; // let initial default limit be 50
        this.officeHours= "not set";
        this.feedbackList=new ArrayList<>();
    }

    public LocalDate getAddDropDeadline(){
        return addDropDeadline;
    }

    public void setAddDropDeadline(LocalDate addDropDeadline) {
        this.addDropDeadline = addDropDeadline;
    }

    public boolean canBeAddedOrDropped(){
        if(addDropDeadline == null){
            return true;
        }
        return LocalDate.now().isBefore(addDropDeadline);
    }

    public void addFeedback(Feedback<?> feedback){
        feedbackList.add(feedback);
    }

    public void viewFeedback(){
        System.out.println("feedback for course: " +title);
        for (Feedback<?> feedback : feedbackList){
            System.out.println(feedback);
        }
    }

    public String getCode(){
        return code; // returns the course code
    }

    public String getTitle() {
        return title; // returns the course title
    }

    public int getCredits() {
        return credits; // returns the credits for the course
    }

    public void setCredits(int credits){
        this.credits=credits; // sets the credits for the course
    }

    public int getSemester(){
        return semester; // returns the semester of the course
    }

    public String getSchedule() {
        return schedule; // returns the schedule of the course
    }

    public void setSchedule(String schedule){
        this.schedule = schedule; // sets the schedule of the course
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents; // returns the list of enrolled students in the course
    }

    public List<Course> getPrerequisites(){
        return prerequisites; // returns the prerequisites of the course
    }

    public void setSyllabus(String syllabus){
        this.syllabus = syllabus; // sets the syllabus for the course
    }

    public void setEnrollmentLimit(int limit){
        this.enrollmentLimit = limit; // sets the enrollment limit for the course
    }

    public int getEnrollmentLimit(){
        return enrollmentLimit; // returns the enrollment limit for the course
    }

    public void addStudent(Student student){
        enrolledStudents.add(student); // registers the student for the course
    }

    public void setOfficeHours(String officeHours){
        this.officeHours=officeHours; // sets the office hours for the course
    }

    public boolean isPrerequisiteMet(List<Grade> completedCourses) {
        // Check if all prerequisites are met
        for (Course prereq : prerequisites) {
            boolean met = false;
            for (Grade grade : completedCourses) {
                if (grade.getCourse().equals(prereq)) { // if the grade is assigned to the prereq course
                    met = true;
                    break;
                }
            }
            if (!met){
                return false;
            }
        }
        return true;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor; // sets the professor for the course
    }

    public Professor getProfessor(){
        return professor; // returns the professor details for the course
    }

    @Override
    public String toString() { // how an object of a course class is represented when it is converted into a string.
        return title + " (" + code + ") - " + credits + " credits"; // eg: Advanced Programming (CSE201) - 4 credits
    }
}
