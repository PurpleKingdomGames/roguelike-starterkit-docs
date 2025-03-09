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

  val customLabel: Label[Int] =
    TerminalLabel(
      ctx => "Count: " + ctx.reference,
      TerminalLabel.Theme(charSheet, RGBA.Magenta, RGBA.Cyan)
    )

final case class Model(count: Int, label: Label[Int])
object Model:

  val initial: Model =
    Model(
      42,
      CustomComponents.customLabel
    )

@JSExportTopLevel("IndigoGame")
object LabelExample extends IndigoSandbox[Unit, Model]:

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
    case e =>
      val ctx = UIContext(context)
        .withSnapGrid(CustomComponents.charSheet.size)
        .moveParentBy(Coords(5, 5))
        .withPointerCoords(
          Coords(context.frame.input.pointers.position / CustomComponents.charSheet.size.toPoint)
        )
        .withMagnification(2)
        .copy(reference = model.count)

      model.label.update(ctx)(e).map { l =>
        model.copy(label = l)
      }

  def present(context: Context[Unit], model: Model): Outcome[SceneUpdateFragment] =
    val ctx = UIContext(context)
      .withSnapGrid(CustomComponents.charSheet.size)
      .moveParentBy(Coords(5, 5))
      .withPointerCoords(
        Coords(context.frame.input.pointers.position / CustomComponents.charSheet.size.toPoint)
      )
      .withMagnification(2)
      .copy(reference = model.count)

    model.label
      .present(ctx)
      .map(l => SceneUpdateFragment(l))
