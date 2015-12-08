package cat


import java.util
import java.util.Collections

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random

class GameLogic(horizontals:Int, verticals:Int) {
  val rand = new Random()

  def makeTurn(state:GameState, po:Option[Point]):GameState = {
    val valid: Boolean = po.exists(p => state.isOpen(p) && p != state.cat)
    if (valid) {
      val newState = state.copy(closedCells = state.closedCells + po.get)
      moveCat(newState)
    } else state
  }

  def moveCat(state:GameState):GameState = {
    if (state.cells(state.cat).terminal) {
      newGame()
    } else {
      findNextCatPos(state).map(p => state.copy(cat = p)).getOrElse(newGame())
    }
  }

  def findNextCatPos(state:GameState):Option[Point] = {
    val queue = mutable.Queue.empty[(Point, Point)]
    val seen = mutable.Set.empty[Point]
    queue ++= rand.shuffle(state.cells(state.cat).neighbors.filter(state.isOpen).map(p => (p, p)))
    while(queue.nonEmpty) {
      val (cur, start) = queue.dequeue()
      if (!seen(cur)) {
        seen += cur
        if (state.cells(cur).terminal) {
          return Some(start)
        }
        queue ++= state.cells(cur).neighbors.filter(state.isOpen).map(p => (p, start))
      }
    }
    None
  }

  def newGame():GameState = {
    val cells = mutable.Map.empty[Point, Cell]
    for (y <- -horizontals / 2 to horizontals / 2) {
      for (x <-
           -verticals / 2 - y / 2 + (if (y < 0 && -y % 2 == 1) 1 else 0)
             to
             verticals / 2 - y / 2 - (if (y % 2 == 0) 0 else 1) + (if (y < 0 && -y % 2 == 1) 1 else 0)
      ) {
        val point: Point = new Point(x, y)
        cells += (point -> new Cell(point, null, false))
      }
    }
    val neighbors = Array((1, 0), (-1, 0), (0, 1), (0, -1), (1, -1), (-1, 1)).map(p => new Point(p._1, p._2))
    cells.values.foreach(c => {
      c.neighbors = neighbors.map(n => c.hexCoords + n).filter(cells.contains)
      c.terminal = c.neighbors.length < 6
    })
    val closed = rand.shuffle(cells.keys).toArray.take(rand.nextInt(6) + 4).toSet
    new GameState(cells.toMap, closed, new Point(0, 0))
  }
}