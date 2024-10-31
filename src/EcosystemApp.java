import java.io.*;
import java.util.*;

public class EcosystemApp {
    private static final Scanner scanner = new Scanner(System.in);
    public static double waterLevel;
    private static final List<Plant> plants = new ArrayList<>();
    private static final List<Animal> animals = new ArrayList<>();
    private static String ecosystemFileName;

    public static void main(String[] args) {
        clearConsole();
        System.out.println("Добро пожаловать в симуляцию экосистемы! \n" +
                "Уважаемый пользователь, представь что ты создаешь свой остров, \n" +
                "с озером в середине, который можешь населить любыми животными или растениями, \n" +
                "которые будут жить и выживать самостоятельно! Используя твои параметры, которые \n" +
                "укажешь перед запуском (такие как: кол-во дней существования экосистемы,  уровень воды в озере), \n" +
                "программа укажет тебе как смогла развиться твоя экосистема). \n" +
                "На жизнь в твоей экосистеме будут влиять различные параметры и ресурсы, \n" +
                "перед запуском экосистемы, рекомендуем ознакомится с логикой и правилами программы (1-й пункт меню). \n" +
                "Также состояние твоей экосистемы будет храниться в текстовом документе (ИмяЭкосистемы.txt), а все действия, \n" +
                "произошедшие за каждый день, будут  описаны в, созданном программой, документе ИмяЭкосистемы logs.txt \n");
        chooseEcosystem();
        mainMenu();
    }

    private static void chooseEcosystem() {
        System.out.println("Придумайте имя для экосистемы или введите существующее (файл .txt):");
        ecosystemFileName = scanner.nextLine();

        File file = new File(ecosystemFileName + ".txt");
        if (file.exists()) {
            loadEcosystem();
        } else {
            System.out.println("Новая экосистема будет создана.");
        }
    }

    private static void loadEcosystem() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ecosystemFileName + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 4) { // Формат строки для животных
                    String name = parts[0];
                    String type = parts[1];
                    String size = parts[2];
                    int quantity = Integer.parseInt(parts[3]);
                    animals.add(new Animal(name, type, size, quantity));
                } else if (parts.length == 3) { // Формат строки для растений
                    String name = parts[0];
                    String size = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    plants.add(new Plant(name, size, quantity));
                }
            }
            System.out.println("Экосистема загружена.");
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке экосистемы: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка формата данных: " + e.getMessage());
        }
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("Основное меню:");
            System.out.println("1. Логика и правила программы");
            System.out.println("2. Создать виды животных и растений");
            System.out.println("3. Сохранить");
            System.out.println("4. Показать статус экосистемы");
            System.out.println("5. Запустить экосистему");
            System.out.println("6. Выйти из программы");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("");
                    break;
                case "2":
                    clearConsole();
                    createSpeciesMenu();
                    break;
                case "3":
                    clearConsole();
                    saveEcosystem();
                    break;
                case "4":
                    clearConsole();
                    showEcosystemStatus();
                    break;
                case "5":
                    clearConsole();
                    logToFile("Новый запуск экосистемы:");
                    runEcosystem();
                    break;
                case "6":
                    clearConsole();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Некорректный ввод, попробуйте снова.");
            }
        }
    }
    public static void clearConsole(){
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void createSpeciesMenu() {
        while (true) {
            clearConsole();
            System.out.println("Меню создания видов:");
            System.out.println("1. Создать виды животных");
            System.out.println("2. Создать виды растений");
            System.out.println("3. Предыдущая страница");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    createAnimals();
                    break;
                case "2":
                    createPlants();
                    break;
                case "3":
                    clearConsole();
                    return;
                default:
                    System.out.println("Некорректный ввод, попробуйте снова.");
            }
        }
    }

    private static void createAnimals() {
        System.out.println("Введите тип животного (травоядное/хищное):");
        String type = scanner.nextLine();
        System.out.println("Введите размер животного (крупное/мелкое):");
        String size = scanner.nextLine();
        System.out.println("Введите наименование животного:");
        String name = scanner.nextLine();
        System.out.println("Введите количество:");
        int quantity = Integer.parseInt(scanner.nextLine());
        //существует ли уже такое животное
        boolean found = false;
        for (Animal animal : animals) {
            if (animal.getName().equalsIgnoreCase(name) &&
                    animal.getSize().equalsIgnoreCase(size) &&
                    animal.getType().equalsIgnoreCase(type)) {
                animal.increaseQuantity(quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            animals.add(new Animal(name, type, size, quantity));
            System.out.println("Животное добавлено.");
        } else {
            System.out.println("Количество существующего животного обновлено.");
        }
    }

    private static void createPlants() {
        System.out.println("Введите размер растения (крупное/мелкое):");
        String size = scanner.nextLine();
        System.out.println("Введите наименование растения:");
        String name = scanner.nextLine();
        System.out.println("Введите количество:");
        int quantity = Integer.parseInt(scanner.nextLine());

        //существует ли уже такое растение
        boolean found = false;
        for (Plant plant : plants) {
            if (plant.getName().equalsIgnoreCase(name) && plant.getSize().equalsIgnoreCase(size)) {
                plant.increaseQuantity(quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            plants.add(new Plant(name, size, quantity));
            System.out.println("Растение добавлено.");
        } else {
            System.out.println("Количество существующего растения обновлено.");
        }
    }

    private static void saveEcosystem() {
        String filePath =  ecosystemFileName + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Plant plant : plants) {
                writer.write(plant.getName() + ", " + plant.getSize() + ", " + plant.getQuantity());
                writer.newLine();
            }
            for (Animal animal : animals) {
                writer.write(animal.getName() + ", " + animal.getType() + ", " + animal.getSize() + ", " + animal.getQuantity());
                writer.newLine();
            }
            System.out.println("Экосистема сохранена.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении экосистемы: " + e.getMessage());
        }
    }

    private static void showEcosystemStatus() {
        System.out.println("Растения:");
        for (Plant plant : plants) {
            System.out.println(plant);
        }
        System.out.println("Животные:");
        for (Animal animal : animals) {
            System.out.println(animal);
        }
    }

    private static void runEcosystem() {
        System.out.println("Введите количество дней существования экосистемы:");
        int days = Integer.parseInt(scanner.nextLine());
        System.out.println("Выберите уровень воды в озере (1-100%):");
        double waterLevel = Double.parseDouble(scanner.nextLine()) / 100.0;


        for (int day = 0; day < days; day++) {
            if (waterLevel <= 0) {
                System.out.println("Уровень воды достиг нуля. Экосистема не может продолжать существование.");
                break;
            }
            interactSpecies(day, animals, plants);
            waterLevel -= calculateWaterUsage(animals, plants);
            checkRain(day, waterLevel, plants);
            removeDeadSpecies();
        }
        System.out.println("По истечению " + days + " дней, состояние экосистемы составило:");
        showEcosystemStatus();
    }

    private static double calculateWaterUsage(List<Animal> animals, List<Plant> plants) {
        double totalWaterUsage = 0.0;
        for (Plant plant : plants) {
            if (plant.getSize().equalsIgnoreCase("мелкое")) {
                totalWaterUsage += plant.getQuantity() * 0.0001;
            } else {
                totalWaterUsage += plant.getQuantity() * 0.0002;
            }
        }
        for (Animal animal : animals) {
            if (animal.getSize().equalsIgnoreCase("мелкое")) {
                totalWaterUsage += animal.getQuantity() * 0.0005;
            } else {
                totalWaterUsage += animal.getQuantity() * 0.001;
            }
        }
        return totalWaterUsage;
    }
    private static void checkRain(int day, double waterLevel, List<Plant> plants) {
        if (day % 10 == 0 && day !=0) {
            boolean increaseWater = new Random().nextBoolean();
            if (increaseWater) {
                double increaseAmount = waterLevel * 0.4;
                waterLevel += increaseAmount; // увеличиваем уровень воды
                if (waterLevel > 1.0){
                    waterLevel = 1.0;
                    logToFile("Пошел дождь, уровень воды в озере стал максимальным " + waterLevel*100 + "%");
                }else{
                logToFile("Пошел дождь, уровень воды в озере стал больше на 40% и стал: " + waterLevel*100 + "%");
                }
                logToFile("После дождя выросли:");
                for (Plant plant : plants) {
                    plant.increaseQuantity(+1);
                    logToFile(plant.getName() + " +1");
                }
            } else {
                logToFile("Дождь не пошел, текущий уровень воды: " + waterLevel*100 + "%");
            }
        }
    }

    private static void logToFile(String message) {
        String file = ecosystemFileName + " logs.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    private static void interactSpecies( int day, List<Animal> animals, List<Plant> plants) {
        List<Animal> fedAnimals = new ArrayList<>();
        List<Animal> toRemove = new ArrayList<>(); // Список для удаления мертвых животных
        String message = (day + 1) +" день:";
        logToFile(message);
        boolean hasEaten = false;
        // травоядные
        for (Animal animal : animals) {
            if (animal.getType().equalsIgnoreCase("травоядное") && animal.getRemainingLifeDays() == 1) { // сначала обрабатываем травоядных
                    for (Plant plant : plants) {
                        if (animal.getSize().equalsIgnoreCase("мелкое") && plant.getSize().equalsIgnoreCase("мелкое")) {
                            plant.increaseQuantity(-1);
                            animal.setRemainingLifeDays(3); // 3 дня жизни
                            fedAnimals.add(animal);
                            hasEaten = true;
                            logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ") съел мелкое растение. Получено 3 дня жизни.");
                            break;
                        } else if (animal.getSize().equalsIgnoreCase("мелкое") && plant.getSize().equalsIgnoreCase("крупное")) {
                            plant.increaseQuantity(-1);
                            animal.setRemainingLifeDays(9); // 2 дня жизни за крупное растение
                            fedAnimals.add(animal);
                            hasEaten = true;
                            logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ") съел крупное растение. Получено 9 дня жизни.");
                            break;
                        } else if (animal.getSize().equalsIgnoreCase("крупное")) {
                            if (plant.getSize().equalsIgnoreCase("мелкое")) {
                                plant.increaseQuantity(-1);
                                animal.setRemainingLifeDays(1); // 1 день жизни
                                fedAnimals.add(animal);
                                hasEaten = true;
                                logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ") съел мелкое растение. Получено 1 день жизни.");
                                break;
                            } else if (plant.getSize().equalsIgnoreCase("крупное")) {
                                plant.increaseQuantity(-1);
                                animal.setRemainingLifeDays(6); // 9 дней жизни
                                fedAnimals.add(animal);
                                hasEaten = true;
                                logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ") съел крупное растение. Получено 6 дней жизни.");
                                break;
                            }
                        }
                    }

            }
        }

        //хищники
        for (Animal animal : animals) {
            if (animal.getType().equalsIgnoreCase("хищное") && animal.getRemainingLifeDays() == 1) {
                for (Animal prey : animals) {
                    if (prey != animal && prey.getQuantity() > 0) { // Проверяем, что это не само животное
                        if (animal.getSize().equalsIgnoreCase("мелкое") && prey.getType().equalsIgnoreCase("травоядное") && prey.getSize().equalsIgnoreCase("мелкое")) {
                            prey.increaseQuantity(-1);
                            animal.setRemainingLifeDays(4); // 4 дня жизни за мелкое травоядное
                            fedAnimals.add(animal);
                            hasEaten = true;
                            logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ") съел мелкое травоядное. Получено 4 дня жизни.");
                            break;
                        } else if (animal.getSize().equalsIgnoreCase("крупное")) {
                            if (prey.getType().equalsIgnoreCase("травоядное")) {
                                if (prey.getSize().equalsIgnoreCase("крупное")) {
                                    prey.increaseQuantity(-1);
                                    animal.setRemainingLifeDays(4); // 4 дня жизни за крупное травоядное
                                    fedAnimals.add(animal);
                                    hasEaten = true;
                                    logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ") съел крупное травоядное. Получено 4 дня жизни.");
                                    break;
                                } else if (prey.getSize().equalsIgnoreCase("мелкое")) {
                                    prey.increaseQuantity(-1);
                                    animal.setRemainingLifeDays(2); // 2 дня жизни за мелкое травоядное
                                    fedAnimals.add(animal);
                                    hasEaten = true;
                                    logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ") съел мелкое травоядное. Получено 2 дня жизни.");
                                    break;
                                }
                            } else if (prey.getType().equalsIgnoreCase("хищное") && prey.getSize().equalsIgnoreCase("мелкое")) {
                                prey.increaseQuantity(-1);
                                animal.setRemainingLifeDays(2); // 2 дня жизни за мелкое хищное
                                fedAnimals.add(animal);
                                hasEaten = true;
                                logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ") съел мелкое хищное. Получено 2 дня жизни.");
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (Animal animal : animals) {
            if (!hasEaten) { // если животное не поело, уменьшаем дни жизни
                animal.decreaseLifeDays();
                logToFile(animal.getName() + " не смог поесть и потерял день жизни.");
            }
        }
        // проверка, нужно ли удалить животное
        for (Animal animal : animals) {
            if (animal.getRemainingLifeDays() <= 0 && !animal.isLogged()) {
                toRemove.add(animal); // Добавляем в список на удаление
                logToFile("Животное "+ animal.getName() + " умерло так как закончились дни жизни.");
                animal.setLogged(true); // Устанавливаем флаг, что сообщение о смерти залогировано
            }

        }

        animals.removeAll(toRemove); // удаляем мертвых животных

        for (Animal animal : fedAnimals) {
            if (animal.getQuantity() > 1) {
                logToFile("Вид: " + animal.getName() + " размножился, +1");
                animal.increaseQuantity(1);
            }
        }
    }

    private static void removeDeadSpecies() {
        // удаляем растения, если их количество достигло нуля
        plants.removeIf(plant -> {
            if (plant.getQuantity() <= 0) {
                logToFile("Растение " + plant.getName() + " было удалено, т.к. количество вида достигло нуля.");
                return true; // Удалить растение
            }
            return false; // Не удалять
        });

        // удаляем животных, если их количество достигло нуля или закончились дни жизни
        animals.removeIf(animal -> {
            if (animal.getQuantity() <= 0) {
                logToFile("Животное " + animal.getName() + " было удалено, т.к. количество популяции данного вида достигло нуля.");
                return true;
            }
            return false;
        });
    }
}