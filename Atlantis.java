import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.media.AudioClip;
import java.net.URL;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import java.util.*;

public class Atlantis extends Application implements EventHandler<InputEvent>
{
	GraphicsContext gc;
	int x = 0;
	int y = 0;
	Image AtlantisGameHome;
	Image Bullet;
	AnimateObjects animate;
	Boolean hit = false;
	int bulletNum = 0;
	int [] bulletX = new int[10];
	int [] bulletY = new int[10];
	boolean [] isShot = new boolean[10];
	int gunX = 464;
	int gunY = 420;
	Thread thread = new Thread();

public static void main(String[] args)
{
launch();
}

public void handle(final InputEvent event){
	if(((KeyEvent)event).getCode() == KeyCode.SPACE){
		isShot[bulletNum] = true;
		bulletX[bulletNum] = gunX + 50;
		bulletY[bulletNum] = gunY + 10;
		bulletNum ++;

		if(bulletNum > bulletX.length - 1){
			bulletNum = 0;
		}
	}

	if (((KeyEvent)event).getCode() == KeyCode.UP ){
		gunY -= 15;
		bulletY[bulletNum] = gunY;
	}

	if (((KeyEvent)event).getCode() == KeyCode.DOWN ){
		gunY += 15;
		bulletY[bulletNum] = gunY;
	}


}
public void start(Stage stage)
{
	URL resource = getClass().getResource("test.wav");
	AudioClip clip = new AudioClip(resource.toString());
	clip.play();
	Group root = new Group();
	Canvas canvas = new Canvas(1000,500);
	gc = canvas.getGraphicsContext2D();
	root.getChildren().add(canvas);
	Scene scene = new Scene(root);
	stage.setScene(scene);
	Text score = new Text(110,10,"Score: ");
	scene.addEventHandler(KeyEvent.KEY_PRESSED,this);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	AtlantisGameHome = new Image("AtlantisGameHome.jpg");
	gc.drawImage(AtlantisGameHome,0,-2);
	Bullet = new Image("Bullet.jpg");
	gc.drawImage(Bullet,0,1);
	thread.start();
	animate = new AnimateObjects();
	animate.start();
	stage.show();

}

public class AnimateObjects extends AnimationTimer{

	public void handle(long now){

		gc.drawImage(AtlantisGameHome,x,100);
			for(int i = 0; i < bulletX.length;i ++){
				if(isShot[i]){
					gc.drawImage(Bullet,bulletX[i],bulletY[i]);
				}
			}
		gc.drawImage(Bullet,x+464,420);

		while(true){
			for(int i = 0; i < bulletX.length;i++){
				if(isShot[i] ){
					bulletX[i] += 20;
				}
				if(bulletX[i] > 500){
				isShot[i] = false;
				bulletX[i] = gunX + 50;
				bulletY[i] = gunY + 10;
			}
			}
			try{
				Thread.sleep(20);
			}
			catch(InterruptedException e){
				gc.drawImage(Bullet,x+464,420);
			}

	}
	}


	}
}


