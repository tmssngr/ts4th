package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public enum Type {
    Bool(1),
    Int(2),
    Ptr(0);

    private final int byteCount;

    Type(int byteCount) {
        this.byteCount = byteCount;
    }

    public int getByteCount(int ptrSize) {
        return byteCount == 0 ? ptrSize : byteCount;
    }
}
