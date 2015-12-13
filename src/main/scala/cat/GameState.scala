package cat

case class Point(x:Int, y:Int) {
  def rotate(r:Double):Point = {
    val sinAng = Math.sin(r)
    val cosAng = Math.cos(r)
    new Point((x * cosAng - y * sinAng).toInt, (x * sinAng + y * cosAng).toInt)
  }

  def distance(p:Point): Double = {
    Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y))
  }

  def +(p:Point) = new Point(x + p.x, y + p.y)
}

class Cell(val hexCoords:Point, var neighbors:Array[Point], var terminal:Boolean)

case class GameState(cells:Map[Point, Cell], closedCells: Set[Point], cat: Point, selectedCell: Option[Point]) {
  def isOpen(p:Point) = !closedCells.contains(p)
  def isSelected(p:Point) = selectedCell.isDefined && p == selectedCell.get && p != cat
}

