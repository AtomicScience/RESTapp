package ru.atomicscience.restapp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomSequenceGenerator {
    private final int maxNumber;


    /**
     * Instantiates a new Random sequence generator.
     * @param maxNumber the max number that could be generated.
     */
    public RandomSequenceGenerator(int maxNumber) {
        this.maxNumber = maxNumber;
    }
    /**
     * Generates a list of natural numbers of defined size, with a defined maximum number
     * @param count the size of the list to return
     * @return the list of integers, all lying within the interval of [0, maxNumber)
     */
    public List<Integer> generateRandomSequence(int count) {
        List<Integer> possibleNumbers = getAllPossibleNumbers();

        Collections.shuffle(possibleNumbers);

        return possibleNumbers.subList(0, count);
    }

    private List<Integer> getAllPossibleNumbers() {
        return IntStream
                .iterate(0, i -> i + 1)
                .boxed()
                .limit(maxNumber)
                .collect(Collectors.toCollection(ArrayList::new));
                // Collectors.toList is avoided here, because
                // it does not guarantee the mutability of the list,
                // which is essential
    }
}
