package org.izumi.jmix.cachepoc.screen.generator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

public class RandomList<T> extends ArrayList<T> {
    private final Random random = new Random();

    public RandomList(int initialCapacity) {
        super(initialCapacity);
    }

    public RandomList() {
    }

    public RandomList(Collection<? extends T> c) {
        super(c);
    }

    @Nullable
    public T next() {
        if (isEmpty()) {
            return null;
        }

        final int index = random.nextInt(size());
        return get(index);
    }

    @Nullable
    public T next(@Nullable T specialValue) {
        if (isEmpty()) {
            return null;
        }

        final int size = size();
        final int index = random.nextInt(size + 1);
        if (index == size) {
            return specialValue;
        } else {
            return get(index);
        }
    }
}
