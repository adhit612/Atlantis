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

public class AdhitThakur extends Application implements EventHandler<InputEvent>
{
	GraphicsContext gc;
	int x = 0;
	int y = 0;
	int score = 0;
	int enemyCount = 0;
	Image AtlantisGameHome;
	Image BulletImage;
	Image hitImage;
	Image shipImage;
	Image AtlantisStartScreen;
	Image gameOver;
	AnimateObjects animate;
	Boolean hit = false;
	Thread thread = new Thread();
	Ship ship;
	Ship shipTwo;
	BulletUp bulletUp;
	BulletLeft bulletLeft;
	BulletRight bulletRight;
	boolean shoot;
	boolean shootLeft;
	boolean shootRight;
	boolean weHaveHit = false;
	boolean startScreen = true;
	boolean gameOverMusicPlaying = false;
	ArrayList <Ship> shipList;
	int enemyStrikeCount = 0;
	int screenSwitch = 0;
	URL bulletAudio = getClass().getResource("Gun9.wav");
	AudioClip clipBullet = new AudioClip(bulletAudio.toString());
	URL explosionAudio = getClass().getResource("Explosion2.wav");
	AudioClip clipExplosion = new AudioClip(explosionAudio.toString());
	URL GameMusic = getClass().getResource("GameMusic.mp3");
	AudioClip clipGameMusic = new AudioClip(GameMusic.toString());
	URL GameStartMusic = getClass().getResource("GameStartMusic.wav");
	AudioClip clipGameStartMusic = new AudioClip(GameStartMusic.toString());
	URL GameOverMusic = getClass().getResource("GameOverMusic.mp3");
	AudioClip clipGameOverMusic = new AudioClip(GameOverMusic.toString());



	class Ship {
		GraphicsContext gc;
		Rectangle2D rect;

		int x =0;
		int y=10;
		int endX = 1000;
		Image image;
		int widthImage = 60;
		int heightImage = 60;
		int speed = (int)(Math.random()*5)+5;
		int survivalCount = 0;
		Image bulletImage;
		int initialY =0;
		BulletDown bulletDown = null;
		int bulletStartX = 0;
		boolean startBullet = false;

		Ship( Image image,Image bulletImage,GraphicsContext gc,int y)
		{
			this.y = initialY = y;
			this.image =image;
			this.gc = gc;
			this.rect = new Rectangle2D(x,y, widthImage,heightImage);
			survivalCount = 0;
			this.bulletImage = bulletImage;
			bulletDown = new BulletDown(bulletImage, gc,y, x);
		}
		public void Reset(){
			x = 0;
			y=initialY;
			survivalCount =0;
			bulletStartX = 0;
			this.rect = new Rectangle2D(x,y, widthImage,heightImage);
			bulletDown.Reset();
			startBullet =false;
		}
		public boolean Move()
		{
			x = x + speed;
			if( x >= endX ){
				x= 0;
				speed = (int)(Math.random()*6)+5;
				//we survived, attack from closer distance
				//next time ...
				survivalCount++;
				y = 2*y;
				if(y > 250){
					y = 250;
				}

				System.out.println( String.format( "Surived count=%d x=%d y=%d", survivalCount, x, y ) );
			}

			//set up attack bullet
			if( survivalCount == 2 )
			{
				if( bulletStartX == 0 )
				{
					bulletStartX = ( int)(Math.random()*700) + 100;
					startBullet = true;
					System.out.println( String.format( "BulletDown Start bulletStartX=%d", bulletStartX ) );
				}

			}

			if( (survivalCount == 3 ) && (x >= bulletStartX) )
			{
				//System.out.println( String.format( "BulletDown Start x=%d y=%d", x,y ) );
				y = 250;
				bulletDown.Start( x,y );
			}
			bulletDown.Move();
			//set up attack bullet


			if( survivalCount > 3 ) {
				Reset();
				System.out.println( String.format( "Attack count=%d x=%d y=%d bulletStartX=%d", survivalCount, x, y,bulletStartX ) );
				return( true );
			}

			gc.drawImage(image,x,y, widthImage,heightImage);
			rect =  new Rectangle2D(x,y, widthImage,heightImage);

			return false;
		}

		public Rectangle2D  getRect()
		{
			return( rect );
		}

	}

class BulletUp {
		int endY = 350;
		int beginY = 0;
		int x =450;
		int y=endY;

		Image image;
		int widthImage = 9;
		int heightImage = 9;
		GraphicsContext gc;
		Rectangle2D rect;
		int speed = 8;

		BulletUp( Image image,GraphicsContext gc)
		{
			this.image =image;
			this.gc = gc;

		}
	public boolean Move(boolean shoot)
		{
			if( shoot )
			{
				y = y-speed;
				if( y <= beginY )
				{
					y= endY;
					rect = new Rectangle2D(x,endY, widthImage,heightImage);
					return( false );
				}

				gc.drawImage(image,x,y, widthImage,heightImage);
				rect = new Rectangle2D(x,y, widthImage,heightImage);
			}

			return( shoot );
		}

		public void Reset(){
			shoot = false;

			System.out.println( String.format( "BulletUp before reset x=%d, y=%d", x, y) );
			x = 450;
			y = endY;
			System.out.println( String.format( "BulletUp after reset x=%d, y=%d", x, y) );
			rect = new Rectangle2D(x,y, widthImage,heightImage);

		}
		public Rectangle2D  getRect()
		{
			return( rect );
		}
	}

	class BulletLeft{
		int endY = 397;
		int beginY = 2;
		int x = 38;
		int y=endY;

		Image image;
		int widthImage = 9;
		int heightImage = 9;
		GraphicsContext gc;
		Rectangle2D rect;
		int speed = 10;

		BulletLeft( Image image,GraphicsContext gc)
		{
			this.image =image;
			this.gc = gc;

		}
		public boolean Move(boolean shootLeft)
		{
			if( shootLeft )
			{

				x = x + 8;
				y = y - 8;
				if( y <= beginY )
				{
					y= endY;
					x = 38;
					rect = new Rectangle2D(x,endY, widthImage,heightImage);
					return( false );
				}

				gc.drawImage(image,x,y, widthImage,heightImage);
				rect = new Rectangle2D(x,y, widthImage,heightImage);
			}

			return( shootLeft );

		}
		public void Reset(){

			shoot = false;
			System.out.println( String.format( "BulletLeft before reset x=%d, y=%d", x, y) );
			x = 40;
			y = endY;
			System.out.println( String.format( "BulletLeft after reset x=%d, y=%d", x, y) );
			rect = new Rectangle2D(x,y, widthImage,heightImage);

		}


		public Rectangle2D  getRect()
		{
			return( rect );
		}
	}

	class BulletRight{
		int endY = 360;
		int beginY = 0;
		int x = 940;
		int y=endY;

		Image image;
		int widthImage = 9;
		int heightImage = 9;
		GraphicsContext gc;
		Rectangle2D rect;
		int speed = 10;

		BulletRight( Image image,GraphicsContext gc)
		{
			this.image =image;
			this.gc = gc;

		}

		public boolean Move(boolean shootRight)
		{
			if( shootRight )
			{

				x = x - 8;
				y = y - 8;
				if( y <= beginY )
				{
					y = endY;
					x = 940;
					rect = new Rectangle2D(x,endY, widthImage,heightImage);
					return( false );
				}

				gc.drawImage(image,x,y, widthImage,heightImage);
				rect = new Rectangle2D(x,y, widthImage,heightImage);
			}

			return( shootRight );

		}
		public void Reset(){

			shoot = false;
			System.out.println( String.format( "BulletRight before reset x=%d, y=%d", x, y) );
			x = 940;
			y = endY;
			System.out.println( String.format( "BulletRight after reset x=%d, y=%d", x, y) );
			rect = new Rectangle2D(x,y, widthImage,heightImage);

		}



		public Rectangle2D  getRect()
		{
			return( rect );
		}

	}

	class BulletDown {
			int endY = 400;
			int beginY = 0;
			int x =0;
			int y=beginY;

			Image image;
			int widthImage = 60;
			int heightImage = 60;
			GraphicsContext gc;
			Rectangle2D rect;
			int speed = 3;
			boolean start = false;

			BulletDown( Image image,GraphicsContext gc, int y, int x)
			{
				this.image = new Image("EnemyBomb.jpg");
				this.gc = gc;
				this.rect = new Rectangle2D(x,y, widthImage,heightImage);
				this.y = y;
				this.x = x;
			}

			public void Start(int x, int y){

				if( start == false )
				{
					this.x = x ;
					this.y = y;
					start = true;
				}

			}
			void Reset() {
				start = false;
				x =0;
				y=beginY;

			}


			void setXY( int x, int y, int endY, int beginY )
			{
					this.x = x;
					this.y=y;
					this.endY= endY;
					this.beginY= beginY;
			}

			public void Move()
			{
					if( start )
					{
						y = y+speed;
						if( y >= endY )
						{

							return;
						}

					 gc.drawImage(image,x,y, widthImage,heightImage);
					 rect = new Rectangle2D(x,y, widthImage,heightImage);
				}

			}

			public Rectangle2D  getRect()
			{
				return( rect );
			}
	}

public static void main(String[] args)
{
launch();
}

public void handle(final InputEvent event){
	if(((KeyEvent)event).getCode() == KeyCode.SPACE){
		shoot = true;
		System.out.println( "In KeyEvent handle KeyCode.SPACE");
		clipBullet.play();
	}
	if(((KeyEvent)event).getCode() == KeyCode.LEFT){
			System.out.println( "In KeyEvent handle KeyCode.LEFT");
			shootLeft = true;
			clipBullet.play();
	}
	if(((KeyEvent)event).getCode() == KeyCode.RIGHT){
				System.out.println( "In KeyEvent handle KeyCode.RIGHT");
				shootRight = true;
				clipBullet.play();

	}

	if (((KeyEvent)event).getCode() == KeyCode.UP ){
		System.out.println( "In KeyEvent handle KeyCode.UP");

	}
	if (((KeyEvent)event).getCode() == KeyCode.ENTER ){
			System.out.println( "In KeyEvent handle KeyCode.ENTER");
			for(int i = 0; i < shipList.size(); i ++){
				shipList.get(i).Reset();
			}
			enemyStrikeCount = 0;
			score = 0;
			startScreen = true;
			clipGameOverMusic.stop();
			gameOverMusicPlaying = false;
			clipGameStartMusic.play();
	}
	if (((KeyEvent)event).getCode() == KeyCode.S ){
		System.out.println( "In KeyEvent handle KeyCode.ENTER");
		clipGameStartMusic.stop();
		clipGameMusic.play();
		score = 0;
		enemyStrikeCount = 0;
		startScreen = false;
	}



	if (((KeyEvent)event).getCode() == KeyCode.DOWN ){
		System.out.println( "In KeyEvent handle KeyCode.DOWN");

	}


}
public void start(Stage stage)
{
	Group root = new Group();
	Group root2 = new Group();
	Canvas canvas = new Canvas(1000,500);
	Canvas canvas2 = new Canvas(1000,500);
	gc = canvas.getGraphicsContext2D();
	root.getChildren().add(canvas);
	Scene scene = new Scene(root);
	stage.setScene(scene);
	scene.addEventHandler(KeyEvent.KEY_PRESSED,this);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	AtlantisGameHome = new Image("AtlantisGameHome.jpg");
	AtlantisStartScreen = new Image("AtlantisStartScreen.gif");
	if(startScreen){
		clipGameStartMusic.play();
	}
	else{
		clipGameStartMusic.stop();
		clipGameMusic.play();
	}
	if(enemyStrikeCount >= 3){
		clipGameOverMusic.play();
	}

	BulletImage = new Image("Bullet.jpg");
	hitImage = new Image("hit.jpg");
	shipImage = new Image("Ship.jpg");
	gameOver = new Image("GameOver.jpg");

	shipList = new ArrayList<>();
	bulletUp = new BulletUp(BulletImage, gc );
	bulletLeft = new BulletLeft(BulletImage,gc);
	bulletRight = new BulletRight(BulletImage,gc);
	thread.start();
	animate = new AnimateObjects();
	animate.start();
	stage.show();

}

public class AnimateObjects extends AnimationTimer{

	private void RulesSetUp(){

		gc.drawImage(AtlantisStartScreen,0,0,1000,500  );

		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(1);
		Font font = Font.font("Playbill", FontWeight.NORMAL,40);
		gc.setFont(font);
		gc.fillText( "CONTROLS:" ,500,190);
		gc.strokeText( "CONTROLS:" ,500,190);

		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(1);
		Font font2 = Font.font("Times New Roman", FontWeight.NORMAL,25);
		gc.setFont(font2);
		gc.fillText( "SPACE = middle cannon" ,500,230);
		gc.strokeText( "SPACE = middle cannon" ,500,230);

		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(1);
		gc.setFont(font2);
		gc.fillText( "RIGHT ARROW = right cannon" ,500,260);
		gc.strokeText( "RIGHT ARROW = right cannon" ,500,260);

		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(1);
		gc.setFont(font2);
		gc.fillText( "LEFT ARROW = left cannon" ,500,290);
		gc.strokeText( "LEFT ARROW = left cannon" ,500,290);

		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(1);
		gc.setFont(font);
		gc.fillText( "RULES:" ,90,190);
		gc.strokeText( "RULES" ,90,190);

		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(1);
		gc.setFont(font2);
		gc.fillText( "Destroy the ships!" ,90,230);
		gc.strokeText( "Destroy the ships!" ,90,230);

		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(1);
		gc.setFont(font2);
		gc.fillText( "Loss if ship reaches edge 3 times!" ,90,260);
		gc.strokeText( "Loss if ship reaches edge 3 times!" ,90,260);

		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(1);
		gc.setFont(font2);
		gc.fillText( "Bombs drop every 3 times!" ,90,290);
		gc.strokeText( "Bombs drop every 3 times!" ,90,290);



		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(1);
		gc.setFont(font);
		gc.fillText( "Press S to Start" ,450,470);
		gc.strokeText( "Press S to Start" ,450,470);
	}
	public void handle(long now){
		gc.drawImage(AtlantisGameHome,0,-2);
		int randomNumber = (int)(Math.random()*3)+1;

		if(startScreen){

			RulesSetUp();
			return;
		}

		if(startScreen){

		 startScreen = false;

		}

		if(startScreen == false){
			//SCORE
			gc.setFill(Color.YELLOW);
			gc.setStroke(Color.YELLOW);
			gc.setLineWidth(1);
			Font font = Font.font("Playbill", FontWeight.NORMAL,40);
			gc.setFont(font);
			gc.fillText( "" +score,450,480);
			gc.strokeText( "" +score,450,480);

			//ENEMTY STRIKE
			gc.setFill(Color.RED);
			gc.setStroke(Color.RED);
			gc.setLineWidth(1);
			gc.setFont(font);
			gc.fillText( "STRIKES:" +enemyStrikeCount,100,480);
		}


	//sleep to show hit image
		if( weHaveHit )
		{

			System.out.println( "WE HAVE HIT !!!!" );
			try{
				Thread.sleep(100);
			 }
			catch(InterruptedException e){

			}

			weHaveHit = false;

		}

		if( enemyStrikeCount >= 3 )
		{
			clipGameMusic.stop();

		if(gameOverMusicPlaying == false){
			clipGameOverMusic.play();
			gameOverMusicPlaying = true;
		}
			shoot = false;
			gc.drawImage(gameOver,0,0,1000,500);
			gc.setFill(Color.YELLOW);
			gc.setStroke(Color.YELLOW);
			gc.setLineWidth(1);
			Font font = Font.font("Playbill", FontWeight.NORMAL,40);
			gc.setFont(font);
			gc.fillText( "Press ENTER to Restart" ,450,480);
			gc.strokeText( "Press ENTER to Restart" ,450,480);

			//System.out.println( "GAME OVER RESTART YOUR GAME TO PLAY AGAIN!!!!" );
			return;
		}


		if(enemyCount < randomNumber)
		{
			shipList.add(new Ship(shipImage,BulletImage,gc,(int)(Math.random()*91)+10));
			enemyCount++;
		}

		shoot = bulletUp.Move(shoot);
		shootLeft = bulletLeft.Move(shootLeft);
		shootRight = bulletRight.Move(shootRight);

		Rectangle2D  bulletUpRect = bulletUp.getRect();
		Rectangle2D  bulletLeftRect = bulletLeft.getRect();
		Rectangle2D bulletRightRect = bulletRight.getRect();



	for(int i = 0; i < shipList.size(); i ++){
		boolean ret = shipList.get(i).Move();
		if( ret == true ){
			enemyStrikeCount++;
			shipList.get(i).Reset();
		}

		if(shipList.get(i).getRect().intersects(bulletUpRect)){
			clipExplosion.play();
			System.out.println( "BULLET UP HIT !!!!" );

			gc.drawImage(hitImage,shipList.get(i).getRect().getMinX(),shipList.get(i).getRect().getMinY(),2*shipList.get(i).getRect().getWidth(), 2*shipList.get(i).getRect().getHeight() );
			weHaveHit = true;
			shipList.get(i).Reset();
			bulletUp.Reset();
			shoot = false;
			score += 100;
		}
		if(shipList.get(i).getRect().intersects(bulletLeftRect)){
			clipExplosion.play();
			System.out.println( "BULLET LEFT HIT !!!!" );

			gc.drawImage(hitImage,shipList.get(i).getRect().getMinX(),shipList.get(i).getRect().getMinY(),2*shipList.get(i).getRect().getWidth(), 2*shipList.get(i).getRect().getHeight() );
			weHaveHit = true;
			shipList.get(i).Reset();
			bulletLeft.Reset();
			shootLeft = false;
			score += 100;
		}
		if(shipList.get(i).getRect().intersects(bulletRightRect)){
			clipExplosion.play();
			System.out.println( "BULLET RIGHT HIT !!!!" );

			gc.drawImage(hitImage,shipList.get(i).getRect().getMinX(),shipList.get(i).getRect().getMinY(),2*shipList.get(i).getRect().getWidth(), 2*shipList.get(i).getRect().getHeight() );
			weHaveHit = true;
			shipList.get(i).Reset();
			bulletRight.Reset();
			shootRight = false;
			score += 100;
		}
	}

}


	}
}