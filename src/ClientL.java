import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.net.Socket;

public class ClientL extends JFrame {
    static int WINDOW_HEIGHT = 550;
    static int WINDOW_WIDTH = 900;

    Socket socket = null;
    InputStreamReader isr = null;
    OutputStreamWriter osw = null;
    BufferedReader br = null;
    BufferedWriter bw = null;
    String data = "100";

    ClientL(){
        DrawingPane p = new DrawingPane();
        add(p);

        /*
        this just takes the mouse Y coordinates
         */
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }
            @Override
            public void mouseMoved(MouseEvent e) {
                data = String.valueOf(e.getY()-100);

            }
        });


        /*
        this portion is responsible for creating the frame for the client
         */
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.black);
        setVisible(true);
        setResizable(false);


        /*
        this sends the mouse Y coordinate values to the server to process and receives
        the coordinate values of the necessary elements which it then offloads to the
        DrawingPane class to draw on screen. This refreshes every time the server sends
        the data and the cycle completes 60 times every second which is controlled by
        the servers tick rate.
         */
        try {
            socket = new Socket("localhost", 1234);
            osw = new OutputStreamWriter(socket.getOutputStream());
            bw = new BufferedWriter(osw);
            isr = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(isr);
            String receivedCoordinates;
            while (true) {
                bw.write(data);
                bw.newLine();
                bw.flush();
                receivedCoordinates = br.readLine();
                if(receivedCoordinates!=null){
                    String s[] = receivedCoordinates.split(" ");
                    p.setDrawingData(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2]),Integer.parseInt(s[3]),Integer.parseInt(s[4]),Integer.parseInt(s[5]));
                    repaint();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String args[]) {
        new ClientL();
    }
}


