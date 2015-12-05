package cat

import org.scalajs.dom


trait Graphics {
  def setColor(color:Int)
  def fillPolygon(xPoints: Array[Int], yPoints: Array[Int], nPoints: Int)
  def drawPolygon(xPoints: Array[Int], yPoints: Array[Int], nPoints: Int)
  def fillRect(x:Int, y:Int, width:Int, height:Int)
}

class CtxGraphics(val ctx:dom.CanvasRenderingContext2D) extends Graphics {
  override def setColor(color: Int): Unit = {
    ctx.fillStyle = "#" + color.toHexString
  }

  override def fillRect(x: Int, y: Int, width: Int, height: Int): Unit = {
    ctx.fillRect(x, y, width, height)
  }

  override def drawPolygon(xPoints: Array[Int], yPoints: Array[Int], nPoints: Int): Unit = {
    ctx.beginPath()
    ctx.moveTo(xPoints(0), yPoints(0))
    for (i <- xPoints.indices.drop(1)) {
      ctx.lineTo(xPoints(i), yPoints(i))
    }
    ctx.closePath()
    ctx.lineWidth = 1
    ctx.stroke()
  }
  override def fillPolygon(xPoints: Array[Int], yPoints: Array[Int], nPoints: Int): Unit = {
    ctx.beginPath()
    ctx.moveTo(xPoints(0), yPoints(0))
    for (i <- xPoints.indices.drop(1)) {
      ctx.lineTo(xPoints(i), yPoints(i))
    }
    ctx.closePath()
    ctx.fill()
  }
}


class MainCanvas(c: UserActionsListener, val width:Int, val height:Int) {
  private var data: Data = null

  private def cellToView(cell: Point): Point = new Point(
    getWidth / 2 + (cell.x.toDouble * Config.CENTERS_DISTANCE.toDouble + cell.y * Config.CENTERS_DISTANCE * Math.cos(Math.PI / 3)).toInt,
    getHeight / 2 - (cell.y.toDouble * Config.CENTERS_DISTANCE.toDouble * Math.sin(Math.PI / 3)).toInt
  )

  private def viewToCellIndex(view: Point): Int = {
    // TODO try functionally
    for (i: Int <- data.cells.indices) {
      var cell = data.cells(i)
      if (cellToView(cell.cellCoords).distance(view) <= Config.CELL_RADIUS) {
        return i
      }
    }
    -1
  }

  def update(d: Data) = {
    data = d
  }

  private def drawCell(g: Graphics, cell: MyCell) = {
    val p = cellToView(cell.cellCoords)
    val x = new Array[Int](6)
    val y = new Array[Int](6)
    for (i <- 0 until 6) {
      val cp = new Point(0, Config.CELL_RADIUS).rotate(Math.PI * i / 3)
      x(i) = p.x + cp.x
      y(i) = p.y + cp.y
    }
    if (cell.walked) {
      g.setColor(0xbbbbbb)
      g.fillPolygon(x, y, 6)
    }
    g.setColor(0x555555)
    g.drawPolygon(x, y, 6)
  }



  private def drawCat(g: Graphics, cell: MyCell) = {
    val p: Point = cellToView(cell.cellCoords)

    val width = (2.0 * Config.CELL_RADIUS * Math.cos(Math.PI / 6)).toInt
    val height = 2 * Config.CELL_RADIUS
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
        g.setColor(if (mask(i)(j) > 0) 0xff0000 else 0xffffff)
        g.fillRect(p.x + size * j - catWidth * size / 2, p.y + size * i - catHeight * size / 2, size, size)
      }
    }
  }
  
  def draw(g: Graphics ) = {
    g.fillRect(0, 0, width, height)
    g.setColor(0xffffff)
    data.cells.foreach(cell => drawCell(g, cell))
    drawCat(g, data.catCell)
  }


  def onClick(p:Point) = {
    val cellIndex: Int = viewToCellIndex(p)
    if (cellIndex > 0) {
      c.cellClicked(cellIndex)
    }
  }

  def getWidth = width
  def getHeight = height
}