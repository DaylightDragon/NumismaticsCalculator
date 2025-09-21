package org.daylight.coinscalculator.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class CoinChangeLimited {

    public static class Result {
        boolean exact;
        int sum;
        int coins;
        Map<Integer, Integer> composition; // номинал → количество
        int overpay;

        public boolean isExact() {
            return exact;
        }

        public int getSum() {
            return sum;
        }

        public int getCoins() {
            return coins;
        }

        public int getOverpay() {
            return overpay;
        }

        public Map<Integer, Integer> getComposition() {
            return composition;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            int check = 0;
            sb.append(exact ? "Exact" : "Overpay").append(" sum: ").append(sum)
                    .append(", coins: ").append(coins).append("overpay: ").append(getOverpay()).append("\n");
            for (Map.Entry<Integer, Integer> e : composition.entrySet()) {
                check += e.getKey() * e.getValue();
                sb.append(e.getKey()).append(" x ").append(e.getValue()).append("\n");
            }
            sb.append("Checking total sum: ").append(check).append("\n");
            return sb.toString();
        }
    }

    public static Result solveInfinite(int target, int[] values) {
        Result res = new Result();
        res.exact = true;
        res.sum = target;
        res.coins = 0;
        res.composition = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i++) {
            if(values[i] <= 0) continue;
            int amount = target / values[i];
            res.composition.put(values[i], amount);
            target -= amount * values[i];
        }
        res.overpay = target;
        return res;
    }

    public static Result solveLimitedFast(int target, int[] values, int[] counts) {
//        System.out.println(target + " " + Arrays.toString(values) + " " + Arrays.toString(counts));
        int n = values.length;

        Result res = new Result();
        res.exact = false;
        res.sum = 0;
        res.coins = 0;
        res.overpay = target;

        res.composition = new LinkedHashMap<>();
        int sum = 0;
        int coins = 0;

        // Жадно забираем от больших к меньшим
        for (int i = 0; i < n; i++) {
            if (values[i] <= 0 || counts[i] <= 0) continue;
            int take = Math.min(counts[i], target / values[i]); // error here
            if (take > 0) {
                res.composition.put(values[i], take);
                target -= take * values[i];
                sum += take * values[i];
                coins += take;
            }
        }

        // Если всё закрыли — отлично
        if (target == 0) {
            res.exact = true;
            res.sum = sum;
            res.coins = coins;
            res.overpay = 0;
            return res;
        }

        // Не хватило: добавляем переплату (берём минимальные номиналы)
        for (int i = n - 1; i >= 0 && target > 0; i--) {
            if (counts[i] > res.composition.getOrDefault(values[i], 0)) {
                res.composition.merge(values[i], 1, Integer::sum);
                sum += values[i];
                coins++;
                target -= values[i]; // уйдём в минус (переплата)
            }
        }

        res.exact = (target <= 0);
        res.sum = sum;
        res.coins = coins;
        res.overpay = - target;
        return res;
    }

    public static void main(String[] args) {
        int target = 87;
        int[] values = {50, 20, 10, 5};
        int[] counts = {1, 2, 1, 1};

        Result res = solveLimitedFast(target, values, counts);
//        if (res != null) {
//            System.out.println(res);
//        } else {
//            System.out.println("No combination possible");
//        }
    }
}
