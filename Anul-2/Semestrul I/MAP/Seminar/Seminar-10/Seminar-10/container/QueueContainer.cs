namespace Seminar_10;

public class QueueContainer : AbstractContainer
{
    public QueueContainer(int capacity):base(capacity)
    {
    }

    public override Task Remove()
    {
        if (top > -1)
        {
            Task removed = tasks[0];
            for (int i = 0; i < top; i++)
            {
                tasks[i] = tasks[i + 1];
            }

            return removed;
        }

        throw new InvalidOperationException("Queue container is empty");
    }
    
}