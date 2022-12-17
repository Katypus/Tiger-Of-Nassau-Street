// This class handles all the graphics involved in
// the game, including the animations of Tony making the hoagies,
// updating the day, funds, number of hoagies, price, and the
// cutscene that plays when the game is beat.

import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getDialogService;


public class AnimationComponent extends Component {

    // A texture that contains all AnimationChannels that play during the game
    private AnimatedTexture texture;

    // Instance of HoagieHaven class, which deals with calculations of funds, demand, etc.
    private HoagieHaven ho;

    // "win condition": amount of money needed to win the game
    private int winCon;

    // whether Tony is making a hoagie or not
    private boolean hoa;

    // whether it is the beginning of the game or not
    private boolean start;

    // can be 1 or 0, used to determine if user is increasing price of hoagie or not
    private int price;

    // one sprite animation for when Tony isn't cooking, and one for when he is
    private AnimationChannel idle, cooking;

    // a romantic animation that plays when you win
    private AnimationChannel win;

    // instance of PropertyMap, allows for update of game variables
    private PropertyMap state;

    // Entity of the tiger cub
    private Entity tonyKid;

    public AnimationComponent(HoagieHaven haven) {

        // initialize AnimationChannels using spritesheets, specifying size of frames, and Duration of animation
        idle = new AnimationChannel(FXGL.image("spritesheet (2).png"), 5, 600, 600, Duration.seconds(0.5), 0, 0);
        cooking = new AnimationChannel(FXGL.image("spritesheet (2).png"), 5, 600, 600, Duration.seconds(2), 0, 4);
        win = new AnimationChannel(FXGL.image("spritesheet (3).png"), Duration.seconds(25), 7);

        //initialize texture to idle AnimationChannel
        texture = new AnimatedTexture(idle);

        // initialize HoagieHaven instance
        ho = haven;

        //initialize PropertyMap object
        state = FXGL.getWorldProperties();

        // select win condition
        winCon = 5000;

        // set start to true
        start = true;

    }

    // this method runs whenever an entity is added to the screen
    // in this case, Tony would be added to the screen, and texture
    // would be added to him
    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    // this method loops every frame, checking for changes (usually from user input)
    @Override
    public void onUpdate(double tpf) {

        // checks if user is making hoagies
        if (hoa) {

            // if so, change the AnimationChannel from idle to cooking
            if (texture.getAnimationChannel() == idle) {
                texture.playAnimationChannel(cooking);

                // update number of hoagies made
                ho.makeHoagie();

                // prevent animation from looping
                hoa = false;

            } else {
                // if user is not making hoagies, loop idle animation
                texture.loopAnimationChannel(idle);
            }
        }

        // if the price variable is 1, update price variable
        // and set price variable back to 0
        if (price > 0) {
            ho.changePrice();
            price = 0;
        }

        // if the Tony-Son random event has occurred, put a baby tiger
        // sprite on the screen
        if (ho.babyTiger()) {
            tonyKid = FXGL.entityBuilder()
                    .at(600, 200)
                    .view("tonykid.png")
                    .buildAndAttach();
        }

        // if player has won, clear the screen and end game
        if (ho.funds() > winCon) {
            Tiger.clearScreen();
            // if babyTiger is on the screen, take him off
            if (ho.babyTiger()) {
                tonyKid.removeFromWorld();
            }
            winner();
        }

        if (start) {
            getDialogService().showMessageBox("Help Tony the Tiger run Hoagie Haven! \n" +
                    "Press W to make hoagies for the day, \n" + "and press D to increase prices!" +
                    "\nRandom events occassionally occur, so \nkeep in mind the concept of supply and demand!" +
                    "\nHelp Tony raise $5000 to a buy a Christmas present for a special someone!");
            start = false;
        }
    }

    // play the winner ending animation
    public void winner() {
        if (texture.getAnimationChannel() == idle || texture.getAnimationChannel() == cooking) {
            texture.playAnimationChannel(win);
        }
    }

    // set hoa to true (so the hoagie block in OnUpdate runs),
    // update the number of hoagies on screen, and
    // play the cooking animation channel
    public void makeHoagie() {
        hoa = true;
        state.setValue("Hoagies", ho.hoagies());
        texture.playAnimationChannel(cooking);
    }

    // set price to 1 (so the price block in OnUpdate runs),
    // and update the price on screen
    public void upPrice() {
        price = 1;
        state.setValue("Price", (int) ho.price());
    }

    // code that runs at the end of each day
    public void endDay() {
        // if user has made enough to end the game, skip other code
        if (ho.funds() > winCon) {
            getDialogService().showMessageBox("CONGRATULATIONS!\nYou've made " + winCon + " dollars!"
                    + "\nFinally, you can buy what you've been saving up for...");
        } else {
            // else, run HoagieHaven methods to simulate a day and fetch a random event
            ho.simulateDay();
            ho.random();

            // show the profit and event in Message Boxes
            getDialogService().showMessageBox(ho.profitMessage());
            getDialogService().showMessageBox(ho.eventDescription());

            // update variables
            ho.endDay();

            // update game variables
            state.setValue("Day", ho.day());
            state.setValue("Price", 0);
            state.setValue("Hoagies", 0);
            state.setValue("Funds", (int) ho.funds());
        }
    }

}
