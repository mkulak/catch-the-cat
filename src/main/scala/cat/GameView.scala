package cat

import org.scalajs.dom


class GameView(val ctx:dom.CanvasRenderingContext2D, val width:Int, val height:Int) {
  val BG_COLOR = 0xffffff
  val CLOSED_CELL_COLOR = 0xbbbbbb
  val SELECTED_CELL_COLOR = 0xA1D490
  val CELL_BORDER_COLOR = 0x555555
  val CAT_COLOR = 0xff0000

  val VISIBLE_CELL_RADIUS = 21
  val REAL_CELL_RADIUS = 24
  val H = (Math.sqrt(3) * REAL_CELL_RADIUS / 2).toInt
  val V = 3 * REAL_CELL_RADIUS / 2
  val PADDING_TOP = V
  val PADDING_LEFT = H

  val hexPoints = (0 until 6).map(i => new Point(0, VISIBLE_CELL_RADIUS).rotate(Math.PI * i / 3))
  val catMask = Array(
    Array(0, 1, 0, 0, 0, 1, 0, 0, 1, 0),
    Array(0, 1, 1, 1, 1, 1, 0, 0, 0, 1),
    Array(1, 1, 0, 1, 0, 1, 1, 0, 0, 1),
    Array(1, 1, 1, 0, 1, 1, 1, 1, 1, 0),
    Array(0, 0, 1, 1, 1, 1, 1, 1, 1, 0),
    Array(0, 1, 0, 1, 0, 0, 1, 0, 1, 0)
  )

  ctx.lineWidth = 1

  def maxHexSize:(Int, Int) = {
    val horizontals = odd(width / (H * 2))
    val verticals = odd(height / V - 1)
    (horizontals, verticals)
  }

  def screenToHex(p: Point) = {
    val yh = round((p.y - PADDING_TOP).toDouble / V)
    val xh = round((p.x - yh * H - PADDING_LEFT).toDouble / (H * 2))
    new Point(xh, yh)
  }

  def hexToScreen(p: Point) = new Point(
    p.x * H * 2 + p.y * H + PADDING_LEFT,
    p.y * V + PADDING_TOP
  )

  def round(d:Double) = math.round(d).toInt
  def odd(v:Int) = if (v % 2 == 0) v - 1 else v

  def render(state: GameState) {
    setColor(BG_COLOR)
    fillRect(0, 0, width, height)
    state.cells.values.foreach(cell => drawCell(cell, state.isOpen(cell.hexCoords), state.isSelected(cell.hexCoords)))
    drawCat(state.cat)
  }

  def drawCell(cell: Cell, open:Boolean, selected:Boolean) {
    val p = hexToScreen(cell.hexCoords)
    if (!open) {
      setColor(CLOSED_CELL_COLOR)
      drawHex(p, fill=true)
    } else if (selected) {
      setColor(SELECTED_CELL_COLOR)
      drawHex(p, fill=true)
    }
    setColor(CELL_BORDER_COLOR)
    drawHex(p, fill=false)
  }

  def drawCat(cat: Point) {
    val p: Point = hexToScreen(cat)
    val width = (2.0 * VISIBLE_CELL_RADIUS * Math.cos(Math.PI / 6)).toInt
    val height = 2 * VISIBLE_CELL_RADIUS
    val catWidth = 10
    val catHeight = 6
    val size = (width / catWidth) min (height / catHeight)
    for (i <- catMask.indices) {
      for (j <- catMask(i).indices) {
        setColor(if (catMask(i)(j) > 0) CAT_COLOR else BG_COLOR)
        fillRect(p.x + size * j - catWidth * size / 2, p.y + size * i - catHeight * size / 2, size, size)
      }
    }
  }

  def setColor(color: Int): Unit = {
    ctx.fillStyle = "#" + color.toHexString
  }

  def fillRect(x: Int, y: Int, width: Int, height: Int): Unit = {
    ctx.fillRect(x, y, width, height)
  }

  def drawHex(p:Point, fill:Boolean): Unit = {
    ctx.beginPath()
    ctx.moveTo(p.x + hexPoints(0).x, p.y + hexPoints(0).y)
    hexPoints.drop(1).foreach(h => ctx.lineTo(p.x + h.x, p.y + h.y))
    ctx.closePath()
    if (fill) {
      ctx.fill()
    } else {
      ctx.stroke()
    }
  }
}