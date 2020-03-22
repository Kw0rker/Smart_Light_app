package com.example.student.smartlighttest1;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class Pair<T, F> {
    T first;
    F second;

    public Pair(T first, F value) {
        this.first = first;
        this.second = value;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(first);
    }

    @Override
    public String toString() {
        return "(" + first + "," + second + ")";
    }
}
