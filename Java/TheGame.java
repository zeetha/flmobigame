package uk.ac.reading.sis05kol.mooc;

//Other parts of the android libraries that we use
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class TheGame extends GameThread{
	//Will store the image of a ball
	private Bitmap mBall;

	//The X and Y position of the ball on the screen
	//The point is the top left corner, not middle, of the ball
	//Initially at -100 to avoid them being drawn in 0,0 of the screen
	private float mBallX = -100;
	private float mBallY = -100;

	//The speed (pixel/second) of the ball in direction X and Y
	private float mBallSpeedX = 0;
	private float mBallSpeedY = 0;


	//Will store the image of the Paddle used to hit the ball
	private Bitmap mPaddle;
	
	//Paddle's x position. Y will always be the bottom of the screen
	private float mPaddleX = 0;
	
	//Will store the image of the smiley ball (score ball)
	private Bitmap mSmileyBall;

	//The X and Y position of the ball on the screen
	//The point is the top left corner, not middle, of the ball
	//Initially at -100 to avoid them being drawn in 0,0 of the screen
	private float mSmileyBallX = -100;
	private float mSmileyBallY = -100;
	
	//Will store the image of the smiley ball (score ball)
	private Bitmap mSadBall;

	//The X and Y position of the SadBalls on the screen
	//The point is the top left corner, not middle, of the balls
	//Initially at -100 to avoid them being drawn in 0,0 of the screen
	private float[] mSadBallX = {-100,-100,-100};
	private float[] mSadBallY = new float[3];

	//This will store the min distance allowed between a big ball and the small red ball
	//This is used to check collisions
	private float mMinDistanceBetweenRedBallAndBigBall = 0;
	
	// All the versions of above variables for the GHOST
	private Bitmap mGhost;
	private float mGhostX = -100;
	private float mGhostY = -100;
	private float mGhostSpeedX = 0;
	private float mGhostSpeedY = 0;
	private int mGhostBumpCount = 0;
	private boolean mGhostVisible = false;
	private boolean mGhostBallHit = false;
	private float mMinDistanceBetweenRedBallAndGhost = 0;

	//This is run before anything else, so we can prepare things here
	public TheGame(GameView gameView) {
		//House keeping
		super(gameView);

		//Prepare the image so we can draw it on the screen (using a canvas)
		mBall = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.small_red_ball);

		//Prepare the image of the paddle so we can draw it on the screen (using a canvas)
		mPaddle = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.yellow_ball);

		//Prepare the image of the SmileyBall so we can draw it on the screen (using a canvas)
		mSmileyBall =  BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.smiley_ball);

		//Prepare the image of the SadBall(s) so we can draw it on the screen (using a canvas)
		mSadBall =  BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.sad_ball);
		
		mGhost =  BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.ghost);

	}

	//This is run before a new game (also after an old game)
	@Override
	public void setupBeginning() {
		//Initialise speeds
		//mCanvasWidth and mCanvasHeight are declared and managed elsewhere
		mBallSpeedX = mCanvasWidth / 3; 
		mBallSpeedY = mCanvasHeight / 3;

		//Place the ball in the middle of the screen.
		//mBall.Width() and mBall.getHeigh() gives us the height and width of the image of the ball
		mBallX = mCanvasWidth / 2;
		mBallY = mCanvasHeight / 2;

		//Place Paddle in the middle of the screen
		mPaddleX = mCanvasWidth / 2;
		
		//Place SmileyBall in the top middle of the screen
		mSmileyBallX = mCanvasWidth / 2;
		mSmileyBallY = mSmileyBall.getHeight()/2;
		
		//Place all SadBalls forming a pyramid underneath the SmileyBall
		mSadBallX[0] = mCanvasWidth / 3;
		mSadBallY[0] = mCanvasHeight / 3;
		
		mSadBallX[1] = mCanvasWidth - mCanvasWidth / 3;
		mSadBallY[1] = mCanvasHeight / 3;

		mSadBallX[2] = mCanvasWidth / 2;
		mSadBallY[2] = mCanvasHeight / 5;

		//Get the minimum distance between a small ball and a bigball
		//We leave out the square root to limit the calculations of the program
		//Remember to do that when testing the distance as well
		mMinDistanceBetweenRedBallAndBigBall = (mPaddle.getWidth() / 2 + mBall.getWidth() / 2) * (mPaddle.getWidth() / 2 + mBall.getWidth() / 2);
		
		// Reset the Ghost variables
		mGhostX = -100;
		mGhostY = -100;
		mGhostSpeedX = 0;
		mGhostSpeedY = 0;
		mGhostBumpCount = 0;
		mGhostVisible = false;
		mGhostBallHit = false;

		mMinDistanceBetweenRedBallAndGhost = (mGhost.getWidth() / 2 + mBall.getWidth() / 2) * (mGhost.getWidth() / 2 + mBall.getWidth() / 2);
	}

	@Override
	protected void doDraw(Canvas canvas) {
		//If there isn't a canvas to do nothing
		//It is ok not understanding what is happening here
		if(canvas == null) return;

		//House keeping
		super.doDraw(canvas);

		//canvas.drawBitmap(bitmap, x, y, paint) uses top/left corner of bitmap as 0,0 
		//we use 0,0 in the middle of the bitmap, so negate half of the width and height of the ball to draw the ball as expected
		//A paint of null means that we will use the image without any extra features (called Paint)
		
		//draw the image of the ball using the X and Y of the ball
		canvas.drawBitmap(mBall, mBallX - mBall.getWidth() / 2, mBallY - mBall.getHeight() / 2, null);

		//Draw Paddle using X of paddle and the bottom of the screen (top/left is 0,0)
		canvas.drawBitmap(mPaddle, mPaddleX - mPaddle.getWidth() / 2, mCanvasHeight - mPaddle.getHeight() / 2, null);
		
		//Draw SmileyBall
		canvas.drawBitmap(mSmileyBall, mSmileyBallX - mSmileyBall.getWidth() / 2, mSmileyBallY - mSmileyBall.getHeight() / 2, null);
		
		//Loop through all SadBall
		for(int i = 0; i < mSadBallX.length; i++) {
			//Draw SadBall in position i
			canvas.drawBitmap(mSadBall, mSadBallX[i] - mSadBall.getWidth() / 2, mSadBallY[i] - mSadBall.getHeight() / 2, null);
		}
		
		// Always draw the ghost so that it gets reset off the screen if you restart game after losing.
		canvas.drawBitmap(mGhost, mGhostX - mGhost.getWidth() / 2, mGhostY - mGhost.getHeight() / 2, null);
	}

	//This is run whenever the phone is touched by the user
	@Override
	protected void actionOnTouch(float x, float y) {
		//Increase/decrease the speed of the ball making the ball move towards the touch
		mPaddleX = x;// - mPaddle.getWidth()/2;
	}
	
	//This is run whenever the phone moves around its axises 
	@Override
	protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
		//Ensure the paddle stays on the screen
		if(mPaddleX >= 0 && mPaddleX <= mCanvasWidth) {
			//Change the X value according to the x direction of the phone
			mPaddleX = mPaddleX - xDirection;
			
			//after movement ensure it is still on the screen
			if(mPaddleX < 0) mPaddleX = 0;
			if(mPaddleX > mCanvasWidth) mPaddleX = mCanvasWidth;			
		}
	}
	 

	//This is run just before the game "scenario" is printed on the screen
	@Override
	protected void updateGame(float secondsElapsed) {
		float distanceBetweenBallAndPaddle;
		
		//If the ball moves down on the screen perform potential paddle collision
		if(mBallSpeedY > 0) {
			//Get actual distance (without square root - remember?) between the mBall and the ball being checked
			distanceBetweenBallAndPaddle = (mPaddleX - mBallX) * (mPaddleX - mBallX) + (mCanvasHeight - mBallY) *(mCanvasHeight - mBallY);
		
			//Check if the actual distance is lower than the allowed => collision
			if(mMinDistanceBetweenRedBallAndBigBall >= distanceBetweenBallAndPaddle) {

				//Get the present velocity (this should also be the velocity going away after the collision)
				float velocityOfBall = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

				//Change the direction of the ball
				mBallSpeedX = mBallX - mPaddleX;
				mBallSpeedY = mBallY - mCanvasHeight;

				//Get the velocity after the collision
				float newVelocity = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

				//using the fraction between the original velocity and present velocity to calculate the needed
				//speeds in X and Y to get the original velocity but with the new angle.
				mBallSpeedX = mBallSpeedX * velocityOfBall / newVelocity;
				mBallSpeedY = mBallSpeedY * velocityOfBall / newVelocity;
				
			}
		}
	

		//Move the ball's X and Y using the speed (pixel/sec)
		mBallX = mBallX + secondsElapsed * mBallSpeedX;
		mBallY = mBallY + secondsElapsed * mBallSpeedY;


		//Check if the ball hits either the left side or the right side of the screen
		//But only do something if the ball is moving towards that side of the screen
		//If it does that => change the direction of the ball in the X direction
		if((mBallX <= mBall.getWidth() / 2 && mBallSpeedX < 0) || (mBallX >= mCanvasWidth - mBall.getWidth() / 2 && mBallSpeedX > 0) ) {
			mBallSpeedX = -mBallSpeedX;
		}
		
		
		distanceBetweenBallAndPaddle = (mSmileyBallX - mBallX) * (mSmileyBallX - mBallX) + (mSmileyBallY - mBallY) *(mSmileyBallY - mBallY);
		
		//Check if the actual distance is lower than the allowed => collision
		if(mMinDistanceBetweenRedBallAndBigBall >= distanceBetweenBallAndPaddle) {

			//Get the present velocity (this should also be the velocity going away after the collision)
			float velocityOfBall = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

			//Change the direction of the ball
			mBallSpeedX = mBallX - mSmileyBallX;
			mBallSpeedY = mBallY - mSmileyBallY;

			//Get the velocity after the collision
			float newVelocity = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

			//using the fraction between the original velocity and present velocity to calculate the needed
			//speeds in X and Y to get the original velocity but with the new angle.
			mBallSpeedX = mBallSpeedX * velocityOfBall / newVelocity;
			mBallSpeedY = mBallSpeedY * velocityOfBall / newVelocity;
			
			//Increase score
			updateScore(1);
			
		}
		
		//Loop through all SadBalls
		for(int i = 0; i < mSadBallX.length; i++) {
			//Perform collisions (if necessary) between SadBall in position i and the red ball
			
			//Get actual distance (without square root - remember?) between the mBall and the ball being checked
			distanceBetweenBallAndPaddle = (mSadBallX[i] - mBallX) * (mSadBallX[i] - mBallX) + (mSadBallY[i] - mBallY) *(mSadBallY[i] - mBallY);
		
			//Check if the actual distance is lower than the allowed => collision
			if(mMinDistanceBetweenRedBallAndBigBall >= distanceBetweenBallAndPaddle) {

				//Get the present velocity (this should also be the velocity going away after the collision)
				float velocityOfBall = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

				//Change the direction of the ball
				mBallSpeedX = mBallX - mSadBallX[i];
				mBallSpeedY = mBallY - mSadBallY[i];

				//Get the velocity after the collision
				float newVelocity = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

				//using the fraction between the original velocity and present velocity to calculate the needed
				//speeds in X and Y to get the original velocity but with the new angle.
				mBallSpeedX = mBallSpeedX * velocityOfBall / newVelocity;
				mBallSpeedY = mBallSpeedY * velocityOfBall / newVelocity;
			}
		}

		//If the ball goes out of the top of the screen and moves towards the top of the screen =>
		//change the direction of the ball in the Y direction
		if(mBallY <= mBall.getWidth() / 2 && mBallSpeedY < 0) {
			mBallSpeedY = -mBallSpeedY;
		}

		//If the ball goes out of the bottom of the screen => lose the game
		if(mBallY >= mCanvasHeight) {
			setState(GameThread.STATE_LOSE);
		}
		
		// Do Ghost stuff
		if (mGhostVisible)
		{
			// has the Ghost hit the ball?
			// NOTE : Can reuse the variable 'distanceBetweenBallAndPaddle' as its actually the distance between two specified objects.
			distanceBetweenBallAndPaddle = (mGhostX - mBallX) * (mGhostX - mBallX) + (mGhostY - mBallY) *(mGhostY - mBallY);
			
			//Check if the actual distance is lower than the allowed => collision
			if(!mGhostBallHit && (mMinDistanceBetweenRedBallAndGhost >= distanceBetweenBallAndPaddle)) {
				mBallSpeedX = mBallSpeedX * new Float(1.2);
				mBallSpeedY = mBallSpeedY * new Float(1.2);

				mGhostBallHit = true;
			}
			
			if((mGhostX <= mGhost.getWidth() / 2 && mGhostSpeedX < 0) || (mGhostX >= mCanvasWidth - mGhost.getWidth() / 2 && mGhostSpeedX > 0) ) {
				mGhostSpeedX = -mGhostSpeedX;
				mGhostBumpCount++;
				mGhostBallHit = false;
			}
			if((mGhostY <= mGhost.getHeight() / 2 && mGhostSpeedY < 0) || (mGhostY >= mCanvasHeight - mGhost.getHeight() / 2 && mGhostSpeedY > 0) ) {
				mGhostSpeedY = -mGhostSpeedY;
				mGhostBumpCount++;
				mGhostBallHit = false;
			}
			if (mGhostBumpCount > 3)
			{
				mGhostVisible = false;
				mGhostBallHit = false;
				mGhostX = -100;
				mGhostY = -100;
			}
			else
			{
				mGhostX = mGhostX + secondsElapsed * mGhostSpeedX;
				mGhostY = mGhostY + secondsElapsed * mGhostSpeedY;				
			}
		}
		
		if (score > 2 && (score % 3) == 0)
		{
			mGhostVisible = true;
			mGhostSpeedX = 100;
			mGhostSpeedY = 100;
			mGhostBumpCount = 0;
		}
	}	
}

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// 
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.
