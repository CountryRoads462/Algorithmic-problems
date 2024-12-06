package merging_sorted_sequences;

import java.util.*;

public class MergingSortedSequences {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("enter number of sorted sequences: ");
            int numberOfSequences = Integer.parseInt(scanner.nextLine());

            List<Deque<Integer>> sortedSequences = new ArrayList<>();
            System.out.printf("enter %d sorted sequences:\n", numberOfSequences);
            for (int i = 0; i < numberOfSequences; i++) {
                Deque<Integer> sequence = getDequeFromInput(scanner.nextLine());
                sortedSequences.add(sequence);
            }

            List<Integer> mergedList = merge(sortedSequences);

            System.out.println("merged list: " + mergedList);
        }
    }

    private static Deque<Integer> getDequeFromInput(String input) {
        return new ArrayDeque<>(Arrays.stream(input.split(" "))
                .map(Integer::parseInt)
                .toList());
    }

    private static List<Integer> merge(List<Deque<Integer>> sortedSequences) {
        List<Integer> mergedList = new ArrayList<>();

        while (!sortedSequences.isEmpty()) {
            int smallestElem = popSmallestElem(sortedSequences);
            mergedList.add(smallestElem);
        }

        return mergedList;
    }

    private static int popSmallestElem(List<Deque<Integer>> sortedSequences) {
        int min = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < sortedSequences.size(); i++) {
            if (sortedSequences.get(i).getFirst() < min) {
                min = sortedSequences.get(i).getFirst();
                minIndex = i;
            }
        }

        sortedSequences.get(minIndex).removeFirst();
        if (sortedSequences.get(minIndex).isEmpty()) {
            sortedSequences.remove(minIndex);
        }

        return min;
    }
}
