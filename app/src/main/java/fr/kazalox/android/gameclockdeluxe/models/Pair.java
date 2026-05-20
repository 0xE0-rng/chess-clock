package fr.kazalox.android.gameclockdeluxe.models;

import java.io.Serializable;

public class Pair<E> implements Serializable {
    private static final long serialVersionUID = -7296184260800291438L;
    public final E first;
    public final E second;

    public Pair(E e1, E e2) {
        this.first = e1;
        this.second = e2;
    }

    public E other(E e1) {
        return e1.equals(this.first) ? this.second : this.first;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        try {
            Pair<E> other = (Pair) o;
            return this.first.equals(other.first) && this.second.equals(other.second);
        } catch (ClassCastException e) {
            return false;
        }
    }
}
