public class Task
{


    private String TaskName;
    private String TaskNote;

    private String TaskPicture;




    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getTaskNote() {
        return TaskNote;
    }

    public void setTaskNote(String taskNote) {
        TaskNote = taskNote;
    }



    public String getTaskPicture() {
        return TaskPicture;
    }

    public void setTaskPicture(String taskPicture) {
        TaskPicture = taskPicture;
    }

    public boolean readyTask()
    {

        if(getTaskName()!=null&&getTaskPicture()!=null&& getTaskNote()!=null) {   return true;      }
        else {  return false; }

    }
}
