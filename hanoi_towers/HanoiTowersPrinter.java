package hanoi_towers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringJoiner;

public class HanoiTowersPrinter {

    private final List<Stack<Disk>> pegs;
    private final int pegsHeight;
    private final int lengthOfPicture;

    public HanoiTowersPrinter(int numberOfPegs, int numberOfDisks) {
        pegs = createList(numberOfPegs);

        pegsHeight = numberOfDisks + 2;
        lengthOfPicture = numberOfDisks * 2 - 1;

        Stack<Disk> firstPeg = new Stack<>();
        for (; numberOfDisks != 0; numberOfDisks--) {
            firstPeg.add(new Disk(numberOfDisks));
        }

        pegs.set(0, firstPeg);
    }

    private List<Stack<Disk>> createList(int numberOfPegs) {
        List<Stack<Disk>> result = new ArrayList<>();
        for (int i = 1; i <= numberOfPegs; i++) {
            result.add(new Stack<>());
        }
        return result;
    }

    public void moveDisk(int fromPeg, int toPeg) {
        Disk disk = pegs.get(fromPeg - 1).pop();
        pegs.get(toPeg - 1).add(disk);
    }

    public void print() {
        List<int[][]> pictureInMatrixFormatList = new ArrayList<>();

        for (Stack<Disk> peg : pegs) {
            int[][] pictureMatrix = new int[pegsHeight][lengthOfPicture];

            int centerIndex = (pictureMatrix[0].length - 1) / 2;

            int i = 0;
            for (; i < pegsHeight - peg.size(); i++) {
                pictureMatrix[i][centerIndex] = Cell.PART_OF_PEG.code;
            }

            for (Disk disk : peg.reversed()) {
                for (int j = centerIndex - (disk.length - 1); j <= centerIndex + (disk.length - 1); j++) {
                    pictureMatrix[i][j] = Cell.PART_OF_DISK.code;
                }
                i++;
            }

            pictureInMatrixFormatList.add(pictureMatrix);
        }

        String picture = buildPicture(pictureInMatrixFormatList);
        System.out.println(picture);
    }

    public void printMove(int fromPeg, int toPeg) {
        int fromIndex = calculateIndex(fromPeg);
        int toIndex = calculateIndex(toPeg);

        int[][] directionMatrix = new int[3][lengthOfPicture * pegs.size() + pegs.size() - 1];

        int numberOfPeg = 1;

        for (int i = 0; i < directionMatrix[1].length; i++) {
            if (i == fromIndex || i == toIndex) {
                if (i == fromIndex) {
                    directionMatrix[0][i] = fromIndex < toIndex ? Cell.LEFT_CORNER.code : Cell.RIGHT_CORNER.code;
                    directionMatrix[1][i] = Cell.VERTICAL_LINE.code;
                } else {
                    directionMatrix[0][i] = fromIndex < toIndex ? Cell.RIGHT_CORNER.code : Cell.LEFT_CORNER.code;
                    directionMatrix[1][i] = Cell.ARROW_DOWN.code;
                }

            } else if (inRangeExcludeBorders(i, fromIndex, toIndex)) {
                directionMatrix[0][i] = Cell.HORIZONTAL_LINE.code;
            }

            if (i == calculateIndex(numberOfPeg)) {
                directionMatrix[2][i] = numberOfPeg;

                numberOfPeg++;
            }
        }

        String directionPicture = buildDirectionPicture(directionMatrix);
        System.out.println(directionPicture);
    }

    private boolean inRangeExcludeBorders(int i, int fromIndex, int toIndex) {
        if (fromIndex < toIndex) {
            return i > fromIndex && i < toIndex;
        } else {
            return i > toIndex && i < fromIndex;
        }
    }

    private int calculateIndex(int numberOfPeg) {
        int numberOfDisks = pegsHeight - 2;
        return (numberOfDisks - 1) + (numberOfPeg - 1) * numberOfDisks * 2;
    }

    private String buildDirectionPicture(int[][] directionMatrix) {
        StringJoiner sj = new StringJoiner("\n", "", "");
        for (int[] matrix : directionMatrix) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < directionMatrix[0].length; i++) {
                Character symbol = Cell.defineSymbol(matrix[i]);
                sb.append(symbol);
            }
            sj.add(sb);
        }

        return sj.toString();
    }

    private String buildPicture(List<int[][]> pictureInMatrixFormatList) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < pegsHeight; row++) {
            for (int[][] pictureMatrix : pictureInMatrixFormatList) {
                for (int j = 0; j < pictureMatrix[0].length; j++) {
                    Character symbol = Cell.defineSymbol(pictureMatrix[row][j]);
                    sb.append(symbol);
                }
                sb.append(' ');
            }
            sb.append("\n");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private record Disk(
            int length
    ) {
    }

    private enum Cell {
        EMPTY(' ', 0),
        PART_OF_DISK('█', -1),
        PART_OF_PEG('|', -2),
        LEFT_CORNER('┏', -3),
        RIGHT_CORNER('┓', -4),
        VERTICAL_LINE('┃', -5),
        ARROW_DOWN('↓', -6),
        HORIZONTAL_LINE('━', -7);

        final Character symbol;
        final int code;

        Cell(Character symbol, int code) {
            this.symbol = symbol;
            this.code = code;
        }

        static char defineSymbol(int code) {
            for (Cell cell : Cell.values()) {
                if (cell.code == code) {
                    return cell.symbol;
                }
            }

            return String.valueOf(code).charAt(0);
        }
    }
}
