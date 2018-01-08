package navigator;

public class Main {

    public static void main(String[] args)
    {
        MachuPickchuNavigator nav = new MachuPickchuNavigator();

        char[][] map = {
                {'@', '.', '.'},
                {'#', '.', '#'},
                {'X', '.', '.'}
        };

        char[][] route = nav.searchRoute(map);

        printArrayInChar(route);
    }

    public static void printArrayInChar (char[][] array) {
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[i].length; ++j) {
                System.out.print(array[i][j]);
            }
            System.out.println();
        }
    }
}
