package org.neu.cs6650;

import io.swagger.client.model.SwipeDetails;
import java.util.concurrent.ThreadLocalRandom;


public class RandomSwipeDetail {

  //swiper between 1 and 5000
  final static int MINSWIPER = 1;
  final static int MAXSWIPER = 5001;

  //swipee between 1 and 1000000
  final static int MINSWIPEE = 1;
  final static int MAXSWIPEE = 1000001;

  //comment random string of 256 characters
  final static int COMMENTLENGTH = 256;

  final static int TOTALLOWERCASELETTERS = 26;

  protected SwipeDetails getRandomSwipeDetail() {
    SwipeDetails body = new SwipeDetails();
    body.setSwiper(String.valueOf(ThreadLocalRandom.current().nextInt(MINSWIPER, MAXSWIPER)));
    body.setSwipee(String.valueOf(ThreadLocalRandom.current().nextInt(MINSWIPEE, MAXSWIPEE)));
    body.setComment(getRandomString());
//    System.out.println(body);
    return body;
  }

  private String getRandomString() {
    StringBuilder sb = new StringBuilder();
    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < COMMENTLENGTH; i++) {
      sb.append((char)(random.nextInt(TOTALLOWERCASELETTERS) + 'a'));
    }
    return sb.toString();
  }
}

////recourse: https://commons.apache.org/proper/commons-lang/javadocs/api-3.9/org/apache/commons/lang3/RandomStringUtils.html
//
//package org.neu.cs6650;
//import java.util.Random;
//import org.apache.commons.lang3.RandomStringUtils;
//import io.swagger.client.model.SwipeDetails;
//
//public class RandomSwipeDetail {
//
//  Random random;
//
//  //swiper between 1 and 5000
//  int MINSWIPER = 1;
//  int MAXSWIPER = 5001;
//
//  //swipee between 1 and 1000000
//  int MINSWIPEE = 1;
//  int MAXSWIPEE = 1000001;
//
//  //comment random string of 256 characters
//  int COMMENTLENGTH = 256;
//
//  public RandomSwipeDetail() {
//    random = new Random();
//  }
//
//  protected SwipeDetails getRandomSwipeDetail() {
//    SwipeDetails swipeDetails = new SwipeDetails();
//    swipeDetails.setSwiper(this.getRandomIntString(MINSWIPER, MAXSWIPER));
//    swipeDetails.setSwipee(this.getRandomIntString(MINSWIPEE, MAXSWIPEE));
//    swipeDetails.setComment(this.getRandomString());
//    System.out.println(swipeDetails);
//    return swipeDetails;
//  }
//
//  private int getRandomInt(int min, int max) {
//    return random.nextInt(max - min) + min;
//  }
//
//  private String getRandomIntString(int min, int max) {
//    return String.valueOf(getRandomInt(min, max));
//  }
//
//  private String getRandomString() {
//    return RandomStringUtils.randomAlphanumeric(COMMENTLENGTH);
//  }
//}