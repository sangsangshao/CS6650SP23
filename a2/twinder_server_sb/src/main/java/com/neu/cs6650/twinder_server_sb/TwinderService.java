package com.neu.cs6650.twinder_server_sb;
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

    protected SwipeDetailsForConsumer responseMsg(SwipeDetails swipeDetails, String leftOrRight) {
        String left = "left";
        String right = "right";
        if(!left.equalsIgnoreCase(leftOrRight) && !right.equalsIgnoreCase(leftOrRight)) return null;
        if (swipeDetails.getSwiper() != null && swipeDetails.getSwipee() != null && swipeDetails.getComment() != null
                && isValidSwiper(swipeDetails) && isValidSwipee(swipeDetails) && isValidComment(swipeDetails)) {
            return new SwipeDetailsForConsumer(swipeDetails.getSwiper(), swipeDetails.getSwipee(), leftOrRight.toLowerCase());
        }
        return null;
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
