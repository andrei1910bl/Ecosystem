class Plant {
    private final String name;
    private final String size;
    private int quantity;

    public Plant(String name, String size, int quantity) {
        this.name = name;
        this.size = size;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }
    public int getQuantity() {
        return quantity;
    }
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    @Override
    public String toString() {
        return name + " (" + size + "), кол-во: " + quantity + ".";
    }
}