package trevorwarner.individualgame;

import android.widget.ImageButton;

/**
 * Created by BrickMaster logankruse11 on 4/29/2015.
 */
public class Brick {

    private int fullBrickHealth;
    private int currentBrickHealth;
    private int brickHits=0;
    private ImageButton brickButton;

    /**
     * Creates a new brick object at the start of the
     * round with the updated attributes.
     * @param roundCount
     * @param brickButton
     */
    public Brick(int roundCount, ImageButton brickButton){
        this.fullBrickHealth=roundCount*roundCount+2;
        this.currentBrickHealth=fullBrickHealth;
        this.brickButton=brickButton;
        brickButton.setImageResource(R.drawable.brick);
    }

    /**
     * Sets the health of the brick object
     * @param clickPower
     */
    public void setBrickHealth(int clickPower){
        if(clickPower>currentBrickHealth) {
            brickHits += currentBrickHealth;
            currentBrickHealth-=clickPower;
        }else{
            brickHits+=clickPower;
            currentBrickHealth-=clickPower;
        }
        setBrickButton();
    }

    /**
     * Updates the brick image to correspond to its health
     */
    public void setBrickButton(){
        if(currentBrickHealth<=0){
            brickButton.setImageResource(R.drawable.pile_rocks);
        }else if(fullBrickHealth/3>=currentBrickHealth){
            brickButton.setImageResource(R.drawable.broken_brick);
        }else if((fullBrickHealth*2)/3>=currentBrickHealth){
            brickButton.setImageResource(R.drawable.cracked_brick);
        }
    }

    /**
     * returns the current health of the brick
     * @return
     */
    public int getCurrentBrickHealth(){return currentBrickHealth;}

    /**
     * creates the image button that the brick is tied to
     * @return
     */
    public ImageButton getBrickButton(){return brickButton;}

    /**
     * returns the amount of hits on the brick.
     * @return
     */
    public int getBrickHits(){return brickHits;}

}
