class Complaint {
    private String description;
    private String status;

    public Complaint(String description) {
        this.description = description;
        this.status = "Pending";
    }

    public String complaintStatus(){
        return this.status; //returns complaint status (pending or resolved)
    }

    public void resolve() {
        this.status = "Resolved";
    }

    public void pending(){
        this.status = "Pending";
    }

    @Override
    public String toString() {
        return description + " - " + status;
    }
}
