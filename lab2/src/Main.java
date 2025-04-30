//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.Scanner;

public class Main {
    static Scanner s;

    public static void main(String[] args) {
        task1();
        task2();
        task3();
        task4();
        task5();
    }

    static void task1() {
        System.out.print("Задание 1\n");
        System.out.print("введите число: ");
        int n = s.nextInt();

        int count;
        for(count = 0; n != 1; ++count) {
            if (n % 2 == 0) {
                n /= 2;
            } else {
                n = 3 * n + 1;
            }
        }

        System.out.printf("число шагов: %d", count);
        System.out.print("\n");
    }

    static void task2() {
        System.out.print("Задание 2\n");
        System.out.print("введите число: ");
        int n = s.nextInt();
        int sum = 0;

        for(int i = 1; i <= n; ++i) {
            int num = s.nextInt();
            if (i % 2 == 0) {
                sum -= num;
            } else {
                sum += num;
            }
        }

        System.out.printf("сумма ряда: %d", sum, "\n");
        System.out.print("\n");
    }

    static void task3() {
        System.out.print("Задание 3\n");
        System.out.print("координата клада по X: ");
        int klad_x = s.nextInt();
        System.out.print("координата клада по Y: ");
        int klad_y = s.nextInt();
        int x = 0;
        int y = 0;
        int instruction = 0;
        boolean klad = false;

        while(true) {
            System.out.print("направление (север/юг/запад/восток) или 'стоп': ");
            String direction = s.next();
            if (direction.equals("стоп")) {
                break;
            }

            System.out.print("количество шагов: ");
            int steps = s.nextInt();
            ++instruction;
            switch (direction) {
                case "север":
                    y += steps;
                    break;
                case "юг":
                    y -= steps;
                    break;
                case "запад":
                    x -= steps;
                    break;
                case "восток":
                    x += steps;
                    break;
                default:
                    System.out.println("неверное направление");
                    continue;
            }

            if (x == klad_x && y == klad_y) {
                klad = true;
                break;
            }
        }

        if (klad) {
            System.out.println("найден! количество указаний: " + instruction);
        } else {
            System.out.println("не найден((");
        }

        System.out.print("\n");
    }

    static void task4() {
        System.out.print("Задание 4\n");
        System.out.print("количество дорог: ");
        int roads = s.nextInt();
        int best_road = 0;
        int height = 0;

        for(int i = 1; i <= roads; ++i) {
            System.out.print("количество туннелей на дороге " + i + ": ");
            int num = s.nextInt();
            int min_height = Integer.MAX_VALUE;

            for(int j = 0; j < num; ++j) {
                System.out.print("высота туннеля " + (j + 1) + ": ");
                int tunnel_height = s.nextInt();
                if (tunnel_height < min_height) {
                    min_height = tunnel_height;
                }
            }

            if (min_height > height) {
                height = min_height;
                best_road = i;
            }
        }

        System.out.println("номер дороги: " + best_road);
        System.out.println("максимальная высота: " + height);
        System.out.print("\n");
    }

    static void task5() {
        System.out.print("Задание 5\n");
        System.out.print("введите трехзначное число: ");
        int n = s.nextInt();
        int digit1 = n / 100;
        int digit2 = n / 10 % 10;
        int digit3 = n % 10;
        int sum = digit1 + digit2 + digit3;
        int product = digit1 * digit2 * digit3;
        if (sum % 2 == 0 && product % 2 == 0) {
            System.out.println("дважды четное");
        } else {
            System.out.println("не дважды четное");
        }

    }

    static {
        s = new Scanner(System.in);
    }
}
