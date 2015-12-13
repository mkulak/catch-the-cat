package cat


import scala.collection.mutable
import scala.util.Random

class GameLogic(horizontals:Int, verticals:Int) {
  val CLOSED_MIN = 4
  val CLOSED_MAX = 10
  val rand = new Random()
  val neighbors = Array((1, 0), (-1, 0), (0, 1), (0, -1), (1, -1), (-1, 1)).map(p => new Point(p._1, p._2))

  def makeTurn(state:GameState, po:Option[Point]):GameState = {
    val valid = po.exists(p => state.isOpen(p) && p != state.cat)
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
    val cells = (0 until verticals).flatMap(y => {
      (0 until horizontals - (y % 2)).map(x => {
        val p = new Point(x - y / 2, y)
        (p, new Cell(p, null, false))
      })
    }).toMap
    cells.values.foreach(c => {
      c.neighbors = neighbors.map(n => c.hexCoords + n).filter(cells.contains)
      c.terminal = c.neighbors.length < 6
    })
    val closed = rand.shuffle(cells.keys).toArray.take(rand.nextInt(CLOSED_MAX - CLOSED_MIN) + CLOSED_MIN).toSet
    new GameState(cells, closed, new Point(horizontals / 2 - verticals / 4, verticals / 2), None)
  }
}