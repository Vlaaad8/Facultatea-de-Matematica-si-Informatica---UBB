namespace Seminar_10;

public class StackContainer : AbstractContainer
{
    public StackContainer(int capacity):base(capacity)
    {

    }
    public override Task Remove()
    {
        if (top > -1)
        {
            Task task = tasks[top];
            tasks[top] = null;
            top--;
            return task;
        }
        throw new InvalidOperationException("Stack container is empty");
    }
    
}
