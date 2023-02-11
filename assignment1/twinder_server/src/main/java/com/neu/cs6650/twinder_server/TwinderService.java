package com.neu.cs6650.twinder_server;

import org.springframework.stereotype.Service;

@Service
public class TwinderService {
  //swiper between 1 and 5000
  int MINSWIPER = 1;
  int MAXSWIPER = 5000;

  //swipee between 1 and 1000000
  int MINSWIPEE = 1;
  int MAXSWIPEE = 1000000;

  //comment random string of length between 1 and 256 characters
  int MINCOMMENTLENGTH = 1;
  int MAXCOMMENTLENGTH = 256;

  protected SwipeDetails responseMsg(SwipeDetails swipeDetails, String leftOrRight) {
    String left = "left";
    String right = "right";
    if (swipeDetails.getSwiper() != null && swipeDetails.getSwipee() != null
        && swipeDetails.getComment() != null && (left.equalsIgnoreCase(leftOrRight)
        && isValidSwiper(swipeDetails)
        && isValidSwipee(swipeDetails)
        && isValidComment(swipeDetails)
        || right.equalsIgnoreCase(leftOrRight))) {
      return swipeDetails;
    }
    return null;
  }

  public boolean isValidSwiper(SwipeDetails swipeDetails) {
//    try {
      int num = Integer.parseInt(swipeDetails.getSwiper());
      return num >= MINSWIPER && num <= MAXSWIPER;
//    }
//    catch(NumberFormatException e) {
//      return false;
//    }
  }

  public boolean isValidSwipee(SwipeDetails swipeDetails) {
//    try {
      int num = Integer.parseInt(swipeDetails.getSwipee());
      return num >= MINSWIPEE && num <= MAXSWIPEE;
//    }
//    catch(NumberFormatException e) {
//      return false;
//    }
  }

  public boolean isValidComment(SwipeDetails swipeDetails) {
    return swipeDetails.getComment() != null && swipeDetails.getComment().length() >= MINCOMMENTLENGTH && swipeDetails.getComment().length() <= MAXCOMMENTLENGTH;
  }
}
