import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;


public class Shield extends Object {

    final static String path = "src/sounds/";


    public Shield(int x, int y) {
        super(x, y);
        initializeShield();
    }

    private void initializeShield() {
        loadImage("src/Images/shield.png");
        getImageDimensions();
    }

    public void move(int a) {
        x -= a;

        if(y<20){
            y = 20;
        } else if(y>260){
            y=260;
        }
    }

    public void move() {

        x -= 1;

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
                    + "powerup" + ".wav"));
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