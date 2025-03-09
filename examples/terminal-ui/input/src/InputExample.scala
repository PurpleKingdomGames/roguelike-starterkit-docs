package indigoexamples

import indigo.*
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

  val component: Input[Unit] =
    TerminalInput[Unit]("Hello, world!", 20, TerminalInput.Theme(charSheet))

final case class Model(component: Input[Unit])
object Model:

  val initial: Model =
    Model(
      CustomComponents.component
    )

final case class Log(message: String) extends GlobalEvent

@JSExportTopLevel("IndigoGame")
object InputExample extends IndigoSandbox[Unit, Model]:

  val config: GameConfig =
    Config.config.noResize
      .withMagnification(2)

  val assets: Set[AssetType] =
    Assets.assets.assetSet

  val fonts: Set[FontInfo]        = Set()
  val animations: Set[Animation]  = Set()
  val shaders: Set[ShaderProgram] = roguelikestarterkit.shaders.all

  def setup(assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def initialModel(startupData: Unit): Outcome[Model] =
    Outcome(Model.initial)

  def updateModel(context: Context[Unit], model: Model): GlobalEvent => Outcome[Model] =
    case Log(message) =>
      println(message)
      Outcome(model)

    case e =>
      val ctx = UIContext(context)
        .withSnapGrid(CustomComponents.charSheet.size)
        .moveParentBy(Coords(5, 5))
        .withPointerCoords(
          Coords(context.frame.input.pointers.position / CustomComponents.charSheet.size.toPoint)
        )
        .withMagnification(2)

      model.component.update(ctx)(e).map { c =>
        model.copy(component = c)
      }

  def present(context: Context[Unit], model: Model): Outcome[SceneUpdateFragment] =
    val ctx = UIContext(context)
      .withSnapGrid(CustomComponents.charSheet.size)
      .moveParentBy(Coords(5, 5))
      .withPointerCoords(
        Coords(context.frame.input.pointers.position / CustomComponents.charSheet.size.toPoint)
      )
      .withMagnification(2)

    model.component
      .present(ctx)
      .map(l => SceneUpdateFragment(l))
