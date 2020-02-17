package com.modorone.juppeteer.util;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Pair<S, T> {

    private final S first;
    private final T second;

    /**
     * Creates a new {@link Pair} for the given elements.
     *
     * @param first  must not be {@literal null}.
     * @param second must not be {@literal null}.
     * @return
     */
    public static <S, T> Pair<S, T> of(S first, T second) {
        return new Pair<>(first, second);
    }

    public Pair(S first, T second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first element of the {@link Pair}.
     *
     * @return
     */
    public S getFirst() {
        return first;
    }

    /**
     * Returns the second element of the {@link Pair}.
     *
     * @return
     */
    public T getSecond() {
        return second;
    }

    /**
     * A collector to create a {@link Map} from a {@link Stream} of {@link Pair}s.
     *
     * @return
     */
    public static <S, T> Collector<Pair<S, T>, ?, Map<S, T>> toMap() {
        return Collectors.toMap(Pair::getFirst, Pair::getSecond);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}