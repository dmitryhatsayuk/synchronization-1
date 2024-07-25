package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static int routesCount = 1000;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static Runnable logic = () -> {
        String string = generateRoute("RLRFR", 100);
        counter(string);
    };

    public static void main(String[] args) {

        try (ExecutorService threadPool = Executors.newFixedThreadPool(routesCount)) {
            for (int i = 0; i < routesCount; i++) {
                threadPool.submit(logic);
            }
            threadPool.shutdown();
        }
        mapPrinter(sizeToFreq);
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void counter(String string) {

        char[] charArray = string.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            int count = 0;
            if (charArray[i] == 'R') {
                int j = i;
                while (charArray[j] == 'R') {
                    count++;
                    j++;
                    if (j >= charArray.length) {
                        break;
                    }
                }
                i = j;
            }

            if (count > 0) {
                synchronized (sizeToFreq) {
                    sizeToFreq.merge(count, 1, Integer::sum);
                }
            }


        }
    }

    public static void mapPrinter(Map<Integer, Integer> map) {

        Optional<Map.Entry<Integer, Integer>> maxEntry = map.entrySet().stream().max(Map.Entry.comparingByValue());
        System.out.println("Самое частое количество повторений " + maxEntry.get().getValue() + " (встретилось " + maxEntry.get().getKey() + " раз)");
        System.out.println("Другие размеры:");
        map.remove(maxEntry.get().getKey());
        for (var entry : map.entrySet()) {
            System.out.println("-" + entry.getValue() + " (" + entry.getKey() + " раз)");
        }
    }
}




