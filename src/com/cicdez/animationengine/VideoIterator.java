package com.cicdez.animationengine;

import java.util.Iterator;
import java.util.function.Function;

public class VideoIterator implements Iterator<Frame> {
    private final Function<Integer, Frame> generator;
    private int current;
    private int frames;
    
    public VideoIterator(Function<Integer, Frame> generator) {
        this.generator = generator;
    }
    
    public void setFrames(int frames) {
        this.frames = frames;
    }
    
    @Override
    public Frame next() {
        current++;
        return generator.apply(current);
    }
    
    @Override
    public boolean hasNext() {
        return current < frames;
    }
}
