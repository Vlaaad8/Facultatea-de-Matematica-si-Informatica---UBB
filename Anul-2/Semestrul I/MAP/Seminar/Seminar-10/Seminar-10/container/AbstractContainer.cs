namespace Seminar_10;

public abstract class AbstractContainer:Container
{    protected Task[] tasks; 
     protected int top;

     public AbstractContainer(int capacity)
     {
         tasks = new Task[capacity];
         top = -1;
     }

     public abstract Task Remove();

    public void Add(Task task)
    {
        if (top < tasks.Length-1)
        {
            top++;
            tasks[top] = task;
        }
        else throw new InvalidOperationException("Container is full");
    }

    public int Size()
    {
        return top + 1;
    }

    public bool IsEmpty()
    {
        return top == -1;

    }

    public Task GetTopTask()
    {
        return tasks[top];
    }

    public int Top
    {
        get => top;
    }
}