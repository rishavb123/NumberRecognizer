package io.bhagat.projects.handwrittendigits;


import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import io.bhagat.ai.supervised.NeuralNetwork;
import io.bhagat.util.SerializableUtil;

public class VisualNetworkTest {

	public static final String root = "C:\\Users\\Bhagat\\Downloads\\mnist_png\\mnist_png\\mnist_png\\all\\";
	
	private static SimplePanel simplePanel;
	private static NeuralNetwork neuralNetwork;
	
	private static int correct;
	private static int total;
	
	public static void main(String[] args) throws IOException {
		ArrayList<double[]> images = new ArrayList<>();
		ArrayList<Integer> labels = new ArrayList<>();
        
        Scanner imageScanner = new Scanner(new File("files/csv/test/images.csv"));
        Scanner labelScanner = new Scanner(new File("files/csv/test/labels.csv"));
		
        correct = 0;
        total = 0;
        
		while(imageScanner.hasNext()){
        	String[] stringImageArr = imageScanner.next().split(",");
        	double[] imageArr = new double[784];
        	
        	for(int i = 0; i < 784; i++)
        	{
        		imageArr[i] = Integer.parseInt(stringImageArr[i]) / 255.0;
        	}		
        	images.add(imageArr);
        	labels.add(Integer.parseInt(labelScanner.next()));
        }
		
		imageScanner.close();
		labelScanner.close();
		
		try {
			neuralNetwork = SerializableUtil.deserialize("mnist/network.ser");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		int index = (int)(Math.random()*images.size());
		double[] outputs = neuralNetwork.feedForward(images.get(index));
		int guess = 0;
		for(int i = 1; i < outputs.length; i++)
			if(outputs[i] > outputs[guess])
				guess = i;
		
		if(guess == (int) labels.get(index))
			correct++;
		total++;
		
		simplePanel = new SimplePanel(root + index + ".png", "Guess: "+guess+"\nAnswer: "+labels.get(index)+"\nCorrect: "+correct+"\nTotal: "+total+"\nAccuracy: " + (Math.round(10000.0 * correct / total) / 100.0) + "%");
		JFrame frame = new JFrame("Test");
		frame.setPreferredSize(new Dimension(300, 300));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextArea area=new JTextArea("Welcome to javatpoint");  
        frame.getContentPane().add(area);  
        frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int index = (int)(Math.random()*images.size());
				double[] outputs = neuralNetwork.feedForward(images.get(index));
				int guess = 0;
				for(int i = 1; i < outputs.length; i++)
					if(outputs[i] > outputs[guess])
						guess = i;
				
				if(guess == (int) labels.get(index))
					correct++;
				total++;
				
				simplePanel.setImage(root + index + ".png");
				simplePanel.setStr("Guess: "+guess+"\nAnswer: "+labels.get(index)+"\nCorrect: "+correct+"\nTotal: "+total+"\nAccuracy: " + (Math.round(10000.0 * correct / total) / 100.0) + "%");
				simplePanel.repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
        	
        });
		frame.getContentPane().add(simplePanel);
		frame.pack();
		frame.setVisible(true);
	}
	
}
