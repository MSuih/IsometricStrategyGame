package game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Level {
    List<List<MapSquare>> squares = new ArrayList<>();
    public Level() {
        squares.addAll(Stream.generate(ArrayList<MapSquare>::new).limit(30).collect(Collectors.toList()));
        squares.forEach((l) ->
            l.addAll(Stream.generate(MapSquare::new).limit(30).collect(Collectors.toList()))
        );
    }

    public boolean insideBounds(int x, int y) {
        return (x < squares.size()? y < squares.get(x).size(): false);
    }
    public MapSquare.SquareType getType(int x, int y) {
        return squares.get(x).get(y).getType();
    }
}
