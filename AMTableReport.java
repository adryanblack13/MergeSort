/*
Author: Adrian Miller
Professor Didulo
CMSC 451: Design and analysis of Computer Algorithms
Project 1 : Benchmark Algorithm
Selected Algorithm: Mergesort

Classes/interfaces needed
Main
GetStats

purpose: To print out size and count of Iterative and Recursive benchmark of mergesort algorithm
*/


import java.awt.BorderLayout;
import java.io.File;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class AMTableReport {

	public static void main(String[] args) throws Exception {
		//Let the user select the input file
		JFileChooser fc = new JFileChooser();
		int ans = fc.showOpenDialog(null);
		if(ans == JFileChooser.APPROVE_OPTION) {
			//This means user selected a file
			File file = fc.getSelectedFile();
			Scanner scan = new Scanner(file);
			//Init 2d-array of stats and values needed
			Object[][] values = new Object[10][5];
			Object[] columnNames = {"Size", "Avg Count", "Coef Count", "Avg Time", "Coef Time"};
			for(int i = 0; i < 10; i++) {
				String line = scan.nextLine().trim(); // 100 (192323,545) (.....)
				String[] parts = line.split(" "); // {"100", "(192323,545)","(324234,324)",....}
				values[i][0] = parts[0];
				int[] times = new int[50];
				int[] counts = new int[50];
				for(int j = 1; j <= 50; j++) {
					String s = parts[j].substring(1,parts[j].length()-1); // "192323,545" -> s
					String[] v = s.split(","); // ["192323","545"]
					times[j-1] = Integer.parseInt(v[0]);
					counts[j-1] = Integer.parseInt(v[1]);
				}
				//Store stats for times and counts
				double[] timeStats = getStats(times);
				double[] countStats = getStats(counts);
				values[i][1] = countStats[0];
				values[i][2] = countStats[1]*100+"%";
				values[i][3] = timeStats[0];
				values[i][4] = timeStats[1]*100+"%";
			}
			//We're ready to show the table!
			JTable table = new JTable(values, columnNames);
			JScrollPane scroll = new JScrollPane(table);
			table.setFillsViewportHeight(true);
			JFrame frame = new JFrame();
			frame.setLayout(new BorderLayout());
			frame.add(scroll, BorderLayout.CENTER);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} else {
			System.out.println("File not selected."); 
		}
	}

	//Return the stats of the given input array, mean and coefficient of variance
	public static double[] getStats(int[] list) {
		//Find average
		double sum = 0;
		for(int i = 0; i < list.length; i++) {
			sum += list[i]; 
		}
		double avg = sum / list.length;
		//Find standard deviation
		sum = 0;
		for(int i = 0; i < list.length; i++) {
			sum += Math.pow(list[i] - avg, 2);
		}
		double std = Math.sqrt(sum/(list.length-1));
		double[] result = new double[2];
		result[0] = ((double)((int)(avg*100)))/100;  //5.3424234 -> 534.24234 -> 534 -> 5.34
		result[1] = ((double)((int)(std/avg*10000)))/10000;
		return result;
	}
}