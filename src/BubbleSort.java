import java.util.Arrays;

public class BubbleSort implements Runnable{

    private Integer[] toBeSorted;
    private VisualFrame frame;
    private boolean fast;

    public BubbleSort(Integer[] toBeSorted, VisualFrame frame, boolean fast) {
        this.toBeSorted = toBeSorted;
        this.frame = frame;
        this.fast = fast;
    }

    public synchronized void run() {
        if (fast) {
            sortFast();
        } else {
            sortSlow();
            //Arrays.fill(toBeSorted, 0);
            frame.reDrawArray(toBeSorted, -1, -1, -1);
        }
        SortingVisualizer.isSorting=false;
    }

    public synchronized void sortFast() {
        int temp = 0;
        boolean swapped = false;
        for(int i = 0; i<toBeSorted.length-1; i++){
            swapped = false;
            for(int j = 1; j<toBeSorted.length-i; j++){
                if (toBeSorted[j-1]> toBeSorted[j]){
                    temp = toBeSorted[j-1];
                    toBeSorted[j-1] = toBeSorted[j];
                    toBeSorted[j]= temp;
                    swapped = true;
                }
            }
            if(!SortingVisualizer.hasPaused) {
                frame.reDrawArray(toBeSorted);
            }
            try {
                Thread.sleep(SortingVisualizer.sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!swapped) break;
        }
    }

    public synchronized void sortSlow() {

        int temp = 0;
        boolean swapped = false;
        for(int i = 0; i<toBeSorted.length-1; i++){
            swapped = false;
            for(int j = 1; j<toBeSorted.length-i; j++){
                if (toBeSorted[j-1]> toBeSorted[j]){
                    temp = toBeSorted[j-1];
                    toBeSorted[j-1] = toBeSorted[j];
                    toBeSorted[j]= temp;
                    swapped = true;
                }
                //if(!SortingVisualizer.isPausing) {
                    frame.reDrawArray(toBeSorted, j, j + 1);
                //}

                try {
                    Thread.sleep(SortingVisualizer.sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!swapped) break;
        }
    }

}