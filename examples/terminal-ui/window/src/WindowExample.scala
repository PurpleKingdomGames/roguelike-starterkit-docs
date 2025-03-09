package indigoexamples

import indigo.*
import indigoextras.ui.*
import indigoextras.ui.syntax.*
import roguelikestarterkit.*
import roguelikestarterkit.syntax.*
import roguelikestarterkit.ui.*
import generated.*

import scala.scalajs.js.annotation.*

final case class BootData()
object BootData:
  val empty: BootData =
    BootData()

final case class StartUpData()
object StartUpData:
  val empty: StartUpData =
    StartUpData()

final case class Model(num: Int)
object Model:
  val initial: Model =
    Model(42)

final case class ViewModel()
object ViewModel:
  val initial: ViewModel =
    ViewModel()

final case class Log(message: String) extends GlobalEvent

object ColourWindow:

  val charSheet: CharSheet =
    CharSheet(
      Assets.assets.AnikkiSquare10x10,
      Size(10),
      RoguelikeTiles.Size10x10.charCrops,
      RoguelikeTiles.Size10x10.Fonts.fontKey
    )

  final case class ColorPaletteReference(name: String, count: Int, colors: Batch[RGBA])

  val outrunner16 = ColorPaletteReference(
    "outrunner-16",
    16,
    Batch(
      RGBA.fromHexString("4d004c"),
      RGBA.fromHexString("8f0076"),
      RGBA.fromHexString("c70083"),
      RGBA.fromHexString("f50078"),
      RGBA.fromHexString("ff4764"),
      RGBA.fromHexString("ff9393"),
      RGBA.fromHexString("ffd5cc"),
      RGBA.fromHexString("fff3f0"),
      RGBA.fromHexString("000221"),
      RGBA.fromHexString("000769"),
      RGBA.fromHexString("00228f"),
      RGBA.fromHexString("0050c7"),
      RGBA.fromHexString("008bf5"),
      RGBA.fromHexString("00bbff"),
      RGBA.fromHexString("47edff"),
      RGBA.fromHexString("93fff8")
    )
  )

  val windowId: WindowId = WindowId("Color palette")

  def window(
      charSheet: CharSheet
  ): Window[ComponentGroup[Unit], Unit] =
    TerminalWindow(
      windowId,
      charSheet,
      TerminalWindowChrome[Unit](
        windowId,
        charSheet
      )
        .withTitle("Colour palette")
        .build(content(charSheet))
    )
      .moveTo(5, 5)
      .resizeTo(20, 20)

  def content(charSheet: CharSheet): ComponentGroup[Unit] =
    ComponentGroup()
      .withLayout(ComponentLayout.Vertical(Padding.zero.withBottom(1)))
      .add(
        ComponentGroup()
          .withLayout(ComponentLayout.Horizontal(Overflow.Wrap))
          .add(
            outrunner16.colors.map { rgba =>
              Button(Bounds(0, 0, 3, 3))(presentSwatch(charSheet, rgba, None))
                .onClick(Log(s"Clicked on ${rgba.toHexString}"))
                .presentOver(presentSwatch(charSheet, rgba, Option(RGBA.White)))
                .presentDown(presentSwatch(charSheet, rgba, Option(RGBA.Black)))
            }
          )
      )
      .add(
        TerminalButton(
          "Load palette",
          TerminalButton.Theme(
            charSheet,
            RGBA.Silver -> RGBA.Black,
            RGBA.White  -> RGBA.Black,
            RGBA.Black  -> RGBA.White,
            hasBorder = true
          )
        )
      )
      .withBackground { bounds =>
        Layer(
          Shape.Box(
            bounds.toScreenSpace(charSheet.size),
            Fill.Color(RGBA.Cyan.withAlpha(0.5))
          )
        )
      }

  def presentSwatch(
      charSheet: CharSheet,
      colour: RGBA,
      stroke: Option[RGBA]
  ): (UIContext[Unit], Button[Unit]) => Outcome[Layer] =
    (ctx, btn) =>
      Outcome(
        Layer(
          stroke match
            case None =>
              Shape.Box(
                Rectangle(
                  ctx.parent.coords.toScreenSpace(charSheet.size),
                  btn.bounds(ctx).dimensions.toScreenSpace(charSheet.size)
                ),
                Fill.Color(colour)
              )

            case Some(strokeColor) =>
              Shape.Box(
                Rectangle(
                  ctx.parent.coords.toScreenSpace(charSheet.size),
                  btn.bounds(ctx).dimensions.toScreenSpace(charSheet.size)
                ),
                Fill.Color(colour),
                Stroke(2, strokeColor)
              )
        )
      )

@JSExportTopLevel("IndigoGame")
object WindowExample extends IndigoDemo[BootData, StartUpData, Model, ViewModel]:

  val gameMagnification: Int = 2

  def eventFilters: EventFilters =
    EventFilters.Permissive

  def boot(flags: Map[String, String]): Outcome[BootResult[BootData, Model]] =
    Outcome(
      BootResult(
        Config.config.noResize.withMagnification(gameMagnification),
        BootData.empty
      )
        .withAssets(Assets.assets.assetSet)
        .withShaders(indigoextras.ui.shaders.all ++ roguelikestarterkit.shaders.all)
        .withSubSystems(
          WindowManager[Model, Unit](
            SubSystemId("window manager"),
            gameMagnification,
            Size(ColourWindow.charSheet.charSize),
            _ => ()
          )
            .withLayerKey(LayerKey("windows"))
            .register(
              ColourWindow.window(
                ColourWindow.charSheet
              )
            )
            .open(ColourWindow.windowId)
            .focus(ColourWindow.windowId)
        )
    )

  def setup(
      bootData: BootData,
      assetCollection: AssetCollection,
      dice: Dice
  ): Outcome[Startup[StartUpData]] =
    Outcome(Startup.Success(StartUpData.empty))

  def initialModel(startupData: StartUpData): Outcome[Model] =
    Outcome(Model.initial)

  def initialViewModel(startupData: StartUpData, model: Model): Outcome[ViewModel] =
    Outcome(ViewModel.initial)

  def updateModel(context: Context[StartUpData], model: Model): GlobalEvent => Outcome[Model] =
    case Log(message) =>
      println(message)
      Outcome(model)

    case _ =>
      Outcome(model)

  def updateViewModel(
      context: Context[StartUpData],
      model: Model,
      viewModel: ViewModel
  ): GlobalEvent => Outcome[ViewModel] =
    case _ =>
      Outcome(viewModel)

  def present(
      context: Context[StartUpData],
      model: Model,
      viewModel: ViewModel
  ): Outcome[SceneUpdateFragment] =
    Outcome(
      SceneUpdateFragment(
        LayerKey("windows") -> Layer.Stack.empty
      )
    )
