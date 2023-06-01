import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SpaceShip extends Object {

    private int dx;
    private int dy;
    private List<Bullet> bullets;
    final static String path = "src/sounds/";


    public SpaceShip(int x, int y) {
        super(x, y);
        initializeSpaceShip();
    }

    private void initializeSpaceShip() {
        bullets = new ArrayList<>();
        loadImage("src/Images/spaceship.png");
        getImageDimensions();
    }

    public void move() {
        x = (x>=0&&x<=400)?(x+dx):(x);
        if(x<0){
            x = 0;
        } else if(x>400){
            x=400;
        }
        y = (y>=20&&y<=260)?(y+dy):(y);
        if(y<20){
            y = 20;
        } else if(y>260){
            y=260;
        }
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            fire();
        }

        if (key == KeyEvent.VK_LEFT) {
            dx = -3;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 3;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -5;
        }

        if (key == KeyEvent.VK_DOWN) {

            dy = 5;

        }
    }


    public void fire() {
        bullets.add(new Bullet(x + width, y + height / 2,0));
        Clip m = getClip("fire");
        play(m);
    }

    public  void death(){
        Clip m = getClip("bangLarge");
        play(m);
    }

    public static void play(Clip clip) {
        clip.setFramePosition(0);
        clip.start();
    }

    private static Clip getClip(String filename) {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream sample = AudioSystem.getAudioInputStream(new File(path
                    + filename + ".wav"));
            clip.open(sample);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }


}
