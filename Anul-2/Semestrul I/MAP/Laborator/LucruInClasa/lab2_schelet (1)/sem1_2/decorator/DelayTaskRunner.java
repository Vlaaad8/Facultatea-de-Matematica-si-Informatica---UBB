package sem1_2.decorator;


import sem1_2.model.Task;

public class DelayTaskRunner extends AbstractTaskRunner {
    public DelayTaskRunner(TaskRunner runner) {
        super(runner);
    }
    @Override
    public void executeOneTask(){
        try {
            Thread.sleep(3000);
            super.executeOneTask();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void executeAll() {
        try {
            Thread.sleep(3000);
            super.executeAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTask(Task t) {
        try {
            Thread.sleep(3000);
            super.addTask(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasTask() {

        try {
            Thread.sleep(3000);
            return super.hasTask();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}


