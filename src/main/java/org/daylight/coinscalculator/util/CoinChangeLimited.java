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
            int amount = target / values[i];
            res.composition.put(values[i], amount);
            target -= amount * values[i];
        }
        res.overpay = target;
        return res;
    }

    public static Result solveFast(int target, int[] values, int[] counts) {
        int n = values.length;
        Result res = new Result();
        res.composition = new LinkedHashMap<>();
        int sum = 0;
        int coins = 0;

        // Жадно забираем от больших к меньшим
        for (int i = 0; i < n; i++) {
            int take = Math.min(counts[i], target / values[i]);
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

    public static Result solveSlow(int target, int[] values, int[] counts) {
        int n = values.length;
        int maxSum = target;
        for (int i = 0; i < n; i++) {
            maxSum += values[i] * counts[i]; // допустимая верхняя граница для переплаты
        }

        // DP: dp[s] = минимальное число монет для суммы s, или INF
        int INF = Integer.MAX_VALUE / 2;
        int[] dp = new int[maxSum + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        // parent[s] = {предыдущая сумма, номинал, сколько монет добавили}
        class Parent {
            int prev, coin, qty;
            Parent(int p, int c, int q) { prev = p; coin = c; qty = q; }
        }
        Parent[] parent = new Parent[maxSum + 1];

        // Разложение монет по степеням двойки
        for (int i = 0; i < n; i++) {
            int value = values[i];
            int qty = counts[i];
            int power = 1;
            while (qty > 0) {
                int take = Math.min(power, qty);
                int weight = take * value;
                for (int s = maxSum; s >= weight; s--) {
                    if (dp[s - weight] + take < dp[s]) {
                        dp[s] = dp[s - weight] + take;
                        parent[s] = new Parent(s - weight, value, take);
                    }
                }
                qty -= take;
                power *= 2;
            }
        }

        // Ищем точное решение или минимальную переплату
        int bestSum = -1;
        for (int s = target; s <= maxSum; s++) {
            if (dp[s] < INF) {
                bestSum = s;
                break;
            }
        }

        if (bestSum == -1) return null; // даже с переплатой не собрали (маловероятно)

        // Восстанавливаем состав монет
        Map<Integer, Integer> comp = new LinkedHashMap<>();
        int cur = bestSum;
        while (cur > 0) {
            Parent p = parent[cur];
            comp.merge(p.coin, p.qty, Integer::sum);
            cur = p.prev;
        }

        Result result = new Result();
        result.exact = (bestSum == target);
        result.sum = bestSum;
        result.coins = dp[bestSum];
        result.composition = comp;
        result.overpay = target - bestSum;
        return result;
    }

    // Тест
    public static void main(String[] args) {
        int target = 87;
        int[] values = {50, 20, 10, 5};
        int[] counts = {1, 2, 1, 1};

        Result res = solveFast(target, values, counts);
        if (res != null) {
            System.out.println(res);
        } else {
            System.out.println("No combination possible");
        }
    }
}
