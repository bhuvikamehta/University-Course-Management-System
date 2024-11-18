class CourseFullException extends Exception {
    public CourseFullException(String message) {
        super(message);
    }
}

class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
}

class DropDeadlinePassedException extends Exception {
    public DropDeadlinePassedException(String message) {
        super(message);
    }
}
