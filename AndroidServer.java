import java.io.IOException;

import io.bhagat.ai.supervised.NeuralNetwork;
import io.bhagat.math.Function;
import io.bhagat.server.Server;
import io.bhagat.server.Server.ConnectionIndex;
import io.bhagat.util.SerializableUtil;

public class AndroidServer {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Server server = new Server(2000, 10);
		NeuralNetwork neuralNetwork;
		neuralNetwork = SerializableUtil.deserialize("network.ser");
		server.setCallback(new Function<ConnectionIndex, Object>() {

			@Override
			public Object f(ConnectionIndex x) {
				double[] outputs = neuralNetwork.feedForward((double[]) x.getObject());
				Integer guess = 0;
				for(int i = 1; i < outputs.length; i++)
					if(outputs[i] > outputs[guess])
						guess = i;
				return guess;
			}
			
		});
		server.start();
	}

}
