package cat



class DecisionMaker(val data:GameState) {
//  def calcNextCellIndex():Int = {
//    var hasSomethingToLook = true
//    var stepsToFinish = new HashMap[Cell, Cell]
//    var thisStepAssigned = new HashMap[Cell, Cell]
//    do {
//      stepsToFinish ++= thisStepAssigned
//      thisStepAssigned = new HashMap[Cell, Cell]
//      for (cell:Cell <- data.cells) {
//        if (!stepsToFinish.contains(cell) && !cell.closed) {
//          if (cell.terminal) {
//            thisStepAssigned += (cell -> null)
//          } else {
//            for (neighbor:Cell <- cell.neighbors) {
//              if (stepsToFinish.contains(neighbor)) {
//                thisStepAssigned += (cell -> neighbor)
//              }
//            }
//          }
//        }
//      }
//    } while (thisStepAssigned.size > 0)
//    if (stepsToFinish.contains(data.catCell)) {
//      return if (stepsToFinish(data.catCell) == null) -1 else data.getCellIndex(stepsToFinish(data.catCell).hexCoords)
//    } else {
//      for (neighbor:Cell <- data.catCell.neighbors) {
//        if (!neighbor.closed) {
//          return data.getCellIndex(neighbor.hexCoords)
//        }
//      }
//      return -2
//    }
//  }
//  def calcNextCellIndex2():Int = {
//    val emptyCells = data.cells.filterNot(_.closed).toArray
//    val next = emptyCells((Math.random() * emptyCells.length).toInt)
//    data.getCellIndex(next.hexCoords)
//  }

}