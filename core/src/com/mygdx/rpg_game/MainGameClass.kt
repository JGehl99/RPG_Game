package com.mygdx.rpg_game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils

class MainGameClass : ApplicationAdapter() {

    private lateinit var player: Player
    private lateinit var camera: CustomCamera
    private lateinit var tileMap: TileMap
    private lateinit var joystick: Joystick

    private lateinit var stage: Stage

    private val tileSize = 16f
    private val visibleTilesWidth = 10f
    private val visibleTilesHeight = 10f

    override fun create() {

        stage = Stage()

        player = Player(5f * tileSize, 5f * tileSize)

        joystick = Joystick(player)

        Gdx.input.inputProcessor = joystick

        camera = CustomCamera(
            tileSize * visibleTilesWidth,
            tileSize * visibleTilesHeight,
            player
        )

        tileMap = TileMap("maps/test_map.tmx", camera)
    }

    override fun render() {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 0.0f)

        camera.updateCamera(0f, 0f, tileMap.mapWidth, tileMap.mapHeight)

        tileMap.render()

        player.update(camera.combined)
    }

    override fun dispose() {
        player.dispose()
        tileMap.dispose()
    }
}