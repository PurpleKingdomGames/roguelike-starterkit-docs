package indigoexamples

import indigo.*
import indigoextras.ui.*
import indigoextras.ui.syntax.*
import roguelikestarterkit.*
import roguelikestarterkit.ui.*
import generated.*

object CustomComponents:

  val charSheet: CharSheet =
    CharSheet(
      Assets.assets.AnikkiSquare10x10,
      Size(10),
      RoguelikeTiles.Size10x10.charCrops,
      RoguelikeTiles.Size10x10.Fonts.fontKey
    )

  val component: TextArea[Int] =
    TerminalTextArea[Int](
      ctx => "abc.\nde,f\n0123456! " + ctx.reference,
      TerminalTextArea.Theme(charSheet)
    )

final case class Model(count: Int, component: TextArea[Int])
object Model:

  val initial: Model =
    Model(42, CustomComponents.component)

class TextAreaExample() extends Game[Unit, Unit, Model]:

  val gameId: GameId = GameId("textarea")

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
    case e =>
      val ctx = UIContext(context)
        .withSnapGrid(CustomComponents.charSheet.size)
        .moveParentBy(Coords(5, 5))
        .copy(reference = model.count)

      model.component.update(ctx)(e).map { c =>
        model.copy(component = c)
      }

  def present(context: Context, model: Model): Outcome[SceneUpdateFragment] =
    val ctx = UIContext(context)
      .withSnapGrid(CustomComponents.charSheet.size)
      .moveParentBy(Coords(5, 5))
      .copy(reference = model.count)

    model.component
      .present(ctx)
      .map(l => SceneUpdateFragment(LayerKey("demo") -> l))
