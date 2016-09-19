package hello;

public enum TestConstant {
    MAX_CUSTOMERS (50),
    MAX_WAIT_BETWEEN_ACTIONS(1500);

    private int value;

    TestConstant(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}