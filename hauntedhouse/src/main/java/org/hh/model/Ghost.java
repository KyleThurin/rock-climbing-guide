package org.hh.model;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.Random;

public class Ghost {

    private static final int DEFAULT_GHOST_Y = 0;
    private static final int DEFAULT_GHOST_SPEED = 10;
    // New constants for vertical movement
    private static final int VERTICAL_BOUNCE_HEIGHT = 50; // How high the ghost can float/bounce
    private static final int VERTICAL_GRAVITY = 1;       // How fast it falls
    private static final int FLOATING_AREA_HEIGHT = 200; // The total height range the ghost floats in

    private PImage ghostImg;
    private String direction;
    private int x;
    private int y;
    private int speed = 5;
    private int transparency = 125;
    private PApplet p;

    // New variables for vertical movement
    private int ySpeed = 0; //Vertical speed
    private int initialY;   //Store the initial Y to define the floating range
    private boolean floatingUp = true; //Controls the direction of floating

    public Ghost(PApplet p) {
        // Randomly set initial Y
        this(p, (int) p.random(50, p.height / 3), new Random().nextBoolean() ? "left" : "right");
    }
    public Ghost(PApplet p, String direction) {
        this(p, (int) p.random(50, p.height / 3), direction);
    }
    public Ghost(PApplet p, int y, String direction) {
        this.p = p;
        ghostImg = p.loadImage( "images/ghost.png" );
        ghostImg.resize(150, 250); // Keep size consistent

        this.direction = direction;
        this.speed = speed;
        // Starts ghost off-screen
        this.x = direction.equalsIgnoreCase("left") ? p.width + ghostImg.width : -ghostImg.width;
        this.y = y;
        this.initialY = y; // Store the initial Y for vertical bouncing range
    }

    //NOTE:  0 = most transparent; 100 = most opaque
    // 0 = most transparent; 100 = most opaque
    public void setGhostTransparency( int transparency ){

        //this.transparency = ( 255 - ( ( transparency * 255 ) / 100 ) );
        this.transparency = ( transparency * 255 ) / 100;
    }


    public void draw() {

        p.pushMatrix();

        //Ghost transparency
        p.tint(255, this.transparency);

        ////// Vertical Movement Logic \\\\\\
        //If going up
        if (floatingUp) {
            //Decrease Y
            this.y -= ySpeed;
            //If it reached top, start falling
            if (this.y <= initialY - VERTICAL_BOUNCE_HEIGHT) {
                floatingUp = false;
            }
            //If falling down
        } else {
            //Increase Y
            this.y += ySpeed;
            //When it reaches the bottom, start floating up
            if (this.y >= initialY + VERTICAL_BOUNCE_HEIGHT) {
                floatingUp = true;
            }
        }
        //Applies a slight speed change to create the "float" effect
        if (ySpeed < VERTICAL_BOUNCE_HEIGHT / 10) {
            ySpeed += VERTICAL_GRAVITY;
        } else if (ySpeed > VERTICAL_BOUNCE_HEIGHT / 10) {
            ySpeed -= VERTICAL_GRAVITY;
        }
        //Makes sure 'ySpeed' doesn't go below 0 (for continuous movement)
        if (ySpeed <= 0) {
            ySpeed = 1;
        }

        ////// Horizontal Movement Logic \\\\\\
        // Ghost goes right to left
        if( direction.equalsIgnoreCase("left") ){

            // Flip on X axis
            p.scale(-1,1);
            p.image(ghostImg, -this.x, this.y);

            this.x -= speed;

            if( this.x < -ghostImg.width ){
                this.x = p.width + ghostImg.width; // Wrap around to the right

                // When wrapping, also reset vertical position to create new float
                this.y = (int) p.random(50, p.height / 3);
                this.initialY = this.y;
                this.ySpeed = 1;
                this.floatingUp = true;
            }
        // Ghost goes left to right
        } else {


            p.image( ghostImg, this.x, this.y );

            this.x += speed;

            if ( this.x > p.width ) {
                this.x = -ghostImg.width; // Wrap around to the left
                // When wrapping, also reset vertical position
                this.y = (int) p.random(50, p.height / 3);
                this.initialY = this.y;
                this.ySpeed = 1;
                this.floatingUp = true;
            }
        }

        p.popMatrix();
    }
}