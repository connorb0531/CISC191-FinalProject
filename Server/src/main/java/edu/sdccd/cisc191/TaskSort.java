package edu.sdccd.cisc191;

import java.util.ArrayList;
import java.util.List;

public class TaskSort {
    public static void quickSort(List<Task> tasks) {
        if (tasks == null || tasks.size() <= 1) {
            return;
        }
        quickSortRecursive(tasks, 0, tasks.size() - 1);
    }

    private static void quickSortRecursive(List<Task> tasks, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(tasks, low, high);
            quickSortRecursive(tasks, low, partitionIndex - 1);
            quickSortRecursive(tasks, partitionIndex + 1, high);
        }
    }

    private static int partition(List<Task> tasks, int low, int high) {
        Task pivot = tasks.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (!tasks.get(j).getDate().isAfter(pivot.getDate())) {
                i++;
                swap((ArrayList<Task>) tasks, i, j);
            }
        }

        swap((ArrayList<Task>) tasks, i + 1, high);
        return i + 1;
    }

    private static void swap(ArrayList<Task> tasks, int i, int j) {
        Task temp = tasks.get(i);
        tasks.set(i, tasks.get(j));
        tasks.set(j, temp);
    }
}
