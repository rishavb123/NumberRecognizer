package io.bhagat.projects.handwrittendigits;

import java.io.IOException;
import java.util.ArrayList;

import io.bhagat.ai.supervised.NeuralNetwork;
import io.bhagat.ai.unsupervised.PrincipalComponentAnalysis;
import io.bhagat.math.linearalgebra.Matrix;
import io.bhagat.util.ArrayUtil;
import io.bhagat.util.SerializableUtil;
import io.bhagat.util.Timer;

public class TrainNetworkWithPCA {

	public static void main(String[] args) throws IOException {
		
		Timer totalTimer = new Timer();
		totalTimer.start();
		
		Timer t = new Timer();
		
		ArrayList<double[]> images = new ArrayList<>();
		ArrayList<double[]> labels = new ArrayList<>();
		
		System.out.println("Reading data . . .");
		t.start();
		
        ReadData.read("csv/train/images.csv", "csv/train/labels.csv", images, labels);
        
        System.out.println("Done Reading data: " + t.elapsed() + " ms");
        
        System.out.println("Reducting dimensions of input data . . .");
        t.restart();
        
        Matrix inputs = new Matrix(ArrayUtil.newArrayFromArrayList(images, new double[images.size()][images.get(0).length]));
        PrincipalComponentAnalysis pca = new PrincipalComponentAnalysis(inputs);
        Matrix newInputs = pca.dimensionReduction();
        double[][] inputArr = newInputs.getData();
                
        NeuralNetwork neuralNetwork = new NeuralNetwork(inputArr[0].length, 64, 10);
        
        System.out.println("Done Reducing Dimensions to " + inputArr[0].length + ": " + t.elapsed() + " ms");
        
        System.out.println("Training . . .");
        t.restart();
        for(int i = 0; i < inputArr.length; i++)
        	neuralNetwork.train(inputArr[i], labels.get(i));
        System.out.println("Done Training: " + t.elapsed() + " ms");
         
        System.out.println("Serializing . . .");
        t.restart();
        neuralNetwork.serialize("mnist/network-pca.ser");
        SerializableUtil.serialize(pca, "mnist/pca.ser");
        System.out.println("Done Serializing: " + t.elapsed() + " ms");
        
        System.out.println("Total Program Done: " + totalTimer.elapsed() + " ms");

	}

}
