package sem1_2;

import sem1_2.decorator.DelayTaskRunner;
import sem1_2.decorator.PrinterTaskRunner;
import sem1_2.decorator.StrategyTaskRunner;
import sem1_2.decorator.TaskRunner;
import sem1_2.factory.Strategy;
import sem1_2.model.*;

import java.time.LocalDateTime;

public class Sem1_2Test {
    public static MessageTask[] createMessages() {
        MessageTask msg1 = new MessageTask("1", "feedback lab 2", "Te-ai descurcat bine", "teacher", "student", LocalDateTime.now());
        MessageTask msg2 = new MessageTask("2", "feedback lab 3", "Te-ai descurcat bine", "teacher", "student", LocalDateTime.now());
        MessageTask msg3 = new MessageTask("3", "feedback lab 4", "Te-ai descurcat bine", "teacher", "student", LocalDateTime.now());
        MessageTask msg4 = new MessageTask("4", "feedback lab 5", "Te-ai descurcat bine", "teacher", "student", LocalDateTime.now());
        MessageTask msg5 = new MessageTask("5", "feedback lab 6", "Te-ai descurcat bine", "teacher", "student", LocalDateTime.now());

        return new MessageTask[]{msg1, msg2, msg3, msg4, msg5};
    }


    public static void main(String[] args) {
        MessageTask[] messageTasks = createMessages();
        for (MessageTask messageTask: messageTasks) {
            System.out.println(messageTask);
        }

        int array[]={3,20,1,30,20,1,422,2393};
        Task bubbleSorting= new SortingTask(array,"1a3","Sortarea Bubble",new BubbleSort());
        System.out.println("Executam Sortarea conform BubbleSort");
        bubbleSorting.execute();
        int array2[]={3,20,1,30,20,1,422,2393,38};
        System.out.println("Executam Sortarea conform QuickSort");
        Task quickSorting= new SortingTask(array2,"1a3","Sortarea Quick",new QuickSort());

        quickSorting.execute();

        TaskRunner strategyTaskRunner = new StrategyTaskRunner(Strategy.valueOf(args[0]));
        for (MessageTask m : messageTasks) {
            strategyTaskRunner.addTask(m);
        }
        strategyTaskRunner.executeAll();

        TaskRunner printerTaskRunner = new PrinterTaskRunner(strategyTaskRunner);
        for (MessageTask m : messageTasks) {
            printerTaskRunner.addTask(m);
        }
        System.out.println("PrinterTaskRunner");
        printerTaskRunner.executeAll();

        TaskRunner delayTaskRunner = new DelayTaskRunner(strategyTaskRunner);
        for (MessageTask m : messageTasks) {
            delayTaskRunner.addTask(m);
        }
        System.out.println("DelayTaskRunner");
        delayTaskRunner.executeAll();
    }
}
