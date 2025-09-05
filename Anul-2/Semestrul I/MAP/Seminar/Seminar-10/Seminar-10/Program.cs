// See https://aka.ms/new-console-template for more information

using Seminar_10;
using Task = Seminar_10.Task;

if (args.Length == 0 || args.Length > 2)
{
    throw new ArgumentException("Invalid number of arguments, you have to provide 2 arguments.");
}

String implementation = args[1].ToLower();
String sorting = args[0].ToLower();
Strategy sortingStrategy;
Strategy containerStrategy;

var messageTask = new MessageTask("A", "Salut", DateTime.Now, "1", "Ce mai faci??");
var messageTask2 = new MessageTask("B", "Salutare", DateTime.Now, "2", "Foarte bine");
var messageTask3 = new MessageTask("C", "Salutare", DateTime.Now, "3", "Tu?");
var messageTask4 = new MessageTask("D", "Salutare", DateTime.Now, "4", "Ma bucur");
var messageTask5 = new MessageTask("E", "Salutare", DateTime.Now, "5", "Sanatate!");

Task[] taskStatic = new Task[] { messageTask, messageTask2, messageTask3, messageTask4, messageTask5 };

if (sorting == "quicksort")
{
    sortingStrategy = Strategy.QuickSort;
}
else
{
    sortingStrategy = Strategy.BubbleSort;
}

SortingTask sortingTask = new SortingTask("10", "Sorting a task vector", taskStatic, sortingStrategy);
sortingTask.Execute();

if (implementation == "fifo")
{
    containerStrategy = Strategy.Fifo;
}
else
{
    containerStrategy = Strategy.Lifo;
}
var factory = TaskContainerFactory.GetInstance();
var stackContainer = factory.CreateContainer(containerStrategy);
stackContainer.Add(messageTask);
stackContainer.Add(messageTask2);
stackContainer.Add(messageTask3);
stackContainer.Add(messageTask4);
stackContainer.Add(messageTask5);


var mainTaskRunner = new StrategyTaskRunner(containerStrategy);
var taskRunner = new PrinterTaskRunner(mainTaskRunner);
taskRunner.AddTask(messageTask);
taskRunner.AddTask(messageTask2);
taskRunner.AddTask(messageTask3);
taskRunner.AddTask(messageTask4);
taskRunner.AddTask(messageTask5);

for (int i = 0; i < 5; i++)
{
    taskRunner.ExecuteOneTask();
}

var delayRunner = new DelayTaskRunner(mainTaskRunner);
delayRunner.AddTask(messageTask);
delayRunner.AddTask(messageTask2);
delayRunner.AddTask(messageTask3);
delayRunner.AddTask(messageTask4);
delayRunner.AddTask(messageTask5);

for (int i = 0; i < 5; i++)
{
    delayRunner.ExecuteOneTask();
}