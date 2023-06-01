import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class View extends JPanel implements ActionListener {
    private Timer timer;
    private int NUMBEROFENEMIES = 10;
    private int bossstrength = 20;
    private int noofPowerUps = 1;
    private final int noofshield = 2;
    private int shieldstrength;
    public static final int INITIAL_LIVES = 2;
    public final int maxlevels = 6;
    private SpaceShip ship;
    private BossEnemy boss;
    int score, lives, level;
    private boolean gamestarted, newlevel,ingame,bosslevel,isbossdead,activeshield;

    boolean check = true;
    private List<Enemy> individualEnemies;
    private List<PowerUp> individualpowerups;
    private List<Shield> individualshiled;

    public View() {
        initializeView();
    }

    private void initializeView() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        level = 1;
        lives = INITIAL_LIVES;
        ingame = true;
        gamestarted = false;
        newlevel = false;
        bosslevel = false;
        isbossdead = false;
        activeshield =false;

        int ICRAFT_X = 0;
        int ICRAFT_Y = 0;
        int bosscraft_x = 690;
        int bosscraft_y = 175;
        ship = new SpaceShip(ICRAFT_X, ICRAFT_Y);
        boss = new BossEnemy(bosscraft_x, bosscraft_y,ship);

        initEnemy();
        initpowup();
        initshield();

        int DELAY = 10;
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void initEnemy() {

        individualEnemies = new ArrayList<>();

        if (newlevel) {
            NUMBEROFENEMIES += 5;
        }
        for (int i = 0; i < NUMBEROFENEMIES; i++) {
            int x = (int) ((Math.random() * 990) + 700);
            int y = (int) (Math.random() * 250);

            individualEnemies.add(new Enemy(x, y));
        }

    }

    public void initpowup() {

        individualpowerups = new ArrayList<>();

        if (newlevel){
            noofPowerUps+=1;
        }
        for (int i = 0; i < noofPowerUps; i++) {
            int x = (int) ((Math.random() * 990) + 700);
            int y = (int) (Math.random() * 250);

            individualpowerups.add(new PowerUp(x, y));
        }

    }

    public void initshield() {

        individualshiled = new ArrayList<>();

        for (int i = 0; i < noofshield; i++) {
            int x = (int) ((Math.random() * 990) + 700);
            int y = (int) (Math.random() * 250);

            individualshiled.add(new Shield(x, y));
        }

    }



    @Override
    public void actionPerformed(ActionEvent e) {

        updateBullets();
        updateSpaceShip();
        updateEnemies();
        updatePowerups();
        updateshield();
        checkCollision();
        repaint();


        if (bosslevel) {

            updateBossEnemy();
            updateBullets();
            updatePowerups();
            updateshield();
            updateSpaceShip();
            checkCollision();
            repaint();
        }


    }


    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        ImageIcon ii = new ImageIcon("src/Images/spaceback.jpeg");
        Image image = ii.getImage();
        g.drawImage(image, 0, 0, null);
        if (ingame && !gamestarted) {
            GameBegin(g);
        } else if (ingame) {
            drawObjects(g);
        } else if (bosslevel && !gamestarted) {
            GameBegin(g);
        } else if (bosslevel) {
            drawBosslevel(g);
        } else {
            try {
                GameOver(g);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void GameBegin(Graphics g) {
        int b_WIDTH = 700;
        int b_HEIGHT = 350;
        String msg = "\nHEINZ";
        String msg1 = "\nPress SPACEBAR to Fire  \ntowards Oncoming Inators";
        String msg4 = "\n Use ARROW KEYS to Control Ship";
        String msg2 = "\nRemember: \nDo NOT get close to the Inators";
        String msg3 = "\nPress ENTER to Begin";
        Font small = new Font("Helvetica", Font.BOLD, 10);
        FontMetrics fm = getFontMetrics(small);


        g.setColor(Color.white);
        g.setFont(small);
        if (bosslevel) {
            String bosslev = "\nBOSS LEVEL";
            String mmm = "\n Press ENTER to Continue";
            g.drawString(bosslev, (b_WIDTH - fm.stringWidth(bosslev)) / 2,
                    b_HEIGHT / 2);
            g.drawString(mmm, (b_WIDTH - fm.stringWidth(mmm)) / 2,
                    (b_HEIGHT / 2) + 10);
        } else {
            if (newlevel) {
                String newlev = "\nNEW LEVEL";
                String mmm = "\n Press ENTER to Continue";
                g.drawString(newlev, (b_WIDTH - fm.stringWidth(newlev)) / 2,
                        b_HEIGHT / 2);
                g.drawString(mmm, (b_WIDTH - fm.stringWidth(mmm)) / 2,
                        (b_HEIGHT / 2) + 10);


            } else {
                g.drawString(msg, (b_WIDTH - fm.stringWidth(msg)) / 2,
                        b_HEIGHT / 2);
                g.drawString(msg1, (b_WIDTH - fm.stringWidth(msg1)) / 2,
                        (b_HEIGHT / 2) + 10);
                g.drawString(msg4, (b_WIDTH - fm.stringWidth(msg4)) / 2,
                        (b_HEIGHT / 2) + 20);
                g.drawString(msg2, (b_WIDTH - fm.stringWidth(msg2)) / 2,
                        (b_HEIGHT / 2) + 30);
                g.drawString(msg3, (b_WIDTH - fm.stringWidth(msg3)) / 2,
                        (b_HEIGHT / 2) + 40);
            }
        }
    }

    private void GameOver(Graphics g) throws FileNotFoundException {

        if (level < maxlevels && lives > 0) {
            level++;
            if (level == maxlevels) {
                bosslevel = true;
                gamestarted = false;
                initshield();
                initpowup();
                drawBosslevel(g);
            } else {
                gamestarted = false;
                newlevel = ingame = true;
                bosslevel = false;
                initEnemy();
                initpowup();
                initshield();
                drawObjects(g);
            }
        } else {
            timer.stop();

            int b_WIDTH = 700;
            int b_HEIGHT = 350;
            Font small = new Font("Helvetica", Font.BOLD, 14);
            Font big = new Font("Times", Font.BOLD, 20);
            FontMetrics fm = getFontMetrics(small);

            String msg;
            String msg2;
            int tot = ((maxlevels * (maxlevels + 1)) * 5) + 25;//total number of enemies across  all levels

            if (score == tot || isbossdead) {
                msg = "Congratulations You Beat Doofensmirtz";
                msg2 = "Your score :" + score;
                g.setColor(Color.GREEN);

            } else {
                msg = "Game Over";
                msg2 = "Your Score :" + score;
                g.setColor(Color.white);
            }
            g.setFont(small);
            g.drawString(msg, (b_WIDTH - fm.stringWidth(msg)) / 2,
                    b_HEIGHT / 10);
            g.setFont(big);
            g.drawString(msg2, (b_WIDTH - fm.stringWidth(msg2)) / 2,
                    (b_HEIGHT / 10) + 50);

            String msg3 = "HIGH SCORES";

            g.setFont(small);

            g.drawString(msg3, (b_WIDTH - fm.stringWidth(msg3)) / 2, (b_HEIGHT / 10) + 100);

            g.setFont(big);

            if (check) {
                highscores();
                check = false;
            }


            Integer[] highScores = getHighScores(); // populate array from file


            for (int i = 0; i < highScores.length; i++) { //display list of high-scores
                String msg4 = Integer.toString(highScores[i]);

                g.drawString(msg4, (b_WIDTH - fm.stringWidth(msg4)) / 2, ((b_HEIGHT / 10) + 150) + 20 * i);

            }
        }
    }

    private void drawBosslevel(Graphics g) {

        for (PowerUp up : individualpowerups) {
            if (up.isVisible()) {
                g.drawImage(up.getImage(), up.getX(), up.getY(), this);
            }
        }

        for (Shield shield : individualshiled) {
            if (shield.isVisible()) {
                g.drawImage(shield.getImage(), shield.getX(), shield.getY(), this);
            }
        }

        if (boss.isVisible()) {
            g.drawImage(boss.getImage(), boss.getX(), boss.getY(),
                    this);
        }

        List<Bullet> en = boss.getBullets();

        for (Bullet bullet : en) {
            if (bullet.isVisible()) {
                g.drawImage(bullet.getImage(), bullet.getX(),
                        bullet.getY(), this);
            }
        }

        if (ship.isVisible()) {
            g.drawImage(ship.getImage(), ship.getX(), ship.getY(),
                    this);
        }

        List<Bullet> ms = ship.getBullets();

        for (Bullet bullet : ms) {
            if (bullet.isVisible()) {
                g.drawImage(bullet.getImage(), bullet.getX(),
                        bullet.getY(), this);
            }
        }
        g.setColor(Color.white);
        g.drawString("Boss Strength: " + bossstrength, 5, 15);
        g.drawString("Level:" + level, 150, 15);
        g.drawString("Lives:" + lives, 300, 15);
        g.drawString("Score:" + score, 600, 15);
        if (activeshield){
            g.setColor(Color.WHITE);
            g.drawString("Shield Strength:" + shieldstrength, 450, 15);
        }
    }

    private void drawObjects(Graphics g) {

        if (ship.isVisible()) {
            g.drawImage(ship.getImage(), ship.getX(), ship.getY(),
                    this);
        }

        List<Bullet> ms = ship.getBullets();

        for (Bullet bullet : ms) {
            if (bullet.isVisible()) {
                g.drawImage(bullet.getImage(), bullet.getX(),
                        bullet.getY(), this);
            }
        }

        for (Enemy carrier : individualEnemies) {
            if (carrier.isVisible()) {
                g.drawImage(carrier.getImage(), carrier.getX(), carrier.getY(), this);
            }
        }

        for (PowerUp up : individualpowerups) {
            if (up.isVisible()) {
                g.drawImage(up.getImage(), up.getX(), up.getY(), this);
            }
        }

        for (Shield shield : individualshiled) {
            if (shield.isVisible()) {
                g.drawImage(shield.getImage(), shield.getX(), shield.getY(), this);
            }
        }




        g.setColor(Color.WHITE);
        g.drawString("Enemies left: " + individualEnemies.size(), 5, 15);
        g.drawString("Level:" + level, 150, 15);
        g.drawString("Lives:" + lives, 300, 15);
        g.drawString("Score:" + score, 600, 15);
        if (activeshield){
            g.setColor(Color.WHITE);
            g.drawString("Shield Strength:" + shieldstrength, 450, 15);
        }
    }

    private void highscores() {

        try {
            Integer[] scores = getHighScores(); //Populate array from file

            for (int i = 0; i < 5; i++) {

                Arrays.sort(scores); //sort array in ascending order so that lowest high-score is replaced first

                if (score > scores[i]) {

                    scores[i] = score;
                    break;
                }
            }

            Arrays.sort(scores, Collections.reverseOrder()); // rearrange scores by highest-first

            FileWriter out = new FileWriter("highscores.txt");
            for (int highsc : scores) {
                out.write("\n" + highsc);
            }
            out.close();

        } catch (Exception n) {
            File highScores = new File("highscores.txt");

            try {

                FileWriter writer = new FileWriter(highScores);

                writer.write("\n" + score);

                writer.close();

            } catch (IOException ioException) {

                ioException.printStackTrace();
            }
        }
    }

    private Integer[] getHighScores() throws FileNotFoundException {

        File myObj = new File("highscores.txt");
        Scanner myReader = new Scanner(myObj);
        myReader.nextLine();
        Integer[] data = new Integer[5];
        Arrays.fill(data, 0);

        for (int i = 0; myReader.hasNext(); i++) {
            data[i] = Integer.parseInt(myReader.next());
        }

        myReader.close();

        return data;
    }

    private void updateBullets() {

        List<Bullet> bullets = ship.getBullets();

        for (int i = 0; i < bullets.size(); i++) {

            Bullet singlebullet = bullets.get(i);

            if (singlebullet.isVisible()) {
                singlebullet.move();
            } else {

                bullets.remove(i);
            }
        }

        if (bosslevel) {


            List<Bullet> enemybullets = boss.getBullets();

            for (int i = 0; i < enemybullets.size(); i++) {

                Bullet singlebullet = enemybullets.get(i);

                if (singlebullet.isVisible()) {
                    singlebullet.enemymove();
                } else {
                    enemybullets.remove(i);
                }
            }
        }


    }

    private void updateSpaceShip() {
        if (ship.isVisible()) {
            ship.move();
        }
    }

    private void updateBossEnemy() {
        if (gamestarted) {
            if (bosslevel) {
                if (boss.isVisible()) {
                    boss.move();
                }
            }
        } else {
            timer.isRunning();
        }
    }

    private void updateEnemies() {

        if (individualEnemies.isEmpty()) {
            ingame = false;
            return;
        }

        if (gamestarted) {
            for (int i = 0; i < individualEnemies.size(); i++) {

                Enemy a = individualEnemies.get(i);

                if (a.isVisible()) {
                    a.move(level);
                } else {
                    individualEnemies.remove(i);

                }
            }

        } else {
            timer.isRunning();
        }
    }

    private void updatePowerups() {

        if (gamestarted) {
            for (int i = 0; i < individualpowerups.size(); i++) {

                PowerUp a = individualpowerups.get(i);

                if (a.isVisible()) {
                    if (bosslevel){
                        a.move();
                    }else {
                        a.move(level);
                    }
                } else {
                    individualpowerups.remove(i);
                }
            }

        } else {
            timer.isRunning();
        }

    }

    private void updateshield() {

        if (gamestarted) {
            for (int i = 0; i < individualshiled.size(); i++) {

                Shield a = individualshiled.get(i);

                if (a.isVisible()) {
                    if (bosslevel){
                        a.move();
                    }else {
                        a.move(level);
                    }
                } else {
                    individualshiled.remove(i);
                }
            }

        } else {
            timer.isRunning();
        }
    }

    public void checkCollision() {

        Rectangle spaceshipbounds = ship.getBounds();
        Rectangle enemybossbounds = boss.getBounds();

        if (bosslevel) {
            List<Bullet> bullets = ship.getBullets();

            if (spaceshipbounds.intersects(enemybossbounds)) {
                ship.death();
                ship.setVisible(false);
                boss.setVisible(false);
                bosslevel = false;
                score = score - 5;
                if (score < 0) {
                    score = 0;
                }
            }

            for (Bullet individualbullet : bullets) {
                Rectangle bulletbounds = individualbullet.getBounds();

                if (bulletbounds.intersects(enemybossbounds)) {
                    if (bossstrength > 0) {
                        bossstrength--;
                        score = score + 2;
                        boss.death();
                        individualbullet.setVisible(false);
                        boss.setVisible(true);
                    } else {
                        isbossdead = true;
                        boss.death();
                        score += 5;
                        boss.setVisible(false);
                        bosslevel = false;
                    }
                }
            }

            List<Bullet> enemybullets = boss.getBullets();

            for (Bullet individualenemybullet : enemybullets) {

                Rectangle enemybulletbounds = individualenemybullet.getBounds();

                if (spaceshipbounds.intersects(enemybulletbounds)) {
                    if (!activeshield) {
                        if (lives > 0) {
                            ship.death();
                            ship.setVisible(true);
                            individualenemybullet.setVisible(false);
                            lives--;
                            score = score - 2;
                        } else {
                            ship.death();
                            individualenemybullet.setVisible(false);
                            ship.setVisible(false);
                            bosslevel = false;
                        }
                    }else{
                        shieldstrength--;
                        if (shieldstrength<=0){
                            activeshield =false;
                        }
                        ship.setVisible(true);
                        individualenemybullet.setVisible(false);
                    }

                }

            }

            for (PowerUp powerUp:individualpowerups){
                Rectangle powerupbounds = powerUp.getBounds();

                if (spaceshipbounds.intersects(powerupbounds)){
                    if (lives < 5) {
                        lives++;
                        ship.setVisible(true);
                        powerUp.death();
                        powerUp.setVisible(false);
                    }else{
                        ship.setVisible(true);
                        powerUp.death();
                        powerUp.setVisible(false);
                    }
                }
            }

            for (Shield shield:individualshiled){
                Rectangle shieldbounds = shield.getBounds();

                if (spaceshipbounds.intersects(shieldbounds)){
                    if (shieldstrength<4) {
                        activeshield = true;
                        shieldstrength += 2;
                        ship.setVisible(true);
                        shield.death();
                        shield.setVisible(false);
                    }else{
                        ship.setVisible(true);
                        shield.death();
                        shield.setVisible(false);
                    }
                }
            }


        }

        for (Enemy enemy : individualEnemies) {

            Rectangle enemybounds = enemy.getBounds();

            if (spaceshipbounds.intersects(enemybounds)) {
                if (!activeshield) {
                    if (lives > 0) {
                        ship.death();
                        ship.setVisible(true);
                        enemy.setVisible(false);
                        lives--;
                        score--;
                        if (score < 0) {
                            score = 0;
                        }

                    } else {
                        ship.death();
                        ship.setVisible(false);
                        enemy.setVisible(false);
                        ingame = false;
                    }
                }else{
                    shieldstrength--;
                    if (shieldstrength<=0){
                        activeshield = false;
                    }
                    enemy.death();
                    enemy.setVisible(false);
                    ship.setVisible(true);
                }
            }
        }

        for (PowerUp powerUp:individualpowerups){
            Rectangle powerupbounds = powerUp.getBounds();

            if (spaceshipbounds.intersects(powerupbounds)){
                if (lives < 5) {
                    lives++;
                    ship.setVisible(true);
                    powerUp.death();
                    powerUp.setVisible(false);
                }else{
                    ship.setVisible(true);
                    powerUp.death();
                    powerUp.setVisible(false);
                }
            }
        }


        for (Shield shield:individualshiled){
            Rectangle shieldbounds = shield.getBounds();

            if (spaceshipbounds.intersects(shieldbounds)){
                if (shieldstrength<4) {
                    activeshield = true;
                    shieldstrength += 1;
                    ship.setVisible(true);
                    shield.death();
                    shield.setVisible(false);
                }else{
                    ship.setVisible(true);
                    shield.death();
                    shield.setVisible(false);
                }
            }
        }

        List<Bullet> bullets = ship.getBullets();

        for (Bullet individualbullet : bullets) {

            Rectangle bulletbounds = individualbullet.getBounds();

            for (Enemy enemy : individualEnemies) {

                Rectangle enemyBounds = enemy.getBounds();

                if (bulletbounds.intersects(enemyBounds)) {
                    score++;
                    enemy.death();
                    individualbullet.setVisible(false);
                    enemy.setVisible(false);
                }
            }
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            ship.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
                    gamestarted = true;
                    //started = false;
            }
            if (ingame && gamestarted) {
                ship.keyPressed(e);
            }
            if (bosslevel) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gamestarted = true;
                }
                ship.keyPressed(e);

            }

        }

    }


}



