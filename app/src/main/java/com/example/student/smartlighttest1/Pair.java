package com.example.student.smartlighttest1;

public class Pair<T, F> {
    T first;
    F second;

    public Pair(T first, F value) {
        this.first = first;
        this.second = value;
    }

    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return first.equals(pair.first) &&
                second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return first.hashCode() * second.hashCode() + first.hashCode() % second.hashCode();
    }

    @Override
    public String toString() {
        return "(" + first + "," + second + ")";
    }
}
