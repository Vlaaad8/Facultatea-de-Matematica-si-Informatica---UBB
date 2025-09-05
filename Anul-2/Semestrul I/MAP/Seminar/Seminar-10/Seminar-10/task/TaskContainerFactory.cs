namespace Seminar_10;

public class TaskContainerFactory:Factory
{
    private static TaskContainerFactory instance;
    private TaskContainerFactory():base()
    {
        
    }

    public static TaskContainerFactory GetInstance()
    {
        if (instance == null)
        {
            instance = new TaskContainerFactory();
        }
        return instance;
    }

    public Container CreateContainer(Strategy strategy)
    {
        if (strategy == Strategy.Fifo)
        {
            return new QueueContainer(10);
        }
            return new StackContainer(10);
    }
}