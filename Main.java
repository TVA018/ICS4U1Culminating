import tba.APIFetcher;
import tui.Menus;

public class Main {
    public static void main(String[] args) {
        APIFetcher.start();

        while (Menus.MAIN_MENU.exec() > 0) {}
    }
}