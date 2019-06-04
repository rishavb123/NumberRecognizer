package io.bhagat.numberrecognizer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private DrawSurface drawSurface;
    private Client client;

    private volatile Object sendObject;

    private static final String serverIp = "server.bhagat.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawSurface = new DrawSurface(this);
        setContentView(drawSurface);

        runAsync(new Runnable() {
            @Override
            public void run() {
                client = new Client(serverIp, 2000);
                client.setCallback(new Function<Client, Object>() {
                    public Object f(Client client) {
                        drawSurface.setGuess((int)(client.read()));
                        return null;
                    }
                });
                client.start();

                runAsync(new Runnable() {
                    @Override
                    public void run() {
                        while(true) {
                            if(sendObject != null)
                            {
                                client.send(sendObject);
                                sendObject = null;
                            }
                        }
                    }
                });

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.close();
    }

    public void send(final Object obj)
    {
        sendObject = obj;
    }

    public void runAsync(Runnable runnable)
    {
        new RunAsync().execute(runnable);
    }

    class RunAsync extends AsyncTask<Runnable, Void, Void> {

        @Override
        protected Void doInBackground(Runnable... runnables) {

            runnables[0].run();

            return null;
        }
    }

}
