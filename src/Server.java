
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    ServerSocket sessionInstance;


    Server() throws IOException {
        sessionInstance = new ServerSocket(1234);

        while (true) {
            Socket player1 = sessionInstance.accept();
            Socket player2 = sessionInstance.accept();


            new Thread(()->{

                double frameRate = 30.0;
                double timePerFrame = 1000000000.0/frameRate;
                double lastFrame = 0;
                int score1 = 0;
                int score2 = 0;

                int ballX = 0;
                int ballY = 0;
                AtomicInteger player1paddle = new AtomicInteger();
                AtomicInteger player2paddle = new AtomicInteger();
                BallLogic b= new BallLogic();




                while(true) {

                    if (System.nanoTime() - lastFrame >= timePerFrame) {
                        lastFrame = System.nanoTime();
                        new Thread(()->{

                            try {

                                player1paddle.set(ConnectionHandler.getCoordinatesFromPlayer(player1));


                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }).start();
                        new Thread(()->{
                            try {

                                player2paddle.set(ConnectionHandler.getCoordinatesFromPlayer(player2));

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }).start();
                        int paddle1 = player1paddle.get();
                        int paddle2 = player2paddle.get();



                        /*
                        This whole portion sends off the received coordinate data to the ballLogic class
                        and returns the updated coordinates to the clients
                         */
                        String s[] = b.setCoordinates(paddle1,paddle2,ballX,ballY,score1,score2).split(" ");
                        paddle1 = Integer.parseInt(s[0]);
                        paddle2 = Integer.parseInt(s[1]);
                        ballX = Integer.parseInt(s[2]);
                        ballY = Integer.parseInt(s[3]);
                        score1 = Integer.parseInt(s[4]);
                        score2 = Integer.parseInt(s[5]);

                        ConnectionHandler.sendTheUpdatedCoordinates(player1, paddle1, paddle2, ballX, ballY,score1,score2);
                        ConnectionHandler.sendTheUpdatedCoordinates(player2, paddle2, paddle1, ballX, ballY,score1,score2);
                    }

                }


            }).start();
        }



    }

    public static void main(String[] args) throws IOException {
        new Server();

    }


}
