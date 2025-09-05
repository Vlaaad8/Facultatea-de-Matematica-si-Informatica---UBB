import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!");
        //Task t=new Task("1","Salut");
        //System.out.println(t);
        MessageTask m = new MessageTask("1", "Mesaj", "Salut", "eu", "tu", LocalDateTime.now());
        MessageTask m1 = new MessageTask("1", "Mesaj", "Salut", "eu", "tu", LocalDateTime.now());
        MessageTask m2 = new MessageTask("1", "Mesaj", "Salut", "eu", "tu", LocalDateTime.now());
        MessageTask m3 = new MessageTask("1", "Mesaj", "Salut", "eu", "tu", LocalDateTime.now());
        MessageTask m4 = new MessageTask("1", "Mesaj", "Salut", "eu", "tu", LocalDateTime.now());
        m.execute();
        MessageTask[] list2=new MessageTask[5];
        list2[0] = m;
        list2[1] = m1;
        list2[2] = m2;
        list2[3] = m3;
        list2[4] = m4;
//        for (int i = 0; i < list2.length; i++) {
//            list2[i].execute();
//        }

        Collection<MessageTask> list = new ArrayList<>();
        Collection<MessageTask> l=new HashSet<>();
        list.add(m);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);

        for (MessageTask mt : list) {
            mt.execute();
        }
    }
}