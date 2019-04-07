/*
Author: Yanhua Luo
Project: CIS 422 Project 2: Music Maker

Functions:
reference the Module Interface Specification to learn more about
how to use each function.

Launches the frame and the whole application.
*/
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Position;

import java.util.Hashtable;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;


public class frame1 extends JFrame  {
    private JPanel contentPane;
    public ButtonGroup buttonGroup_1 = new ButtonGroup();
    private JButton playAllButton;
    private JLabel deleteLabel;
    private JButton stopButton;
    private JLabel loadLabel;
    private JButton pauseButton;
    private JLabel soundLabel;
    public JLabel recordLabel;
    private JLabel saveLabel;
    private JLabel dirLabel;
    private static JLabel timeLable;

    private static JLabel timerLabel;
    private JLabel pauseLabel;
    private JSlider speedSlider;
    private JSlider panSlider;
    private static JSlider volumeSlider;
    public static Timer timer;
    private static Timer timer2;
    public static int timeCount = 1;
    public static int time2Count = 1;
    private static JProgressBar progressBar;
    private static JTree tree;
    private JScrollPane scrollPane;
    private JPanel panelA;
    private JPanel panel_1;
    private JPanel panelC;
    public static int childCount = 0;
    public static String fileName;
    private boolean stopProBar;
    private JTextField searchTextField;
    private JButton searchButton;
    private int currentpb;
    private int currenttimer;

    Icon volumeIcon = new ImageIcon(frame1.class.getResource("volume.png"));
    Icon noVolumeIcon = new ImageIcon(frame1.class.getResource("noVolume.png"));
    Icon saveIcon = new ImageIcon(frame1.class.getResource("save.png"));
    Icon recordIcon = new ImageIcon(frame1.class.getResource("play.png"));
    Icon timerIcon = new ImageIcon(frame1.class.getResource("oval.png"));
    Icon pauseIcon = new ImageIcon(frame1.class.getResource("stop.png"));
    ImageIcon songIcon = new ImageIcon(frame1.class.getResource("musicalNote.png"));
    ImageIcon songListIcon = new ImageIcon(frame1.class.getResource("songList.png"));
    Icon deleteIcon = new ImageIcon(frame1.class.getResource("delete.png"));
    Icon loadIcon = new ImageIcon(frame1.class.getResource("load.png"));

    //Location of arraylist for all the clips
    soundManager soundManager = new soundManager();

    //Creates the Frame
    public frame1() {
        initComponents();
        createEvents();
    }

    //Timer Function
    public void timeUp(int nn){
    	timer.stop();
    	if (nn == 0){
        	//Save it
        	fileName = JOptionPane.showInputDialog("Input the file name");
        	addSong(fileName);
        	timeCount = 0;
        	timerLabel.setText(frameFunction.convertToMinHour(timeCount));
        }
        else {
        	timeCount = 0;
        	timerLabel.setText(frameFunction.convertToMinHour(timeCount));
        }
    }

    //Tree Node, Adding/deleting song Functions
    private static DefaultMutableTreeNode getSelectedNode() {
			return (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    }

    //Add song to TreePath to be displayed
    private void addSong(String fileName){
    	DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        model.insertNodeInto(new DefaultMutableTreeNode(fileName), root, childCount++);
    }


    private void removeSelectedSong() {
        DefaultMutableTreeNode selectedNode = getSelectedNode();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        try {
        	model.removeNodeFromParent(selectedNode);
        }catch(IllegalArgumentException e){
        	e.getMessage();
        	JOptionPane.showMessageDialog(frame1.this, "You can not delete root folder", "Error",
        	          JOptionPane.ERROR_MESSAGE);
        }
    }

    private static TreePath[] getTreePath(){
    	TreePath[] paths = tree.getSelectionPaths();
    	return paths;
    }

    ActionListener updateTimerLabel = new ActionListener(){
        public void actionPerformed(ActionEvent e){
            timerLabel.setText(frameFunction.convertToMinHour(timeCount++));
            if (timeCount >= 299){
            	int nn = JOptionPane.showConfirmDialog(frame1.this, "Do you want to save the song", "Time is Up!!",JOptionPane.YES_NO_OPTION);
            	timeUp(nn);
            }
        }
    };

   ActionListener updateProBar = new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	String s = getSelectedNode().toString();
        	int length = (int) soundManager.getMax();
            progressBar.setMinimum(1);
            progressBar.setMaximum(1000);
            int pro = progressBar.getValue();
            timeLable.setText(frameFunction.convertToMinHour(time2Count++));
            int val = 1000/length;

            if (val+pro >= 1000){
                progressBar.setValue(1000);
                timer2.stop();
                return;
            }
            else if (stopProBar == true){
            	if (soundManager.isPause == true){
            		currentpb = pro+val;
            		currenttimer = time2Count;
            		timer2.stop();
            		return;
            	}
            	timer2.stop();
            	return;
            }else{
            	progressBar.setValue( pro+val);
            }
            if (time2Count == length){
            }
        }
    };

    ChangeListener getSliderValue = new ChangeListener(){
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
		    if (!source.getValueIsAdjusting()) {
		        int fps = (int)source.getValue();
		    }
		}
    };

    ChangeListener speedChange = new ChangeListener(){
		public void stateChanged(ChangeEvent e) {
			try {
				JSlider source = (JSlider)e.getSource();
				float fps = source.getValue() / 10;
			}catch (NullPointerException e1){
	    		System.out.println("No Song is Play at this Point");
			}
		}
    };

    ChangeListener panChange = new ChangeListener(){
		public void stateChanged(ChangeEvent e) {
			try {
				JSlider source = (JSlider)e.getSource();
				float fps = (int)source.getValue();
				soundManager.panForAll(fps);
			}catch (NullPointerException e1){
	    		System.out.println("No Song is Play at this Point");
			}
		}
    };

    ChangeListener volumeChange = new ChangeListener(){
		public void stateChanged(ChangeEvent e) {
			try {
				JSlider source = (JSlider)e.getSource();
				float fps = (int)source.getValue();
                if (fps == 0){
                    soundLabel.setIcon(noVolumeIcon);
                } else {
                    soundLabel.setIcon(volumeIcon);
                }
				soundManager.volumeForAll(fps);
			}catch (NullPointerException e1){
	    		System.out.println("No Song is Play at this Point");
			}
		}
    };

    // This method contains all the code for creating and initializing componenets
    public void initComponents(){
        setIconImage(Toolkit.getDefaultToolkit().getImage(frame1.class.getResource("microphone.png")));
        setTitle("Recorder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 469);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setForeground(Color.WHITE);
        contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2));
        setContentPane(contentPane);

        panelA = new JPanel();
        panelA.setForeground(new Color(0, 100, 0));
        panelA.setBackground(new Color(255, 255, 255));
        panelA.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Record Audio", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
        scrollPane = new JScrollPane();
        progressBar = new JProgressBar();

        panelC = new JPanel();
        panelC.setFont(panelC.getFont().deriveFont(panelC.getFont().getStyle() | Font.BOLD, 13f));
        panelC.setForeground(new Color(0, 128, 0));
        panelC.setBackground(Color.WHITE);
        panelC.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Edit Audio", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelC.setEnabled(true);

        playAllButton = new JButton("Play ");
        playAllButton.setFont(new Font("Lucida Grande", Font.BOLD, 15));
        playAllButton.setIcon(new ImageIcon(frame1.class.getResource("play-button.png")));
        playAllButton.setBackground(new Color(255, 255, 255));

        stopButton = new JButton("Stop");
        stopButton.setFont(new Font("Lucida Grande", Font.BOLD, 15));
        stopButton.setIcon(new ImageIcon(frame1.class.getResource("stop_2.png")));

        pauseButton = new JButton("Pause");
        pauseButton.setFont(new Font("Lucida Grande", Font.BOLD, 15));
        pauseButton.setIcon(new ImageIcon(frame1.class.getResource("pause.png")));

        timeLable = new JLabel();
        timeLable.setText("00:00");

        //GroupLayout
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
        	gl_contentPane.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panelA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addGap(45)
        					.addComponent(playAllButton, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
        					.addGap(30)
        					.addComponent(pauseButton))
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addGap(18)
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
        							.addGap(6))
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        								.addComponent(stopButton, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
        								.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 357, GroupLayout.PREFERRED_SIZE))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(timeLable)))))
        			.addGap(0)
        			.addComponent(panelC, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
        			.addContainerGap())
        );
        gl_contentPane.setVerticalGroup(
        	gl_contentPane.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addComponent(panelC, GroupLayout.PREFERRED_SIZE, 189, Short.MAX_VALUE)
        					.addGap(127))
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addGap(12)
        							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
        							.addGap(18)
        							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        								.addGroup(gl_contentPane.createSequentialGroup()
        									.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
        										.addComponent(playAllButton, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
        										.addComponent(pauseButton)
        										.addComponent(stopButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        									.addPreferredGap(ComponentPlacement.RELATED))
        								.addComponent(timeLable)))
        						.addComponent(panelA, GroupLayout.PREFERRED_SIZE, 430, Short.MAX_VALUE))
        					.addGap(7))))
        );

        Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>();
		position.put(0, new JLabel("0"));
		position.put(50, new JLabel("50"));
		position.put(100, new JLabel("100"));

        Hashtable<Integer, JLabel> speed = new Hashtable<Integer, JLabel>();
		speed.put(0, new JLabel("0"));
		speed.put(1, new JLabel("1"));
		speed.put(2, new JLabel("2"));

        Hashtable<Integer, JLabel> panLabel = new Hashtable<Integer, JLabel>();
		panLabel.put(0, new JLabel("1"));
		panLabel.put(500, new JLabel("0"));
		panLabel.put(1000, new JLabel("-1"));

		JLabel speedLb = new JLabel("Speed");
        speedSlider = new JSlider(0,1000);

        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(speedChange); // Add change listener to the slider

        JLabel panLb = new JLabel("Pan");
        panLb.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        panSlider = new JSlider(0,1000);
        panSlider.setFont(new Font("Lucida Grande", Font.PLAIN, 8));
        panSlider.setMajorTickSpacing(500);
        panSlider.setMinorTickSpacing(500);
        panSlider.setPaintTicks(true);
        panSlider.setPaintLabels(true);
        panSlider.setLabelTable(panLabel);
		panSlider.addChangeListener(panChange);

        soundLabel = new JLabel(volumeIcon);

        volumeSlider = new JSlider(0,100);
        volumeSlider.setBorder(null);
        volumeSlider.addChangeListener(volumeChange);

        GroupLayout gl_panelC = new GroupLayout(panelC);
        gl_panelC.setHorizontalGroup(
        	gl_panelC.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelC.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_panelC.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panelC.createSequentialGroup()
        					.addGroup(gl_panelC.createParallelGroup(Alignment.LEADING)
        						.addGroup(gl_panelC.createSequentialGroup()
        							.addGap(9)
        							.addComponent(soundLabel))
        						.addComponent(speedLb))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(gl_panelC.createParallelGroup(Alignment.LEADING)
        						.addGroup(gl_panelC.createSequentialGroup()
        							.addComponent(volumeSlider, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        							.addContainerGap())
        						.addComponent(speedSlider, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)))
        				.addGroup(gl_panelC.createSequentialGroup()
        					.addComponent(panLb, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(panSlider, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
        					.addGap(13))))
        );
        gl_panelC.setVerticalGroup(
        	gl_panelC.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelC.createSequentialGroup()
        			.addGap(25)
        			.addGroup(gl_panelC.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panelC.createSequentialGroup()
        					.addGap(8)
        					.addComponent(speedLb, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
        				.addGroup(gl_panelC.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(speedSlider, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)))
        			.addGap(47)
        			.addGroup(gl_panelC.createParallelGroup(Alignment.LEADING)
        				.addComponent(panLb, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
        				.addComponent(panSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(51)
        			.addGroup(gl_panelC.createParallelGroup(Alignment.LEADING)
        				.addComponent(soundLabel)
        				.addComponent(volumeSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(58))
        );
        panelC.setLayout(gl_panelC);

        panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Saved Recordings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
        panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_1.setForeground(new Color(0, 100, 0));
        panel_1.setBackground(Color.WHITE);
        scrollPane.setViewportView(panel_1);


        tree = new frameFunction.MyTree();

        tree.setToolTipText("");
        tree.setBackground(Color.WHITE);

        tree.setDropMode(DropMode.ON);
        tree.setShowsRootHandles(true);

        //need to create root here first and it cannot set to invisible.
        tree.setModel(new DefaultTreeModel(
        	new DefaultMutableTreeNode("Saved Recording") {
        	}
        ));

        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
            	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            	if (node == null){
            		return;
            	}

            }
        };

        tree.addTreeSelectionListener(treeSelectionListener);
        tree.setRowHeight(30);

        frameFunction.MyTreeCellRenderer renderer = new frameFunction.MyTreeCellRenderer();
        renderer.setBorderSelectionColor(frameFunction.selectedColor);
        renderer.setBackgroundSelectionColor(frameFunction.selectedColor);
        renderer.setOpenIcon(songListIcon);
        renderer.setLeafIcon(songIcon);
        renderer.setClosedIcon(songIcon);
        tree.setCellRenderer( renderer);

        deleteLabel = new JLabel(deleteIcon);

        searchTextField = new JTextField();
        searchTextField.setText("");
        searchTextField.setColumns(10);

        searchButton = new JButton("Search");

        loadLabel = new JLabel(loadIcon);
        GroupLayout gl_panel_1 = new GroupLayout(panel_1);
        gl_panel_1.setHorizontalGroup(
        	gl_panel_1.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel_1.createSequentialGroup()
        			.addGap(17)
        			.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panel_1.createSequentialGroup()
        					.addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(searchButton)
        					.addContainerGap(49, Short.MAX_VALUE))
        				.addGroup(gl_panel_1.createSequentialGroup()
        					.addGap(8)
        					.addComponent(deleteLabel, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
        					.addGap(44)
        					.addComponent(loadLabel, GroupLayout.PREFERRED_SIZE, 137, Short.MAX_VALUE)
        					.addGap(50))
        				.addGroup(gl_panel_1.createSequentialGroup()
        					.addComponent(tree, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
        					.addGap(20))))
        );
        gl_panel_1.setVerticalGroup(
        	gl_panel_1.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel_1.createSequentialGroup()
        			.addGap(9)
        			.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
        				.addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
        				.addGroup(gl_panel_1.createSequentialGroup()
        					.addGap(1)
        					.addComponent(searchButton)))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(tree, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGap(185)
        			.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
        				.addComponent(loadLabel)
        				.addComponent(deleteLabel))
        			.addGap(8))
        );
        panel_1.setLayout(gl_panel_1);

        saveLabel = new JLabel(saveIcon);
        saveLabel.setFont(new Font("Verdana", Font.BOLD, 13));
        saveLabel.setForeground(new Color(255, 215, 0));
        saveLabel.setText("Save");
        saveLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        saveLabel.setHorizontalTextPosition(JLabel.CENTER);
        saveLabel.setVerticalTextPosition(JLabel.CENTER);

        recordLabel = new JLabel(recordIcon);
        recordLabel.setForeground(new Color(255, 215, 0));
        recordLabel.setFont(new Font("Verdana", Font.BOLD, 13));
        recordLabel.setText("Start");
        recordLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        recordLabel.setHorizontalTextPosition(JLabel.CENTER);
        recordLabel.setVerticalTextPosition(JLabel.CENTER);

        timerLabel = new JLabel(timerIcon);
        timerLabel.setForeground(new Color(255, 215, 0));
        timerLabel.setText("00:00");
        timerLabel.setHorizontalTextPosition(JLabel.CENTER);
        timerLabel.setVerticalTextPosition(JLabel.CENTER);
        timerLabel.setFont(new Font("Lucida Grande", Font.BOLD, 40));

        timer = new Timer(1000, updateTimerLabel);
        timer2 = new Timer(1000, updateProBar);

        pauseLabel = new JLabel(pauseIcon);
        pauseLabel.setFont(new Font("Verdana", Font.BOLD, 13));
        pauseLabel.setForeground(new Color(255, 215, 0));
        pauseLabel.setText("Stop");
        pauseLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        pauseLabel.setHorizontalTextPosition(JLabel.CENTER);
        pauseLabel.setVerticalTextPosition(JLabel.CENTER);

        dirLabel = new JLabel("Tap the Start to start recording");
        dirLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dirLabel.setFont(new Font("Lucida Grande", Font.BOLD, 12));

        GroupLayout gl_panelA = new GroupLayout(panelA);
        gl_panelA.setHorizontalGroup(
        	gl_panelA.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelA.createSequentialGroup()
        			.addGroup(gl_panelA.createParallelGroup(Alignment.TRAILING)
        				.addGroup(gl_panelA.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(pauseLabel)
        					.addGap(18)
        					.addComponent(recordLabel)
        					.addGap(18)
        					.addComponent(saveLabel, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
        					.addGap(23))
        				.addComponent(timerLabel, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
        			.addGap(0))
        		.addGroup(gl_panelA.createSequentialGroup()
        			.addComponent(dirLabel, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
        			.addContainerGap())
        );
        gl_panelA.setVerticalGroup(
        	gl_panelA.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_panelA.createSequentialGroup()
        			.addGap(28)
        			.addComponent(timerLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGap(27)
        			.addComponent(dirLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGap(58)
        			.addGroup(gl_panelA.createParallelGroup(Alignment.BASELINE)
        				.addComponent(recordLabel)
        				.addComponent(pauseLabel)
        				.addComponent(saveLabel))
        			.addGap(8))
        );
        panelA.setLayout(gl_panelA);
        contentPane.setLayout(gl_contentPane);
    }

    //This method contains all the code for creating events
    public void createEvents(){
        // MouseListener
        recordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0){
                if (timer.isRunning()){
                	File file = new File("null.wav");
    				file.delete();
    				record.start();
                	timer.restart();
                	timeCount = 0;
                }
                else{
                	record.start();
                	timer.start();
                	dirLabel.setText("Recording \nTap Start to record again ");
                }
            }
        });

        // ActionListener
        pauseLabel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent arg0){
                timerLabel.setText(frameFunction.convertToMinHour(timeCount++));
                dirLabel.setText("Record Stop");
                if (timer.isRunning()){
                    timer.stop();
                }
                record.finish();
            }
        });


        saveLabel.addMouseListener(new MouseAdapter(){
        	@Override
        	public void mouseClicked(MouseEvent arg0){
        		timer.stop();
                fileName = JOptionPane.showInputDialog(frame1.this,"Input the file name");
                //handle empty filename
                while (fileName.isEmpty()){
                	fileName = JOptionPane.showInputDialog("You need to input the file name");
                }
                //handle duplicated filename
            	int startRow = 0;
            	TreePath searchNodepath = tree.getNextMatch(fileName, startRow, Position.Bias.Forward);
            	while (searchNodepath != null){
            		fileName = JOptionPane.showInputDialog("input another file name");
            		searchNodepath = tree.getNextMatch(fileName, startRow, Position.Bias.Forward);
                }

                //handle saving empty recording
                if (timeCount == 0){
                	JOptionPane.showMessageDialog(frame1.this, "Empty Recording", "Error", JOptionPane.ERROR_MESSAGE);
                	return;
                }

                //creat the null.wav from saveLoad.java, rename it after user input fileName
                File oldFile = new File("null.wav");
                File newFile = new File(fileName +".wav");
                oldFile.renameTo(newFile);
                addSong(newFile.getAbsolutePath());

                timeCount = 0;
                timerLabel.setText(frameFunction.convertToMinHour(timeCount));
                dirLabel.setText("Tap the Play to start recording");
                tree.expandRow(0);
        	}
        });

        deleteLabel.addMouseListener(new MouseAdapter(){
        	@Override
        	public void mouseClicked(MouseEvent arg0){
        		try{
            		int n = JOptionPane.showConfirmDialog(frame1.this, "Do you want to delete the song", "",JOptionPane.YES_NO_OPTION);
            		// n == 1, No; n == 0, YES
            		if (n == 0){
            			TreePath[] paths = getTreePath();
            			for (TreePath path : paths) {
            				String songName = path.getLastPathComponent().toString();
            				File file = new File(songName+".wav");
                            removeSelectedSong();
                            childCount--;
                        }
            		}else{
            			//do nothing
            		}
        		}catch(Exception e1){
            		JOptionPane.showMessageDialog(frame1.this, "No Song to Delete", "Error", JOptionPane.ERROR_MESSAGE);
        		}
        	}
        });

        playAllButton.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		stopProBar = false;
        		progressBar.setValue(0);
        		timeLable.setText("00:00");
        		time2Count = 1;
        		 TreePath[] paths = getTreePath();
        		 if (soundManager.isPause == true){
        			 progressBar.setValue(currentpb);
        			 timer2.restart();
        		 }
        		 if (soundManager.allPlaying == true){
        			 progressBar.setValue(0);
        			 timer2.start();
        		 }
        		 for (TreePath path : paths) {
        			 String songName = path.getLastPathComponent().toString();
                     soundManager.addClip(new play(songName));
                 }
                 try{
                    timer2.start();
         			soundManager.playAll();
         		} catch (Exception ex){
         			ex.printStackTrace();
    			 }
        	}
        });

        searchButton.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		String searchName = searchTextField.getText();
        		int startRow = 0;
            	TreePath searchNodepath = tree.getNextMatch(searchName, startRow, Position.Bias.Forward);
            	if (searchNodepath == null){
            		JOptionPane.showMessageDialog(frame1.this, "File do not exsit", "Error", JOptionPane.ERROR_MESSAGE);
            		searchTextField.setText("");
                  	return;
            	}else{
            		tree.setSelectionPath(searchNodepath);
            		searchTextField.setText("");
            	}
        	}
        });

        stopButton.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		stopProBar = true;
        		soundManager.stopAll();
        		soundManager.removeAll();
        	}
        });

        loadLabel.addMouseListener(new MouseAdapter(){
        	@Override
        	public void mouseClicked(MouseEvent arg0){
        		String fullName = null;
    			try {
					fullName = saveLoad.load();
					String[] songName = fullName.split("/");
	        		String fileName = songName[songName.length -1];
	        		String name = fileName.split((Pattern.quote(".")))[0];
	        		addSong(fullName);
	        		tree.expandRow(0);
				} catch (Exception e1) {
					System.out.println("Exception: import cancel");
				}

        	}
        });

        pauseButton.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		int played = (int) frameFunction.getSongLength(getSelectedNode().toString());
        		stopProBar = true;
                soundManager.pauseAll();
        	}
        });
    }
}
