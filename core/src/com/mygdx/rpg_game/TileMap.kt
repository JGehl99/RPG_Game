package com.mygdx.rpg_game

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Matrix4

/**
 *
 * Simple TiledMap wrapper class that incorporated the renderer as well. Provides map information
 * such as width, height, and tileSize.
 *
 * @param filepath Path to .tmx file
 *
 * @constructor Gets a layer from the tiledMap and retrieves map width and height in pixels.
 *
 * @author Joshua Gehl
 */


class TileMap(filepath: String) {

    // Load in the map from a file
    private var tiledMap: TiledMap = TmxMapLoader().load(filepath)

    // Create tiledMapRenderer
    private var tiledMapRenderer: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)

    // Tile size and max width and height of map
    private val tileSize = 16f
    var mapWidth: Float = 0f
    var mapHeight: Float = 0f

    // Grab a layer form the map and get width and height in tiles, multiply by tile size to get
    // pixels
    init {
        val layer = tiledMap.layers["Layer"] as TiledMapTileLayer
        mapWidth =  layer.width.toFloat() * tileSize
        mapHeight = layer.height.toFloat() * tileSize
    }

    /**
     * Sets the map to be in the [camera]'s view, then renders the [tiledMap].
     *
     * @param camera CustomCamera object
     *
     * @author Joshua Gehl
     */
    fun render(camera: CustomCamera) {
        tiledMapRenderer.setView(camera)
        tiledMapRenderer.render()
    }

    /** Dispose of any unneeded objects */
    fun dispose() {
        tiledMap.dispose()
        tiledMapRenderer.dispose()
    }
}