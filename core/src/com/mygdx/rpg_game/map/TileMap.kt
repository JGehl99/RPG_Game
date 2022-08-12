package com.mygdx.rpg_game.map

import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.mygdx.rpg_game.CustomCamera


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


class TileMap(filepath: String, private val world: World) {

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
        val layer = tiledMap.layers["GroundLayer"] as TiledMapTileLayer
        mapWidth =  layer.width.toFloat() * tileSize
        mapHeight = layer.height.toFloat() * tileSize

        parseMap()
    }

    private fun getBodyDef(x: Float, y: Float): BodyDef {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.position[x] = y
        return bodyDef
    }

    private fun parseMap() {
        for (obj in tiledMap.layers["CollisionPolygons"].objects) {
            when (obj) {
                is RectangleMapObject -> {
                    val rectangle: Rectangle = obj.rectangle
                    val bodyDef = getBodyDef(
                        rectangle.x + rectangle.width/2,
                        rectangle.y + rectangle.height/2
                    )

                    val body = world.createBody(bodyDef)
                    val polygonShape = PolygonShape()
                    polygonShape.setAsBox(rectangle.width / 2f, rectangle.height / 2f)
                    body.createFixture(polygonShape, 0.0f)
                    polygonShape.dispose()
                }
                is EllipseMapObject -> {
                    val ellipse = obj.ellipse
                    val bodyDef = getBodyDef(
                        ellipse.x,
                        ellipse.y
                    )

                    val body = world.createBody(bodyDef)
                    val polygonShape = PolygonShape()
                    polygonShape.setAsBox(ellipse.width / 2f, ellipse.height / 2f)
                    body.createFixture(polygonShape, 0.0f)
                    polygonShape.dispose()
                }
                is PolygonMapObject -> {
                    val polygon: Polygon = obj.polygon
                    val bodyDef = getBodyDef(
                        polygon.x,
                        polygon.y
                    )

                    val body: Body = world.createBody(bodyDef)
                    val polygonShape = PolygonShape()
                    polygonShape.set(polygon.vertices)
                    body.createFixture(polygonShape, 0.0f)
                    polygonShape.dispose()
                }
            }
        }
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