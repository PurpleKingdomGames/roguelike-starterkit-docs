package indigoexamples

import indigo.*
import indigoextras.ui.*
import indigoextras.ui.syntax.*
import generated.Config
import generated.Assets
import roguelikestarterkit.*
import roguelikestarterkit.syntax.*
import roguelikestarterkit.ui.*

import scala.scalajs.js.annotation.*

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

@JSExportTopLevel("IndigoGame")
object ButtonExample extends IndigoSandbox[Unit, Model]:

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

      model.button.update(ctx)(e).map { b =>
        model.copy(button = b)
      }

  def present(context: Context[Unit], model: Model): Outcome[SceneUpdateFragment] =
    val ctx = UIContext(context)
      .withSnapGrid(CustomComponents.charSheet.size)
      .moveParentBy(Coords(5, 5))
      .withPointerCoords(
        Coords(context.frame.input.pointers.position / CustomComponents.charSheet.size.toPoint)
      )
      .withMagnification(2)

    model.button
      .present(ctx)
      .map(l => SceneUpdateFragment(l))
