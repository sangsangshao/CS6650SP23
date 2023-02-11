package org.neu.cs6650;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import java.util.concurrent.CountDownLatch;
import io.swagger.client.model.SwipeDetails;
import io.swagger.client.api.SwipeApi;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadedClient {

  final static private int NUMTHREADS = 50;
//  final static String LOCALBASEPATH = "http://localhost:8080/twinder";
  final static String EC2PATH = "http://54.185.43.194:8080/twinder_server-0.0.1-SNAPSHOT/twinder";

  final static private int TOTALREQUESTS = 500000;
  final static private int ROUNDS = TOTALREQUESTS / NUMTHREADS;
//  private int count = 0;
//
//  synchronized public void inc() {
//    count++;
//  }
//
//  public int getVal() {
//    return this.count;
//  }

  public static void main(String[] args) throws InterruptedException {
//    final MultithreadedClient counter = new MultithreadedClient();
    CountDownLatch completed = new CountDownLatch(NUMTHREADS);
    AtomicInteger count = new AtomicInteger();
    RandomSwipeDetail randomSwipeDetail = new RandomSwipeDetail();

    ApiClient apiClient = new ApiClient();
//    apiClient.setBasePath(LOCALBASEPATH);
    apiClient.setBasePath(EC2PATH);
    SwipeApi apiInstance = new SwipeApi();
    apiInstance.setApiClient(apiClient);

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < NUMTHREADS; i++) {

      Runnable runnable = () -> {
        for (int k = 0; k < ROUNDS; k++) {
          SwipeDetails swipeDetails = randomSwipeDetail.getRandomSwipeDetail();
          String leftOrRight;
          if (Math.random() < 0.5) {
            leftOrRight = "left";
          } else {
            leftOrRight = "right";
          }
          try {
//            System.out.println("Running on Thread: " + Thread.currentThread().getName());
            ApiResponse<Void> response = apiInstance.swipeWithHttpInfo(swipeDetails, leftOrRight);
            //201: Done, and created. count

            if (response.getStatusCode() == 201) {
//              counter.inc();
              count.getAndIncrement();
            } else {
              //retry the request up to 5 times before counting it as a failed request.
              for (int j = 0; j < 5; j++) {
                response = apiInstance.swipeWithHttpInfo(swipeDetails, leftOrRight);
                if (response.getStatusCode() == 201) {
//                  counter.inc();
                  count.getAndIncrement();
                  break;
                }
              }
            }
          } catch (ApiException e) {
            System.err.println("Exception when calling SwipeApi#swipe");
            e.printStackTrace();
          }
        }
        completed.countDown();
      };
      Thread thread = new Thread(runnable);
      thread.setName(i + "");
      thread.start();
    }
    completed.await();
    long endTime = System.currentTimeMillis();
    long wallTime = endTime - startTime;
    System.out.println("Number of successful requests sent: " + count.get());
//    System.out.println("Number of successful requests sent: " + statistics.getSuccess());
    System.out.println("Number of unsuccessful requests: " + (TOTALREQUESTS - count.get()));
    System.out.println("The total run time for all threads to complete is: " + wallTime + "ms. (" + (double)wallTime / 1000 + "s.)");
//    System.out.println("Value should be equal to 500K (500000)" + " It is: " + multithreadedClient.getVal());
    System.out.println("Value should be equal to " + TOTALREQUESTS + " It is: " + count.get());
    System.out.println("The total throughput in requests per second(total number of request / wall time): " + ((double)count.get() * 1000 / wallTime));
  }
}