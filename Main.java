import tba.APIFetcher;
import tui.TUICore;

public class Main {
    public static void main(String[] args) {
        APIFetcher.start(); // Fetch information from TBA

        while (TUICore.MAIN_MENU.exec() > 0) {} // Run the menu until stopped

        System.out.println("Thank you for using MADStrat, see you next time!");
    }
}