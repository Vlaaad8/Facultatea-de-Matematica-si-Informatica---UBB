package sem1_2.model;
import static java.util.Collections.swap;

public class BubbleSort extends AbstractSorter{
    @Override
    public void sort(int[] list){
        for (int i=0;i<list.length;i++){
            for(int j=i+1;j<list.length;j++){
                if(list[i]>list[j]){
                    int aux=list[i];
                    list[i]=list[j];
                    list[j]=aux;
                }
            }
        }
    }
}
