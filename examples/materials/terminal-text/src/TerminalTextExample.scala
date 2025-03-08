package indigoexamples

import indigo.*
import generated.Config
import generated.Assets
import roguelikestarterkit.*
import ultraviolet.syntax.*

import scala.annotation.nowarn
import scala.scalajs.js.annotation.*

final case class Model()
object Model:
  val initial: Model = Model()

@JSExportTopLevel("IndigoGame")
object TerminalTextExample extends IndigoSandbox[Unit, Model]:

  val config: GameConfig =
    Config.config.noResize
      .withMagnification(2)

  val assets: Set[AssetType] =
    Assets.assets.assetSet

  val fonts: Set[FontInfo]       = Set(RoguelikeTiles.Size10x10.Fonts.fontInfo)
  val animations: Set[Animation] = Set()

  val shaders: Set[ShaderProgram] =
    roguelikestarterkit.shaders.all ++ Set(CustomShader.shader)

  def setup(assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def initialModel(startupData: Unit): Outcome[Model] =
    Outcome(Model.initial)

  def updateModel(context: Context[Unit], model: Model): GlobalEvent => Outcome[Model] =
    case _ =>
      Outcome(model)

  def present(
      context: Context[Unit],
      model: Model
  ): Outcome[SceneUpdateFragment] =

    val message: String =
      """
      |╔═════════════════════╗
      |║ Hit Space to Start! ║
      |╚═════════════════════╝
      |""".stripMargin

    Outcome(
      SceneUpdateFragment(
        Text(
          message,
          RoguelikeTiles.Size10x10.Fonts.fontKey,
          TerminalText(Assets.assets.AnikkiSquare10x10, RGBA.Cyan, RGBA.Blue)
        ),
        Text(
          message,
          RoguelikeTiles.Size10x10.Fonts.fontKey,
          TerminalText(Assets.assets.AnikkiSquare10x10, RGBA.Yellow, RGBA.Red)
            .withShaderId(CustomShader.shader.id)
        ).moveBy(0, 40),
        Text(
          message,
          RoguelikeTiles.Size10x10.Fonts.fontKey,
          TerminalText(
            Assets.assets.AnikkiSquare10x10,
            RGBA.White,
            RGBA.Zero,
            RGBA.Magenta.withAlpha(0.75)
          )
        ).moveBy(0, 80)
      )
    )

object CustomShader:

  val shader: UltravioletShader =
    UltravioletShader.entityFragment(
      ShaderId("custom terminal text shader"),
      EntityShader.fragment[CustomShader.Env](
        CustomShader.frag,
        CustomShader.Env.ref
      )
    )

  final case class Env(
      FOREGROUND: vec3,
      BACKGROUND: vec4,
      MASK: vec4
  ) extends FragmentEnvReference

  object Env:
    val ref =
      Env(vec3(0.0f), vec4(0.0f), vec4(0.0f))

  final case class RogueLikeTextData(
      FOREGROUND: vec3,
      BACKGROUND: vec4,
      MASK: vec4
  )

  @nowarn("msg=unused")
  inline def frag: Shader[Env, Unit] =
    Shader[Env] { env =>
      ubo[RogueLikeTextData]

      def fragment(color: vec4): vec4 =
        val maskDiff: Boolean = abs(env.CHANNEL_0.x - env.MASK.x) < 0.001f &&
          abs(env.CHANNEL_0.y - env.MASK.y) < 0.001f &&
          abs(env.CHANNEL_0.z - env.MASK.z) < 0.001f &&
          abs(env.CHANNEL_0.w - env.MASK.w) < 0.001f

        val c: vec4 =
          if (maskDiff) {
            env.BACKGROUND
          } else {
            vec4(env.CHANNEL_0.rgb * (env.FOREGROUND.rgb * env.CHANNEL_0.a), env.CHANNEL_0.a)
          }

        c * (1.0f - (vec4(env.SCREEN_COORDS.x) / 250.0f)) // Example custom mod
    }
