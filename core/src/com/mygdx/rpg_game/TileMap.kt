package com.mygdx.rpg_game

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer

/**
 *
 * Simple TiledMap wrapper class that incorporated the renderer as well. Provides map information
 * such as width, height, and tileSize.
 *
 * @param filepath Path to .tmx file
 * @param camera Reference to [CustomCamera]
 *
 * @constructor Gets a layer from the tiledMap and retrieves map width and height in pixels.
 *
 * @author Joshua Gehl
 */


class TileMap(filepath: String, private var camera: CustomCamera) {

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

    // Render function to render map
    fun render() {
        tiledMapRenderer.setView(camera)
        tiledMapRenderer.render()
    }

    // Disposal of assets
    fun dispose() {
        tiledMap.dispose()
        tiledMapRenderer.dispose()
    }
}