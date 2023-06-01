public class Bullet extends Object {

    double angle;
    public Bullet(int x, int y, double angle) {
        super(x, y);
        this.angle = angle;
        initializeBullet();
    }

    private void initializeBullet() {
        loadImage("src/Images/Bulletpic.png");
        getImageDimensions();
    }

    public void move() {
        int bullet_SPEED = 5;
        x += bullet_SPEED * Math.cos(angle);
        y += bullet_SPEED * Math.sin(angle);
        int BOARD_WIDTH = 660;
        if (x > BOARD_WIDTH) {
            visible = false;
        }
    }

    public void  enemymove(){
        int bullet_SPEED = 5;
        x -= bullet_SPEED;

        int BOARD_WIDTH = 0;
        if (x < BOARD_WIDTH) {
            visible = false;
        }
    }
}
