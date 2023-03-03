package com.neu.cs6650.twinder_server_sb;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping(value = "/twinder")
public class TwinderController {
    @Autowired
    private TwinderService twinderService;
    @Autowired
    private RabbitMQService rabbitMQService;

    @PostMapping("swipe/{leftOrRight}")
    public ResponseEntity<SwipeDetails> doPost(@PathVariable("leftOrRight") String leftOrRight, @RequestBody SwipeDetails swipeDetails) throws IOException {
//        SwipeDetails response = twinderService.responseMsg(swipeDetails, leftOrRight);
        SwipeDetailsForConsumer response = twinderService.responseMsg(swipeDetails, leftOrRight);

        if(response != null) {
            rabbitMQService.send(response);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
