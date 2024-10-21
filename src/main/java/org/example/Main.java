package org.example;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        LaunchPoolInfo.readFile();
        for (LaunchPoolInfo launchPool : LaunchPoolInfo.getLaunchPools()) {
            System.out.println(launchPool.toString());
        }
        System.out.println(LaunchPoolInfo.getAmount());
    }
}