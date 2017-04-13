# Getting Started
Clone or fork this repo
 - `git clone https://github.com/tacocat/lambda-starter-kit.git`

Run your game
 - `./gradlew run`

Congrats! You are ready to go.

## Prerequisites
 - [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (8 or higher)
 - [Gradle](https://gradle.org/)  (3.4.1 or higher)

## Adding behavior
When running your game for the first time, you'll see three squares on screen. Let's open `src/main/java/GameApp.java` and see how the squares are defined.

```java
game.init((systems, entities, components) -> {
    // Register game systems
    // systems.add( ... )

    // Setup initial game state
    entities.add(
        new Entity(
            new Renderable(new Square(Color.BLUE), 50f, -50f)
        ),
        new Entity(
            new Renderable(new Square(Color.GREEN), -50f, -50f)
        ),
        new Entity(
            new Renderable(new Square(Color.RED), 0f, 50f)
        )
    );
});
```
When initializing our game, we are given a reference to the`entities` list. Here we can add any `Entity` we want present at the start of the game.

Our three squares are nice, but boring! Let's make them do _something_.

When defining an `Entity`, we can give a list of `Behaviors` that `Entity` will follow. Each of our three entities is already given the `Renderable` behavior, which lets that entity be represented by a `Graphic` drawn at a specific location in the game world.

If we want an `Entity` to do more, we can give it a new `Behavior`. Let's try:
```java
new Entity(
    new Renderable(new Square(Color.GREEN), -50f, -50f),
    new ConstantSpeed(1f, 0f)
)
```

Check out the changes by running the game (`./gradlew run`). Now one of our squares is charging along at a constant speed.

## Defining new game logic
We got our game started quickly by using common prebuilt logic, but now let's add something custom. Whenever a square gets too close to the right side of the screen, let's punish it by destroying it.

Game logic is defined in `GameSystems`, so let's make one.

```java
// src/main/java/SquareDestroyer.java
import com.tacocat.lambda.core.system.GameSystem;

public class SquareDestroyer extends GameSystem {
    @Override
    protected void update() {
        
    }
}
```

The first thing your IDE will ask after extending `GameSystem` is to implement the `update` method. Let's stub it out for now.

Next step, make sure our game knows to use our system.

```java
// src/main/java/GameApp.java
game.init((systems, entities, components) -> {
    // Register game systems
    systems.add(new SquareDestroyer());

    ...
});
```

When initializing our game, we can add any `GameSystem` we want to use.

Okay, `SquareDestroyer` is ready to go. Now we can move to the important part, implementing our game logic.

Each `Entity` is backed by a set of `Components`, which represent pieces of game data. Our `GameSystems` are designed to find all active `Components` of a certain type and perform an action based on that data.

In our case, any entity with the `Renderable` behavior has a `Transform` component with `(x, y, z)` values. For `SquareDestroyer` to do it's job, we want to find all `Transform` components, check the `x` value, and destroy the `Entity` if it's too far to the right.

Here's what the code looks like:
```java
// src/main/java/SquareDestroyer.java
protected void update() {
    // Get all transform components from any Entity with the Renderable behavior
    findAllByName(Renderable.TRANSFORM).forEach(transform -> {
        // Get the X value from the transform component
        // and check if it's too far to the right
        if (transform.get(Transform.X) > 250f) {
            // If so, destroy the Entity this component belongs to
            destroy(transform.getParent());
        }
    });
}
```

Next time we run our game, any square too far to the right will disappear.
