package SeleniumTestPackage;

import java.util.*;
import java.io.*;
import java.net.URI;
import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GUI {
	
	public JFrame mainFrame;
	public JPanel mainPanel;
	
	public JPanel rowPanel;
	
	public JLabel profileLabel;
	public JTextField profileEntry;
	public JLabel driverLabel;
	public JTextField driverEntry;
	public JLabel userdataLabel;
	public JTextField userdataEntry;
	
	public JLabel rowLabel;
	public JTextField rowEntry;
	public JButton rowEntryEnter;
	public JButton rowEntryClear;
	
	public JPanel parsedIDPanel;
	public JLabel parsedIDLabel;
	public JTextArea parsedIDEntry;
	public JScrollPane parsedIDEntryScroll;
	
	public JPanel collectorPanel;
	public JLabel collectorLabel;
	public JTextArea collectorOutput;
	public JScrollPane collectorOutputScroll;
	
	public JPanel scriptPanel;
	public JLabel scriptLabel;
	public JTextArea scriptOutput;
	public JScrollPane scriptOutputScroll;
	
	
	public GUI() {
		mainPanel = new JPanel();
		mainPanel.setLayout(
			new GridLayout(4,1));
			//new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		rowPanel = new JPanel(new FlowLayout(3));
		//rowPanel.setBorder(new EmptyBorder(0,0,0,0));
		mainPanel.add(rowPanel);
		parsedIDPanel = new JPanel(new FlowLayout(3));
		mainPanel.add(parsedIDPanel);
		collectorPanel = new JPanel(new FlowLayout(3));
		mainPanel.add(collectorPanel);
		scriptPanel = new JPanel(new FlowLayout(3));
		mainPanel.add(scriptPanel);
		
		userdataLabel = new JLabel("Chrome 'User Data' Folder Location");
		userdataLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		rowPanel.add(userdataLabel);
		userdataEntry = new JTextField(15);
		userdataEntry.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rowPanel.add(userdataEntry);
		
		profileLabel = new JLabel("Chrome Profile Name: ");
		profileLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		rowPanel.add(profileLabel);
		profileEntry = new JTextField(12);
		profileEntry.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rowPanel.add(profileEntry);
		
		driverLabel = new JLabel("'chromedriver.exe' Location: ");
		driverLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		rowPanel.add(driverLabel);
		driverEntry = new JTextField(42);
		driverEntry.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rowPanel.add(driverEntry);
		
		rowLabel = new JLabel("Row Data: ");
		rowLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		rowPanel.add(rowLabel);
		
		rowEntry = new JTextField(45);
		rowEntry.setFont(new Font("Tahoma", Font.PLAIN, 20));
		rowEntry.addActionListener(action -> {
			ParseEnter();
		});
		rowPanel.add(rowEntry);
		
		rowEntryEnter = new JButton("Enter");
		rowEntryEnter.setFont(new Font("Tahoma", Font.BOLD, 16));
		rowEntryEnter.addActionListener(action -> {
			ParseEnter();
		});
		rowPanel.add(rowEntryEnter);
		
		rowEntryClear = new JButton("Clear");
		rowEntryClear.setFont(new Font("Tahoma", Font.BOLD, 16));
		rowEntryClear.addActionListener(action -> {
			ParseClear();
		});
		rowPanel.add(rowEntryClear);
		
		parsedIDLabel = new JLabel("Item Links: ");
		parsedIDLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		parsedIDPanel.add(parsedIDLabel);
		
		parsedIDEntry = new JTextArea(8, 55);
		parsedIDEntry.setFont(new Font("Tahoma", Font.PLAIN, 16));
		parsedIDEntryScroll = new JScrollPane(parsedIDEntry);
		parsedIDPanel.add(parsedIDEntryScroll);
		
		collectorLabel = new JLabel("Status: ");
		collectorLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		collectorPanel.add(collectorLabel);
		
		collectorOutput = new JTextArea(8, 55);
		collectorOutput.setFont(new Font("Tahoma", Font.PLAIN, 16));
		collectorOutputScroll = new JScrollPane(collectorOutput);
		collectorPanel.add(collectorOutputScroll);
		
		scriptLabel = new JLabel(
			"Script (CTRL+A, Copy, & Paste into Studio Command Line): ");
		scriptLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		scriptPanel.add(scriptLabel);
		
		scriptOutput = new JTextArea(8, 55);
		scriptOutput.setFont(new Font("Tahoma", Font.PLAIN, 16));
		scriptOutputScroll = new JScrollPane(scriptOutput);
		scriptPanel.add(scriptOutputScroll);
		
		mainFrame = new JFrame("Collector");
		mainFrame.setBounds(50, 50, 100, 100);
		mainFrame.setSize(800, 850);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().add(mainPanel);
		
		mainFrame.setVisible(true);
	}
	
	public void LoadConfigurations() {
		File config = new File(
			"config.txt" // src/SeleniumTestPackage/
		);
		Scanner scan = null;
		try {
			scan = new Scanner(config);
		} catch (FileNotFoundException e) {
			collectorOutput.setText("config.txt not found");
		}
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			
			if (line.contains("userdataPath") && userdataEntry != null)
				userdataEntry.setText(scan.nextLine());
			else if (line.contains("profileName") && profileEntry != null)
				profileEntry.setText(scan.nextLine());
			else if (line.contains("driverPath") && driverEntry != null)
				driverEntry.setText(scan.nextLine());
		}
	}
	
	public void ParseEnter() {
		if (userdataEntry != null && userdataEntry.getText() != ""
		&& driverEntry != null && driverEntry.getText() != ""
		&& profileEntry != null && profileEntry.getText() != ""
		&& parsedIDEntry != null) {
			String[] URLList = AssetIDParser.Parse(rowEntry.getText());
			String URLListDisplay = "";
			String outputListDisplay = "";
			
			mainFrame.setTitle("Collector - Running...");
			
			if (URLList.length > 0) {
				for (int i=0; i<URLList.length; i++) {
					URLListDisplay += URLList[i] + ", \n";
				}
				parsedIDEntry.setText(URLListDisplay);
				
				String[] outputList = Collector.collectItems(
					profileEntry.getText(), 
					userdataEntry.getText(),
					driverEntry.getText(),
					URLList);
				
				for (int i=0; i<outputList.length; i++) {
					outputListDisplay += outputList[i] + "\n";
				}
				collectorOutput.setText(outputListDisplay);
				
				scriptOutput.setText(generateScript(URLList));
			}
			
			mainFrame.setTitle("Collector");
		}
	}
	
	public String generateScript(String[] URLList) {
		File luaScript = new File(
			"FreeModelScanner.lua" // src/SeleniumTestPackage/
		);
		Scanner scan = null;
		try {
			scan = new Scanner(luaScript);
		} catch (FileNotFoundException e) {
			System.out.println("no file found");
		}
		String newScript = "";
		String listToInsert = "";
		
		if (URLList.length > 0) {
			for (int i=0; i<URLList.length; i++) {
				listToInsert += "\"" + URLList[i] + "\",\n";
			}
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String newLine = line;
				
				if (line.contains("--0!replace")) {
					newLine = line.replace(line, listToInsert);
				}
				
				newScript += newLine + "\n";
			}
		}
		return newScript;
	}
	
	public void ParseClear() {
		if (rowEntry != null) {
			rowEntry.setText("");
		}
	}
	
}
