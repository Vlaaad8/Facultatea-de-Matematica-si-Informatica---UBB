using System.Runtime.InteropServices.Marshalling;

namespace Seminar_10;

public class BubbleSort : AbstractSort<Task>
{
    public override void Sort(Task[] array)
    {Console.WriteLine("Executing Bubble Sort...");
        for (int i = 1; i < array.Length-1; i++)
        {
            for (int j = i + 1; j < array.Length; j++)
            {
                if (array[i].IsGreaterThan(array[j]))
                {
                    (array[i], array[j]) = (array[j], array[i]);
                }
            }
        }
    }
}
