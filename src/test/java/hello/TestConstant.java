package hello;

public enum TestConstant {
    MAX_CUSTOMERS (3),
    MAX_WAIT_BETWEEN_ACTIONS(50);

    private int value;

    TestConstant(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}