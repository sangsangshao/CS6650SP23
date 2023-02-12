import com.google.gson.Gson;
import java.io.BufferedReader;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "SwipeServlet", value = "/SwipeServlet")
public class SwipeServlet extends HttpServlet {
  int MINSWIPER = 1;
  int MAXSWIPER = 5000;

  //swipee between 1 and 1000000
  int MINSWIPEE = 1;
  int MAXSWIPEE = 1000000;

  //comment random string of length between 1 and 256 characters
  int MINCOMMENTLENGTH = 1;
  int MAXCOMMENTLENGTH = 256;

//  @Override
//  protected void doGet(HttpServletRequest request, HttpServletResponse response)
//      throws ServletException, IOException {
//
//  }

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();

    try {
      StringBuilder sb = new StringBuilder();
      String s;
      while ((s = request.getReader().readLine()) != null) {
        sb.append(s);
      }

      SwipeDetails swipeDetails = gson.fromJson(sb.toString(), SwipeDetails.class);
      Status status = new Status();
      if (isValidSwipeDetails(swipeDetails) == 201) {
        status.setSuccess(true);
        status.setDescription("Write successful.");
        response.setStatus(HttpServletResponse.SC_CREATED);
      } else if (isValidSwipeDetails(swipeDetails) == 400){
        status.setSuccess(false);
        status.setDescription("Invalid inputs.");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      } else {
        status.setSuccess(false);
        status.setDescription("User not found.");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
      response.getOutputStream().print(gson.toJson(status));
      response.getOutputStream().flush();
    } catch (Exception ex) {
      ex.printStackTrace();
      Status status = new Status();
      status.setSuccess(false);
      status.setDescription(ex.getMessage());
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getOutputStream().print(gson.toJson(status));
      response.getOutputStream().flush();
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
//    String reqBody = request.getReader().lines().collect(Collectors.joining());
//    MsgSender sender = new MsgSender(response);
    String urlPath = request.getPathInfo();
//    if (urlPath == null || urlPath.isEmpty()) {
//      sender.send(HttpServletResponse.SC_NOT_FOUND, "Invalid URL or missing parameters.");
//    }
    String[] urlParts = urlPath.split("/");
//    if (this.isValidUrl(urlParts)) {
    if(urlParts.length == 3 && urlParts[1].equals("swipe") && (urlParts[2].equals("left") ||
        urlParts[2].equals("right"))) {
      processRequest(request, response);
    }
    else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND, "Invalid URL or missing parameters.");
    }
//    } else {
//      sender.send(HttpServletResponse.SC_NOT_FOUND, "Invalid URL or missing parameters.");
//    }
//    System.out.println(reqBody);
//    response.setStatus(HttpServletResponse.SC_OK);
//    response.getWriter().write(reqBody);
  }

  private Integer isValidSwipeDetails(SwipeDetails swipeDetails) {
    if (isValidSwiper(swipeDetails) && isValidSwipee(swipeDetails) && isValidComment(swipeDetails)) {
      return 201;
    }
    return 404;
  }

  private boolean isValidUrl(String[] urlParts) {
    //twinder/swipe/leftorright
    //[, twinder, swipe, leftorright]
//    return urlParts.length == 4 && urlParts[1].equals("twinder") && urlParts[2].equals("swipe");
    if(urlParts.length != 3 || !urlParts[1].equals("swipe") || (!urlParts[2].equals("left") &&
        !urlParts[2].equals("right"))) {
      return false;
    }
    return true;
  }

  public boolean isValidSwiper(SwipeDetails swipeDetails) {
    int num = Integer.parseInt(swipeDetails.getSwiper());
    return num >= MINSWIPER && num <= MAXSWIPER;
  }

  public boolean isValidSwipee(SwipeDetails swipeDetails) {
    int num = Integer.parseInt(swipeDetails.getSwipee());
    return num >= MINSWIPEE && num <= MAXSWIPEE;
  }

  public boolean isValidComment(SwipeDetails swipeDetails) {
    return swipeDetails.getComment() != null && swipeDetails.getComment().length() >= MINCOMMENTLENGTH && swipeDetails.getComment().length() <= MAXCOMMENTLENGTH;
  }
}
