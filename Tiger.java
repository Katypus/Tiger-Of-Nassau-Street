// client class
// Senses user input, displays labels, pictures, and entities on screen
// that do not have AnimationComponents

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;


public class Tiger extends GameApplication {

    // Entities on screen
    @NotNull
    private static Entity tony, kitchen, block;

    // instance of HoagieHaven
    private HoagieHaven ho;

    // sets the size of the window, the name above the window, and the version
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1600);
        settings.setHeight(800);
        settings.setTitle("Tiger Of Nassau Street");
        settings.setVersion("0.1");
    }

    // Calls methods in AnimationComponent depending on what key is pressed
    @Override
    protected void initInput() {

        // Press W to make a hoagie
        FXGL.getInput().addAction(new UserAction("Hoagie") {
            @Override
            protected void onAction() {
                tony.getComponent(AnimationComponent.class).makeHoagie();
            }
        }, KeyCode.W);

        // Press D to up price
        FXGL.getInput().addAction(new UserAction("Up Price") {
            @Override
            protected void onAction() {
                tony.getComponent(AnimationComponent.class).upPrice();
            }
        }, KeyCode.D);

        //Press Enter once satisfied with hoagie number and price to end the day
        FXGL.getInput().addAction(new UserAction("Enter") {
            @Override
            protected void onAction() {
                tony.getComponent(AnimationComponent.class).endDay();
            }
        }, KeyCode.ENTER);

    }

    @Override
    protected void initGame() {

        // white christmas background that is invisible until the ending ;)
        FXGL.entityBuilder()
                .at(0, 0)
                .view("white christmas.jpg")
                .buildAndAttach();

        // kitchen background that is visible for most of the game
        kitchen = FXGL.entityBuilder()
                .view("kitchen.png")
                .at(0, -500)
                .buildAndAttach();

        // white rectangle behind the game variables
        block = FXGL.entityBuilder()
                .view("img_3.png")
                .at(950, -350)
                .buildAndAttach();

        // Tony the Tiger sprite
        tony = FXGL.entityBuilder()
                .at(350, 200)
                .with(new AnimationComponent(ho))
                .buildAndAttach();
        
    }

    // initializes a number of game variables
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        // creates a new HoagieHaven instance
        ho = new HoagieHaven(100);

        // Maps certain player variables to the corresponding instance variables
        // in HoagieHaven
        vars.put("Funds", (int) ho.funds());
        vars.put("Hoagies", ho.hoagies());
        vars.put("Day", ho.getDay());
        vars.put("Price", (int) ho.price());

        // Some saved fonts to use later
        vars.put("Font", Color.MIDNIGHTBLUE);
        vars.put("Font 2", Color.CORNFLOWERBLUE);

    }

    @Override
    protected void initUI() {

        // initialize all texts
        Text uiFunds = new Text();
        Text hoagieNum = new Text();
        Text dayNum = new Text();
        Text fundLabel = new Text("DAY: \nFUNDS: ");
        Text hoagieLabel = new Text("HOAGIES: \nPRICE: ");
        Text priceNum = new Text();

        // set Font style, size, and color
        uiFunds.setFont(new Font("Comic Sans MS", 72));
        fundLabel.setFont(new Font("Comic Sans MS", 72));
        hoagieNum.setFont(new Font("Comic Sans MS", 72));
        hoagieLabel.setFont(new Font("Comic Sans MS", 72));
        dayNum.setFont(new Font("Comic Sans MS", 72));
        priceNum.setFont(new Font("Comic Sans MS", 72));

        fundLabel.fillProperty().bind(getop("Font"));
        uiFunds.fillProperty().bind(getop("Font"));
        dayNum.fillProperty().bind(getop("Font"));
        priceNum.fillProperty().bind(getop("Font 2"));
        hoagieLabel.fillProperty().bind(getop("Font 2"));
        hoagieNum.fillProperty().bind(getop("Font 2"));

        // set where each label will be
        uiFunds.setTranslateX(getAppWidth() - 200);
        uiFunds.setTranslateY(200);

        fundLabel.setTranslateX(getAppWidth() - 600);
        fundLabel.setTranslateY(100);

        hoagieLabel.setTranslateX(getAppWidth() - 600);
        hoagieLabel.setTranslateY(300);

        hoagieNum.setTranslateX(getAppWidth() - 200);
        hoagieNum.setTranslateY(300);

        priceNum.setTranslateX(getAppWidth() - 200);
        priceNum.setTranslateY(400);

        dayNum.setTranslateX(getAppWidth() - 200);
        dayNum.setTranslateY(100);


        // bind to game variables
        uiFunds.textProperty().bind(getip("Funds").asString());
        hoagieNum.textProperty().bind(getip("Hoagies").asString());
        dayNum.textProperty().bind(getip("Day").asString());
        priceNum.textProperty().bind(getip("Price").asString());

        // add all labels to screen
        addUINode(uiFunds);
        addUINode(fundLabel);
        addUINode(hoagieNum);
        addUINode(hoagieLabel);
        addUINode(dayNum);
        addUINode(priceNum);
    }

    // removes kitchen image, block image, and all labels from screen
    // will be called when user wins game
    public static void clearScreen() {
        kitchen.removeFromWorld();
        block.removeFromWorld();

        getGameScene().clearUINodes();
    }

    // launches game
    public static void main(String[] args) {
        launch(args);
    }
}
