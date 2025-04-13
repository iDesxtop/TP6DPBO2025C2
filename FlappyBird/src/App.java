import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args){
        // membuat settingan frame
        JFrame frame = new JFrame("Flappy Bird"); // title flappy bird

        // buat objek JPanel
        FlappyBird flappyBird = new FlappyBird();



        frame.add(flappyBird);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Jika ingin mempertahankan ukuran yang sudah ditetapkan, jangan gunakan pack()
         frame.pack();

        frame.setVisible(true);
        flappyBird.requestFocus();
    }
}