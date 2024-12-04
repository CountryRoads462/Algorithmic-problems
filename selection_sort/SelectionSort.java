package selection_sort;

import java.util.Arrays;
import java.util.Scanner;

public class SelectionSort {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.print("enter amount of number: ");
            int amount = scanner.nextInt();

            int[] arr = new int[amount];
            System.out.printf("enter %d numbers:\n", amount);
            for (int i = 0; i < amount; i++) {
                arr[i] = scanner.nextInt();
            }

            System.out.println("before: " + Arrays.toString(arr));

            selectingSort(arr);

            System.out.println("after: " + Arrays.toString(arr));
        }
    }

    private static void selectingSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int min = arr[i];
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < min) {
                    min = arr[j];
                    minIndex = j;
                }
            }

            arr[minIndex] = arr[i];
            arr[i] = min;
        }
    }
}
