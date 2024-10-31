public class Animal {
    private String name;
    private String type;
    private String size;
    private int quantity;
    private int remainingLifeDays; // для отслеживания дней жизни
    private boolean isLogged; // флаг для отслеживания логирования смерти

    // Конструктор
    public Animal(String name, String type, String size, int quantity) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.quantity = quantity;
        this.remainingLifeDays = 0;
      //  this.isLogged = false;
        if (size.equalsIgnoreCase("мелкое")) {
            this.remainingLifeDays = 2; // изначально
        } else if (size.equalsIgnoreCase("крупное")) {
            this.remainingLifeDays = 4; // изначально
        }
        this.isLogged = false;
    }



    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }
    public int getQuantity() {
        return quantity; // Метод для получения количества
    }

    public void increaseQuantity(int amount) {
        this.quantity = amount;
    }
    public int getRemainingLifeDays() {
        return remainingLifeDays;
    }
    public void setRemainingLifeDays(int days) {
        remainingLifeDays = days;
    }
    // Метод для уменьшения дней жизни
    public void decreaseLifeDays() {
        this.remainingLifeDays--;
    }
    @Override
    public String toString() {
        return name + " - " + type + " (" + size + "), кол-во: " + quantity + ".";
    }
    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }
}