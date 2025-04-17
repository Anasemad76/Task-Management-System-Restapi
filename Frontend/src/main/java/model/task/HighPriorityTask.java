package model.task;



import java.time.LocalDate;


public class HighPriorityTask extends Task {
    private static final int HIGH_PRIORITY = 3;

    private boolean isApproved=false;

    public HighPriorityTask() {}

    public HighPriorityTask(String taskTitle, String taskDescription, boolean isCompleted, String dueDate) {
        super(taskTitle, taskDescription, isCompleted, HIGH_PRIORITY, dueDate);

    }


    public boolean getisApproved() {
        return isApproved;
    }

    @Override
    public int getPriority() {
            return HIGH_PRIORITY;
    }
    @Override
    public String toString() {
        return super.toString()+ " | approved : " +isApproved ;
    }
}
