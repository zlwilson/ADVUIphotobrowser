package photoviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class PhotoViewer extends JFrame {
	
	private Dimension size = new Dimension(600,400);
	private JLabel status;
	private PicViewer picViewer;
	
	public PhotoViewer() {
		super("Zack's Photo Viewer");
		this.setPreferredSize(size);
		
		setupUI();
	}

	private void setupUI() {
		makeMenu();
		
		// panel for pics
		JScrollPane scroll = new JScrollPane();
		picViewer = new PicViewer("label");
		scroll.getViewport().add(picViewer);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.getContentPane().add(scroll, BorderLayout.CENTER);
		
		makeToolBar();
		
		// status bar
		JPanel statusBar = new JPanel();
		status = new JLabel("Welcome to Zack's photo viewer");
		statusBar.setBackground(new Color(220,220,220));
		statusBar.add(status);
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		this.pack();
	}

	private void makeToolBar() {
		// tool bar on the left
		JPanel toolBar = new JPanel();
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.PAGE_AXIS));
		toolBar.setBackground(new Color(200, 200, 200));
		JLabel toolTitle = new JLabel("Sorting Tools ");
		toolTitle.setFont(new Font("Courier New", Font.BOLD, 16));
		toolBar.add(toolTitle);
		JToggleButton people = new JToggleButton("People", false);
		people.addActionListener(e -> updateStatus("People"));
		JToggleButton places = new JToggleButton("Places", false);
		places.addActionListener(e -> updateStatus("Places"));
		JToggleButton school = new JToggleButton("School", false);
		school.addActionListener(e -> updateStatus("School"));
		toolBar.add(people);
		toolBar.add(places);
		toolBar.add(school);
		
		// color picker
		JLabel atTitle = new JLabel("Annotation Tools ");
		atTitle.setFont(new Font("Courier New", Font.BOLD, 16));
		toolBar.add(atTitle);
		toolBar.add(new JLabel(" Color:"));
		JRadioButton red = new JRadioButton("Red", true);
		red.addActionListener(e -> updateColor(Color.RED));
		JRadioButton blue = new JRadioButton("Blue", false);
		blue.addActionListener(e -> updateColor(Color.BLUE));
		JRadioButton black = new JRadioButton("Black", false);
		black.addActionListener(e -> updateColor(Color.BLACK));
		ButtonGroup colorPicker = new ButtonGroup();
		colorPicker.add(red);
		colorPicker.add(blue);
		colorPicker.add(black);
		toolBar.add(red);
		toolBar.add(blue);
		toolBar.add(black);
		
		// JColorChooser for custom colors
		JButton colorChooser = new JButton("Custom color...");
		colorChooser.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(PhotoViewer.this, "Choose custom color", picViewer.getColor());
				if (newColor != null) {
					picViewer.updateColor(newColor);
				} else {
					picViewer.updateColor(Color.RED);
					red.setSelected(true);
				}
			}
			
		});
		toolBar.add(colorChooser);
		
		// pen editor
		toolBar.add(new JLabel(" Pen size:"));
		JRadioButton small = new JRadioButton("Small", true);
		small.addActionListener(e -> updatePenSize(1));
		JRadioButton medium = new JRadioButton("Medium", false);
		medium.addActionListener(e -> updatePenSize(3));
		JRadioButton large = new JRadioButton("Large", false);
		large.addActionListener(e -> updatePenSize(7));
		ButtonGroup penSizes = new ButtonGroup();
		penSizes.add(small);
		penSizes.add(medium);
		penSizes.add(large);
		toolBar.add(small);
		toolBar.add(medium);
		toolBar.add(large);
		
		this.getContentPane().add(toolBar, BorderLayout.EAST);
	}

	private void updateColor(Color c) {
		this.picViewer.updateColor(c);
	}
	
	private void updatePenSize(int i) {
		this.picViewer.updateSize(i);
	}

	private void makeMenu() {
		// setup menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu viewMenu = new JMenu("View");
				
		// file menu
		JMenuItem imp = new JMenuItem("Import");
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		imp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev) {
	            // open JFileChooser
				FileFilter filter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
	            fileChooser.setFileFilter(filter);
	            int response = fileChooser.showOpenDialog(null);
	            try {
	            	if (response == JFileChooser.APPROVE_OPTION) {
		                String pathName = fileChooser.getSelectedFile().getPath();
		                ImageIcon image = new ImageIcon(pathName);
		                updateStatus("Imported: " + pathName);
		                picViewer.setImage(image);
		                picViewer.clearAnnotations();
		                picViewer.getModel().showPhoto();
		            } else {
		                JOptionPane.showMessageDialog(null, "Pick one later then");
		            }
	            } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
			}
		});
		JMenuItem delete = new JMenuItem("Delete");
		delete.addActionListener(e -> updateStatus("Oops not delete isn't implemented yet!"));
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev) {
	            System.exit(0);
			}
		});
		fileMenu.add(imp); fileMenu.add(delete); fileMenu.add(quit);
		
		// view menu
		JRadioButtonMenuItem photoViewer = new JRadioButtonMenuItem("Photo Viewer", true);
		photoViewer.addActionListener(e -> updateStatus("Photo viewer mode"));
		JRadioButtonMenuItem browser = new JRadioButtonMenuItem("Browser", false);
		browser.addActionListener(e -> updateStatus("Browser mode"));
		ButtonGroup radioButtons = new ButtonGroup();    
		radioButtons.add(photoViewer);
		radioButtons.add(browser);
		viewMenu.add(photoViewer);
		viewMenu.add(browser);
		
		menuBar.add(fileMenu);
		menuBar.add(viewMenu);
		
		this.setJMenuBar(menuBar);
	}

	private void updateStatus(String string) {
		this.status.setText(string);
	}
}
