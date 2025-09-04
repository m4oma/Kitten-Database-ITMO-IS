package scenarios;

import controllers.*;
import dao.KittenDao;
import dao.MistressDao;
import services.KittenService;
import services.MistressService;

import java.io.IOException;
import utils.Scanner;

public class MistressScenario implements ScenarioInterface {
    private final Scanner scanner;
    private final KittenControllerInterface kittenController;
    private final MistressControllerInterface mistressController;

    public MistressScenario() {
        this.scanner = new Scanner(System.in);
        this.kittenController = new KittenController(new KittenService(new KittenDao(), new MistressDao()));
        this.mistressController = new MistressController(new MistressService(new MistressDao(), new KittenDao()));
    }

    @Override
    public WhoAmI run() throws IOException {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Написать привет!");
        System.out.println("2. Стать обычным человеком");
        System.out.println("3. Конец....");
        System.out.print("Введите номер операции: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> {
                System.out.println("Всем котятам привет!");
            }
            case 2 -> {
                System.out.println("Вы больше не госпожа котят(");
                return WhoAmI.USER;
            }
            case 3 -> {
                System.out.println("До свидания");
                System.exit(0);
            }
            default -> {
                System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
        return WhoAmI.MISTRESS;
    }
}
