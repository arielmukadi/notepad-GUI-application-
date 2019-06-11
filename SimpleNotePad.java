/* Ariel Mukadi
 *  
 * CS 410 Software Engineering - Spring 2019
 * 
 * Assignment 03
 * 
 * This is a java implementation of a simple notepad GUI application 
 * 
 * */


package assigment;
import javax.swing.*;

import javax.swing.text.Position;

import javax.swing.text.StyledDocument;
import javax.swing.text.View;

import java.awt.*;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.print.PageFormat;

import java.awt.print.Printable;

import java.awt.print.PrinterException;

import java.awt.print.PrinterJob;

import java.io.*;

import java.nio.file.Path;

import java.nio.file.Paths;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.Scanner;





public class SimpleNotePad extends JFrame implements ActionListener{

	// Creating my Arraylist to keep the files 

	ArrayList<JMenuItem> rFile =new ArrayList<JMenuItem>();

	

	// Creating button
	JTextArea textInput;

	JMenuBar barMenu = new JMenuBar();

	JMenu fileMenu = new JMenu("File");  

	JMenu editMenu = new JMenu("Edit");

	JMenu openRecent = new JMenu("Recent");

	JTextPane noteArea = new JTextPane(); 

	JMenuItem openFileMenu = new JMenuItem("Open");
	
	JMenuItem printFileMenu = new JMenuItem("Print File"); 
	
	JMenuItem saveFileMenu = new JMenuItem("Save File"); 
	
	JMenuItem newFileMenu = new JMenuItem("New File"); 


	JMenuItem copyMenu = new JMenuItem("Copy"); 
	
	JMenuItem pasteMenu = new JMenuItem("Paste");
	

	JMenuItem ReplaceMenu = new JMenuItem("replace");






	private Object textArea;



	private SimpleNotePad() {

		setTitle("A Simple Notepad Tool");

		fileMenu.add(openFileMenu);
		
		fileMenu.addSeparator();
		
		fileMenu.add(printFileMenu);
		
		fileMenu.addSeparator();
		
		fileMenu.add(saveFileMenu);
		
		fileMenu.addSeparator();
		
		fileMenu.add(newFileMenu);
		
		fileMenu.addSeparator();  
		
		fileMenu.add(openRecent); 

		fileMenu.addSeparator();  


		//editMenu.add(undoMenu);
		editMenu.add(ReplaceMenu);

		editMenu.add(pasteMenu);
		
		editMenu.add(copyMenu);



		newFileMenu.addActionListener(this);	

		newFileMenu.setActionCommand("new");

		saveFileMenu.addActionListener(this);

		saveFileMenu.setActionCommand("save");

		printFileMenu.addActionListener(this);

		printFileMenu.setActionCommand("print");

		copyMenu.addActionListener(this);

		copyMenu.setActionCommand("copy");

		pasteMenu.addActionListener(this);

		pasteMenu.setActionCommand("paste");

		openFileMenu.addActionListener(this);

		openFileMenu.setActionCommand("Open File");

		openRecent.addActionListener(this); 

		openRecent.setActionCommand("Recent");

		ReplaceMenu.addActionListener(this); 

		ReplaceMenu.setActionCommand("replace");





		barMenu.add(fileMenu);

		barMenu.add(editMenu);



		setJMenuBar(barMenu);



		add(new JScrollPane(noteArea));



		setPreferredSize(new Dimension(600,600));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);



		pack();	

	}



	public static void main(String[] args) {

		SimpleNotePad app = new SimpleNotePad();

	}

	private void pasteImplementation() {

		StyledDocument doc = noteArea.getStyledDocument();

		//Position position = doc.getEndPosition();

		noteArea.paste();	

	}



	@Override

	// EveryTime there is a button clicked , it checks here

	public void actionPerformed(ActionEvent event) {

		if(event.getActionCommand().equals("new")) {

			noteArea.setText("");

		}

		else if(event.getActionCommand().equals("save")) { 	

			File fileToWrite = null;

			JFileChooser fc = new JFileChooser();	

			int returnVal = fc.showSaveDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) 

				fileToWrite = fc.getSelectedFile();

			String filename = fileToWrite.toString();

			JMenuItem recent = new JMenuItem(filename);

			System.out.println("We have the file "+filename);

			recent.setActionCommand(filename);

			recent.addActionListener(this);

			rFile.add(recent);

			openRecent.add(recent);

			try {

				PrintWriter out = new PrintWriter(new FileWriter(fileToWrite));

				out.println(noteArea.getText());

				JOptionPane.showMessageDialog(null, "File is saved successfully...");

				out.close();

			} catch (IOException ex) {



			}

		}else if(event.getActionCommand().equals("print")) { 	

			try{

				PrinterJob pjob = PrinterJob.getPrinterJob();

				pjob.setJobName("Sample Command Pattern");

				pjob.setCopies(1);

				pjob.setPrintable(new Printable() {



					public int print(Graphics pg, PageFormat pf, int pageNum) {

						if (pageNum>0)		

							return Printable.NO_SUCH_PAGE;	



						pg.drawString(noteArea.getText(), 500, 500);	

						paint(pg);



						return Printable.PAGE_EXISTS;

					}

				});



				if (pjob.printDialog() == false)	

					return;				



				pjob.print();			 

			} catch (PrinterException pe) { 

				JOptionPane.showMessageDialog(null,

						"Printer error" + pe, "Printing error", 

						JOptionPane.ERROR_MESSAGE);
		}
			}	

	

		else if(event.getActionCommand().equals("replace")) {
			//pasteImplementation();
			
			String find = noteArea.getSelectedText();
			String replace = "";

			if(!find.isEmpty()) {
			replace = JOptionPane.showInputDialog("Replace or insert with");
			int n = JOptionPane.showConfirmDialog(noteArea,"Are you sure you want to replace?",
			"View carefully", JOptionPane.YES_NO_OPTION);	

			if (n == JOptionPane.YES_OPTION) {
			noteArea.replaceSelection(replace);
			} else if (n == JOptionPane.NO_OPTION) {
			return;
			} 

		}}

		else if(event.getActionCommand().equals("paste")) { 

			pasteImplementation();

		}else if(event.getActionCommand().equals("ok")) { 

			String text=noteArea.getSelectedText();
			String replace1=textInput.getText();
			
			if(text!=null){
				replace1=textInput.getText().replaceAll(text, replace1);
				noteArea.setText(replace1);
			}

		}

		else if(event.getActionCommand().equals("Open File")) {

			JFileChooser open = new JFileChooser();  

			int option = open.showOpenDialog(this);

			if (option == JFileChooser.APPROVE_OPTION) {

				File file = null;

				FileReader filer = null;

				try {

					file = open.getSelectedFile();

					String filepath = file.getAbsolutePath();

					noteArea.setText(filepath);

					filer = new FileReader(file);

					JMenuItem newItem = new JMenuItem(filepath);

					newItem.setActionCommand(filepath);

					newItem.addActionListener(this);

					rFile.add(newItem);

					openRecent.add(newItem);

					noteArea.read(filer,"");

				} catch (Exception ex) {

					System.out.println(ex.getMessage());

				}

			}

		}

		else {

			for(JMenuItem recent : rFile) {

				if(event.getActionCommand().equals(recent.getText())) {

					FileReader filer = null;

					File file = null;

					try {

						file = new File(event.getActionCommand());

						filer = new FileReader(file);

						noteArea.read(filer, "");

					}

					catch(Exception ex) {

						System.out.println(ex.getMessage());

					}

				}

			}

}



	}

}