namespace Seminar_10;

public interface TaskRunner
{
    void ExecuteOneTask();
    void ExecuteAll();
    void AddTask(Task task);
    bool HasTasks();
}