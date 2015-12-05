package example

import cat.{Config, CtxGraphics, Controller}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.html

@JSExport
object ScalaJSExample {
  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    val game:Controller = new Controller

    val g = new CtxGraphics(ctx)

    game.startGame()
    game.render(g)
    game.render(g)

    canvas.addEventListener("click", { (e: dom.MouseEvent) =>
      val r = canvas.getBoundingClientRect()
      val x: Int = e.clientX.toInt - canvas.clientLeft - r.left.toInt
      val y: Int = e.clientY.toInt - canvas.clientTop - r.top.toInt
      dom.console.log(s"$x, $y")
      game.makeTurn(x, y)
      game.render(g)
      (): js.Any
    })

  }
}
