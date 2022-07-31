package byow.lab12;

import org.junit.Test;

import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 52;
    private static final int HEIGHT = 60;
    private static final Random RANDOM = new Random(69420);

    private static class Position {
        private int x;
        private int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static int hexShift(int s, int i) {
        if (s <= i) {
            i = 2 * s - 1 - i;
        }
        return -i;
    }

    public static int width(int s, int i) {
        if (s <= i) {
            i = 2 * s - i - 1;
        }
        return s + 2 * i;
    }

    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        if (s < 2) {
            throw new IllegalArgumentException("hexagon must be at least size 2");
        }
        for (int i = 0; i < 2 * s; i+=1) {
            int y = p.y + i;
            int x = p.x + hexShift(s, i);
            int width = width(s, i);
            Position newP = new Position(x, y);
            for (int j = 0; j < width; j+=1) {
                world[newP.x + j][newP.y] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
            }
        }
    }

    public static TETile randomTile() {
        int tileNum = RANDOM.nextInt(10);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.WATER;
            case 3: return Tileset.UNLOCKED_DOOR;
            case 4: return Tileset.LOCKED_DOOR;
            case 5: return Tileset.TREE;
            case 6: return Tileset.SAND;
            case 7: return Tileset.MOUNTAIN;
            case 8: return Tileset.GRASS;
            case 9: return Tileset.AVATAR;
            default: return Tileset.NOTHING;
        }
    }

    public static void addTile(TETile[][] world, Position p, int s, int t) {
        Position p2 = new Position(p.x, p.y);
        TETile t2 = randomTile();
        for (int i = 0; i < t; i++) {
            p2.y += 2 * s;
            addHexagon(world, p2, s, t2);
            t2 = randomTile();
        }
    }

    public static Position downShift(Position p, int s) {
        Position newP = new Position(p.x, p.y);
        newP.x += 2 * s - 1;
        newP.y += s;
        return newP;
    }

    public static Position upShift(Position p, int s) {
        Position newP = new Position(p.x, p.y);
        newP.x += 2 * s - 1;
        newP.y -= s;
        return newP;
    }
    public static void draw(TETile[][] hex, int s) {
        Position p = new Position(s, 2 * s);
        addTile(hex, p, s, 3);
        p = upShift(p, s);
        addTile(hex, p, s, 4);
        p = upShift(p, s);
        addTile(hex, p, s, 5);
        p = downShift(p, s);
        addTile(hex, p, s, 4);
        p = downShift(p, s);
        addTile(hex, p, s, 3);
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] hex = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                hex[x][y] = Tileset.NOTHING;
            }
        }
        draw(hex, 5);
        ter.renderFrame(hex);

    }

}