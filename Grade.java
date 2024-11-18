class Grade {
    private Course course;
    private String letterGrade;

    public Grade(Course course, String letterGrade) {
        this.course = course;
        this.letterGrade = letterGrade;
    }

    public Course getCourse() {
        return course;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    @Override
    public String toString() {
        return course.getTitle() + ": " + letterGrade;
    }

    public void setLetterGrade(String newGrade) {
        this.letterGrade=newGrade;
    }
}
