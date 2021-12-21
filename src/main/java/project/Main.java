package project;

import project.model.Game;
import project.services.GameService;
import project.services.LogService;
import project.services.serialize.SerializeService;

import java.util.Scanner;

public class Main {
    private static final String[] menu = {"quit", "show next turn", "show field", "save game", "load game", "new game"};

    public static void main(String[] args) {
        consoleMenu();
    }

    private static String getMenu() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < menu.length + 1; i++) {
            sb.append(i).append(" - ").append(getMenu(i)).append("\n");
        }
        return sb.toString();
    }

    private static String getMenu(int a) {
        return a < menu.length ? menu[a] : menu[0];
    }

    public static void consoleMenu() {
        Scanner scn = new Scanner(System.in);
        GameService gs = new GameService();
        SerializeService ss = new SerializeService();
        LogService ls = new LogService();
        int a;
        System.out.println("1 - load game\n" +
                "2 - new game");
        a = scn.nextInt();
        ls.info("User input: " + a + " (" + getMenu(a + 3) + ")");
        Game g;
        if (a == 2) {
            g = ss.deserialize();
            if (g == null) {
                g = new Game();
                ls.error("Loading failed! New game started");
            } else {
                ls.info("Game load");
            }
        } else {
            g = new Game();
            ls.info("New game started");
        }
        ls.info(gs.printField(g));
        do {
            System.out.println(getMenu());
            a = scn.nextInt();
            ls.info("User input: " + a + " (" + getMenu(a) + ")");
            switch (a) {
                case 1:
                    ls.info(gs.nextTurn(g));
                    break;
                case 2:
                    ls.info(gs.printField(g));
                    break;
                case 3:
                    ss.serialize(g);
                    ls.info("Game saved");
                    break;
                case 4:
                    g = ss.deserialize();
                    ls.info(gs.printField(g));
                    ls.info("Game load");
                    break;
                case 5:
                    g = new Game();
                    System.out.println();
                    ls.info("New game started");
                    break;
                default:
                    a = 0;
                    break;
            }
            if (gs.checkWin(g) != null) {
                a = 0;
            }
        } while (a != 0);
    }
}
