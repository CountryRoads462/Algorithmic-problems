package hanoi_towers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HanoiTowers {

    private static int TOTAL_NUMBER_OF_STEPS = 0;
    private static int pegs;
    private static HanoiTowersPrinter hanoiTowersPrinter;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("enter number of disks: ");
            int disks = scanner.nextInt();
            if (disks < 0) {
                System.out.println("\nnumber of disks has to be positive number!");
                continue;
            }

            System.out.print("enter number of pegs: ");
            pegs = scanner.nextInt();
            if (pegs < 3) {
                System.out.println("\nnumber of pegs has to be more than 2!");
                continue;
            }

            hanoiTowersPrinter = new HanoiTowersPrinter(pegs, disks);
            System.out.println();
            hanoiTowersPrinter.print();
            System.out.println();

            hanoiTowers(disks, 1, pegs, new HashSet<>(Collections.emptySet()));
            System.out.println("Total number of steps = " + TOTAL_NUMBER_OF_STEPS + "\n");
            TOTAL_NUMBER_OF_STEPS = 0;
        }
    }

    private static void hanoiTowers(int disks, int fromPeg, int toPeg, HashSet<Integer> usedPegs) {
        Optional<Integer> unusedPegIf3 = if3PegsIsFreeChooseUnusedPeg(fromPeg, toPeg, usedPegs);

        if (unusedPegIf3.isPresent()) {
            int unusedPeg = unusedPegIf3.get();
            hanoiTowers3Pegs(disks, fromPeg, toPeg, unusedPeg);

        } else {
            hanoiTowers4PegsAndMore(disks, fromPeg, toPeg, usedPegs);
        }
    }

    private static Optional<Integer> if3PegsIsFreeChooseUnusedPeg(int fromPeg, int toPeg, HashSet<Integer> usedPegs) {
        Set<Integer> integerHashSet = IntStream.rangeClosed(1, pegs)
                .boxed()
                .collect(Collectors.toCollection(HashSet::new));

        integerHashSet.remove(fromPeg);
        integerHashSet.remove(toPeg);
        integerHashSet.removeAll(usedPegs);

        if (integerHashSet.size() == 1) {
            return integerHashSet.stream().findFirst();
        }

        return Optional.empty();
    }

    private static void hanoiTowers3Pegs(int disks, int fromPeg, int toPeg, int unusedPeg) {
        if (disks == 1) {
            moveDisk(fromPeg, toPeg);
            return;
        }

        hanoiTowers3Pegs(disks - 1, fromPeg, unusedPeg, toPeg);

        moveDisk(fromPeg, toPeg);

        hanoiTowers3Pegs(disks - 1, unusedPeg, toPeg, fromPeg);
    }

    private static void hanoiTowers4PegsAndMore(int disks, int fromPeg, int toPeg, HashSet<Integer> usedPegs) {
        if (disks == 1) {
            moveDisk(fromPeg, toPeg);
            return;
        }

        int k = calculateK(disks, pegs - usedPegs.size());

        int i = tryToChooseI(fromPeg, toPeg, usedPegs).orElseThrow(() -> new RuntimeException("Unexpected behaviour!\n"));

        hanoiTowers(k, fromPeg, i, new HashSet<>(usedPegs));

        usedPegs.add(i);

        hanoiTowers(disks - k, fromPeg, toPeg, new HashSet<>(usedPegs));

        hanoiTowers(k, i, toPeg, new HashSet<>(Collections.emptySet()));
    }

    private static void moveDisk(int fromPeg, int toPeg) {
        hanoiTowersPrinter.moveDisk(fromPeg, toPeg);
        hanoiTowersPrinter.printMove(fromPeg, toPeg);

        hanoiTowersPrinter.print();
        System.out.printf("Move disk from %d to %d\n\n", fromPeg, toPeg);

        TOTAL_NUMBER_OF_STEPS++;
    }

    private static int calculateK(int disks, int pegs) {
        return Math.max(1, disks - nearestInt(((double) (pegs - 2) / 2) * Math.sqrt(2 * disks + 1)) + pegs - 3);
    }

    private static int nearestInt(double value) {
        return (int) (value + 0.5);
    }

    private static Optional<Integer> tryToChooseI(int fromPeg, int toPeg, Set<Integer> usedPegs) {
        for (int i = 1; i <= pegs; i++) {
            if (i != fromPeg && i != toPeg && !usedPegs.contains(i)) {
                return Optional.of(i);
            }
        }

        return Optional.empty();
    }
}
