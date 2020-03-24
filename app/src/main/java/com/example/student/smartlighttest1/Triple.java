package com.example.student.smartlighttest1;

public class Triple<T1, T2, T3> {
    T1 first;
    T2 second;
    T3 third;

    public Triple(T1 t1, T2 t2, T3 tr) {
        this.first = t1;
        this.second = t2;
        this.third = tr;
    }

    public T1 first() {
        return first;
    }

    public T2 second() {
        return second;
    }

    public T3 third() {
        return third;
    }
}
