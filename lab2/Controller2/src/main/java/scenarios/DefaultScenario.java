package scenarios;

import controllers.*;
import dao.KittenDao;
import dao.MistressDao;
import services.KittenService;
import services.MistressService;

import java.io.IOException;
import java.time.LocalDate;
import utils.Scanner;

public class DefaultScenario implements ScenarioInterface {
    private final Scanner scanner;
    private final KittenControllerInterface kittenController;
    private final MistressControllerInterface mistressController;

    public DefaultScenario() {
        this.scanner = new Scanner(System.in);
        this.kittenController = new KittenController(new KittenService(new KittenDao(), new MistressDao()));
        this.mistressController = new MistressController(new MistressService(new MistressDao(), new KittenDao()));
    }

    @Override
    public WhoAmI run() throws IOException {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Создать котенка");
        System.out.println("2. Создать госпожу");
        System.out.println("3. Стать котенком");
        System.out.println("4. Стать госпожой");
        System.out.println("5. Конец....");
        System.out.print("Введите номер операции: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> {
                System.out.println("Введите имя котенка");
                String name = scanner.nextString();
                System.out.println("Введите дату рождения котенка в формате yyyy-MM-dd");
                String date = scanner.nextString();
                LocalDate birthDate = LocalDate.parse(date);
                System.out.println("""
                        Введите породу котенка из вариантов: SIAMESE,
                            PERSIAN,
                            BENGAL,
                            BRITISH,
                            RUSSIAN_BLUE,
                            BIRMAN""");
                String breed = scanner.nextString();
                System.out.println("""
                        Введите цвет котенка из вариантов: BLACK,
                            WHITE,
                            ORANGE,
                            GRAY""");
                String colour = scanner.nextString();
                kittenController.createLonelyKitten(name, birthDate, breed, colour);
                return WhoAmI.USER;
            }
            case 2 -> {
                System.out.println("Введите имя госпожи");
                String mistressName = scanner.nextString();
                System.out.println("Введите дату рождения котенка в формате yyyy-MM-dd");
                String date = scanner.nextString();
                LocalDate birthDate = LocalDate.parse(date);
                mistressController.create(mistressName, birthDate);
                return WhoAmI.USER;
            }
            case 3 -> {
                System.out.println("Поздравляем, теперь вы котенок!");
                return WhoAmI.KITTEN;
            }
            case 4 -> {
                System.out.println("Поздравляем, теперь вы прекрасная госпожа котят!");
                return WhoAmI.MISTRESS;
            }
            case 5 -> {
                System.out.println("До свидания");
                System.exit(0);
            }
            default -> {
                System.out.println("Неверный выбор. Попробуйте снова.");
                return WhoAmI.USER;
            }
        }
        return WhoAmI.USER;
    }
}
