/* BitNotes is a Windows that autosaves text files to your Desktop
 *   Copyright (C) 2016  Steven Burgess
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Programmer: Steven Burgess
 * Last Update: 5/10/2016
 * Project: BitNotes Text editor
 * Project Purpose: To serve as a basic open source
 * 					text editor with autosave
 * Class Name: BitNotesMainFrame
 * Class Purpose: To serve as the main frame for the 
 * 				  BitNotes text editor application
 */

//standard java imports
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

//Swing imports
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.text.Element;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

//AWT Imports
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;

//Event Listening imports
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//ActionListener is implemented only once here because
//the interface will only be used by a select few buttons
public class BitNotesMainFrame 
	extends JFrame implements ActionListener, KeyListener, DocumentListener{

	//version
	private static final long serialVersionUID = 1L;
	
	//GUI JComponents from Swing Libraries
	private JFrame controls;
	private JButton updateButton, newFileButton, openFileButton;
	private JTextField changeTabSizeField, fileNameField, fontSizeTextField;
	private JLabel changeTabSizeLabel, fileNameLabel, fontSizeLabel, fileType;
	private JPanel settingsPanel, mainPanel;
	private JTextArea noteArea, lineNumber;
	private JScrollPane scrolling, mainScrollPane;
	private JFileChooser fileChooser;
	private JComboBox<String> fileTypeChoice;
	
	//Objects created from AWT library imports
	private FlowLayout settingsFlow;
	private GridLayout mainPanelLayout;
	private Dimension minWindowSize, defaultWindowSize;
	private Font font;
	
	//IO Objects
	private File savedFile;
	
	//primitive types
	private int fontSize, tabIndentSize;
	private String genericFileName, textFileName, fileExtension;
	
	//<constructor>
	public BitNotesMainFrame() {
		
		//always needed as the first command in a thing
		super("BitNotes");
		
		//initial window setting components and setting the look and feel
		//of a windows application
		addWindowBasics();
		
		//add layout to thing
		addPanelsAndLayouts();
		
		//set the fontSize, tabIndentSize, characterLinePlacement, and fileName
		setPrimitiveValues();
		
		//setup of JTextArea
		setupTextArea();
		
		addLineArea();
		
		//add settings components of JFrame
		addSettingsComponents();
		
		//crearing the main panel for the project
		mainPanel = new JPanel();
		mainScrollPane = new JScrollPane(mainPanel);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(lineNumber, BorderLayout.WEST);
		mainPanel.add(noteArea, BorderLayout.CENTER);
		
		//adding a document listener, key listener, and showing the window
		noteArea.getDocument().addDocumentListener(this);
		this.addKeyListener(this);
		this.getContentPane().add(mainScrollPane, BorderLayout.CENTER);
		this.add(settingsPanel, BorderLayout.NORTH);
		this.setVisible(true);
	}
	
	//basic construction of the window
	public void addWindowBasics() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		defaultWindowSize = new Dimension(1280,720);
		minWindowSize = new Dimension(900,600);
		this.setSize(defaultWindowSize);
		this.setMinimumSize(minWindowSize);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("BitNotesLogo.png"));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//this helps show the total number of linesin the program
	public void addLineArea(){
		lineNumber = new JTextArea("1");
		lineNumber.setFont(font);
		lineNumber.setBackground(Color.DARK_GRAY);
		lineNumber.setForeground(new Color(0,128,0));
		lineNumber.setEditable(false);
	}

	public void addPanelsAndLayouts() {
		//create JPanel object
		settingsPanel = new JPanel();
		
		//create Layout management object
		settingsFlow = new FlowLayout();

		
		//add Layout to JPanel
		settingsPanel.setLayout(settingsFlow);
	}
	
	//sets the standard primitive types 
	public void setPrimitiveValues() {
		fontSize = 16;
		tabIndentSize = 4;
		Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < 16){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        
        fileExtension = ".txt";

        genericFileName = "0x" + sb.toString() + fileExtension;
	}

	//creates the notepad area
	public void setupTextArea(){
		Color textColor = new Color(0,128,0);
		font = new Font("Courier Std", Font.PLAIN, fontSize);
		noteArea = new JTextArea();
		scrolling = new JScrollPane(noteArea);
		noteArea.setFont(font);
		noteArea.setTabSize(tabIndentSize);
		noteArea.setBackground(Color.BLACK);
		noteArea.setForeground(textColor);
		noteArea.setSelectionColor(textColor);
		noteArea.setCaretColor(textColor);
		noteArea.addKeyListener(this);
		noteArea.setVisible(true);
		scrolling.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrolling.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	
	//add the setting Components to the frame
	public void addSettingsComponents(){
		
		//creating labels
		changeTabSizeLabel = new JLabel("Tab Size");
		fileNameLabel = new JLabel("File Name");
		fontSizeLabel = new JLabel("Font Size");
		fileType = new JLabel("File Type");
		
		//creating text fields
		changeTabSizeField = new JTextField(3);
		fileNameField = new JTextField(30);
		fontSizeTextField = new JTextField(3);
		//changeTabSizeField.addKeyListener(this);
		//fileNameField.addKeyListener(this);
		//fontSizeTextField.addKeyListener(this);
		
		//creating the JComboBox
		fileTypeChoice = new JComboBox();
		fileTypeChoice.addItem("Text File (*.txt)");
		fileTypeChoice.addItem("Java File (*.java)");
		fileTypeChoice.addItem("C File (*.c)");
		fileTypeChoice.addItem("Header File (*.h)");
		fileTypeChoice.addItem("C++ File (*.cpp)");
		fileTypeChoice.addItem("C# File (*.cs)");
		fileTypeChoice.addItem("Python File (*.py)");
		fileTypeChoice.addItem("PHP File (*.php)");
		fileTypeChoice.addItem("JavaScript File (*.js)");
		fileTypeChoice.addItem("JavaScript Object Notation File (*.json)");
		fileTypeChoice.addItem("Ruby File (*.rb)");
		fileTypeChoice.addItem("Perl File (*.pl)");
		fileTypeChoice.addItem("Visual Basic File (*.vb)");
		fileTypeChoice.addItem("Assembly File (*.asm)");
		fileTypeChoice.addItem("Objective C File (*.m)");
		fileTypeChoice.addItem("Swift File (*.swift)");
		fileTypeChoice.addItem("R File (*.r)");
		fileTypeChoice.addItem("SQL File (*.sql)");
		fileTypeChoice.addItem("Go File (*.go)");
		fileTypeChoice.addItem("LOLCODE File (*.lol)");
		fileTypeChoice.addItem("HyperText Markup Language File (*.html)");
		fileTypeChoice.addItem("eXtensible Markup Language File (*.xml)");
		fileTypeChoice.setSelectedIndex(0);
		fileTypeChoice.addActionListener(this);
		
		//adding default values to textFile
		changeTabSizeField.setText(Integer.toString(tabIndentSize));
		fileNameField.setText(genericFileName);
		fontSizeTextField.setText(Integer.toString(fontSize));
		
		//creating buttons
		updateButton = new JButton("Update File (Ctrl+U)");
		newFileButton = new JButton("New File (Ctrl+N)");
		openFileButton = new JButton("Open File (Ctrl+O)");
		
		//add ActionListeners to the buttons
		updateButton.addActionListener(this);
		newFileButton.addActionListener(this);
		openFileButton.addActionListener(this);
		
		//add the above components to the settingsPanel
		settingsPanel.add(newFileButton);
		settingsPanel.add(openFileButton);
		settingsPanel.add(changeTabSizeLabel);
		settingsPanel.add(changeTabSizeField);
		settingsPanel.add(fileNameLabel);
		settingsPanel.add(fileNameField);
		settingsPanel.add(fileTypeChoice);
		settingsPanel.add(fontSizeLabel);
		settingsPanel.add(fontSizeTextField);
		settingsPanel.add(updateButton);
		settingsPanel.addKeyListener(this);
	}
	
	
	//main funtion
	public static void main(String[] args) {
		new BitNotesMainFrame();
	}

	@Override
	//actionListener for the program
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().equals(newFileButton)){
			openNewTextFile();
		}
		
		if(e.getSource().equals(openFileButton)){
			openFile();
		}
		
		if(e.getSource().equals(updateButton)){
			
			updateFile();
			
		}
		
	}
	
	//opens a file from button press or control and O
	public void openFile(){
		fileChooser = new JFileChooser();
		int fileOption = fileChooser.showOpenDialog(this);
		File textFileToOpen = fileChooser.getSelectedFile();
		String filePath = textFileToOpen.getAbsolutePath();
		textFileName = textFileToOpen.getName();
		
		if(fileOption == JFileChooser.APPROVE_OPTION){
			try{
				Scanner sc = new Scanner(new FileReader(fileChooser.getSelectedFile().getPath()));
				
				while(sc.hasNext()){
					this.noteArea.append(sc.nextLine() + "\n");
				}
				
			}catch(IOException e1){
				e1.printStackTrace();
			}
		}
		
		fileNameField.setText(textFileName);
	}
	
	//opens a new window with the text file
	public void openNewTextFile() {
		new BitNotesMainFrame();
	}
	
	//updates the file and autosaves the file
	public void updateFile(){
		
		textFileName = fileNameField.getText();
		int breakPoint;
		for(int i = 0; i < textFileName.length(); i++){
			if(textFileName.charAt(i) == '.'){
				breakPoint = i;
				textFileName = textFileName.substring(0, breakPoint);
				break;	
			}
		}
		
		genericFileName = textFileName;
		
		Object selectedItem = fileTypeChoice.getSelectedItem();
		
		String item = selectedItem.toString();
		
		//choose what file type
		if(item.equals("Text File (*.txt)")){
			fileExtension = ".txt";
		}else if(item.equals("Java File (*.java)")){
			fileExtension = ".java";
		}else if(item.equals("C File (*.c)")){
			fileExtension = ".c";
		}else if(item.equals("Header File (*.h)")){
			fileExtension = ".h";
		}else if(item.equals("C++ File (*.cpp)")){
			fileExtension = ".cpp";
		}else if(item.equals("C# File (*.cs)")){
			fileExtension = ".cs";
		}else if(item.equals("Python File (*.py)")){
			fileExtension = ".py";
		}else if(item.equals("PHP File (*.php)")){
			fileExtension = ".php";
		}else if(item.equals("JavaScript File (*.js)")){
			fileExtension = ".js";
		}else if(item.equals("JavaScript Object Notation File (*.json)")){
			fileExtension = ".json";
		}else if(item.equals("Ruby File (*.rb)")){
			fileExtension = ".rb";
		}else if(item.equals("Perl File (*.pl)")){
			fileExtension = ".pl";
		}else if(item.equals("Visual Basic File (*.vb)")){
			fileExtension = ".vb";
		}else if(item.equals("Assembly File (*.asm)")){
			fileExtension = ".asm";
		}else if(item.equals("Objective C File (*.m)")){
			fileExtension = ".m";
		}else if(item.equals("Swift File (*.swift)")){
			fileExtension = ".swift";
		}else if(item.equals("R File (*.r)")){
			fileExtension = ".r";
		}else if(item.equals("SQL File (*.sql)")){
			fileExtension = ".sql";
		}else if(item.equals("Go File (*.go)")){
			fileExtension = ".go";
		}else if(item.equals("LOLCODE File (*.lol)")){
			fileExtension = ".lol";
		}else if(item.equals("HyperText Markup Language File (*.html)")){
			fileExtension = ".html";
		}else if(item.equals("eXtensible Markup Language File (*.xml)")){
			fileExtension = ".xml";
		}
		
		fileNameField.setText(genericFileName + fileExtension);
		
		//updates tab size
		try{
			int tabSize = Integer.parseInt(changeTabSizeField.getText());
			tabIndentSize = tabSize;
			changeTabSizeField.setText(Integer.toString(tabIndentSize));
			noteArea.setTabSize(tabIndentSize);
		}catch(Exception e3){
			JOptionPane.showMessageDialog(this, "Please enter a number as your input for the tab size");
			changeTabSizeField.setText("");
		}
		
		//updates font size
		try{
			int newFontSize = Integer.parseInt(fontSizeTextField.getText());
			fontSize = newFontSize;
			font = null;
			font = new Font("Courier New", Font.PLAIN, fontSize);
			noteArea.setFont(font);
			lineNumber.setFont(font);
			fontSizeTextField.setText(Integer.toString(fontSize));
		}catch(Exception e4){
			JOptionPane.showMessageDialog(this, "Please enter a number as your input for the font");
			fontSizeTextField.setText("");
		}
		saveFile();	
		
	}

	@Override
	//from KeyListener
	public void keyTyped(KeyEvent e) {
		//does nothing
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if ((e.getKeyCode() == KeyEvent.VK_N) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            openNewTextFile();
        }
		
		if ((e.getKeyCode() == KeyEvent.VK_O) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            openFile();
        }
		
		if ((e.getKeyCode() == KeyEvent.VK_U) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            updateFile();
        }
	}
	
	//saves the file to the user's desktop
	public void saveFile(){
		String fileText = noteArea.getText();
		String mainFolder = System.getProperty("user.home");
		savedFile = new File
				(mainFolder + "\\Desktop\\" +  genericFileName + fileExtension);
				
		FileWriter fw;
		try {
			fw = new FileWriter(savedFile);
			fw.write(fileText);
			fw.flush();
			fw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

	@Override
	//from KeyListener
	public void keyReleased(KeyEvent e) {
		updateFile();
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		lineNumber.setText(getLineNumber());
		
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		lineNumber.setText(getLineNumber());
		
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		lineNumber.setText(getLineNumber());
		
		updateFile();
		
		
	}
	
	
	//fucntion that gets the line number of the thing
	public String getLineNumber(){
		int position = noteArea.getDocument().getLength();
		Element rootElement = noteArea.getDocument().getDefaultRootElement();
		String newLineNumber = "1" + System.getProperty("line.seperator") + "\n";
		
		for(int i = 2; i < rootElement.getElementIndex(position) + 2; i++){
			if(newLineNumber != null){
				newLineNumber += i + System.getProperty("line.seperator") + "\n";
			}
		}
		
		//gets rid of the word null at the end of every number
		return newLineNumber.replaceAll("null", "");	
	}
}