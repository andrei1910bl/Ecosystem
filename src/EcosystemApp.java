import java.io.*;
import java.util.*;

public class EcosystemApp {
    private static final Scanner scanner = new Scanner(System.in);
    public static double waterLevel;
    private static final List<Plant> plants = new ArrayList<>();
    private static final List<Animal> animals = new ArrayList<>();
    private static String ecosystemFileName;
    private static int climateChoice;
    private static int rainCheckFrequency;


    public static void main(String[] args) {
        clearConsole();
        System.out.println("Добро пожаловать в симуляцию экосистемы! \n" +
                "Уважаемый пользователь, представь что ты создаешь свой остров, \n" +
                "с водоемом, который можешь населить любыми животными или растениями, \n" +
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
                    System.out.println("Главный параметр отвечающий за существование экосистемы – это уровень воды в озере,\n" +
                            " ни одно животное или растение в экосистеме не сможет прожить ни дня без воды. Перед запуском экосистемы, \n" +
                            "пользователю будет предложено выбрать уровень воды в озере (в процентах), чем больше уровень воды, тем больше\n" +
                            "шансов на выживание у видов).\n" +
                            "Но не волнуйтесь, вода в озере – восполняемый параметр, а если быть точнее, то в программе предусмотрена логика \n" +
                            "дождей, и каждый 10-й день существования будет проверяться условие, пойдет ли дождь (шанс выпадения осадков – 50%).\n" +
                            "Если же дождь пошел, то уровень воды в озере поднимется на 40% от текущего уровня (уровень воды не сможет подняться " +
                            "больше 100 процентов).\n" +
                            " \n" +
                            "Также  в экосистему пользователь может добавить различные виды животных и растений, которым нужно будет задать параметры:\n" +
                            "Для растений: крупное/мелкое, кол-во, его наименование.\n" +
                            "Для животных: травоядное/хищное, крупное/мелкое, кол-во, его наименование.\n" +
                            "\n" +
                            "Растение будет существовать, пока уровень воды будет выше нуля, а также количество вида будет увеличиваться на одну \n" +
                            "единицу (+1) с каждым выпадением осадков. Частота выпадения осадков зависит от выбора климата: 1. Умеренный (осадки \n" +
                            "могут выпасть раз в 10 дней), 2. Тропический ( раз в 5 дней), 3. Экваториальный (раз в 15 дней).\n" +
                            "Для существования животных логика сложнее, чем у растений. Как указанно ранее животные делятся по классу питания \n" +
                            "и размеру. Размер животного влияет на его изначальное количество дней, которое оно может прожить без еды (мелкое: 2 дня; \n" +
                            "крупное: 4 дня), а также на уровень насыщаемости. Животное будет есть только в том случае, если оно голодно. Голодным \n" +
                            "животное становится, когда у него остается 1 день жизни. После оно начинает искать еду и питается в соответствии с пищевой \n" +
                            "цепью. Рассмотрим пищевую цепь:\n" +
                            "Крупный хищник питается: крупным травоядным (4), мелким травоядным (2), мелким хищником (2))\n" +
                            "Мелкий хищник питается: мелким травоядным (4)\n" +
                            "Крупное травоядное питается: крупным растением (6), мелким растением (1)\n" +
                            "Мелкое травоядное питается: крупным растением (9), мелким растением (3)\n" +
                            "P.s. в скобках указано сколько дней жизни получает животное, съев растение .\n" +
                            "\n" +
                            "Также в экосистеме предусмотрена возможность размножения животных, чтобы они могли размножится, нужно чтобы выполнялось \n" +
                            "следующее условие: животное поело и его количество больше единицы. \n");
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
            logToFile(String.valueOf(plant));
        }
        System.out.println("Животные:");
        for (Animal animal : animals) {
            System.out.println(animal);
            logToFile(String.valueOf(animal));
        }
    }

    private static void runEcosystem() {
        System.out.println("Введите количество дней существования экосистемы:");
        int days = Integer.parseInt(scanner.nextLine());
        chooseClimate();
        System.out.println("Выберите уровень воды в озере (1-100%):");
        double waterLevel = Double.parseDouble(scanner.nextLine()) / 100.0;

        for (int day = 0; day < days; day++) {
            if (waterLevel <= 0) {
                System.out.println("Уровень воды достиг нуля. Экосистема не может продолжать существование.");
                logToFile("Уровень воды достиг нуля. Экосистема не может продолжать существование.");
                break;
            }
            interactSpecies(day, animals, plants);
            waterLevel -= calculateWaterUsage(animals, plants);
            checkRain(day, waterLevel, plants);
            removeDeadSpecies();
        }
        System.out.println("На последний день существования экосистемы, ее состояние составило:");
        showEcosystemStatus();
    }
    private static void chooseClimate() {
        System.out.println("Выберите климат существования животных:");
        System.out.println("1. Умеренный");
        System.out.println("2. Тропический");
        System.out.println("3. Экваториальный");

        while (true) {
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                climateChoice = 1;
                rainCheckFrequency = 10; // Проверка на дождь раз в 10 дней
                break;
            } else if (choice.equals("2")) {
                climateChoice = 2;
                rainCheckFrequency = 5; // Проверка на дождь раз в 5 дней
                break;
            } else if (choice.equals("3")) {
                climateChoice = 3;
                rainCheckFrequency = 15; // Проверка на дождь каждые 15 дней
                break;
            } else {
                System.out.println("Некорректный ввод, попробуйте снова.");
            }
        }

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
        if (day % rainCheckFrequency == 0 && day != 0) {
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
                    plant.increaseQuantity(plant.getQuantity());
                    logToFile(plant.getName() +" + " + plant.getQuantity());
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
                           if (plant.getQuantity() - animal.getQuantity()<=0){
                               animal.increaseQuantity(plant.getQuantity());
                               plant.increaseQuantity(0);
                           }else {
                               plant.increaseQuantity(-animal.getQuantity());
                               animal.setRemainingLifeDays(3); // 3 дня жизни
                           }
                            fedAnimals.add(animal);
                            hasEaten = true;
                            logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ", кол-во: " + animal.getQuantity() + ") съел мелкое растение " + plant.getName() + "(осталось: " +plant.getQuantity()+"). Получено 3 дня жизни.");
                            break;
                        } else if (animal.getSize().equalsIgnoreCase("мелкое") && plant.getSize().equalsIgnoreCase("крупное")) {
                            if (plant.getQuantity() - animal.getQuantity()<=0){
                                animal.increaseQuantity(plant.getQuantity());
                                plant.increaseQuantity(0);
                            }else {
                                plant.increaseQuantity(-animal.getQuantity());
                                animal.setRemainingLifeDays(9); // 2 дня жизни за крупное растение
                            }
                            fedAnimals.add(animal);
                            hasEaten = true;
                            logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize()+ ", кол-во: " + animal.getQuantity() + ") съел крупное растение " + plant.getName() + "(осталось: " +plant.getQuantity()+").  Получено 9 дней жизни.");
                            break;
                        } else if (animal.getSize().equalsIgnoreCase("крупное")) {
                            if (plant.getSize().equalsIgnoreCase("мелкое")) {
                                if (plant.getQuantity() - animal.getQuantity()<=0){
                                    animal.increaseQuantity(plant.getQuantity());
                                    plant.increaseQuantity(0);
                                }else {
                                    plant.increaseQuantity(-animal.getQuantity());
                                    animal.setRemainingLifeDays(1); // 1 день жизни
                                }
                                fedAnimals.add(animal);
                                hasEaten = true;
                                logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ", кол-во: " + animal.getQuantity() + ") съел мелкое растение " + plant.getName() + "(осталось: " +plant.getQuantity()+"). Получено 1 день жизни.");
                                break;
                            } else if (plant.getSize().equalsIgnoreCase("крупное")) {
                                if (plant.getQuantity() - animal.getQuantity()<=0){
                                    animal.increaseQuantity(plant.getQuantity());
                                    plant.increaseQuantity(0);
                                }else {
                                    plant.increaseQuantity(-animal.getQuantity());
                                    animal.setRemainingLifeDays(6); // 9 дней жизни
                                }
                                fedAnimals.add(animal);
                                hasEaten = true;
                                logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize()+ ", кол-во: " + animal.getQuantity() + ") съел крупное растение " + plant.getName() + "(осталось: " +plant.getQuantity()+"). Получено 6 дней жизни.");
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
                            if (prey.getQuantity() - animal.getQuantity()<=0){
                                animal.increaseQuantity(prey.getQuantity());
                                prey.increaseQuantity(0);
                            }else {
                                prey.increaseQuantity(-animal.getQuantity());
                                animal.setRemainingLifeDays(4); // 4 дня жизни за мелкое травоядное
                            }
                            fedAnimals.add(animal);
                            hasEaten = true;
                            logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ", кол-во: " + animal.getQuantity() + ") съел мелкое травоядное " + prey.getName() + "(осталось: " + prey.getQuantity()+"). Получено 4 дня жизни.");
                            break;
                        } else if (animal.getSize().equalsIgnoreCase("крупное")) {
                            if (prey.getType().equalsIgnoreCase("травоядное")) {
                                if (prey.getSize().equalsIgnoreCase("крупное")) {
                                    if (prey.getQuantity() - animal.getQuantity()<=0){
                                        animal.increaseQuantity(prey.getQuantity());
                                        prey.increaseQuantity(0);
                                    }else {
                                        prey.increaseQuantity(-animal.getQuantity());
                                        animal.setRemainingLifeDays(4); // 4 дня жизни за крупное травоядное
                                    }
                                    fedAnimals.add(animal);
                                    hasEaten = true;
                                    logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ", кол-во: " + animal.getQuantity() + ") съел крупное травоядное " + prey.getName() + "(осталось: " + prey.getQuantity()+"). Получено 4 дня жизни.");
                                    break;
                                } else if (prey.getSize().equalsIgnoreCase("мелкое")) {
                                    if (prey.getQuantity() - animal.getQuantity()<=0){
                                        animal.increaseQuantity(prey.getQuantity());
                                        prey.increaseQuantity(0);
                                    }else {
                                        prey.increaseQuantity(-animal.getQuantity());
                                        animal.setRemainingLifeDays(2); // 2 дня жизни за мелкое травоядное
                                    }
                                    fedAnimals.add(animal);
                                    hasEaten = true;
                                    logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ", кол-во: " + animal.getQuantity() + ") съел мелкое травоядное " + prey.getName() + "(осталось: " + prey.getQuantity()+"). Получено 2 дня жизни.");
                                    break;
                                }
                            } else if (prey.getType().equalsIgnoreCase("хищное") && prey.getSize().equalsIgnoreCase("мелкое")) {
                                if (prey.getQuantity() - animal.getQuantity()<=0){
                                    animal.increaseQuantity(prey.getQuantity());
                                    prey.increaseQuantity(0);
                                }else {
                                    prey.increaseQuantity(-animal.getQuantity());
                                    animal.setRemainingLifeDays(2); // 2 дня жизни за мелкое хищное
                                }
                                fedAnimals.add(animal);
                                hasEaten = true;
                                logToFile(animal.getName() + " (тип: " + animal.getType() + ", размер: " + animal.getSize() + ", кол-во: " + animal.getQuantity() + ") съел мелкое хищное " + prey.getName() + "(осталось: " + prey.getQuantity()+"). Получено 2 дня жизни.");
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (Animal animal : animals) {
            if (!fedAnimals.contains(animal) && animal.getQuantity() > 0) {
                animal.decreaseLifeDays();
                logToFile(animal.getName() + " (кол-во: " + animal.getQuantity() +") не смог поесть и потерял день жизни.");
            }
        }
        // проверка, нужно ли удалить животное
        for (Animal animal : animals) {
            if (animal.getRemainingLifeDays() <= 0 && !animal.isLogged()) {
                toRemove.add(animal); // Добавляем в список на удаление
                logToFile("Животное "+ animal.getName() + " было удалено, так как закончились дни жизни.");
                animal.setLogged(true); // Устанавливаем флаг, что сообщение о смерти залогировано
            }

        }

        animals.removeAll(toRemove); // удаляем мертвых животных

        for (Animal animal : fedAnimals) {
            if (animal.getQuantity() > 1) {
                logToFile("Вид: " + animal.getName() + " размножился, +1");
                animal.increaseQuantity(+1);
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