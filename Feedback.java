class Feedback<T> {
    private T feedback;
    private Student student;

    public Feedback(T feedback, Student student) {
        this.feedback = feedback;
        this.student = student;
    }

    public T getFeedback() {
        return feedback;
    }

    public Student getStudent() {
        return student;
    }

    @Override
    public String toString() {
        return "Feedback from " + student.getName() + ": " + feedback.toString();
    }
}
