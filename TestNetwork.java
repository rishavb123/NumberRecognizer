package io.bhagat.projects.handwrittendigits;

import java.io.IOException;
import java.util.ArrayList;

import io.bhagat.ai.supervised.DataPoint;
import io.bhagat.ai.supervised.DataSet;
import io.bhagat.ai.supervised.NeuralNetwork;
import io.bhagat.util.SerializableUtil;
import io.bhagat.util.Timer; 

public class TestNetwork {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Timer t = new Timer();
		
		ArrayList<double[]> images = new ArrayList<>();
		ArrayList<double[]> labels = new ArrayList<>();
		
		System.out.println("Reading data . . .");
		t.start();
		
        ReadData.read("csv/test/images.csv", "csv/test/labels.csv", images, labels);
        
        System.out.println("Done Reading data: " + t.elapsed() + " ms");
        
        NeuralNetwork neuralNetwork = SerializableUtil.deserialize("mnist/network.ser");
        
        DataSet dataSet = new DataSet();
        
        System.out.println("Testing data . . .");
		t.start();
		
        for(int i = 0; i < images.size(); i++)
        	dataSet.add(new DataPoint(images.get(i), labels.get(i)));
        
        double accuracy = neuralNetwork.test(dataSet);
        
        System.out.println("Done Testing data: " + t.elapsed() + " ms\n");
        
        System.out.println("Accuracy: " + accuracy);
        
	}

}
