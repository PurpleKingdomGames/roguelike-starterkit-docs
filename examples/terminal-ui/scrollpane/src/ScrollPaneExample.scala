package indigoexamples

import indigo.*
import indigo.syntax.*
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

  val scrollPaneBounds =
    Bounds(0, 0, 20, 10)

  val listOfLabels: ComponentList[Int] =
    ComponentList(Dimensions(20, 20)) { (ctx: UIContext[Int]) =>
      (1 to ctx.reference).toBatch.map { i =>
        ComponentId("lbl" + i) ->
          TerminalLabel[Int](
            "Custom label " + i,
            TerminalLabel.Theme(charSheet, RGBA.Black, RGBA.Yellow)
          )
      }
    }
      .withLayout(ComponentLayout.Vertical(Padding(1)))

  val pane: ScrollPane[ComponentList[Int], Int] =
    TerminalScrollPane(
      BindingKey("custom-scroll-pane"),
      BoundsMode.fixed(scrollPaneBounds.dimensions),
      listOfLabels,
      charSheet
    )

final case class Model(count: Int, component: ScrollPane[ComponentList[Int], Int])
object Model:

  val initial: Model =
    Model(
      4,
      CustomComponents.pane
    )

class ScrollPaneExample() extends Game[Unit, Unit, Model]:

  val gameId: GameId = GameId("scrollpane")

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
      val ctx =
        UIContext(context)
          .withSnapGrid(CustomComponents.charSheet.size)
          .moveParentBy(Coords(5, 5))
          .copy(reference = model.count)

      model.component.update(ctx)(e).map { c =>
        model.copy(component = c)
      }

  def present(context: Context, model: Model): Outcome[SceneUpdateFragment] =
    val ctx =
      UIContext(context)
        .withSnapGrid(CustomComponents.charSheet.size)
        .moveParentBy(Coords(5, 5))
        .copy(reference = model.count)

    val scrollPaneBorder =
      Shape.Box(
        CustomComponents.scrollPaneBounds
          .toScreenSpace(CustomComponents.charSheet.size, Magnification.x1)
          .moveTo(
            ctx.parent.coords.toScreenSpace(CustomComponents.charSheet.size, Magnification.x1)
          ),
        Fill.None,
        Stroke(1, RGBA.Cyan)
      )

    model.component
      .present(ctx)
      .map { c =>
        SceneUpdateFragment(
          LayerKey("demo") -> Layer.Stack(
            c,
            Layer.Content(scrollPaneBorder)
          )
        )
      }
