package org.hh.house;

import org.hh.model.Ghost;
import org.hh.model.Lightning;
import org.hh.model.Pumpkin;
import processing.core.PApplet;
import processing.core.PImage;

public class HauntedHouse extends PApplet {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private PImage scaryHouse;

    //Creates an instance variables for 'Ghost', 'Pumpkin', and 'Lightning' (not initialized)
    private Ghost ghost;
    private Pumpkin pumpkin;
    private Lightning lightning;

    //Default bounce height
    private final int DEFAULT_PUMPKIN_BOUNCE_HEIGHT = 15;

    //Index/set the increased bounce height
    private final int INCREASED_PUMPKIN_BOUNCE_HEIGHT = 30;


    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        this.scaryHouse = loadImage("images/scaryHouse1.jpg");
        this.scaryHouse.resize(WIDTH, HEIGHT);

        //Assigning the instance variables to new objects
        this.ghost = new Ghost(this);
        this.pumpkin = new Pumpkin(this);
        this.pumpkin.bounce(); //Calls the bounce method for the pumpkin
        this.pumpkin.setBounceHeight(5);
        this.pumpkin.moveRight(2); //Makes the pumpkin move to the right # pixels per frame
        this.pumpkin.setBounceHeight(DEFAULT_PUMPKIN_BOUNCE_HEIGHT); //Sets the initial bounce height
        this.lightning = new Lightning(this);

    }

    public void draw() {
        drawBackground(this.scaryHouse);

        //Calls the draw() method for 'ghost' and 'pumpkin'
        this.ghost.draw();
        this.pumpkin.draw();

        //Only calls the draw() method for 'lightning' if 'mousePressed'
        if(mousePressed){
            this.lightning.draw();
        }

        drawGrayscale(false); //true: Turns the grayscale effect on
    }

    // This method is called once every time a mouse button is pressed.
    public void mousePressed() {
        // When the mouse is pressed, increase the pumpkin's bounce height
        this.pumpkin.bounceSpeed = -this.INCREASED_PUMPKIN_BOUNCE_HEIGHT; // Force an upward bounce immediately
    }

    // This method is called once every time a mouse button is released.
    public void mouseReleased() {
        // When the mouse is released, reset the pumpkin's bounce height to its default
        this.pumpkin.setBounceHeight(DEFAULT_PUMPKIN_BOUNCE_HEIGHT);
    }

    // ---------------------------------------------------------
    // Here are some other methods you can try:
    //  pumpkin.setPumpkinColor();

    //  pumpkin.stop();
    //  ghost.setGhostTransparency();
    //  lightning.setLightningFlash();
    // ---------------------------------------------------------

    void drawBackground(PImage bgImage) {
        background(bgImage);
        drawFloor();
    }

    void drawFloor() {
        int floorHeight = 30;

        this.pushStyle();

        fill(10, 10, 30);
        rect(0, height - floorHeight, width, floorHeight);

        this.popStyle();
    }

    // Call this method at the very bottom of the draw() method!
    void drawGrayscale(boolean grayscaleEnabled) {
        if (grayscaleEnabled) {
            filter(GRAY);
        }
    }
}
