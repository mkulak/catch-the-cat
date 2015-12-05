package cat

//import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
//import javax.imageio.ImageIO
//import javax.servlet.ServletConfig
//
//object CatServlet {
//  private val game:Controller = new Controller
//}
//
//class CatServlet extends HttpServlet {
//  override def init(config: ServletConfig) {
//    CatServlet.game.startGame()
//  }
//
//  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
//    val xStr = req getParameter "x"
//    val yStr = req getParameter "y"
//    if (xStr != null && yStr != null) {
//      val x = xStr.toInt
//      val y = yStr.toInt
//      CatServlet.game.makeTurn(x, y)
//    }
//    resp.setContentType("image/jpg")
//    val out = resp.getOutputStream
//    ImageIO.write(CatServlet.game.renderField, "JPG", out)
//    out.flush()
//  }
//}