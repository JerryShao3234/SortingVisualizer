import java.util.ArrayList;
import java.util.Collections;

public class SortingVisualizer {

    private static Thread sortingThread;

    public static VisualFrame frame;
    public static Integer[] toBeSorted;
    public static volatile boolean isSorting = false; //global now!
    public static volatile boolean hasPaused = false; //global now!
    public static volatile boolean isDrawing = false; //global now!
    public static volatile boolean isPausing = false; //global now!
    public static int sortDataCount = 100;
    public static int sleep = 20;
    public static int blockWidth;
    // Stepped depicts whether the values are incremental or random. True is incremental.
    public static boolean stepped = false;

    public synchronized static void main(String[] args) {
        frame = new VisualFrame();
        resetArray();
        frame.setLocationRelativeTo(null);
    }

    public synchronized static void resetArray(){
        // If we are currently in a sorting method, then isSorting should be true
        // We do not want to reinitialize/reset the array mid sort.
        if (isSorting || hasPaused) return;
        toBeSorted = new Integer[sortDataCount];
        blockWidth = (int) Math.max(Math.floor(500/sortDataCount), 1);
        for(int i = 0; i<toBeSorted.length; i++){
            if (stepped) {
                toBeSorted[i] = i;
            } else {
                toBeSorted[i] = (int) (sortDataCount*Math.random());
            }
        }
        // If we're using incremental values, they are already sorted. This shuffles it.
        if (stepped) {
            ArrayList<Integer> shuffleThis = new ArrayList<>();
            for (int i = 0; i < toBeSorted.length; i++) {
                shuffleThis.add(toBeSorted[i]);
            }
            Collections.shuffle(shuffleThis);
            toBeSorted = shuffleThis.toArray(toBeSorted);
        }
        frame.preDrawArray(toBeSorted);
    }

    public synchronized static void startSort(String type){

        if(hasPaused) hasPaused = false;

        if (sortingThread == null || !isSorting){

           // resetArray();

            isSorting = true;
            hasPaused = false;

            switch(type){
                case "Bubble":
                    sortingThread = new Thread(new BubbleSort(toBeSorted, frame, false));
                    break;
            }


            sortingThread.start();

        }

    }

    public synchronized static void pauseSort() {
        isSorting = false;
        hasPaused = true;
        isPausing = true;

        try {

            if(isDrawing) {isPausing = false; pauseSort();}
            sortingThread.stop();

        }
        catch (Exception e) {
            System.out.println("Failed to pause :|");
        }
        finally {
            isPausing = false;
        }

        ///isPausing = true;
    }


}