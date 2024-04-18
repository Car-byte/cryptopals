package org.carbyte.common;

public class MutableReference<T> {
    private T value;

    public MutableReference(final T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(final T value) {
        this.value = value;
    }
}
