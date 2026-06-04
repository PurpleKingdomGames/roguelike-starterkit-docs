package indigoexamples

/** The terminal emulator is an immutable representation of a terminal screen. It is as safe to use
  * as any other immutable data structure, but can be a little slow when the grid size is large.
  *
  * In this example we simulate setting up a fixed terminal and caching the output ready for
  * rendering. Note that the cached values in this example, are stored in the model, but usually
  * they would be kept in the view model (there is no view model in an IndigoSandbox game, hence the
  * contrived storage mechanism).
  */

import indigo.*
import generated.Config
import generated.Assets
import roguelikestarterkit.*

final case class Model(terminal: TerminalEmulator, cachedTiles: Option[TerminalClones])
object Model:
  val initial: Model =
    val terminal: TerminalEmulator =
      TerminalEmulator(Size(3, 3))
        .put(
          Point(0, 0) -> MapTile(Tile.`░`, RGBA.Cyan, RGBA.Blue),
          Point(1, 0) -> MapTile(Tile.`░`, RGBA.Cyan, RGBA.Blue),
          Point(2, 0) -> MapTile(Tile.`░`, RGBA.Cyan, RGBA.Blue),
          Point(0, 1) -> MapTile(Tile.`░`, RGBA.Cyan, RGBA.Blue),
          Point(1, 1) -> MapTile(Tile.`@`, RGBA.Magenta),
          Point(2, 1) -> MapTile(Tile.`░`, RGBA.Cyan, RGBA.Blue),
          Point(0, 2) -> MapTile(Tile.`░`, RGBA.Cyan, RGBA.Blue),
          Point(1, 2) -> MapTile(Tile.`░`, RGBA.Cyan, RGBA.Blue),
          Point(2, 2) -> MapTile(Tile.`░`, RGBA.Cyan, RGBA.Blue)
        )

    Model(terminal, None)

class TerminalEmulatorExample() extends Game[Unit, Unit, Model]:

  val gameId: GameId = GameId("terminal-emulator")

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
    case FrameTick if model.cachedTiles.isEmpty =>

      val cachedTiles: TerminalClones =
        model.terminal.toCloneTiles(
          CloneId("demo"),
          Point.zero,
          RoguelikeTiles.Size10x10.charCrops
        ) { (fg, bg) =>
          Graphic(10, 10, TerminalMaterial(Assets.assets.AnikkiSquare10x10, fg, bg))
        }

      Outcome(model.copy(cachedTiles = Some(cachedTiles)))

    case _ =>
      Outcome(model)

  def present(context: Context, model: Model): Outcome[SceneUpdateFragment] =
    model.cachedTiles match
      case None =>
        Outcome(SceneUpdateFragment.empty)

      case Some(tiles) =>
        Outcome(
          SceneUpdateFragment(tiles.clones)
            .addCloneBlanks(tiles.blanks)
        )
