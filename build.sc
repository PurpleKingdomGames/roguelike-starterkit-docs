import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $file.scripts.rlskmodule

import indigoplugin._

object examples extends mill.Module {

  object fragment extends mill.Module {

    object basics extends mill.Module {

      object minimal extends rlskmodule.RLSKModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Minimal Fragment Shader Setup")
      }

    }

  }

}
