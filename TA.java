class TA extends Student {
    Course course;

    public TA(String name, String email, String password, String course1) {
        super(name, email, password);
        this.course = Main.findCourseByCode(course1);
    }

    public void viewGradesForCourse() {
        System.out.println("Viewing grades for course: " + course.getTitle());
        boolean gradesFound = false;
        for (Student student : course.getEnrolledStudents()) {
            for (Grade grade : student.getCompletedCourses()) {
                if (grade.getCourse().equals(course)) {
                    gradesFound=true;
                    System.out.println(student.getName() + ": " + grade.getLetterGrade());
                }
            }
        }
        if (!gradesFound){
            System.out.println("no grades assigned yet.");
        }
    }

    public void assignGrade() {
        if (course.getEnrolledStudents().isEmpty()){
            System.out.println("no student is currently enrolled in this course.");
            return;
        }
        System.out.println("enter student email you want to assign grade to: ");
        String gradeStudent = Main.scanner.nextLine();
        Student tograde = null;
        for (Student student : Main.students) {
            if (student.email.equals(gradeStudent)) {
                tograde = student;
                break;
            }
        }
        if (tograde==null){
            System.out.println("enter valid email address.");
            return;
        }
        if (!course.getEnrolledStudents().contains(tograde)){
            System.out.println("student is not enrolled in this course");
            return;
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

    private boolean isValidGrade(String grade){
        return grade.equals("A+") || grade.equals("A") || grade.equals("B+") || grade.equals("B") || grade.equals("C+") || grade.equals("C") || grade.equals("D+") || grade.equals("F");
    }

    public void editGrade(){
        System.out.print("Enter student email to edit grade: ");
        String studentEmail = Main.scanner.nextLine();
        Student student = Main.findStudentByEmail(studentEmail);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        if (!course.getEnrolledStudents().contains(student)){
            System.out.println("student not enrolled in the course.");
            return;
        }
        // find existing grade
        Grade existingGrade = null;
        for (Grade grade : student.getCompletedCourses()) {
            if (grade.getCourse().equals(course)) {
                existingGrade = grade;
                break;
            }
        }
        if (existingGrade == null) {
            System.out.println("No existing grade found.");
            return;
        }
        System.out.println("Current Grade: " + existingGrade.getLetterGrade());
        System.out.print("Enter new grade: "); //input new grade
        String newGrade = Main.scanner.nextLine();
        if (!isValidGrade(newGrade)) {
            System.out.println("please enter a valid grade");
            return;
        }
        // Update the grade
        existingGrade.setLetterGrade(newGrade);
        System.out.println("Grade updated successfully.");
    }

    @Override
    public void displayMenu() {
        System.out.println("TA Menu:");
        System.out.println("1. View Available Courses");
        System.out.println("2. Register for Courses");
        System.out.println("3. Drop Courses");
        System.out.println("4. View Schedule");
        System.out.println("5. Track Academic Progress");
        System.out.println("6. Submit Complaint");
        System.out.println("7. Submit Feedback");
        System.out.println("8. View Grades for a Course");
        System.out.println("9. Assign Grades to Students");
        System.out.println("10. Edit Grades");
        System.out.println("0. Logout");
    }
}
