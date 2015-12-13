package cat

import org.scalajs.dom



class GameView(val ctx:dom.CanvasRenderingContext2D, val width:Int, val height:Int) {
  val CELL_RADIUS = 21

  val R = 24
  val H = (Math.sqrt(3) * R / 2).toInt
  val V = 3 * R / 2
//  val PADDING = R + 5


  ctx.lineWidth = 1

  def maxHexSize:(Int, Int) = {
    val horizontals = width / (H * 2)
    val verticals = height / V - 1
    (horizontals, verticals)
  }
  
  def findHexAt(state: GameState, p:Point) = state.cells.get(screenToHex(p)).map(_.hexCoords)

  def screenToHex(p: Point) = {
    val yh = round((p.y - V).toDouble / V)
    val xh = round((p.x - yh * H - H).toDouble / (H * 2))
    new Point(xh, yh)
  }

  def hexToScreen(p: Point) = new Point(
    p.x * H * 2 + p.y * H + H,
    p.y * V + V
  )

  def round(d:Double):Int = math.round(d).toInt

  def render(state: GameState) {
    fillRect(0, 0, width, height)
    setColor(0xffffff)
    state.cells.values.foreach(cell => drawCell(cell, state.isOpen(cell.hexCoords), state.isSelected(cell.hexCoords)))
    drawCat(state.cat)
  }

  def drawCell(cell: Cell, open:Boolean, selected:Boolean) {
    val p = hexToScreen(cell.hexCoords)
    val x = new Array[Int](6)
    val y = new Array[Int](6)
    for (i <- 0 until 6) {
      val cp = new Point(0, CELL_RADIUS).rotate(Math.PI * i / 3)
      x(i) = p.x + cp.x
      y(i) = p.y + cp.y
    }
    if (!open) {
      setColor(0xbbbbbb)
      drawHex(x, y, fill=true)
    } else if (selected) {
      setColor(0xA1D490)
      drawHex(x, y, fill=true)
    }
    setColor(0x555555)
    drawHex(x, y, fill=false)
  }

  def drawCat(cat: Point) {
    val p: Point = hexToScreen(cat)

    val width = (2.0 * CELL_RADIUS * Math.cos(Math.PI / 6)).toInt
    val height = 2 * CELL_RADIUS
    val catWidth = 10
    val catHeight = 6
    val size = (width / catWidth) min (height / catHeight)
    val mask = Array(
      Array(0, 1, 0, 0, 0, 1, 0, 0, 1, 0),
      Array(0, 1, 1, 1, 1, 1, 0, 0, 0, 1),
      Array(1, 1, 0, 1, 0, 1, 1, 0, 0, 1),
      Array(1, 1, 1, 0, 1, 1, 1, 1, 1, 0),
      Array(0, 0, 1, 1, 1, 1, 1, 1, 1, 0),
      Array(0, 1, 0, 1, 0, 0, 1, 0, 1, 0)
    )
    for (i <- mask.indices) {
      for (j <- mask(i).indices) {
        setColor(if (mask(i)(j) > 0) 0xff0000 else 0xffffff)
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

  def drawHex(xPoints: Array[Int], yPoints: Array[Int], fill:Boolean): Unit = {
    ctx.beginPath()
    ctx.moveTo(xPoints(0), yPoints(0))
    for (i <- xPoints.indices.drop(1)) {
      ctx.lineTo(xPoints(i), yPoints(i))
    }
    ctx.closePath()
    if (fill) {
      ctx.fill()
    } else {
      ctx.stroke()
    }
  }
}