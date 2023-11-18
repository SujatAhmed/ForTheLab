
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler {

    public  Socket player1;
    public  Socket player2;

    public static InputStreamReader in;
    public static OutputStreamWriter out;
    public static BufferedWriter writer;
    public static BufferedReader reader;

    public static int getCoordinatesFromPlayer(Socket player) throws IOException {
        in = new InputStreamReader(player.getInputStream());
        reader = new BufferedReader(in);
        String s = reader.readLine();
        if(s!=null) return Integer.parseInt(s);
        else return 0;
    }
    public static void sendTheUpdatedCoordinates(Socket player, int paddleOfPlayer, int paddleOfOtherPlayer, int ballX, int ballY,int score1, int score2){
        try {
            out = new OutputStreamWriter(player.getOutputStream());
            writer = new BufferedWriter(out);
            String s = paddleOfPlayer+" "+paddleOfOtherPlayer+" "+ballX+" "+ballY+" "+score1+" "+score2;
            if(s!=null) {
                writer.write(s);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
