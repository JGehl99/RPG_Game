package com.mygdx.rpg_game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.ScreenUtils
import com.mygdx.rpg_game.entity.player.Player


/**
 * Main Game class, creates all objects, renders all objects, disposes of all objects.
 *
 * @author Joshua Gehl
 */
class MainGameClass : ApplicationAdapter() {

    private lateinit var player: Player
    private lateinit var camera: CustomCamera
    private lateinit var tileMap: TileMap
    private lateinit var bgMusic: Music

    var world = World(Vector2(0f, 0f), false)
    private var multiplexer = InputMultiplexer()

    // Tile size in pixels required for viewports
    private val tileSize = 16f

    // Number of tiles visible on the screen, essentially this is the amount of zoom on the map
    private val visibleTilesWidth = 10f
    private val visibleTilesHeight = 10f

    private lateinit var debugRenderer: Box2DDebugRenderer

    // create() function, part of the ApplicationAdapter() interface
    override fun create() {

        debugRenderer = Box2DDebugRenderer()

        // Create Player object, x and y are spawn coordinates
        player = Player(Vector2(5*16f, 5*16f), Gdx.files.internal("anim/player/"))


        // Every input object needs to be added to the multiplexer or else only one input device can
        // exist.
        multiplexer.addProcessor(player)
        Gdx.input.inputProcessor = multiplexer

        // Create CustomCamera, display the # of visible tiles specified above
        camera = CustomCamera(
            tileSize * visibleTilesWidth,
            tileSize * visibleTilesHeight
        )

        // Create TileMap
        tileMap = TileMap("maps/test_map.tmx", world)

        // Load and play background music
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("bg_music.mp3"))
        bgMusic.isLooping = true
        bgMusic.play()

        player.setupBody(world)
    }

    // Render class called once every frame
    override fun render() {

        // Clear screen
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 0.0f)

        // Center camera on player, includes clamping the camera viewport to the maps
        camera.followPlayer(player, tileMap.mapWidth, tileMap.mapHeight)

        // Render tileMap
        tileMap.render(camera)

        // Render player, pass camera projection view matrix to function
        player.render(camera.combined)

        debugRenderer.render(world, camera.combined)
        world.step(1/60f, 8, 3)
    }

    // Dispose of any unneeded assets on stop
    override fun dispose() {
        player.dispose()
        tileMap.dispose()
        bgMusic.dispose()
    }
}