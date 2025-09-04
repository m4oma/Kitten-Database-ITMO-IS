import scenarios.*;

public class Main {
    public static void main(String[] args) {
        WhoAmI me = WhoAmI.USER;
        while (true) {
            if (me == WhoAmI.USER) {
                ScenarioInterface scenario = new DefaultScenario();
                try {
                    me = scenario.run();
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (me == WhoAmI.KITTEN) {
                ScenarioInterface scenario = new KittenScenario();
                try {
                    me = scenario.run();
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                ScenarioInterface scenario = new MistressScenario();
                try {
                    me = scenario.run();
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
