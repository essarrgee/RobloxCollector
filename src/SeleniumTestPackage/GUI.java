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
	public JLabel rowLabel;
	public JTextField rowEntry;
	public JButton rowEntryEnter;
	public JButton rowEntryClear;
	
	public JPanel parsedIDPanel;
	public JLabel parsedIDLabel;
	public JTextArea parsedIDEntry;
	
	public JPanel collectorPanel;
	public JLabel collectorLabel;
	public JTextArea collectorOutput;
	
	public JPanel scriptPanel;
	public JLabel scriptLabel;
	public JTextArea scriptOutput;
	
	
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
		
		rowLabel = new JLabel("Row Data: ");
		rowLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		rowPanel.add(rowLabel);
		
		rowEntry = new JTextField(55);
		rowEntry.setFont(new Font("Tahoma", Font.PLAIN, 16));
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
		
		parsedIDEntry = new JTextArea(10, 55);
		parsedIDEntry.setFont(new Font("Tahoma", Font.PLAIN, 16));
		parsedIDPanel.add(parsedIDEntry);
		
		collectorLabel = new JLabel("Status: ");
		collectorLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		collectorPanel.add(collectorLabel);
		
		collectorOutput = new JTextArea(10, 55);
		collectorOutput.setFont(new Font("Tahoma", Font.PLAIN, 16));
		collectorPanel.add(collectorOutput);
		
		scriptLabel = new JLabel(
			"Script (Run this in Studio Command Line): ");
		scriptLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		scriptPanel.add(scriptLabel);
		
		scriptOutput = new JTextArea(20, 55);
		scriptOutput.setFont(new Font("Tahoma", Font.PLAIN, 16));
		scriptPanel.add(scriptOutput);
		
		mainFrame = new JFrame("Collector");
		mainFrame.setBounds(50, 50, 100, 100);
		mainFrame.setSize(800, 800);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().add(mainPanel);
		
		mainFrame.setVisible(true);
	}
	
	public void ParseEnter() {
		if (parsedIDEntry != null) {
			String[] URLList = AssetIDParser.Parse(rowEntry.getText());
			String URLListDisplay = "";
			String outputListDisplay = "";
			
			mainFrame.setTitle("Collector - Running...");
			
			if (URLList.length > 0) {
				for (int i=0; i<URLList.length; i++) {
					URLListDisplay += URLList[i] + ", \n";
				}
				parsedIDEntry.setText(URLListDisplay);
				
				String[] outputList = Collector.collectItems(URLList);
				
				for (int i=0; i<outputList.length; i++) {
					outputListDisplay += outputList[i] + "\n";
				}
				collectorOutput.setText(outputListDisplay);
			}
			
			mainFrame.setTitle("Collector");
		}
	}
	
	public String generateScript(String[] URLList) {
		File luaScript = new File("FreeModelScanner.lua");
		Scanner scan = new Scanner(luaScript);
		String newScript = "";
		String listToInsert = "";
		
		if (URLList.length > 0) {
			for (int i=0; i<URLList.length; i++) {
				listToInsert += "\"" + URLList[i] + "\",";
			}
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String newLine = line;
				
				if (line.matches("*0!replace!0*")) {
					newLine = line.replace(line, listToInsert);
				}
				
				newScript += newLine;
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
