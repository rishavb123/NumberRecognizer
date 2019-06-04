package io.bhagat.projects.handwrittendigits;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import io.bhagat.ai.supervised.NeuralNetwork;
import io.bhagat.util.Timer;

public class TrainNetwork {

	public static void main(String[] args) throws FileNotFoundException {
				
		Timer totalTimer = new Timer();
		totalTimer.start();
		
		Timer t = new Timer();
		
		ArrayList<double[]> images = new ArrayList<>();
		ArrayList<double[]> labels = new ArrayList<>();
		
		System.out.println("Reading data . . .");
		t.start();
		
        ReadData.read("csv/train/images.csv", "csv/train/labels.csv", images, labels);
        
        System.out.println("Done Reading data: " + t.elapsed() + " ms");
        
        NeuralNetwork neuralNetwork = new NeuralNetwork(784, 64, 10);
        
        System.out.println("Training . . .");
        t.restart();
        for(int i = 0; i < images.size(); i++)
        	neuralNetwork.train(images.get(i), labels.get(i));
        System.out.println("Done Training: " + t.elapsed() + " ms");
         
        System.out.println("Serializing . . .");
        t.restart();
        neuralNetwork.serialize("mnist/network.ser");
        System.out.println("Done Serializing: " + t.elapsed() + " ms");
        
        System.out.println("Total Program Done: " + totalTimer.elapsed() + " ms");
        
	}

}
