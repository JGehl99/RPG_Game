package com.mygdx.rpg_game.ecs.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.mygdx.rpg_game.CustomCamera
import com.mygdx.rpg_game.ecs.component.*

class Rock(override var pos: Vector2, override var spritePath: String) :
    PositionComponent, RenderComponent, SpriteComponent, DisposalComponent, Box2DComponent {

    override var spriteBatch: SpriteBatch = SpriteBatch()
    override var texture: Texture = Texture(Gdx.files.internal(spritePath))

    override fun dispose() {
        texture.dispose()
    }

    override fun setPosition(pos: Vector2): Vector2 {
        this.pos = pos
        return this.pos
    }

    override fun render(camera: CustomCamera) {
        spriteBatch.projectionMatrix = camera.combined
        spriteBatch.begin()
        spriteBatch.draw(texture, pos.x - texture.width/2, pos.y - texture.height/2)
        spriteBatch.end()
    }

    override lateinit var bodyDef: BodyDef
    override lateinit var body: Body
    override lateinit var fixtureDef: FixtureDef

    override fun setupBody(world: World) {
        val shape = PolygonShape()
        shape.setAsBox(texture.width / 2f, texture.height / 2f, Vector2(0f, 0f), 0f)

        bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.fixedRotation = true

        bodyDef.position.set(this.pos)
        body = world.createBody(bodyDef)
        body.isSleepingAllowed = false
        fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 1f
        body.createFixture(shape, 0.0f)
        shape.dispose()
    }
}