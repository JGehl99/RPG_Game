package com.mygdx.rpg_game.ecs.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.mygdx.rpg_game.CustomCamera
import com.mygdx.rpg_game.ecs.component.DisposalComponent
import com.mygdx.rpg_game.ecs.component.PositionComponent
import com.mygdx.rpg_game.ecs.component.RenderComponent
import com.mygdx.rpg_game.ecs.component.SpriteComponent

class Rock(override var pos: Vector2, override var spritePath: String) : PositionComponent, RenderComponent, SpriteComponent, DisposalComponent {

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
}