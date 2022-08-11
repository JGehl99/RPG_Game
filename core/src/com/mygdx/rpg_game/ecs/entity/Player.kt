package com.mygdx.rpg_game.ecs.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.mygdx.rpg_game.CustomCamera
import com.mygdx.rpg_game.ecs.component.*
import com.mygdx.rpg_game.entity.Anim
import com.mygdx.rpg_game.entity.player.Joystick
import com.mygdx.rpg_game.entity.player.PlayerState
import java.util.*

class Player(override var pos: Vector2, override var animConfig: String) :
    InputProcessor, PositionComponent, AnimationComponent, Box2DComponent,
    RenderComponent, DisposalComponent {

    override var animations: EnumMap<PlayerState, Anim> = EnumMap(PlayerState::class.java)
    override lateinit var currentAnimation: Anim
    override var spriteBatch: SpriteBatch = SpriteBatch()

    private var playerState: PlayerState = PlayerState.STATE_DOWN     // Current state
    private var isTouched = false                   // Flag to check if screen is touched
    private var originalPoint: Vector2 = Vector2()  // First xy point that user touched screen
    private var currentPoint: Vector2 = Vector2()   // Currently touched xy point
    private var oToCurrent: Vector2 = Vector2()         // Direction vec: currentPoint - OriginalPoint
    private val joystick: Joystick = Joystick()     // Class to draw joystick on screen
    private var largeCircleRadius = 200f
    private var smallCircleRadius = 25f

    private var deadzoneThreshold = 0.3f
    private var deadzoneRadius = deadzoneThreshold * largeCircleRadius
    private var outerRecip = 1.0f / (largeCircleRadius - deadzoneRadius)

    override lateinit var bodyDef: BodyDef
    override lateinit var body: Body
    override lateinit var fixtureDef: FixtureDef

    var maxVelocity = 1.25f

    init {
        loadAnimations()
    }

    /** Update the current animation of the Player based on playerState and return the current frame */
    override fun getCurrentFrame(): TextureRegion? {
        currentAnimation = when (playerState) {
            PlayerState.STATE_IDLE -> animations[PlayerState.STATE_IDLE]!!
            PlayerState.STATE_DOWN -> animations[PlayerState.STATE_DOWN]!!
            PlayerState.STATE_LEFT -> animations[PlayerState.STATE_LEFT]!!
            PlayerState.STATE_RIGHT -> animations[PlayerState.STATE_RIGHT]!!
            PlayerState.STATE_UP -> animations[PlayerState.STATE_UP]!!
        }
        return currentAnimation.getNextFrame()
    }

    override fun loadAnimations() {
        val spriteFolderHandler = Gdx.files.internal(animConfig)

        val properties = Properties()
        properties.load(spriteFolderHandler.read())

        properties.forEach { entry ->
            //Gdx.app.log("Properties", "${entry.key}, ${entry.value}")
            animations[try {PlayerState.valueOf(entry.key as String)} catch (e: IllegalArgumentException ){PlayerState.STATE_IDLE}] = Anim(entry.value as String, 1/10f)
        }
        currentAnimation = animations[PlayerState.STATE_IDLE]!!
    }

    override fun setPosition(pos: Vector2): Vector2 {
        this.pos = pos
        return this.pos
    }

    override fun setupBody(world: World) {
        val shape = PolygonShape()
        shape.setAsBox(currentAnimation.width / 2, currentAnimation.height / 2, Vector2(0f, 0f), 0f)

        bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
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

    override fun render(camera: CustomCamera) {

        this.pos = this.body.position

        spriteBatch.projectionMatrix = camera.combined
        spriteBatch.begin()
        spriteBatch.draw(
            getCurrentFrame(),
            this.pos.x - currentAnimation.width/2,
            this.pos.y - currentAnimation.height/2
        )
        spriteBatch.end()

        joystick.render(originalPoint, currentPoint)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        // Set flag
        isTouched = true

        // Store original touch point
        originalPoint = Vector2(screenX.toFloat(), screenY.toFloat())

        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        // Reset flag
        isTouched = false

        // Set player velocity to 0
        this.body.setLinearVelocity(0f, 0f)

        body.setLinearVelocity(0f, 0f)

        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        // If finger is being dragged across screen
        if (isTouched) {

            // Create Vector2 to hold the current touch point
            currentPoint = Vector2(screenX.toFloat(), screenY.toFloat())

            // Create direction Vector2 between origPoint and currentPoint
            oToCurrent = currentPoint.cpy().sub(originalPoint)

            val o2cMag = oToCurrent.len()

            //generate velocity scalar
            val velocityScalar: Float = if(o2cMag > largeCircleRadius){
                1f
            }else{
                (0.5f * (o2cMag - deadzoneRadius) + 0.5f * kotlin.math.abs(o2cMag - deadzoneRadius)) * outerRecip
            }

            // Gdx.app.log("Info", "dirVec: $oToCurrent")

            val scalingFactor = 1.0f/o2cMag * 4000f * Gdx.graphics.deltaTime

            val vx = oToCurrent.x * scalingFactor * velocityScalar * this.maxVelocity
            val vy = -oToCurrent.y * scalingFactor * velocityScalar * this.maxVelocity

            this.body.setLinearVelocity(vx, vy)

            // Gets dot product of direVec with diagonal vector in an X shape at center of largeCircle
            val aDotDir = Vector2(1f, 1f).dot(oToCurrent)
            val bDotDir = Vector2(1f, -1f).dot(oToCurrent)

            when (true) {
                // + + = moving right
                (aDotDir >= 0f && bDotDir >= 0) -> {
                    this.playerState = PlayerState.STATE_RIGHT
                }
                // + - = moving down
                (aDotDir >= 0f && bDotDir < 0) -> {
                    this.playerState = PlayerState.STATE_DOWN
                }
                // - + = moving up
                (aDotDir < 0f && bDotDir >= 0) -> {
                    this.playerState = PlayerState.STATE_UP
                }
                // - - = moving left
                (aDotDir < 0f && bDotDir < 0) -> {
                    this.playerState = PlayerState.STATE_LEFT
                }
            }
        }
        return true
    }

    override fun keyDown(keycode: Int): Boolean { return true }
    override fun keyUp(keycode: Int): Boolean { return true }
    override fun keyTyped(character: Char): Boolean { return true }
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean { return true }
    override fun scrolled(amountX: Float, amountY: Float): Boolean { return true }

    override fun dispose() {
        this.dispose()
        spriteBatch.dispose()
        animations.forEach { it.value.dispose() }
    }
}