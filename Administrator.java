import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

class Administrator extends User {

    public Administrator(String name, String email,String password) {
        super(name, email, password);
    }

    public void manageCourseCatalog(List<Course> courseCatalog) {
        System.out.println("Manage Course Catalog:");
        System.out.println("1. view all courses");
        System.out.println("2. add courses");
        System.out.println("3. delete courses");

        int choice = Main.scanner.nextInt();
        Main.scanner.nextLine();

        switch (choice){
            case 1 -> {
                for (Course course : courseCatalog) {
                    System.out.println(course);
                }
            }
            case 2 -> {
                // input all the course details to add a new course
                System.out.println("enter course code: ");
                String coursecode= Main.scanner.nextLine();
                System.out.println("enter course title: ");
                String coursetitle = Main.scanner.nextLine();
                System.out.println("enter course credits: ");
                int coursecredits= Main.scanner.nextInt();
                Main.scanner.nextLine();
                System.out.println("enter course timings & location: ");
                String courseschedule = Main.scanner.nextLine();
                System.out.println("Enter the semester for this course: ");
                int coursesemester = Main.scanner.nextInt();
                Main.scanner.nextLine();

                Course newcourse = new Course(coursesemester,coursecode,coursetitle,coursecredits,courseschedule);
                courseCatalog.add(newcourse);
                System.out.println("course added successfully");
            }
            case 3 -> {
                System.out.println("enter course code: ");
                String coursecodedel= Main.scanner.nextLine();

                Course courseToDelete = null;
                for (Course course : courseCatalog) {
                    if (course.getCode().equals(coursecodedel)) {
                        courseToDelete = course;
                        break;
                    }
                }
                if (courseToDelete != null) {
                    courseCatalog.remove(courseToDelete);
                    System.out.println("Course " + coursecodedel + " deleted successfully.");
                } else {
                    System.out.println("Course not found.");
                }
            }
        }
    }

    public void manageStudentRecords(List<Student> students) {
        System.out.println("Manage Student Records:");
        System.out.println("1. view student records");
        System.out.println("2. update student grades");

        int choice = Main.scanner.nextInt();
        Main.scanner.nextLine();

        switch(choice){
            case 1 -> {
                for (Student student : students) {
                    //prints student records
                    System.out.println("name: "+student.name);
                    System.out.println("contact: "+student.email);
                    System.out.println("current semester: "+ student.getSemester());
                    System.out.println("-----------------------------");
                }
            }
            case 2 -> {
                //assigns grades to students
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
                System.out.println("Enter course code to assign or update grade for: ");
                String courseCode = Main.scanner.nextLine();
                Course selectedCourse = null;
                for (Course course : tograde.getRegisteredCourses()) {
                    if (course.getCode().equals(courseCode)) {
                        selectedCourse = course;
                        break;
                    }
                }
                if (selectedCourse == null) {
                    System.out.println("Course not found for this student!");
                    return;
                }
                System.out.println("enter grade: ");
                String assignedgrade = Main.scanner.nextLine();

                if (!isValidGrade(assignedgrade)) {
                    System.out.println("please enter a valid grade");
                    return;
                }

                Grade grade = new Grade(selectedCourse, assignedgrade);
                assert tograde != null;
                if (assignedgrade!="F") {
                    tograde.addCompletedCourse(grade);
                }
                System.out.println("grades assigned successfully");
            }
        }
    }
    private boolean isValidGrade(String grade){
        return grade.equals("A+") || grade.equals("A") || grade.equals("B+") || grade.equals("B") || grade.equals("C+") || grade.equals("C") || grade.equals("D+") || grade.equals("F");
    }

    public void assignProfessorToCourse(Course course, Professor professor) {
        course.setProfessor(professor);
        professor.addCourse(course);
        System.out.println("Assigned " + professor.name + " to " + course.getTitle());
    }

    public void handleComplaints(List<Complaint> complaints) {
        //sort complaints based on their status (pending or resolved)
        complaints.sort((c1,c2) -> c1.complaintStatus().compareTo(c2.complaintStatus()));

        System.out.println("Handle Complaints:");
        for (Complaint complaint : complaints) {
            System.out.println(complaint);

            if(Objects.equals(complaint.complaintStatus(),"Pending")){
                System.out.println("enter status: (Pending or Resolved)");
                String complaintstatus = Main.scanner.nextLine();

                if (Objects.equals(complaintstatus, "Pending")){
                    complaint.pending();
                }
                if (Objects.equals(complaintstatus,"Resolved")){
                    complaint.resolve();
                    System.out.println("Complaint resolved successfully.");
                }
            }
        }
    }


    public void setDropDeadlineForCourses(int semester, List<Course> courseCatalog){
        System.out.println("enter drop deadline (yyyy-mm-dd): ");
        String dateinput = Main.scanner.nextLine();
        try{
            LocalDate deadline = LocalDate.parse(dateinput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            boolean coursesfound = false;
            for (Course course : courseCatalog){
                if (course.getSemester()==semester){
                    course.setAddDropDeadline(deadline);
                    coursesfound=true;
                }
            }
            if (coursesfound){
                System.out.println("add/drop deadline set.");
            }
            else{
                System.out.println("no courses found.");
            }
        } catch(DateTimeParseException e) {
            System.out.println("invalid date format. use yyyy-MM-dd.");
        }
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Manage Course Catalog");
        System.out.println("2. Manage Student Records");
        System.out.println("3. Assign Professors to Courses");
        System.out.println("4. Handle Complaints");
        System.out.println("5. Set Drop Deadline");
        System.out.println("0. Logout");
    }
}
