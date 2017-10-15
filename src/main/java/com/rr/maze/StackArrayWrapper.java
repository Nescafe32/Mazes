package com.rr.maze;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

@AllArgsConstructor
@EqualsAndHashCode
public class StackArrayWrapper {
    private Integer[] myArray;

    @Override
    public String toString() {
        return Arrays.toString(myArray);
    }
}