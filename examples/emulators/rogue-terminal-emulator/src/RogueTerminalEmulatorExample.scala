package indigoexamples

/** The rogue terminal emulator is the _mutable_ cousin of the terminal emulator, and is also used
  * to render terminal-like screens. As it is mutable, it must be used with more care, it is however
  * significantly faster, and can be used without caching in most cases, simplifying your game's
  * lifecycle.
  *
  * In this example, we'll instantiate a rogue terminal emulator, and fill it with some tiles, then
  * render it on every single frame. Because we can.
  */

import indigo.*
import generated.Config
import generated.Assets
import roguelikestarterkit.*

final case class Model()
object Model:
  val initial: Model = Model()

class RogueTerminalEmulatorExample() extends Game[Unit, Unit, Model]:

  val gameId: GameId = GameId("rogue-terminal-emulator")

  val eventFilters: EventFilters = EventFilters.Permissive

  def boot(flags: Map[String, String]): Outcome[BootResult[Unit, Model]] =
    Outcome(
      BootResult
        .noData(Config.config)
        .withAssets(Assets.assets.assetSetRelative)
        .withShaders(roguelikestarterkit.shaders.all)
    )

  def scenes(bootData: Unit): NonEmptyBatch[Scene[Unit, Model]] =
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

  def present(context: Context, model: Model): Outcome[SceneUpdateFragment] =
    val terminal: RogueTerminalEmulator =
      RogueTerminalEmulator(Size(11, 11))
        .fill(MapTile(Tile.DARK_SHADE, RGBA.Yellow, RGBA.Black))
        .fillRectangle(Rectangle(1, 1, 9, 9), MapTile(Tile.MEDIUM_SHADE, RGBA.Yellow, RGBA.Black))
        .fillCircle(Circle(5, 5, 4), MapTile(Tile.LIGHT_SHADE, RGBA.Yellow, RGBA.Black))
        .mapLine(Point(0, 10), Point(10, 0)) { case (_, tile) =>
          tile.withForegroundColor(RGBA.Red)
        }
        .put(Point(5, 5), MapTile(Tile.`@`, RGBA.Cyan))

    val tiles =
      terminal.toCloneTiles(
        CloneId("demo"),
        Point.zero,
        RoguelikeTiles.Size10x10.charCrops
      ) { (fg, bg) =>
        Graphic(10, 10, TerminalMaterial(Assets.assets.AnikkiSquare10x10, fg, bg))
      }

    Outcome(tiles.toSceneUpdateFragment)
