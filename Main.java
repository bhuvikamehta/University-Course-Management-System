import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    private static List<Course> courseCatalog = new ArrayList<>();
    public static List<Student> students = new ArrayList<>();
    private static List<Professor> professors = new ArrayList<>();
    private static List<Complaint> complaints = new ArrayList<>();
    private static List<Administrator> administrators = new ArrayList<>();
    private static List<TA> tas = new ArrayList<>();


    public static void main(String[] args) {
        // Initialize with hard coded data
        initializeData();

        while (true) {
            //displays main menu
            System.out.println("Welcome to the University Course Registration System");
            System.out.println("1. Login as Student");
            System.out.println("2. Login as Professor");
            System.out.println("3. Login as Administrator");
            System.out.println("4. Login as TA");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> studentLogin();
                case 2 -> professorLogin();
                case 3 -> adminLogin();
                case 4 -> taLogin();
                case 0 -> {
                    System.out.println("Exiting application...");
                    return;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void studentLogin(){
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // checks whether student details are correct
        Student student = findStudentByEmail(email);

        try{
            if (student != null && student.password.equals(password)){
                studentMenu(student);
            }
            else{
                throw new InvalidLoginException("invalid email or password");
            }
        } catch(InvalidLoginException e){
            System.out.println(e.getMessage());
        }
    }

    private static void professorLogin(){
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // checks whether prof details are correct
        Professor professor = findProfessorByEmail(email);

        try{
            if (professor != null && professor.password.equals(password)){
                professorMenu(professor);
            }
            else{
                throw new InvalidLoginException("invalid email or password.");
            }
        } catch(InvalidLoginException e){
            System.out.println(e.getMessage());
        }
    }

    private static void adminLogin() {
        System.out.print("Enter admin email: ");
        String email = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        // checks whether admin details are correct
        Administrator administrator = findAdministratorByEmail(email);

        try{
            if(administrator != null && administrator.password.equals(password)){
                adminMenu(administrator);
            }
            else{
                throw new InvalidLoginException("invalid email or password.");
            }
        } catch(InvalidLoginException e){
            System.out.println(e.getMessage());
        }
    }

    private static void taLogin(){
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // checks whether TA details are correct
        TA ta = findTaByEmail(email);

        try{
            if (ta != null && ta.password.equals(password)){
                taMenu(ta);
            }
            else{
                throw new InvalidLoginException("invalid email or password");
            }
        } catch(InvalidLoginException e){
            System.out.println(e.getMessage());
        }
    }

    private static void studentMenu(Student student){
        while (true) {
            student.displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> student.viewAvailableCourses(courseCatalog);
                case 2 -> {
                    System.out.print("Enter course code to register: ");
                    String code = scanner.nextLine();
                    Course course = findCourseByCode(code);
                    if (course != null) {
                        student.registerCourse(course);
                    } else {
                        System.out.println("Course not found.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter course code to drop: ");
                    String code = scanner.nextLine();
                    Course course = findCourseByCode(code);
                    if (course != null) {
                        student.dropCourse(course);
                    } else {
                        System.out.println("Course not found.");
                    }
                }
                case 4 -> student.viewSchedule();
                case 5 -> student.viewGrades();
                case 6 -> {
                    System.out.print("Enter complaint description: ");
                    String description = scanner.nextLine();
                    student.submitComplaint(description);
                }
                case 7 -> {
                    if (student.getCompletedCourses().isEmpty()){
                        System.out.println("No courses completed yet.");
                    } else {
                        System.out.println("Choose the course code from the following:");
                        int i = 0;
                        for (Grade grade : student.getCompletedCourses()) {
                            Course course = grade.getCourse();
                            System.out.println((i + 1) + ". " + course.getCode());
                            i++;
                        }
                        String coursechosen = scanner.nextLine();

                        Course courseForFeedback = findCourseByCode(coursechosen);
                        student.giveFeedback(courseForFeedback, student);
                    }
                }
                case 0 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void professorMenu(Professor professor) {
        while (true) {
            professor.displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> professor.manageCourses();
                case 2 -> professor.viewEnrolledStudents();
                case 3 -> {
                    if (professor.getCourses().isEmpty()) {
                        System.out.println("You are not assigned any courses.");
                        return;
                    }
                    System.out.println("Choose the course code from the following:");
                    int i = 0;
                    for (Course course : professor.getCourses()) {
                        System.out.println((i + 1) + ". " + course.getCode() + " - " + course.getTitle());
                        i++;
                    }
                    String coursecode = scanner.nextLine();
                    Course course = findCourseByCode(coursecode);
                    professor.viewFeedback(course);
                }
                case 0 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void adminMenu(Administrator admin) {
        while (true) {
            admin.displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> admin.manageCourseCatalog(courseCatalog);
                case 2 -> admin.manageStudentRecords(students);
                case 3 -> {
                    System.out.print("Enter course code to assign professor: ");
                    String code = scanner.nextLine();
                    Course course = findCourseByCode(code);
                    if (course != null) {
                        System.out.print("Enter professor email: ");
                        String email = scanner.nextLine();
                        Professor professor = findProfessorByEmail(email);
                        if (professor != null) {
                            admin.assignProfessorToCourse(course, professor);
                        } else {
                            System.out.println("Professor not found.");
                        }
                    } else {
                        System.out.println("Course not found.");
                    }
                }
                case 4 -> admin.handleComplaints(complaints);
                case 5 -> {
                    System.out.println("enter semester to set deadline for: ");
                    int semester = scanner.nextInt();
                    scanner.nextLine();
                    admin.setDropDeadlineForCourses(semester,courseCatalog);
                }
                case 0 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void taMenu(TA ta){
        while(true){
            ta.displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                //student functionalities
                case 1 -> ta.viewAvailableCourses(courseCatalog);
                case 2 -> {
                    System.out.print("Enter course code to register: ");
                    String code = scanner.nextLine();
                    Course course = findCourseByCode(code);
                    if (course != null) {
                        ta.registerCourse(course);
                    } else {
                        System.out.println("Course not found.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter course code to drop: ");
                    String code = scanner.nextLine();
                    Course course = findCourseByCode(code);
                    if (course != null) {
                        ta.dropCourse(course);
                    } else {
                        System.out.println("Course not found.");
                    }
                }
                case 4 -> ta.viewSchedule();
                case 5 -> ta.viewGrades();
                case 6 -> {
                    System.out.print("Enter complaint description: ");
                    String description = scanner.nextLine();
                    ta.submitComplaint(description);
                }
                case 7 -> {
                    if (ta.getCompletedCourses().isEmpty()){
                        System.out.println("No courses completed yet.");
                    } else {
                        System.out.println("Choose the course code from the following:");
                        int i = 0;
                        for (Grade grade : ta.getCompletedCourses()) {
                            Course course = grade.getCourse();
                            System.out.println((i + 1) + ". " + course.getCode());
                            i++;
                        }
                        String coursechosen = scanner.nextLine();

                        Course courseForFeedback = findCourseByCode(coursechosen);
                        ta.giveFeedback(courseForFeedback, ta);
                    }
                }
                //ta functionalities
                case 8 -> ta.viewGradesForCourse();
                case 9-> ta.assignGrade();
                case 10-> ta.editGrade();
                case 0-> {
                    System.out.println("logging out...");
                    return;
                }
                default -> System.out.println("invalid choice, please try again");
            }
        }
    }

    static Student findStudentByEmail(String email) {
        for (Student student : students) {
            if (student.email.equals(email)) {
                return student;
            }
        }
        return null;
    }

    private static Professor findProfessorByEmail(String email) {
        for (Professor professor : professors) {
            if (professor.email.equals(email)) {
                return professor;
            }
        }
        return null;
    }

    private static Administrator findAdministratorByEmail(String email){
        for (Administrator administrator : administrators ){
            if (administrator.email.equals(email)){
                return administrator;
            }
        }
        return null;
    }

    private static TA findTaByEmail(String email){
        for (TA ta : tas){
            if (ta.email.equals(email)){
                return ta;
            }
        }
        return null;
    }

    public static Course findCourseByCode(String code) {
        for (Course course : courseCatalog) {
            if (course.getCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

    private static void initializeData() {

        Course course1 = new Course(1,"CSE101", "Introduction to Programming", 4, "Mon 9am - 11am in C101");
        Course course2 = new Course(1,"MTH100", "Linear Algebra", 4, "Tue 10am - 12pm in C102");
        Course course3 = new Course(1,"COM101","Introduction to Communication",4,"Fri 3pm - 4:30pm in C01" );
        Course course4 = new Course(2,"CSE201","Data Structures & Algorithms",4,"Mon 3pm - 5pm in C201");
        Course course5 = new Course(2,"ECE113","Basic Electronics",4,"Tue 9am - 11am in C101");
        Course course6 = new Course(2,"CSE112","Computer Organisation",4,"Thrus 1pm - 3pm in C101");

        courseCatalog.add(course1);
        courseCatalog.add(course2);
        courseCatalog.add(course3);
        courseCatalog.add(course4);
        courseCatalog.add(course5);
        courseCatalog.add(course6);

        Professor prof1 = new Professor("bn jain", "bnjain@iiit.in", "1234");
        professors.add(prof1);
        Professor prof2 = new Professor("subhajit","subhajit@iiit.in","1234");
        professors.add(prof2);

        Student student1 = new Student("dhruv", "dhruv@iiit.in", "1234");
        students.add(student1);
        Student student2 = new Student("kashvi", "kashvi@iiit.in", "1234");
        students.add(student2);
        Student student3 = new Student("vibha","vibha@iiit.in","1234");
        students.add(student3);

        Administrator admn1 = new Administrator("admin","admin@iiit.in","1234");
        administrators.add(admn1);

        complaints.add(new Complaint("Schedule clash between CSE101 and MTH100"));

        TA ta1 = new TA("gulshan","gulshan@iiit.in","1234", "CSE101");
        tas.add(ta1);
        TA ta2 = new TA("janya","janya@iiit.in","1234", "MTH100");
        tas.add(ta2);
    }
}
