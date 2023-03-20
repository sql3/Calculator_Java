package calculator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static Sign sign;
    static NumberFormat form = NumberFormat.NONE;
    static Map<String, Integer> alphabet = new LinkedHashMap<>();
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the expression:");
        String str = input.nextLine();
        System.out.println("Result:");
        System.out.println(calc(str));
    }

    private static void fillAlphabet() {
        alphabet.put("M", 1000);
        alphabet.put("CM", 900);
        alphabet.put("D", 500);
        alphabet.put("CD", 400);
        alphabet.put("C", 100);
        alphabet.put("XC", 90);
        alphabet.put("L", 50);
        alphabet.put("XL", 40);
        alphabet.put("X", 10);
        alphabet.put("IX", 9);
        alphabet.put("V", 5);
        alphabet.put("IV", 4);
        alphabet.put("I", 1);
    }

    private static String calc(String str) {
        String[] numbers = parser(str);
        if (form == NumberFormat.ARAB) {
            return compute(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1])) + "";
        } else {
            int first = romToArab(numbers[0]);
            int second = romToArab(numbers[1]);
            return arabToRom(compute(first, second));
        }
    }

    private static int compute(int first, int second) {
        return switch (sign) {
            case ADD -> first + second;
            case SUB -> first - second;
            case MULT -> first * second;
            default -> first / second;
        };
    }

    private static int romToArab(String arab) {
        char[] mas = arab.toCharArray();
        int s = 0;
        for (int i = 0; i < mas.length; i++) {
            if (i + 1 < mas.length) {
                if (alphabet.containsKey(mas[i] + "" + mas[i + 1])) {
                    s += alphabet.get(mas[i] + "" + mas[i + 1]);
                    i++;
                } else {
                    s += alphabet.get(mas[i] + "");
                }
            } else {
                s += alphabet.get(mas[i] + "");
            }
        }
        return s;
    }

    private static String arabToRom(int arab) {
        if (arab <= 0) {
            throw new IllegalArgumentException("Invalid input format");
        }
        StringBuilder sb = new StringBuilder();
        while (arab > 0) {
            for (Map.Entry<String, Integer> i : alphabet.entrySet()) {
                while (i.getValue() <= arab) {
                    arab -= i.getValue();
                    sb.append(i.getKey());
                }
            }
        }
        return sb.toString();
    }

    private static String[] parser(String str) {
        String[] mas = str.split(" ");
        if (mas.length != 3) {
            throw new IllegalArgumentException("Invalid input format");
        }
        sign = switch (mas[1]) {
            case "+" -> Sign.ADD;
            case "-" -> Sign.SUB;
            case "*" -> Sign.MULT;
            case "/" -> Sign.DIV;
            default -> throw new IllegalArgumentException("Invalid input format");
        };
        String[] numbers = new String[] {mas[0], mas[2]};
        for (String line : numbers) {
            char[] chMas = line.toCharArray();
            fillAlphabet();
            for (char ch : chMas) {
                if (((int)ch >= (int)'0') && ((int)ch <= (int)'9')) {
                    if (form == NumberFormat.ROM) {
                        throw new IllegalArgumentException("Invalid input format");
                    }
                    form = NumberFormat.ARAB;
                } else {
                    if (alphabet.containsKey(ch + "")) {
                        if (form == NumberFormat.ARAB) {
                            throw new IllegalArgumentException("Invalid input format");
                        }
                        form = NumberFormat.ROM;
                    } else {
                        throw new IllegalArgumentException("Invalid input format");
                    }
                }
            }
        }
        return numbers;
    }
}