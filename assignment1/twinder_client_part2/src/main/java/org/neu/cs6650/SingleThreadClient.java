package org.neu.cs6650;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.model.*;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.SwipeDetails;

public class SingleThreadClient {

  final static String LOCALBASEPATH = "http://localhost:8080/twinder";
//  final static String EC2PATH = "http://54.244.24.203:8080/twinder_server-0.0.1-SNAPSHOT/twinder";

  public static void main(String[] args) {
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(LOCALBASEPATH);
//    apiClient.setBasePath(EC2PATH);
    SwipeApi apiInstance = new SwipeApi();
    apiInstance.setApiClient(apiClient);
//    SwipeDetails details = new SwipeDetails();
//    details.setSwiper("233");
//    details.setSwipee("21345");
//    details.setComment("you are not my type, loser");
    RandomSwipeDetail random = new RandomSwipeDetail();
    SwipeDetails reqBody = random.getRandomSwipeDetail();
//    String leftOrRight = "left";
//    String leftOrRight = "right";
//    String leftOrRight = "aaa"; // error
    String leftOrRight;
    if (Math.random() < 0.5) {
      leftOrRight = "left";
    } else {
      leftOrRight = "right";
    }
    try {
      ApiResponse<Void> response = apiInstance.swipeWithHttpInfo(reqBody, leftOrRight);
      System.out.println("response status: " + response.getStatusCode());
    } catch (ApiException e) {
      System.err.println("Exception when calling SwipeApi#swipe");
      e.printStackTrace();
    }
  }
}