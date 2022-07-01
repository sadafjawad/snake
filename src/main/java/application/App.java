package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Snake Game Application
 */
public class App extends Application {
    
    public enum Directions{up, down, left, right}

    static int speed = 3;
    static int snakeColor = 0;
    static Color foodColor = Color.RED;
    static int foodX = 0;
    static int foodY = 0;
    static int width = 30;
    static int height = 30;
    static int cornerSize = 25;
    static boolean gameOver = false;
    static Random random = new Random();
    static Directions direction = Directions.up;
    static List<Point> snake = new ArrayList<Point>();
    private static Scene scene;


    public static class Point{
        int x,y;
        public Point(int x, int y){
            this.x=x;
            this.y=y;
        }
    }


    @Override
    public void start(Stage stage) throws IOException {
        
        newFood();
        Canvas can = new Canvas(width*cornerSize, height*cornerSize);
        GraphicsContext gr = can.getGraphicsContext2D();
        VBox root = new VBox();
        root.getChildren().add(can);

        new AnimationTimer(){
            long previousFrame = 0;
            public void handle(long currentFrame){
                if (previousFrame == 0){
                    previousFrame = currentFrame;
                    frame(gr);
                    return;
                }
                if (currentFrame - previousFrame > 1000000000/speed){
                    previousFrame = currentFrame;
                    frame(gr);
                }
            }
        }.start();


        scene = new Scene(root, width*cornerSize, height*cornerSize);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key ->{
            if (key.getCode() == KeyCode.UP){
                direction = Directions.up;
            }
            if (key.getCode() == KeyCode.DOWN){
                direction = Directions.down;
            }
            if (key.getCode() == KeyCode.LEFT){
                direction = Directions.left;
            }
            if (key.getCode() == KeyCode.RIGHT){
                direction = Directions.right;
            }

        });

        snake.add(new Point(width/2, height/2));
        snake.add(new Point(width/2, height/2));
        snake.add(new Point(width/2, height/2));
        snake.add(new Point(width/2, height/2));

        stage.setScene(scene);
        stage.setTitle("Snake 🐍");
        stage.show();
    }

    public static void frame(GraphicsContext gr) {


        if (gameOver) {
            gr.setFont(new Font("", 100));
            gr.setFill(Color.WHITE);
            gr.fillText("Haha Loser!", 135, 400);
            return;
        }


        for (int i = snake.size()-1; i>=1; i--){
            snake.get(i).x = snake.get(i-1).x;
            snake.get(i).y = snake.get(i-1).y;
        }

        switch (direction) {
            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case down:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
                break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
                break;

        }

        if (foodX == snake.get(0).x && foodY == snake.get(0).y){
            snake.add(new Point(-1, -1));
            newFood();
        }

        for (int i = 1; i < snake.size(); i++){
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y){
                gameOver = true;
            }
        }

        gr.setFill(Color.BLACK);
        gr.fillRect(0, 0, width*cornerSize, height*cornerSize);


        gr.setFill(foodColor);
        gr.fillOval(foodX*cornerSize, foodY*cornerSize, cornerSize, cornerSize);

        Color sc = Color.GREEN;
        switch(snakeColor){
            case 0: 
                sc = Color.WHEAT;
                break;
            case 1: 
                sc = Color.BLUE;
                break;
            case 2: 
                sc = Color.YELLOW;
                break;
            case 3: 
                sc = Color.LIGHTGREEN;
                break;
            case 4: 
                sc = Color.ORANGE;
                break;
        }

        for (Point c: snake){
            gr.setFill(sc);
            gr.fillRect(c.x*cornerSize, c.y*cornerSize, cornerSize -1, cornerSize -1);
        }
    }   

    // static void setRoot(String fxml) throws IOException {
    //     scene.setRoot(loadFXML(fxml));
    // }

    // private static Parent loadFXML(String fxml) throws IOException {
    //     FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    //     return fxmlLoader.load();
    // }


    public static void newFood(){
        start: while (true){
            foodX=random.nextInt(width);
            foodY=random.nextInt(height); 

            for (Point c: snake){
                if (c.x == foodX && c.y == foodY){
                    continue start;
                }
            }
            snakeColor = random.nextInt(5);
            speed++;
            break;
        }
    }

    public static void begin(String[] args){
        launch(args);
    }

}
