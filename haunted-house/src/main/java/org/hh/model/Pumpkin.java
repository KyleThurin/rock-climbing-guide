package org.hh.model;

import processing.core.PApplet;

public class Pumpkin {

    private static final int STEM_COLOR = 0x2EA22C;
    private static final int EYES_COLOR = 0xF0F000;
    private static final int DEFAULT_PUMPKIN_COLOR = 0xE26238;
    private static final int PUMPKIN_SIZE_PIXELS = 150;

    private int x;
    private int y;
    //Initialize xSpeed to 0
    private int xSpeed = 0;
    private boolean bounce = false;

    //Sets the default bounce height
    private int bounceHeight = 15;
    public int bounceSpeed = 0;
    private int gravity = 1;
    private int pumpkinColor;
    private int glowingEyesColor;
    private int greenStemColor;
    private PApplet p;

    public Pumpkin(PApplet p) {
        this(p, (int)p.random(PUMPKIN_SIZE_PIXELS, p.width - (2 * PUMPKIN_SIZE_PIXELS)));
    }
    public Pumpkin(PApplet p, int x) {
        this(p, x, DEFAULT_PUMPKIN_COLOR);
    }
    public Pumpkin(PApplet p, int x, int pumpkinColor) {
        this.p = p;
        this.x = x;
        this.y = p.height - 300;
        this.glowingEyesColor = p.color(
                p.red(EYES_COLOR) + p.random(15),
                p.green(EYES_COLOR) + p.random(15),
                p.blue(EYES_COLOR) + p.random(255));
        this.greenStemColor = p.color(p.red(STEM_COLOR), p.green(STEM_COLOR), p.blue(STEM_COLOR));
        this.pumpkinColor = p.color(p.red(pumpkinColor), p.green(pumpkinColor), p.blue(pumpkinColor));
    }

    public void setPumpkinColor( int newColor ){
        this.pumpkinColor = newColor;
    }

    // ---------------------------------------------------------
    // Call this method from the setup() method,
    // NOT the draw() method
    // ---------------------------------------------------------
    public void setBounceHeight( int newHeightInPixels ){
        this.bounceHeight = newHeightInPixels;
    }

    public void bounce(){
        this.bounce = true;
    }

    public void stop(){
        this.bounce = false;
        this.xSpeed = 0;
    }

    public void moveRight( int speed ){
        this.xSpeed = speed;
    }

    public void moveLeft( int speed ){
        this.xSpeed = -speed;
    }

    public void draw(){
        p.pushStyle();
        //Vertical movement (bouncing)
        if( bounceSpeed < bounceHeight ){
            bounceSpeed += gravity;
        }

        y += bounceSpeed;
        //Adjusted floor position based on current code
        if( this.y > p.height - 100 ) {
            this.y = p.height - 100;

            if( bounce ) {
                bounceSpeed = -bounceSpeed;
            }
        }
        //Horizontal movement and boundary bouncing
        this.x += xSpeed;

        /*
        //Used for pumpkin looping one way across the screen
        if( this.x > p.width + this.PUMPKIN_SIZE_PIXELS){
            this.x = -this.PUMPKIN_SIZE_PIXELS;
        }

        if( this.x < -this.PUMPKIN_SIZE_PIXELS){
            this.x = p.width;
        }
        */

        //Check if pumpkin hits the RIGHT edge
        if (this.x + PUMPKIN_SIZE_PIXELS / 2 > p.width) {
            //Reposition to prevent going off-screen
            this.x = p.width - PUMPKIN_SIZE_PIXELS / 2;
            //Reverse direction
            this.xSpeed *= -1;
        }

        //Check if pumpkin hits the LEFT edge
        if (this.x - PUMPKIN_SIZE_PIXELS / 2 < 0) {
            //Reposition
            this.x = PUMPKIN_SIZE_PIXELS / 2;
            //Reverse direction
            this.xSpeed *= -1;
        }

        p.ellipseMode(PApplet.CENTER);

        //Black outline
        p.stroke(0);
        p.strokeWeight(2);

        //Draw top stem
        p.fill(greenStemColor);
        p.rect(x - 10, y - (PUMPKIN_SIZE_PIXELS / 2f) - 20, 15, 20);

        //Draw body
        p.fill(pumpkinColor);
        p.ellipse(x, y, PUMPKIN_SIZE_PIXELS, PUMPKIN_SIZE_PIXELS);

        //Set glowing eyes
        p.fill(glowingEyesColor, p.random(200) + 50 );

        //Draw eyes
        p.triangle(x - 30, y - 30, x - 15, y, x - 45, y);
        p.triangle(x + 30, y - 30, x + 15, y, x + 45, y);
        p.triangle(x, y, x + 10, y + 20, x - 10, y + 20);

        //Draw mouth
        p.arc(x, y + 20, 80, 80, 0, PApplet.PI, 0);
        p.fill(pumpkinColor);
        p.arc(x, y + 20, 80, 30, 0, PApplet.PI, 0);

        //Draw squares for teeth
        p.fill(this.pumpkinColor);
        p.rect(x + 10, y + 31, 10, 10);
        p.rect(x - 20, y + 31, 10, 10);
        p.rect(x - 5, y + 50, 10, 10);

        //Remove the top line of the teeth
        p.strokeWeight(3);
        p.stroke(this.pumpkinColor);
        p.fill(this.pumpkinColor);
        p.line(x + 10, y + 31, x + 20, y + 31);
        p.line(x - 20, y + 31, x - 10, y + 31);
        p.line(x - 5, y + 60, x +5, y + 60);

        p.popStyle();
    }
}
