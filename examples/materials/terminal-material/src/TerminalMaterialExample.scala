package indigoexamples

import indigo.*
import generated.Config
import generated.Assets
import roguelikestarterkit.*

final case class Model()
object Model:
  val initial: Model = Model()

class TerminalMaterialExample() extends Game[Unit, Unit, Model]:

  val gameId: GameId = GameId("terminal-material")

  val eventFilters: EventFilters = EventFilters.Permissive

  def boot(flags: Map[String, String]): Outcome[BootResult[Unit, Model]] =
    Outcome(
      BootResult
        .noData(Config.config)
        .withAssets(Assets.assets.assetSetRelative)
        .withFonts(RoguelikeTiles.Size10x10.Fonts.fontInfo)
        .withShaders(roguelikestarterkit.shaders.all)
    )

  def scenes(bootData: Unit): NonEmptyBatch[Scene[Model]] =
    NonEmptyBatch(Scene.empty)

  def initialScene(bootData: Unit): Option[SceneName] =
    None

  def setup(bootData: Unit, assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def initialModel(startupData: Unit): Outcome[Model] =
    Outcome(Model.initial)

  def updateModel(context: Context, model: Model): GlobalEvent => Outcome[Model] =
    case _ =>
      Outcome(model)

  def present(
      context: Context,
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
        LayerKey("demo") -> Layer.Content(
          Text(
            message,
            RoguelikeTiles.Size10x10.Fonts.fontKey,
            TerminalMaterial(Assets.assets.AnikkiSquare10x10, RGBA.Cyan, RGBA.Blue)
          ),
          Text(
            message,
            RoguelikeTiles.Size10x10.Fonts.fontKey,
            TerminalMaterial(
              Assets.assets.AnikkiSquare10x10,
              RGBA.White,
              RGBA.Zero,
              RGBA.Magenta.withAlpha(0.75)
            )
          ).moveBy(0, 80)
        )
      ).withMagnification(Magnification.x2)
    )
