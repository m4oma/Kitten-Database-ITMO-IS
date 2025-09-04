package scenarios;

import controllers.KittenController;
import controllers.KittenControllerInterface;
import dao.KittenDao;
import dao.MistressDao;
import services.KittenService;

import java.io.IOException;
import utils.Scanner;

public class KittenScenario implements ScenarioInterface{
    private final Scanner scanner;
    private final KittenControllerInterface kittenController;

    public KittenScenario() {
        this.scanner = new Scanner(System.in);
        this.kittenController = new KittenController(new KittenService(new KittenDao(), new MistressDao()));
    }

    @Override
    public WhoAmI run() throws IOException {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Подружить котят");
        System.out.println("2. Разругать котят(");
        System.out.println("3. Влюбить котенка");
        System.out.println("4. Стать чловеком");
        System.out.println("5. Конец...");
        System.out.print("Введите номер операции: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> {
                System.out.println("Введите id первого друга");
                int kitten1Id = scanner.nextInt();
                System.out.println("Введите id второго друга");
                int kitten2Id = scanner.nextInt();
                kittenController.makeFriends(kitten1Id, kitten2Id);
            }
            case 2 -> {
                System.out.println("Введите id первого злого котенка");
                int kitten1Id = scanner.nextInt();
                System.out.println("Введите id второго злого котенка");
                int kitten2Id = scanner.nextInt();
                kittenController.breakFriendship(kitten1Id, kitten2Id);
            }
            case 3 -> {
                System.out.println("Введите id влюбленного котенка");
                int kittenId = scanner.nextInt();
                System.out.println("Введите id госпожи");
                int mistressId = scanner.nextInt();
                kittenController.breakFriendship(kittenId, mistressId);
            }
            case 4 -> {
                System.out.println("Вы больше не котенок(");
                return WhoAmI.USER;
            }
            case 5 -> {
                System.out.println("До свидания");
                System.exit(0);
            }
            default -> {
                System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
        return WhoAmI.KITTEN;
    }
}
