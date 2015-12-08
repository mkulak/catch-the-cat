package cat

import org.scalajs.dom
import org.scalajs.dom.{CanvasRenderingContext2D, html}

import scala.scalajs.js.annotation.JSExport

@JSExport
object CatGame {
  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val ctx: CanvasRenderingContext2D = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    val rect = canvas.getBoundingClientRect()
    val view = new GameView(ctx, rect.width.toInt, rect.height.toInt)
    val (h, v) = view.maxHexSize
    val logic = new GameLogic(h, v)
    var state = logic.newGame()

    view.render(state)
    view.render(state)

    canvas.addEventListener("click", (e: dom.MouseEvent) => {
      val p = convertCoords(e)
      val hex = view.findHexAt(state, p)
      val newState = logic.makeTurn(state, hex)
      view.render(newState)
      state = newState
    })

    def convertCoords(e: dom.MouseEvent):Point = {
      val x: Int = e.clientX.toInt - canvas.clientLeft - rect.left.toInt
      val y: Int = e.clientY.toInt - canvas.clientTop - rect.top.toInt
      new Point(x, y)
    }

  }
}
