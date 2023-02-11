package org.neu.cs6650;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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
  private static CopyOnWriteArrayList<Long> latencies = new CopyOnWriteArrayList<>();
  private static CopyOnWriteArrayList<String> lines = new CopyOnWriteArrayList<>();

//  synchronized public void inc() {
//    count++;
//  }

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
      ArrayList<Long> newLatency = new ArrayList<>();
      ArrayList<String> newline = new ArrayList<>();
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
            // starttime
//            long startLatencies = System.currentTimeMillis();
//            ApiResponse<Void> response = apiInstance.swipeWithHttpInfo(swipeDetails, leftOrRight);
//            long endLatencies = System.currentTimeMillis();
//            newLatency.add(endLatencies - startLatencies);
//            String line = startLatencies + ", POST, " + (endLatencies - startLatencies) + ", " + response.getStatusCode() + System.lineSeparator();
//            writer.write(line);
////            newline.add(line);
//            //201: Done, and created. count
//            if (response.getStatusCode() == 201) {
////              counter.inc();
//              count.getAndIncrement();
//            } else {
              //retry the request up to 5 times before counting it as a failed request.
              for (int j = 0; j < 6; j++) {
                long startLatencies = System.currentTimeMillis();
//                LocalDateTime curTime = LocalDateTime.now();
                ApiResponse<Void> response = apiInstance.swipeWithHttpInfo(swipeDetails, leftOrRight);
                if (response.getStatusCode() == 201) {
                  long endLatencies = System.currentTimeMillis();
                  newLatency.add(endLatencies - startLatencies);
                  String line = endLatencies + ", POST, " + (endLatencies - startLatencies) + ", " + response.getStatusCode() + System.lineSeparator();
                  newline.add(line);
//                  counter.inc();
                  count.getAndIncrement();
                  break;
                }
              }
          } catch (ApiException e) {
            System.err.println("Exception when calling SwipeApi#swipe");
            e.printStackTrace();
          }
        }
        latencies.addAll(newLatency);
        lines.addAll(newline);
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
    System.out.println("Number of unsuccessful requests: " + (TOTALREQUESTS - count.get()));
    System.out.println("The total run time for all threads to complete is: " + wallTime + "ms. (" + (double)wallTime / 1000 + "s.)");
//    System.out.println("Value should be equal to 500K (500000)" + " It is: " + multithreadedClient.getVal());
    System.out.println("Value should be equal to " + TOTALREQUESTS + " It is: " + count.get());
    System.out.println("The total throughput in requests per second(total number of request / wall time): " + ((double)count.get() * 1000 / wallTime));

    System.out.println("Mean response time(ms): " + String.format("%.2f", getMean()));
    System.out.println("Median response time(ms): " + getMedian());
    System.out.println("Total throughput in requests per second(throughput = total number of requests/wall time (requests/second)): " + ((double)count.get() * 1000 / wallTime));
    System.out.println("p99 response time(ms): " + getP99());
    System.out.println("Minimum response time(ms): " + getMinimum());
    System.out.println("Maximum response time(ms): " + getMaximum());
    System.out.println("When the number of thread: " + NUMTHREADS + ", Little Law throughput in theory:" + NUMTHREADS / getMean() * 1000);

    writeToCSV();
  }

  public static double getMean() {
    double total = 0;
    for (long latency : latencies) {
      total += latency;
    }
    return total / latencies.size();
  }

  public static double getMedian() {
    Collections.sort(latencies);
    int midIndex = latencies.size() / 2;
    if (latencies.size() % 2 == 0) {
      return (latencies.get(midIndex) + latencies.get(midIndex - 1)) / 2;
    }
    return latencies.get(midIndex);
  }

  public static long getP99 () {
    Collections.sort(latencies);
    return latencies.get((int) Math.ceil(latencies.size() * 0.99) - 1);
  }

  public static long getMinimum() {
    return Collections.min(latencies);
  }

  public static long getMaximum() {
    return Collections.max(latencies);
  }

  public static void writeToCSV() {
    try  {
      BufferedWriter writer = new BufferedWriter(new FileWriter("recordFile.csv"));
      writer.write("start_time, request_type, latency, response_code" + System.lineSeparator());
      for (String line: lines) {
//        System.out.println(line);
        writer.write(line);
      }
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  }
