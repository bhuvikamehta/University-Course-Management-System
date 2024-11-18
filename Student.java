import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class Student extends User {
    private int semester=1;
    private List<Course> registeredCourses;
    private List<Grade> completedCourses;
    private List<Complaint> complaints;
    private int totalCredits;
    private float cgpa=0;

    public Student(String name, String email, String password) {
        super(name, email, password);
        this.registeredCourses = new ArrayList<>();
        this.completedCourses = new ArrayList<>();
        this.complaints = new ArrayList<>();
        this.totalCredits = 0;
    }

    public void addCompletedCourse(Grade grade){
        completedCourses.add(grade);

        Course completedCourse = grade.getCourse();
        if (registeredCourses.contains(completedCourse)){
            registeredCourses.remove(completedCourse);
        }
        if (registeredCourses.isEmpty()){
            semester++;
        }
    }

    public List<Course> getRegisteredCourses(){
        return registeredCourses;
    }

    public void viewAvailableCourses(List<Course> availableCourses) {
        for (Course course : availableCourses) {
            System.out.println(("For semester: ")+ course.getSemester());
            System.out.println("Course Code: " + course.getCode());
            System.out.println("Title: " + course.getTitle());
            System.out.println("Professor: " + (course.getProfessor() != null ? course.getProfessor().name : "TBA"));
            System.out.println("Credits: " + course.getCredits());

            // Displays the prerequisites
            if (!course.getPrerequisites().isEmpty()) {
                System.out.print("Prerequisites: ");
                for (Course prereq : course.getPrerequisites()) {
                    System.out.print(prereq.getTitle() + " ");
                }
                System.out.println();
            } else {
                System.out.println("Prerequisites: None");
            }

            System.out.println("Timings: " + course.getSchedule());
            System.out.println("--------------------------------");
        }
    }


    public void registerCourse(Course course){
        try{
            if (!course.canBeAddedOrDropped()){
                throw new DropDeadlinePassedException("add/drop deadline has passes.");
            }
        }catch(DropDeadlinePassedException e){
            System.out.println(e.getMessage());
        }

        if (course.getCredits() + totalCredits > 20) {
            System.out.println("Cannot register. Credit limit exceeded.");
            return;
        }

        if (!course.isPrerequisiteMet(this.completedCourses)) {
            System.out.println("Cannot register. Prerequisite not met.");
            return;
        }

        try{
            if (course.getEnrolledStudents().size() >= course.getEnrollmentLimit()){
                throw new CourseFullException("course is full.");
            }
        }catch(CourseFullException e){
            System.out.println(e.getMessage());
        }

        for (Grade grade : completedCourses){
            if (grade.getCourse().getCode().equals(course.getCode())){
                System.out.println("registration unsuccessful. course is already completed.");
                return;
            }
        }

        if (course.getSemester()==semester){
            registeredCourses.add(course);
            course.addStudent(this);
            totalCredits += course.getCredits();
            System.out.println("Registered for course: " + course.getTitle());
        }
        else{
            System.out.println("Register for a course of your semester!");
        }
    }

    public void dropCourse(Course course) {
        // Check if the course is in the current semester and registered
        if (!registeredCourses.contains(course)) {
            System.out.println("You are not registered for this course.");
            return;
        }

        try{
            if(course.canBeAddedOrDropped()){
                registeredCourses.remove(course);
                totalCredits -= course.getCredits();
                System.out.println("Dropped course: " + course.getTitle());
            }
            else{
                throw new DropDeadlinePassedException("add/drop deadline has passed");
            }
        }catch(DropDeadlinePassedException e){
            System.out.println(e.getMessage());
        }


    }

    public void viewSchedule() {
        System.out.println("Weekly Schedule:");
        for (Course course : registeredCourses) {
            System.out.println("course code: "+course.getCode());
            System.out.println("timings & location: "+course.getSchedule());
            System.out.println("professor name: "+(course.getProfessor() != null ? course.getProfessor().name : "TBA"));
        }
    }

    public void viewGrades() {
        if (completedCourses.isEmpty()){
            System.out.println("No course is completed yet.");
            return;
        }
        if (semester != 1){
            System.out.println("Congratulations! Promoted to semester: "+semester);
        }
        System.out.println("Completed Courses:");
        float totalPoints = 0;
        int totalCredits = 0;
        for (Grade grade : completedCourses) {
            System.out.println(grade);
            int courseCredits = grade.getCourse().getCredits();
            totalPoints = totalPoints + (gradetopoints(grade.getLetterGrade()) * courseCredits);
            totalCredits = totalCredits + courseCredits;
        }
        if (registeredCourses.isEmpty()){
            cgpa = totalCredits > 0 ? totalPoints/totalCredits : 0;
            System.out.println("CGPA: "+cgpa);
        }
        else{
            System.out.println("CGPA"+ cgpa);
        }
    }

    private float gradetopoints(String letterGrade){
        switch (letterGrade){
            case "A+": return 10.0f;
            case "A": return 9.0f;
            case "B+": return 8.0f;
            case "B": return 7.0f;
            case "C+": return 6.0f;
            case "C": return 5.0f;
            case "D+": return 4.0f;
            default: return 0.0f;
        }
    }

    public float getCGPA(){
        return cgpa;
    }

    public void submitComplaint(String description) {
        complaints.add(new Complaint(description));
        System.out.println("Complaint submitted.");
    }

    public int getSemester(){
        return semester;
    }

    public List<Grade> getCompletedCourses(){
        return completedCourses;
    }

    public void giveFeedback(Course courseForFeedback, Student student){
        if (completedCourses.stream().noneMatch(grade -> grade.getCourse().equals(courseForFeedback))) {
            System.out.println("invalid course code.");
            return;
        }

        System.out.println("Enter numeric rating: (1-5)");
        int numericRating = Main.scanner.nextInt();
        if (numericRating < 1 || numericRating >5){
            System.out.println("invalid rating");
            return;
        }
        Main.scanner.nextLine();

        System.out.println("Enter textual feedback: ");
        String textualComment = Main.scanner.nextLine();

        Feedback<Integer> numericFeedback = new Feedback<>(numericRating,student);
        Feedback<String> textualFeedback = new Feedback<>(textualComment,student);

        courseForFeedback.addFeedback(numericFeedback);
        courseForFeedback.addFeedback(textualFeedback);
        System.out.println("feedback submitted successfully. ");

    }

    @Override
    public void displayMenu() {
        System.out.println("1. View Available Courses");
        System.out.println("2. Register for Courses");
        System.out.println("3. Drop Courses");
        System.out.println("4. View Schedule");
        System.out.println("5. Track Academic Progress");
        System.out.println("6. Submit Complaint");
        System.out.println("7. Submit Feedback");
        System.out.println("0. Logout");
    }
}
