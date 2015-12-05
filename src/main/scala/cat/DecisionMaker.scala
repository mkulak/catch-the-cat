package cat

import collection.mutable.HashMap


class DecisionMaker(val data:Data) {
  def calcNextCellIndex():Int = {
    var hasSomethingToLook = true
    var stepsToFinish = new HashMap[MyCell, MyCell]
    var thisStepAssigned = new HashMap[MyCell, MyCell]
    do {
      stepsToFinish ++= thisStepAssigned
      thisStepAssigned = new HashMap[MyCell, MyCell]
      for (cell:MyCell <- data.cells) {
        if (!stepsToFinish.contains(cell) && !cell.walked) {
          if (cell.isFinishing) {
            thisStepAssigned += (cell -> null)
          } else {
            for (neighbor:MyCell <- cell.neighbors) {
              if (stepsToFinish.contains(neighbor)) {
                thisStepAssigned += (cell -> neighbor)
              }
            }
          }
        }
      }
    } while (thisStepAssigned.size > 0)
    if (stepsToFinish.contains(data.catCell)) {
      return if (stepsToFinish(data.catCell) == null) -1 else data.getCellIndex(stepsToFinish(data.catCell).cellCoords)
    } else {
      for (neighbor:MyCell <- data.catCell.neighbors) {
        if (!neighbor.walked) {
          return data.getCellIndex(neighbor.cellCoords)
        }
      }
      return -2
    }
  }
  def calcNextCellIndex2():Int = {
    val emptyCells = data.cells.filterNot(_.walked).toArray
    val next = emptyCells((Math.random() * emptyCells.length).toInt)
    data.getCellIndex(next.cellCoords)
  }

  def calcNextCellIndex3():Int = {
    for (neighbor: MyCell <- data.catCell.neighbors) {
      if (!neighbor.walked) {
        return data.getCellIndex(neighbor.cellCoords)
      }
    }
    -1
  }
}