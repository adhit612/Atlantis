
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
	Image BulletImage;
	Image hitImage;
	Image shipImage;
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

	class Ship {
		int x =0;
		int y=10;
		int endX = 1000;
		Image image;
		int widthImage = 60;
		int heightImage = 60;
		GraphicsContext gc;
		Rectangle2D rect;
		int speed = (int)(Math.random()*6)+5;

		Ship( Image image,GraphicsContext gc,int y)
		{
			this.y = y;
			this.image =image;
			this.gc = gc;
			this.rect = new Rectangle2D(x,y, widthImage,heightImage);
		}
		public void Reset(){
			x = 0;
		}
		public void Move()
		{
			x = x + speed;
			if( x >= endX ){
				x= 0;
				speed = (int)(Math.random()*6)+5;
			}

			gc.drawImage(image,x,y, widthImage,heightImage);
			rect = new Rectangle2D(x,y, widthImage,heightImage);
		}

		public Rectangle2D  getRect()
		{
			return( rect );
		}

	}

class BulletUp {
		int endY = 300;
		int beginY = 0;
		int x =450;
		int y=endY;

		Image image;
		int widthImage = 20;
		int heightImage = 20;
		GraphicsContext gc;
		Rectangle2D rect;
		int speed = 10;

		BulletUp( Image image,GraphicsContext gc)
		{
			this.image =image;
			this.gc = gc;
			this.rect = new Rectangle2D(x,y, widthImage,heightImage);
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

		public Rectangle2D  getRect()
		{
			return( rect );
		}
	}

	class BulletLeft{
		int endY = 352;
				int beginY = 2;
				int x =150;
				int y=endY;

				Image image;
				int widthImage = 20;
				int heightImage = 20;
				GraphicsContext gc;
				Rectangle2D rect;
				int speed = 10;

				BulletLeft( Image image,GraphicsContext gc)
				{
					this.image =image;
					this.gc = gc;
					this.rect = new Rectangle2D(x,y, widthImage,heightImage);
				}
				public boolean Move(boolean shootLeft)
				{
					if( shootLeft )
					{

						x = x + 5;
						y = y-6;
						if( y <= beginY )
						{
							y= endY;
							x = 30;
							rect = new Rectangle2D(x,endY, widthImage,heightImage);
							return( false );
						}

						gc.drawImage(image,x,y, widthImage,heightImage);
						rect = new Rectangle2D(x,y, widthImage,heightImage);
					}

					return( shootLeft );

				}

				public Rectangle2D  getRect()
				{
					return( rect );
				}
	}

	class BulletRight{
		int endY = 360;
		int beginY = 0;
		int x = 905;
		int y=endY;

		Image image;
		int widthImage = 20;
		int heightImage = 20;
		GraphicsContext gc;
		Rectangle2D rect;
		int speed = 10;

		BulletRight( Image image,GraphicsContext gc)
		{
			this.image =image;
			this.gc = gc;
			this.rect = new Rectangle2D(x,y, widthImage,heightImage);
		}
		public boolean Move(boolean shootRight)
		{
			if( shootRight )
			{

				x = x - 5;
				y = y - 6;
				if( y <= beginY )
				{
					y= endY;
					x = 900;
					rect = new Rectangle2D(x,endY, widthImage,heightImage);
					return( false );
				}

				gc.drawImage(image,x,y, widthImage,heightImage);
				rect = new Rectangle2D(x,y, widthImage,heightImage);
			}

			return( shootRight );

		}

		public Rectangle2D  getRect()
		{
			return( rect );
		}

	}

	class BulletDown {
			int endY = 300;
			int beginY = 0;
			int x =0;
			int y=beginY;

			Image image;
			int widthImage = 20;
			int heightImage = 20;
			GraphicsContext gc;
			Rectangle2D rect;
			int speed = 10;

			BulletDown( Image image,GraphicsContext gc)
			{
				this.image =image;
				this.gc = gc;
				this.rect = new Rectangle2D(x,y, widthImage,heightImage);
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
					y = y-speed;
					if( y <= beginY )
					{
						return;
					}

					gc.drawImage(image,x,y, widthImage,heightImage);
					rect = new Rectangle2D(x,y, widthImage,heightImage);

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
		System.out.println( "In KeyEvent handle KeyCode.SPACE");
		shoot = true;
	}
	if(((KeyEvent)event).getCode() == KeyCode.LEFT){
			System.out.println( "In KeyEvent handle KeyCode.LEFT");
			shootLeft = true;
	}
	if(((KeyEvent)event).getCode() == KeyCode.RIGHT){
				System.out.println( "In KeyEvent handle KeyCode.RIGHT");
				shootRight = true;

	}


	if (((KeyEvent)event).getCode() == KeyCode.UP ){
		System.out.println( "In KeyEvent handle KeyCode.UP");


	}

	if (((KeyEvent)event).getCode() == KeyCode.DOWN ){
		System.out.println( "In KeyEvent handle KeyCode.DOWN");

	}


}
public void start(Stage stage)
{
//	URL resource = getClass().getResource("test.wav");
//	AudioClip clip = new AudioClip(resource.toString());
//	clip.play();
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
	BulletImage = new Image("Bullet.jpg");
	hitImage = new Image("hit.jpg");
	shipImage = new Image("Ship.jpg");

	ship = new Ship(shipImage, gc,10);
	shipTwo = new Ship(shipImage,gc,90);
	bulletUp = new BulletUp(BulletImage, gc );
	bulletLeft = new BulletLeft(BulletImage,gc);
	bulletRight = new BulletRight(BulletImage,gc);


	//gc.drawImage(Bullet,0,1);
	thread.start();
	animate = new AnimateObjects();
	animate.start();
	stage.show();

}

public class AnimateObjects extends AnimationTimer{

	public void handle(long now){

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

		gc.drawImage(AtlantisGameHome,0,-2);

		ship.Move();
		shipTwo.Move();
		shoot = bulletUp.Move(shoot);
		shootLeft = bulletLeft.Move(shootLeft);
		shootRight = bulletRight.Move(shootRight);

		Rectangle2D  shipRect = ship.getRect();
		Rectangle2D shipRectTwo = shipTwo.getRect();
		Rectangle2D  bulletUpRect = bulletUp.getRect();
		Rectangle2D  bulletLeftRect = bulletLeft.getRect();
		Rectangle2D bulletRightRect = bulletRight.getRect();


		if(shipRect.intersects(bulletUpRect) )
		{
			gc.drawImage(hitImage,shipRect.getMinX(),shipRect.getMinY(),2*shipRect.getWidth(), 2*shipRect.getHeight() );
			weHaveHit = true;
			ship.Reset();
		}
		if(shipRect.intersects(bulletLeftRect)){
			gc.drawImage(hitImage,shipRect.getMinX(),shipRect.getMinY(),2*shipRect.getWidth(), 2*shipRect.getHeight() );
			weHaveHit = true;
			ship.Reset();
		}
		if(shipRect.intersects(bulletRightRect)){
			gc.drawImage(hitImage,shipRect.getMinX(),shipRect.getMinY(),2*shipRect.getWidth(), 2*shipRect.getHeight() );
			weHaveHit = true;
			ship.Reset();
		}


		//Ship Two
		if(shipRectTwo.intersects(bulletUpRect) )
		{
			gc.drawImage(hitImage,shipRectTwo.getMinX(),shipRectTwo.getMinY(),2*shipRectTwo.getWidth(), 2*shipRectTwo.getHeight() );
			weHaveHit = true;
			shipTwo.Reset();
		}
		if(shipRectTwo.intersects(bulletLeftRect)){
			gc.drawImage(hitImage,shipRectTwo.getMinX(),shipRectTwo.getMinY(),2*shipRectTwo.getWidth(), 2*shipRectTwo.getHeight() );
			weHaveHit = true;
			shipTwo.Reset();
		}
		if(shipRectTwo.intersects(bulletRightRect)){
			gc.drawImage(hitImage,shipRectTwo.getMinX(),shipRectTwo.getMinY(),2*shipRectTwo.getWidth(), 2*shipRectTwo.getHeight() );
			weHaveHit = true;
			shipTwo.Reset();
}




	}


	}
}
