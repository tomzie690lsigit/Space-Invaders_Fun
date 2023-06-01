import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class BossEnemy extends Object {

    private List<Bullet> bullets;
    final static String path = "src/sounds/";
    long asda;
    int yval  = 1;
    int ydir = 1;
    int xval = 1;
    int xdir =1;

    int shipX;
    int shipY;



    public BossEnemy(int x, int y, SpaceShip ship) {
        super(x, y);
        this.shipX = ship.x+(ship.width/2);
        this.shipY = ship.y + (ship.height/2);
        initializeEnemyBoss();
    }

    private void initializeEnemyBoss() {
        bullets = new ArrayList<>();
        loadImage("src/Images/bossship.png");
        getImageDimensions();
    }

    public void move() {
        xdir = xdir !=0 ? 0 : 1;

        x -= xval * xdir;

        if (x < 350) {
            x = 350;
            xval=-1;
        }
        else if (x>650){
            x=650;
            xval=1;
        }


        ydir = ydir !=0 ? 0 : 1;

        y -= yval * ydir;

        if (y<=20){
            y=20;
            yval = -1;
        } else if(y>=200){
            y=200;
            yval = 1;
        }

        long b = System.currentTimeMillis();
        if(b - asda > 1000) {
            asda = System.currentTimeMillis();
            fire();
        }
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void fire() {
        double angle = Math.atan2((shipY-y),(shipX-x));
        bullets.add(new Bullet(x + width, y + height / 2,angle));
        Clip m = getClip("beat1");
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
}
