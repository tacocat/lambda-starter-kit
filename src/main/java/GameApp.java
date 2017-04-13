import com.tacocat.lambda.common.render.behavior.Renderable;
import com.tacocat.lambda.core.Game;
import com.tacocat.lambda.core.entity.Entity;
import com.tacocat.lambda.core.platform.DesktopPlatform;
import com.tacocat.lambda.graphics.presets.Square;

import java.awt.*;

public class GameApp {
    public static void main(String[] args) {
        Game game = new Game(new DesktopPlatform());

        game.init((systems, entities, components) -> {
            // Register game systems
            // systems.add( ... )

            // Setup initial game state
            entities.add(
                new Entity(
                    new Renderable(new Square(Color.BLUE), 50f, -50f)
                )
            );
            entities.add(
                new Entity(
                    new Renderable(new Square(Color.GREEN), -50f, -50f)
                )
            );
            entities.add(
                new Entity(
                    new Renderable(new Square(Color.RED), 0f, 50f)
                )
            );
        });

        game.run();
    }
}
