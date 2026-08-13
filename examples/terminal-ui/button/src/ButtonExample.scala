package indigoexamples

import indigo.*
import indigoextras.ui.*
import indigoextras.ui.syntax.*
import generated.Config
import generated.Assets
import roguelikestarterkit.*
import roguelikestarterkit.ui.*

object CustomComponents:

  val charSheet: CharSheet =
    CharSheet(
      Assets.assets.AnikkiSquare10x10,
      Size(10),
      RoguelikeTiles.Size10x10.charCrops,
      RoguelikeTiles.Size10x10.Fonts.fontKey
    )

  val customButton: Button[Unit] =
    TerminalButton(
      "Click me!",
      TerminalButton.Theme(
        charSheet,
        RGBA.Silver -> RGBA.Black,
        RGBA.White  -> RGBA.Black,
        RGBA.Black  -> RGBA.White,
        hasBorder = true
      )
    )
      .onClick(Log("Button clicked"))
      .onPress(Log("Button pressed"))
      .onRelease(Log("Button released"))

final case class Log(message: String) extends GlobalEvent

final case class Model(button: Button[Unit])
object Model:

  val initial: Model =
    Model(
      CustomComponents.customButton
    )

class ButtonExample() extends Game[Unit, Unit, Model]:

  val gameId: GameId = GameId("button")

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
