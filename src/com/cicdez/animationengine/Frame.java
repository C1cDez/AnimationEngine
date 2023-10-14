package com.cicdez.animationengine;

import java.awt.*;
import java.util.function.Consumer;

public class Frame {
    private Consumer<Graphics2D> consumer;
    public Frame(Consumer<Graphics2D> consumer) {
        this.consumer = consumer;
    }
    
    public void draw(Graphics2D graphics) {
        consumer.accept(graphics);
    }
    
    public void append(Consumer<Graphics2D> consumer) {
        this.consumer = this.consumer.andThen(consumer);
    }
    public void prepend(Consumer<Graphics2D> consumer) {
        this.consumer = consumer.andThen(this.consumer);
    }
}
