package navigator;

import java.util.ArrayList;
import javafx.util.Pair;

public class MachuPickchuNavigator implements Navigator
{
    private final int roadCell    = -1;           // . (точка) дорога
    private final int barrierCell = -2;           // # (решетка) стена
    private final int startCell   =  0;           // @ (собака) старт
    private final int finishCell  = -3;           // X (заглавный икс) финиш

    private char[][] mapInChar;

    private ArrayList<Pair<Integer, Integer>> queue; // позиции граничных элементов волны для обхода
    private ArrayList<Pair<Integer, Integer>> temp; // позиции новых граничных элементов волны

    public MachuPickchuNavigator() {
        queue = new ArrayList<>();
        temp = new ArrayList<>();
    }

    /**
     * Поиск кратчайшего маршрута на карте города между двумя точками
     * @param map карта города
     * @return карта города с построенным маршрутом
     */
    public char[][] searchRoute (char[][] map)
    {
        if (map.length > 10000 || map[0].length > 10000) return null; // размер карты превышает допустимые

        mapInChar = map;
        int[][] mapInInts = toIntMap(map);
        if (mapInInts == null) return null; // на карте найдены неверные символы
        if (queue.size() != 1) return null; // количество начальных точек не соответствует ожиданиям

        boolean success = false;

        for (int d = 0; ; ++d) // d - количество шагов, на которые распространилась волна
        {
            if(queue.isEmpty()) return null; // случай, когда волна зашла в тупик

            for (int i = 0; i < queue.size(); ++i)
            {
                Pair<Integer,Integer> point = queue.get(i);

                if (determineAroundPositions(mapInInts, point.getKey(), point.getValue() - 1, d)) // верхняя точка окресности
                {
                    success = true;
                    break;
                }

                if (determineAroundPositions(mapInInts, point.getKey() + 1, point.getValue(), d)) // правая точка окресности
                {
                    success = true;
                    break;
                }

                if (determineAroundPositions(mapInInts, point.getKey(), point.getValue() + 1, d)) // нижняя точка окресности
                {
                    success = true;
                    break;
                }

                if (determineAroundPositions(mapInInts, point.getKey() - 1, point.getValue(), d)) // левая точка окресности
                {
                    success = true;
                    break;
                }
            }

            if (success) break; //

            // Формирование новой очереди
            queue.clear();
            queue.addAll(temp);
            temp.clear();
        }

        Pair<Integer,Integer> finish = temp.get(temp.size() - 1);
        int i = finish.getKey();
        int j = finish.getValue();

        for (int d = mapInInts[i][j] - 1; d > 0; --d)
        {
            if (j > 0)
                if (mapInInts[i][j - 1] == d) {
                    mapInChar[i][--j] = '+';
                    continue;
                }

            if (i > 0)
                if (mapInInts[i - 1][j] == d) {
                    mapInChar[--i][j] = '+';
                    continue;
                }

            if (i < map.length -1)
                if (mapInInts[i + 1][j] == d) {
                    mapInChar[++i][j] = '+';
                    continue;
                }

            if (j < map[0].length - 1)
                if (mapInInts[i][j + 1] == d) {
                    mapInChar[i][++j] = '+';
                    continue;
                }
        }

        return mapInChar;
    }



    /**
     * Преобразование символьной карты в целочисленую
     * @param map символьная карта города
     * @return карта города с использованием целочисленных обозначений
     */
    private int[][] toIntMap (char[][] map)
    {
        int[][] out = new int[map.length][map[0].length];

        for (int i = 0; i < map.length; ++i)
        {
            for (int j = 0; j < map[i].length; ++j)
            {
                switch (map[i][j])
                {
                    case '.': {
                        out[i][j] = roadCell;
                        break;
                    }
                    case '#': {
                        out[i][j] = barrierCell;
                        break;
                    }
                    case '@': {
                        out[i][j] = startCell;
                        queue.add(new Pair<>(i, j)); // запоминаем координаты стартовой вершины
                        break;
                    }
                    case 'X': {
                        out[i][j] = finishCell;
                        break;
                    }
                    default: return null; // на карте находятся посторонние символы
                }
            }
        }
        return out;
    }

    /**
     * Обработка позиции в окресности фон Неймана
     * @param map целочисленная карта
     * @param i позиция столбца
     * @param j позиция строки
     * @param d текущее количество шагов до старта
     * @return найдена ли конечная позиция (готовность к построению маршрута)
     */
    private boolean determineAroundPositions(int[][] map, int i, int j, int d)
    {
        if (i < 0 || j < 0 || i > map.length -1 || j > map[0].length - 1) return false;

        boolean result = false;

        switch (map[i][j])
        {
            case finishCell: {
                result = true;
            }
            case roadCell: {
                map[i][j] = d + 1;
                temp.add(new Pair<>(i, j));
            }
        }
        return result;
    }
}
