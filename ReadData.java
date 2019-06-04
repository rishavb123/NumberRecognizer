package io.bhagat.projects.handwrittendigits;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadData {

	public static void read(String imagesFile, String labelsFile, ArrayList<double[]> images, ArrayList<double[]> labels) throws FileNotFoundException {

		Scanner imageScanner = new Scanner(new File("files/" + imagesFile));
		Scanner labelScanner = new Scanner(new File("files/" + labelsFile));
		
		while(labelScanner.hasNext()){
        	String[] stringImageArr = imageScanner.next().split(",");
        	double[] imageArr = new double[784];
        	
        	for(int i = 0; i < 784; i++)
        		imageArr[i] = Integer.parseInt(stringImageArr[i]) / 255.0;
        	
        	double[] labelArr = new double[10];
        	labelArr[Integer.parseInt(labelScanner.next())] = 1;
        	
        	images.add(imageArr);
        	labels.add(labelArr);
        }
		
		imageScanner.close();
		labelScanner.close();
		
	}
	
}
