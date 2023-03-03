package org.neu.cs6650;

public class SwipeDetailsForConsumer {

    private String swiper;
    private String swipee;
    private String leftOrRight;

    public SwipeDetailsForConsumer(String swiper, String swipee, String leftOrRight) {
        this.swiper = swiper;
        this.swipee = swipee;
        this.leftOrRight = leftOrRight;
    }

    public String getSwiper() {
        return swiper;
    }

    public String getSwipee() {
        return swipee;
    }

    public String getLeftOrRight() {
        return leftOrRight;
    }
}
