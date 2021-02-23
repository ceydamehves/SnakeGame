import java.awt.*;
import java.awt.event.*;
import javax.swing.*;//JPanel
import java.util.Random;
import java.util.Scanner;

public class GamePanel extends JPanel implements ActionListener{

	static JTextField t;
	static final int SCREEN_WIDTH = 500;
	static final int SCREEN_HEIGHT = 500;
	static final int UNIT_SIZE = 20; //object size (pixels)
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;//object count
	static final int DELAY = 100;//delay for timer
	//arrays for hold the coordinates
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 3;//because my lucky number
	int applesEaten;
	//apple coordinates
	int appleX;
	int appleY;
	char direction = 'R';//snake begin by going right
	boolean running = false;
	Timer timer;
	Random random;
	
	
	GamePanel(){
		
		random = new Random(); 
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	
	//method for start
	public void startGame() {
		
		newApple();
		running = true;
		timer = new Timer(DELAY,this);//pass with 'this', because i am using action listener
		timer.start();
		
	}
	
	//color
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	//draw 
	public void draw(Graphics g) {
		if(running) {
			//for loop = draw lines across the x and y-axis
			for(int x = 0; x < SCREEN_HEIGHT/UNIT_SIZE; x++) {
				g.drawLine(x*UNIT_SIZE, 0, x*UNIT_SIZE, SCREEN_HEIGHT); //x1, y1, x2, y2 for x-axis
				g.drawLine(0, x*UNIT_SIZE, SCREEN_WIDTH, x*UNIT_SIZE); //x1, y1, x2, y2 for y-axis
			}
			//lets draw the apple
			g.setColor(new Color(153,0,153));
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			//lets draw the body of the snake
			for(int i = 0; i < bodyParts ; i++) {
				if(i == 0) {
					g.setColor(Color.yellow);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(153,255,153));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
		}
			g.setColor(Color.green);
			g.setFont(new Font("Ink Free",Font.BOLD,35));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score : " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score" + applesEaten))/2, g.getFont().getSize() );
		
	}
		else {
			//g is our Graphics that we're receiving with this parameter
			gameOver(g);
		}
	}
	
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}

	//moving the snake (shifting the body parts at x and y axis by using array)
	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() {
		//position for the head of the snake
		if((x[0] == appleX) && (y[0] == appleY) ) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	//check collisions
	public void checkCollisions() {
		//checks if head collides with body
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i] && y[0] == y[i])) //x[0] for head of the snake
			{
				running = false;
			}
		}
		//check the borders 
		//left border
		if(x[0] < 0) {
			running = false;
		}
		//right border 
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//top border
		if(y[0] < 0) {
			running = false;
		}
		//bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	
	//finish method
	public void gameOver(Graphics g) {
		
		//Score
		g.setColor(Color.blue);
		g.setFont(new Font("Ink Free",Font.BOLD,35));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score : " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score" + applesEaten))/2, g.getFont().getSize() );
		//Game Over Text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
			repaint();
		
	}
	
	//inner class 
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT: {
				//limit the user only 90 degrees
				if(direction != 'R') {
					direction = 'L';
					}
				}
			break;
			case KeyEvent.VK_RIGHT: {
				//limit the user only 90 degrees
				if(direction != 'L') {
					direction = 'R';
					}
				}
			break;
			case KeyEvent.VK_UP: {
				//limit the user only 90 degrees
				if(direction != 'D') {
					direction = 'U';
					}
				}
			break;
			case KeyEvent.VK_DOWN: {
				//limit the user only 90 degrees
				if(direction != 'U') {
					direction = 'D';
					}
				}
			break;
			
		}
		}
	}
	

}
