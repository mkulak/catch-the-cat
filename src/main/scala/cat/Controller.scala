package cat


import scala.collection.mutable.ListBuffer

class Controller extends Object with UserActionsListener {
  private val width:Int = 400
  private val height:Int = 400
  private var view: MainCanvas = null
  private var data: Data = null

  def startGame() {
    resetData()
    view = new MainCanvas(this, width, height)
    updateView()
  }

  def makeTurn(x:Int, y:Int) {
    view.onClick(new Point(x, y))
  }

  private def resetData() {
    val cells = new ListBuffer[MyCell]
    var cci = 0
    val horizontals = (Config.HEIGHT / (Config.CENTERS_DISTANCE * Math.sin(Math.PI / 3))).toInt - 1
    val verticals = Config.WIDTH / Config.CENTERS_DISTANCE
    // building correct cells coords
    for (y <- -horizontals / 2 to horizontals / 2) {
      for (x <-
              -verticals / 2 - y / 2 + (if (y < 0 && -y % 2 == 1) 1 else 0)
              to
              verticals / 2 - y / 2 - (if (y % 2 == 0) 0 else 1) + (if (y < 0 && -y % 2 == 1) 1 else 0)
      ) {
        cells += new MyCell(new Point(x, y), List(), false)
        if (x == -1 && y == 0) {
          cci = cells.size
        }
      }
    }
    //
    data = new Data(cells.toList, cci)
    // building neighbors and finishing
    for (cell:MyCell <- data.cells) {
      var neighbors = Array(Array(1, 0), Array(-1, 0), Array(0, 1), Array(0, -1), Array(1, -1), Array(-1, 1))
      var nbrsList = new ListBuffer[MyCell]
      for (neighbor:Array[Int] <- neighbors) {
        val neighborPoint = new Point(cell.cellCoords.x + neighbor(0), cell.cellCoords.y + neighbor(1))
        if (data.hasCell(neighborPoint)) {
          nbrsList += data.getCell(neighborPoint)
        } 
      }
      cell.neighbors = nbrsList.toList
      cell.isFinishing = nbrsList.size < 6
    }
  }

  def resetGame() {
    resetData()
    updateView()
  }

  private def updateView() {
    view.update(data)
  }

  private def rnd[T](a:List[T]):T = a((Math.random * (a.length - 1)).toInt)

  override def cellClicked(cellIndex: Int) {
    val cell: MyCell = data.cells(cellIndex)
    if (cell.walked || cellIndex == data.catCellIndex) {
      // TODO show alert
    } else {
      cell.walked = true      
      val nextIndex = new DecisionMaker(data).calcNextCellIndex3()
      if (nextIndex == -1 || nextIndex == -2) {
        resetGame()
        // TODO show alert
      } else {
        data.catCellIndex = nextIndex
      }
      updateView()
    }
  }

  def resetGameClicked() = resetGame()

  def render(g:Graphics): Unit = {
    view.draw(g)
  }
}