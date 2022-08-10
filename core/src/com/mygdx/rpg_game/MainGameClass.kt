package com.mygdx.rpg_game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.mygdx.rpg_game.entity.player.Player


class MainGameClass : ApplicationAdapter() {

    private lateinit var player: Player
    private lateinit var camera: CustomCamera
    private lateinit var tileMap: TileMap
    private lateinit var bgMusic: Music
    private var multiplexer = InputMultiplexer()

    // Tile size in pixels required for viewports
    private val tileSize = 16f

    // Number of tiles visible on the screen, essentially this is the amount of zoom on the map
    private val visibleTilesWidth = 10f
    private val visibleTilesHeight = 10f

    // create() function, part of the ApplicationAdapter() interface
    override fun create() {

        // Create Player object, x and y are spawn coordinates, spawn it 5 tiles left, 5 tiles up
        player = Player(Vector2(5f * tileSize, 5f * tileSize), Gdx.files.internal("anim/player/"))

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
        tileMap = TileMap("maps/test_map.tmx")

        // Load and play background music
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("bg_music.mp3"))
        bgMusic.isLooping = true
        bgMusic.play()
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
    }

    // Dispose of any unneeded assets on stop
    override fun dispose() {
        player.dispose()
        tileMap.dispose()
        bgMusic.dispose()
    }
}