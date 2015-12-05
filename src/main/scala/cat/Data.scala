package cat

import scala.collection.mutable.{HashMap, Map}

object Config {
  // size of app
  val WIDTH = 400
  val HEIGHT = 400
  // radius of cell
  val CELL_RADIUS = 21
  val CELL_DISTANCE = 2
  //
  def CENTERS_DISTANCE = 2 * CELL_RADIUS  + CELL_DISTANCE
}

class MyCell(var cellCoords:Point, var neighbors:List[MyCell], var isFinishing:Boolean, var walked:Boolean = false)

class Data(cls: List[MyCell], cci: Int) {
  val cells: List[MyCell] = cls
  var catCellIndex: Int = cci

  private val cellsMap: Map[Point, Int] = new HashMap[Point, Int]
  for (i <- cells.indices) {
    cellsMap += (cells(i).cellCoords -> i)
  }

  def catCell: MyCell = cells(catCellIndex)

  def hasCell(p: Point) = cellsMap.contains(p)

  def getCellIndex(p: Point): Int = cellsMap(p)

  def getCell(p: Point): MyCell = cells(cellsMap(p))
}

case class Point(x:Int, y:Int) {
  def rotate(r:Double):Point = {
    val sinAng = Math.sin(r)
    val cosAng = Math.cos(r)
    new Point((x * cosAng - y * sinAng).toInt, (x * sinAng + y * cosAng).toInt)
  }

  def distance(p:Point): Double = {
    Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y))
  }
}