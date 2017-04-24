# IWJ Roadmap
Little comprehensive path to take for designing the rest of the engine.

## Detailed package tree
- `io.neilljohnston.github.iwannajump`
    - `io.neilljohnston.github.iwannajump.elements` Has interactive game elements, basically just sprites and tiles.
        - `Sprite.class` Extends AABB, includes all collision detection.
        - `AABB.class` "Tile" doesn't exist as its own class, instead a tile is a fixed AABB.
    - `io.neilljohnston.github.iwannajump.engine` Core engine stuff, manipulates the relationship between sprites and tiles and the map and stuff.
        - `IWannaJump.class` The main class for LibGDX to call. Extends Game.
        - `Screen.class` Call logic, then render everything.
        - `Manager.class` Assist tracking, collisions, etc.
        - `SplitGridSpacialMap.class` Custom spacial map for use with collision detection broad-phase. It's important that the Manager class doesn't call the SGSM class itself, but rather an overarching interface so that different broad-phases can be implemented.
    - `io.neilljohnston.github.iwannajump.exmath` Extended math functions.
        - `Smooth.class` Smoothing algorithms.

## Top-level components: Comments (2017-04-23)
- Playable parts: Sprites, Tiles
    - Although in the current engine, all collisions are AABB-based with some extra code for slopes, the AABB-collision detections should be open-ended enough to allow for other types of collision detection to be implemented later, if desired.
    - The sprites and tiles are generated from a Tiled map. This is a problem right now, as I don't want Tiled to be a dependency, rather a suggestion (because it is pretty great).
- Rendering: Game screen
    - LibGDX makes it easiest to have the game screen do rendering and logic, which I'm uncomfortable with but have to reside with because it's easier than separating the two. So, the game screen will be an abstract class with concrete methods for rendering and creating the game from a map, otherwise any calls to game logic will be left to override.
- Managing: A game manager
    - I want some form of game manager that tracks a single loaded area, activating/deactivating sprites and tiles as the area changes. This comes paired with a broad-phase collision manager, right one of my own breed: the split-grid spatial map. However, being able to track and load/unload chunks of map is difficult to make, and isn't finished yet.
    - _Only one game manager need exist per game, so this part should be static: for easy access from sprites and such, and because a new one should not ever be created._
- Math
    - The exmath package exists right now, there I'll put all the math necessities. I guess.
