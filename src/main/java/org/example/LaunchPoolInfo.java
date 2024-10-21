package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaunchPoolInfo {
    private final String exchange;
    private final String pool;
    private final String period;
    private final String status;
    private final Map<String, String> pools;
    private static List<LaunchPoolInfo> launchPools = new ArrayList<>();
    private static int amount = 0;

    public static void readFile() {
        String exchange = "", pool = "", period = "", status = "";
        Map<String, String> pools = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
            for (String input = reader.readLine(); input != null; input = reader.readLine()){
                if (input.startsWith("exchange="))
                    exchange = input.substring("exchange=".length());
                else if (input.startsWith("pool="))
                    pool = input.substring("pool=".length());
                else if (input.startsWith("period="))
                    period = input.substring("period=".length());
                else if (input.startsWith("status="))
                    status = input.substring("status=".length());
                else if (input.startsWith("pools=")){
                    for (input = reader.readLine(); !input.equals("---"); input = reader.readLine()){
                        StringBuilder p = new StringBuilder();
                        StringBuilder v = new StringBuilder();
                        int i;
                        for (i = 0; input.charAt(i) != '='; i++)
                            p.append(input.charAt(i));
                        for (i++; i < input.length(); i++)
                            v.append(input.charAt(i));
                        pools.put(p.toString(), v.toString());
                    }
                    launchPools.add(new LaunchPoolInfo(exchange, pool, period, status, pools));
                    pools.clear();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<LaunchPoolInfo> getLaunchPools() {
        return launchPools;
    }

    public static int getAmount() {
        return amount;
    }

    private LaunchPoolInfo(String exchange, String pool, String period, String status, Map<String, String> pools) {
        this.exchange = exchange;
        this.pool = pool;
        this.period = period;
        this.status = status;
        this.pools = new HashMap<>(pools);
        amount++;
    }

    @Override
    public String toString() {
        StringBuilder lpools = new StringBuilder();
        for (Map.Entry<String, String> entry : pools.entrySet()) {
            lpools.append(entry.getKey()).append('=').append(entry.getValue()).append('\n');
        }

        return "exchange: " + exchange +
                "\npool: " + pool +
                "\nperiod: " + period +
                "\nstatus: " + status +
                "\npools: \n" + lpools +
                "---";

    }
}
