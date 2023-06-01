import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;


public class Enemy extends Object {

    public final int INITIAL_X = 700;
    final static String path = "src/sounds/";


    public Enemy(int x, int y) {
        super(x, y);
        initializeEnemy();
    }

    private void initializeEnemy() {
        loadImage("src/Images/Enemypic.png");
        getImageDimensions();
    }

    public void move(int a) {
        if (x < 0) {
            x = INITIAL_X;
        }
        x -= a;

        if(y<20){
            y = 20;
        } else if(y>260){
            y=260;
        }
    }

    public  void death(){
        Clip m = getClip();
        play(m);
    }

    private static Clip getClip() {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream sample = AudioSystem.getAudioInputStream(new File(path
                    + "bangSmall" + ".wav"));
            clip.open(sample);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }

    public static void play(Clip clip) {
        clip.setFramePosition(0);
        clip.start();
    }
}
