import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    // atribut frame
    int frameWidth = 360;
    int frameHeight = 640;

    // atribut skor
    JLabel skorLabel;
    int score = 0;

    // atribut gambar
    Image backgroundImage;
    Image birdImage;
    Image lowerPiperImage;
    Image upperPipeImage;

    // player
    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight =24;
    Player player;

    // atribut pipa
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    // timer dan gravitasi
    Timer gameLoop; // timer dengan satuan MilliSeconds.
    Timer pipesCooldown;
    int gravity = 1;

    // Variabel tambahan untuk status permainan
    private boolean isGameOver = false; // menandai apakah permainan sudah selesai
    private boolean isGameStarted = false; // menandai apakah permainan sudah dimulai

    // constructor
    public FlappyBird(){
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);

        // load image
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPiperImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        // setting label skor
        skorLabel = new JLabel(""); // pada awalnya kosong
        skorLabel.setFont(new Font("Arial", Font.BOLD, 20)); // set font arial dan bold, besar 20
        skorLabel.setForeground(Color.WHITE); // warna putih
        skorLabel.setHorizontalAlignment(JLabel.CENTER); // lokasi x ditengah
        skorLabel.setBounds(0, 30, frameWidth, 50); // lokasi y di atas
        setLayout(null); // tidak akan mengikuti perubahan window
        add(skorLabel); // menampilkan skor label

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage); // lokasi awal player
        pipes = new ArrayList<Pipe>(); // array list penampung pipa

        pipesCooldown = new Timer(4000, new ActionListener() { // seberapa sering pipa muncul dalam milisekon
            @Override
            public void actionPerformed(ActionEvent e) { // ketika spawn pipa, maka tulis
                System.out.println("pipa");
                placePipes(); // dan masukkan pipa ke layar
            }
        });
        pipesCooldown.start(); // jalankan timer pipa

        gameLoop = new Timer(1000/60, this); // fps dari game
        gameLoop.start(); // start fps
    }

    public void paintComponent(Graphics g){ // menggambar sesuai spesifikasi pada draw
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null); // gambar background

        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null); // gambar player

        for(int i = 0; i < pipes.size(); i++){ // array pipa
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), this); // gambar pipa
        }
        if (!isGameStarted) { // jika game belum dimulai
            g.setColor(Color.WHITE); // tulisan berwarna putih
            g.setFont(new Font("Arial", Font.BOLD, 24)); // arial, bold dan size 24
            g.drawString("Press SPACE to Start", frameWidth / 2 - 100, frameHeight / 2); // lokasi di tengah dengan tulisan "Press Space to start)
        }
    }

    public void move(){ // method mengurusi pergerakan pada layar
        player.setVelocityY(player.getVelocityY() + gravity); // player akan turun sesuai gravitas (makin kenceng)
        player.setPosY(player.getPosY() + player.getVelocityY()); // menurunkan posisi player sesuai kecepatan
        player.setPosY(Math.max(player.getPosY(), 0)); // player tidak bisa menembus bagian atas

        for(int i = 0; i < pipes.size();i++){ // cek setiap pipa
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX()); // pipa selalu bergerak ke kiri

            // menghapus pipa yang sudah off-screen
            if (pipe.getPosX() + pipe.getWidth() < 0) {
                pipes.remove(i); // menghapus pipa
                i--; // mengurangi index
                continue; // lanjut ke iterasi selanjutnya
            }

            if (!pipe.getPassed() && pipe.getPosX() + pipe.getWidth() < player.getPosX()) { // mengecek setiap posisi pipa dengan posisi burung, ketika terlewati maka skor nambah
                pipe.setPassed(true); // ubah flag jadi true
                score = score + 1; // tambah skor
                skorLabel.setText("Score: " + score); // update skor
            }
        }
    }

    public void placePipes(){ // method memasukkan pipa
        int randomPosY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2)); // posisi y random
        int openingSpace = frameHeight/4; // gap antar pipa


        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage); // lokasi pipa atas
        upperPipe.setPassed(false); // anggap ini yang dicek untuk skoring
        pipes.add(upperPipe); // masukkan pipa atas ke pipes

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingSpace + pipeHeight), pipeWidth, pipeHeight, lowerPiperImage);
        lowerPipe.setPassed(true); // ini dianggap sudah di pass, sehingga pipa atas saja yang terhitung
        pipes.add(lowerPipe); // masukkan lower pipes ke pipa
    }

    public void checkCollision() {
        Rectangle playerRect = new Rectangle(player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight()); // hitbox dari burung

        for (Pipe pipe : pipes) { // cek untuk setiap pipa
            Rectangle pipeRect = new Rectangle(pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight()); // dapatkan koordinat pipa yang berupa persegi panjang
            if (playerRect.intersects(pipeRect)) { // jika hitbox burung mengenai pipa, maka game over
                isGameOver = true;
                gameLoop.stop();
                pipesCooldown.stop();
                skorLabel.setText("Game Over! Press R to Restart");
            }
        }

        // Deteksi jatuh ke bawah
        if (player.getPosY() + player.getHeight() > frameHeight) {
            isGameOver = true;
            gameLoop.stop();
            pipesCooldown.stop();
            skorLabel.setText("Game Over! Press R to Restart");
        }
    }

    public void resetGame() { // reset flag
        isGameOver = false;
        isGameStarted = false;
        pipes = new ArrayList<>(); // kosongkan pipa
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage); // reset lokasi awal player
        score = 0;
        skorLabel.setText("Press SPACE to Start");

        gameLoop.start(); // jalankan timer game
        pipesCooldown.start(); // jalankan timer pipa

        repaint(); // gambar ulang
    }


    @Override
    public void actionPerformed(ActionEvent e){ // pengecekan setiap frame
        if (isGameStarted && !isGameOver) { // cek apakah masih berjalan dan belum game over
            move(); // maka masih bisa bergerak
            checkCollision(); // cek collision setiap frame
            repaint(); // refresh display per frame
        }
    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { // ketika spasi dipencet
            if (!isGameStarted) { // jika game belum dimulai
                isGameStarted = true; // ubah flag
                skorLabel.setText("Score: 0"); // set text
                return;
            }
            if (!isGameOver) { // jika game belum selesai
                player.setVelocityY(-10); // burung melompat
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_R && isGameOver) { // jika gameover, memencet r untuk restart
            resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e){

    }


}
