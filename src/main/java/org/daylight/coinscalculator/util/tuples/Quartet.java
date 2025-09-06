package org.daylight.coinscalculator.util.tuples;

public class Quartet<A, B, C, D> {
    private final A a;
    private final B b;
    private final C c;
    private final D d;

    public Quartet(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public A getA() { return a; }
    public B getB() { return b; }
    public C getC() { return c; }
    public D getD() { return d; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quartet<?, ?, ?, ?> quartet)) return false;
        return java.util.Objects.equals(a, quartet.a) &&
                java.util.Objects.equals(b, quartet.b) &&
                java.util.Objects.equals(c, quartet.c) &&
                java.util.Objects.equals(d, quartet.d);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(a, b, c, d);
    }

    @Override
    public String toString() {
        return "(" + a + ", " + b + ", " + c + ", " + d + ")";
    }
}
