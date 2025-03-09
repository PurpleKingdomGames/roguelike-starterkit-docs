package indigoexamples

import indigo.*
import indigo.syntax.*
import indigoextras.ui.*
import indigoextras.ui.syntax.*
import roguelikestarterkit.*
import roguelikestarterkit.syntax.*
import roguelikestarterkit.ui.*
import generated.*

import scala.scalajs.js.annotation.*

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

@JSExportTopLevel("IndigoGame")
object ScrollPaneExample extends IndigoSandbox[Unit, Model]:

  val config: GameConfig =
    Config.config.noResize.withMagnification(2)

  val assets: Set[AssetType] =
    Assets.assets.assetSet

  val fonts: Set[FontInfo]       = Set()
  val animations: Set[Animation] = Set()

  val shaders: Set[ShaderProgram] =
    Set() ++ indigoextras.ui.shaders.all ++ roguelikestarterkit.shaders.all

  def setup(assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def initialModel(startupData: Unit): Outcome[Model] =
    Outcome(Model.initial)

  def updateModel(context: Context[Unit], model: Model): GlobalEvent => Outcome[Model] =
    case e =>
      val ctx =
        UIContext(context)
          .withSnapGrid(CustomComponents.charSheet.size)
          .moveParentBy(Coords(5, 5))
          .withPointerCoords(
            Coords(context.frame.input.pointers.position / CustomComponents.charSheet.size.toPoint)
          )
          .withMagnification(2)
          .copy(reference = model.count)

      model.component.update(ctx)(e).map { c =>
        model.copy(component = c)
      }

  def present(context: Context[Unit], model: Model): Outcome[SceneUpdateFragment] =
    val ctx =
      UIContext(context)
        .withSnapGrid(CustomComponents.charSheet.size)
        .moveParentBy(Coords(5, 5))
        .withPointerCoords(
          Coords(context.frame.input.pointers.position / CustomComponents.charSheet.size.toPoint)
        )
        .withMagnification(2)
        .copy(reference = model.count)

    val scrollPaneBorder =
      Shape.Box(
        CustomComponents.scrollPaneBounds
          .toScreenSpace(CustomComponents.charSheet.size)
          .moveTo(ctx.parent.coords.toScreenSpace(CustomComponents.charSheet.size)),
        Fill.None,
        Stroke(1, RGBA.Cyan)
      )

    model.component
      .present(ctx)
      .map(c => SceneUpdateFragment(c).addLayer(scrollPaneBorder))
