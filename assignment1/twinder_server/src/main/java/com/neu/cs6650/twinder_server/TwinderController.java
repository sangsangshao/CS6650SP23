package com.neu.cs6650.twinder_server;


import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/twinder")
public class TwinderController {
  @Autowired
  private TwinderService twinderService;

    @PostMapping("swipe/{leftOrRight}")
  public ResponseEntity<SwipeDetails> doPost(@PathVariable("leftOrRight") String leftOrRight, @RequestBody SwipeDetails swipeDetails) {
    SwipeDetails response = twinderService.responseMsg(swipeDetails, leftOrRight);

    if(response != null) {
      return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
