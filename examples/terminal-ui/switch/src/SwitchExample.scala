package indigoexamples

import indigo.*
import indigoextras.ui.*
import indigoextras.ui.syntax.*
import roguelikestarterkit.*
import roguelikestarterkit.ui.*
import generated.Config
import generated.Assets

object CustomComponents:

  val charSheet: CharSheet =
    CharSheet(
      Assets.assets.AnikkiSquare10x10,
      Size(10),
      RoguelikeTiles.Size10x10.charCrops,
      RoguelikeTiles.Size10x10.Fonts.fontKey
    )

  val component: Switch[Unit] =
    TerminalSwitch[Unit](
      TerminalSwitch.Theme(
        charSheet,
        TerminalTile(Tile.`0`, RGBA.Green, RGBA.Black),
        TerminalTile(Tile.X, RGBA.Red, RGBA.Black)
      )
    ).switchOn
      .onSwitch((_, switch) =>
        Batch(Log(s"Switch is now ${if switch.state.isOn then "on" else "off"}"))
      )

final case class Log(message: String) extends GlobalEvent

final case class Model(button: Switch[Unit])
object Model:

  val initial: Model =
    Model(
      CustomComponents.component
    )

class SwitchExample() extends Game[Unit, Unit, Model]:

  val gameId: GameId = GameId("switch")

  val eventFilters: EventFilters = EventFilters.Permissive

  def boot(flags: Map[String, String]): Outcome[BootResult[Unit, Model]] =
    Outcome(
      BootResult
        .noData(Config.config)
        .withAssets(Assets.assets.assetSetRelative)
        .withShaders(indigoextras.ui.shaders.all ++ roguelikestarterkit.shaders.all)
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
    case Log(message) =>
      println(message)
      Outcome(model)

    case e =>
      val ctx = UIContext(context)
        .withSnapGrid(CustomComponents.charSheet.size)
        .moveParentBy(Coords(5, 5))

      model.button.update(ctx)(e).map { b =>
        model.copy(button = b)
      }

  def present(context: Context, model: Model): Outcome[SceneUpdateFragment] =
    val ctx = UIContext(context)
      .withSnapGrid(CustomComponents.charSheet.size)
      .moveParentBy(Coords(5, 5))

    model.button
      .present(ctx)
      .map(l => SceneUpdateFragment(LayerKey("demo") -> l))
