import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Professor extends User {
    private List<Course> courses;

    public Professor(String name, String email, String password) {
        super(name, email, password);
        this.courses = new ArrayList<>();
    }

    public void addCourse(Course course){
        this.courses.add(course); //adds the assigned course to professor into courses list
    }

    public List<Course> getCourses(){
        return courses;
    }

    public void manageCourses() {
        Scanner scanner= new Scanner(System.in);
        System.out.println("Manage Courses:");
        if (courses.isEmpty()) {
            System.out.println("No courses assigned.");
            return;
        }
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getTitle());
        }

        System.out.print("Select course to manage (Enter number): ");
        int courseIndex = Main.scanner.nextInt();
        Main.scanner.nextLine();

        if (courseIndex < 1 || courseIndex > courses.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Course course = courses.get(courseIndex - 1);
        System.out.println("Managing course: " + course.getTitle());

        while (true) {
            System.out.println("1. Update Syllabus");
            System.out.println("2. Update Class Timings or location");
            System.out.println("3. Update Credits");
            System.out.println("4. Update Prerequisites");
            System.out.println("5. Update Enrollment Limit");
            System.out.println("6. Update Office Hours");
            System.out.println("7. Assign Grades");
            System.out.println("0. Back to previous menu");

            int choice = Main.scanner.nextInt();
            Main.scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new syllabus: ");
                    String newSyllabus = Main.scanner.nextLine();
                    course.setSyllabus(newSyllabus);
                    System.out.println("Syllabus updated.");
                }
                case 2 -> {
                    System.out.print("Enter new class timings & location: ");
                    String newSchedule = Main.scanner.nextLine();
                    course.setSchedule(newSchedule);
                    System.out.println("Class timings & location updated.");
                }
                case 3 -> {
                    System.out.print("Enter new credits: ");
                    int newCredits = Main.scanner.nextInt();
                    Main.scanner.nextLine();
                    course.setCredits(newCredits);
                    System.out.println("Credits updated.");
                }
                case 4 -> {
                    System.out.println("Current prerequisites:");
                    for (Course prereq : course.getPrerequisites()) {
                        System.out.println(prereq.getTitle());
                    }
                    System.out.println("No further functionality for updating prerequisites in this version.");
                }
                case 5 -> {
                    System.out.print("Enter new enrollment limit: ");
                    int newLimit = Main.scanner.nextInt();
                    Main.scanner.nextLine();
                    course.setEnrollmentLimit(newLimit);
                    System.out.println("Enrollment limit updated.");
                }
                case 6 -> {
                    System.out.print("Enter new office hours: ");
                    String newOfficeHours = Main.scanner.nextLine();
                    course.setOfficeHours(newOfficeHours);
                    System.out.println("Office hours updated.");
                }
                case 7 -> {
                    if (course.getEnrolledStudents().isEmpty()){
                        System.out.println("no student is currently enrolled in this course.");
                        return;
                    }
                    System.out.println("enter student email you want to assign grade to: ");
                    String gradestudent = Main.scanner.nextLine();
                    Student tograde = null;
                    for (Student student : Main.students) {
                        if (student.email.equals(gradestudent)) {
                            tograde = student;
                            break;
                        }
                    }
                    if (tograde==null){
                        System.out.println("enter valid email address.");
                    }

                    System.out.println("enter grade: ");
                    String assignedgrade = Main.scanner.nextLine();

                    if (!isValidGrade(assignedgrade)) {
                        System.out.println("please enter a valid grade");
                        return;
                    }

                    Grade grade = new Grade(course, assignedgrade);
                    assert tograde != null;
                    if (assignedgrade != "F") {
                        tograde.addCompletedCourse(grade);
                    }
                    System.out.println("grades assigned successfully");

                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }
    private boolean isValidGrade(String grade){
        return grade.equals("A+") || grade.equals("A") || grade.equals("B+") || grade.equals("B") || grade.equals("C+") || grade.equals("C") || grade.equals("D+") || grade.equals("F");
    }

    public void viewEnrolledStudents() {

        if (courses.isEmpty()) {
            System.out.println("No courses assigned.");
            return;
        }
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getTitle());
        }

        System.out.print("Select course (Enter number): ");
        int courseIndex = Main.scanner.nextInt();
        Main.scanner.nextLine();
        if (courseIndex < 1 || courseIndex > courses.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Course course = courses.get(courseIndex - 1);
        System.out.println("viewing enrolled students in: " + course.getTitle());

        if (course == null){
            System.out.println("course not found.");
            return;
        }
        if (course.getEnrolledStudents().isEmpty()){
            System.out.println("no student is currently enrolled in this course.");
            return;
        }

        System.out.println("Enrolled Students in " + course.getTitle() + ":");
        for (Student student : course.getEnrolledStudents()) {
            System.out.println("Name: " + student.name);
            System.out.println("Email: " + student.email);
            System.out.println("Current Semester " + student.getSemester());
            System.out.println("current cgpa: "+ student.getCGPA());
            System.out.println("-----------------------------");
        }
    }

    public void viewFeedback(Course course){
        for (Course courses : courses){
            if (courses.equals(course)){
                break;
            }
            System.out.println("invalid course code");
            return;
        }
        course.viewFeedback();
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Manage Courses");
        System.out.println("2. View Enrolled Students");
        System.out.println("3. View Feedback");
        System.out.println("0. Logout");
    }
}

