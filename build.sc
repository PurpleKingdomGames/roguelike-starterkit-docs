import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $file.scripts.rlskmodule

import indigoplugin._

object examples extends mill.Module {

  object emulators extends mill.Module {

    object `terminal-emulator` extends rlskmodule.RLSKModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Terminal Emulator Example")
    }

    object `rogue-terminal-emulator` extends rlskmodule.RLSKModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Rogue Terminal Emulator Example")
    }

  }

  object materials extends mill.Module {

    object `terminal-text` extends rlskmodule.RLSKModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Terminal Text Example")
    }

    object `terminal-material` extends rlskmodule.RLSKModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Terminal Material Example")
    }

  }

  object `terminal-ui` extends mill.Module {

    object button extends rlskmodule.RLSKModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("UI Components - Button")
    }

    object label extends rlskmodule.RLSKModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("UI Components - Label")
    }

    object input extends rlskmodule.RLSKModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("UI Components - Input")
    }

    object scrollpane extends rlskmodule.RLSKModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("UI Components - ScrollPane")
    }

    object switch extends rlskmodule.RLSKModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("UI Components - Switch")
    }

    object textarea extends rlskmodule.RLSKModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("UI Components - TextArea")
    }

    object window extends rlskmodule.RLSKModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("UI Components - Window")
    }

  }

}
