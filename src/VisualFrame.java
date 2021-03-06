import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import static jdk.internal.org.jline.utils.Log.warn;

public class VisualFrame extends JFrame {

    private final int MAX_SPEED = 1000;
    private final int MIN_SPEED = 1;
    private final int MAX_SIZE = 500;
    private final int MIN_SIZE = 1;
    private final int DEFAULT_SPEED = 20;
    private final int DEFAULT_SIZE = 100;

    private final String[] Sorts = {"Bubble", "Selection", "Insertion", "Gnome", "Merge", "Radix LSD", "Radix MSD", "Shell", "Quandrix", "Bubble(fast)", "Selection(fast)", "Insertion(fast)", "Gnome(fast)"};

    private int sizeModifier;

    private JPanel wrapper;
    private JPanel arrayWrapper;
    private JPanel buttonWrapper;
    private JPanel textWrapper;
    private JPanel[] squarePanels;
    private JButton start;
    private JButton pause;
    private JFormattedTextField sizeip;
    private JComboBox<String> selection;
    private JSlider speed;
    private JSlider size;
    private JLabel speedVal;
    private JLabel sizeVal;
    private GridBagConstraints c;
    private JCheckBox stepped;

    public  synchronized static void main(String[] args){
        new VisualFrame();
    }

    public  VisualFrame(){
        super("Sorting Visualizer");

        start = new JButton("Start");
        pause = new JButton("Pause");
        buttonWrapper = new JPanel();
        textWrapper = new JPanel();
        arrayWrapper = new JPanel();
        wrapper = new JPanel();
        selection = new JComboBox<String>();
        speed = new JSlider(MIN_SPEED, MAX_SPEED, DEFAULT_SPEED);
        size = new JSlider(MIN_SIZE, MAX_SIZE, DEFAULT_SIZE);
        sizeip = new JFormattedTextField();
        JButton test = new JButton("test");
        speedVal = new JLabel("Speed: 20 ms");
        sizeVal = new JLabel("Size: 100 values");
        stepped = new JCheckBox("Incremental Values");
        c = new GridBagConstraints();

        for(String s : Sorts) selection.addItem(s);

        arrayWrapper.setLayout(new GridBagLayout());
        wrapper.setLayout(new BorderLayout());

        c.insets = new Insets(0,1,0,1);
        c.anchor = GridBagConstraints.SOUTH;

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SortingVisualizer.startSort((String) selection.getSelectedItem());
            }
        });

        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("pause");
                SortingVisualizer.pauseSort();
            }
        });

        stepped.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SortingVisualizer.stepped = stepped.isSelected();
            }
        });

        speed.setMinorTickSpacing(10);
        speed.setMajorTickSpacing(100);
        speed.setPaintTicks(true);

        speed.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                speedVal.setText(("Speed: " + Integer.toString(speed.getValue()) + "ms"));
                validate();
                SortingVisualizer.sleep = speed.getValue();
            }
        });

        size.setMinorTickSpacing(10);
        size.setMajorTickSpacing(100);
        size.setPaintTicks(true);

        size.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                sizeVal.setText(("Size: " + Integer.toString(size.getValue()) + " values"));
                //validate();
                SortingVisualizer.sortDataCount = size.getValue();
                SortingVisualizer.resetArray();
                sizeip.setText(Integer.toString(size.getValue()));
            }
        });

        sizeip.setText("100");
        sizeip.setColumns(3);
        sizeip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(SortingVisualizer.sortDataCount == Integer.parseInt(sizeip.getText())) {
                    return;
                }
                SortingVisualizer.sortDataCount = Integer.parseInt(sizeip.getText());
                SortingVisualizer.resetArray();
                size.setValue(Integer.parseInt(sizeip.getText()));
            }
        });

        buttonWrapper.add(stepped);
        buttonWrapper.add(speedVal);
        buttonWrapper.add(speed);
        buttonWrapper.add(sizeVal);
        buttonWrapper.add(size);
        buttonWrapper.add(sizeip);
        buttonWrapper.add(start);
        buttonWrapper.add(pause);
        buttonWrapper.add(selection);

        wrapper.add(buttonWrapper, BorderLayout.SOUTH);
        wrapper.add(arrayWrapper);
        add(wrapper);

        setExtendedState(JFrame.MAXIMIZED_BOTH );

        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // preDrawArray reinitializes the array of panels that represent the values. They are set based on the size of the window.
    public synchronized void preDrawArray(Integer[] squares){
        squarePanels = new JPanel[SortingVisualizer.sortDataCount];
        arrayWrapper.removeAll();
        // 90% of the windows height, divided by the size of the sorted array.
        sizeModifier =  (int) ((getHeight()*0.9)/(squarePanels.length));
        for(int i = 0; i<SortingVisualizer.sortDataCount; i++){
            squarePanels[i] = new JPanel();
            squarePanels[i].setPreferredSize(new Dimension(SortingVisualizer.blockWidth, squares[i]*sizeModifier));
            squarePanels[i].setBackground(Color.BLUE);
            arrayWrapper.add(squarePanels[i], c);
        }
        repaint();
        validate();
    }

    public synchronized void reDrawArray(Integer[] x){
        if(SortingVisualizer.isPausing) {
            reDrawArray(x);
        }
        reDrawArray(x, -1);
    }

    public synchronized void reDrawArray(Integer[] x, int y){
        reDrawArray(x, y, -1);
    }

    public synchronized void reDrawArray(Integer[] x, int y, int z){
        reDrawArray(x, y, z, -1);
    }

    // reDrawArray does similar to preDrawArray except it does not reinitialize the panel.
    public synchronized void reDrawArray(Integer[] squares, int working, int comparing, int reading){

        SortingVisualizer.isDrawing = true;

        arrayWrapper.removeAll();
        for(int i = 0; i<squarePanels.length; i++){
            squarePanels[i] = new JPanel();
            squarePanels[i].setPreferredSize(new Dimension(SortingVisualizer.blockWidth, squares[i]*sizeModifier));
            if (i == working){
                squarePanels[i].setBackground(Color.green);
            }else if(i == comparing){
                squarePanels[i].setBackground(Color.red);
            }else if(i == reading){
                squarePanels[i].setBackground(Color.yellow);
            }else{
                squarePanels[i].setBackground(Color.blue);
            }
            arrayWrapper.add(squarePanels[i], c);
        }
        repaint();
        validate();

        SortingVisualizer.isDrawing = false;
    }

}
