import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

class MultithreadCalculate extends Thread {
    int PrimeNumCalculate = -1;
    int indexNum = -1;

    public int getPrimeNumCalculate() {
        return PrimeNumCalculate;
    }

    public void setPrimeNumCalculate(int primeNumCalculate) {
        PrimeNumCalculate = primeNumCalculate;
    }

    public int getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }

    public void run() {
        try {
            boolean isPrime = true;
            long startTime = System.nanoTime();
            for (int i = 2; i < getPrimeNumCalculate(); i++) {
                if (getPrimeNumCalculate() % i == 0) {
                    isPrime = false;
                    MultiThreadPrimeNumGen.primeArray[0][getIndexNum()] = getPrimeNumCalculate();
                    MultiThreadPrimeNumGen.primeArray[1][getIndexNum()] = 0;
                    break;
                }
            }

            if (isPrime) {
                MultiThreadPrimeNumGen.primeArray[0][getIndexNum()] = getPrimeNumCalculate();
                MultiThreadPrimeNumGen.primeArray[1][getIndexNum()] = 1;
            }
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            MultiThreadPrimeNumGen.timeArray[getIndexNum()] = (timeElapsed/1000000);
            System.out.println("Thread " + Thread.currentThread().getId() + "; Index: " + getIndexNum() + "; Number: " + getPrimeNumCalculate() + "; isPrime: " + isPrime + "; Time To Calculate: " + (timeElapsed/1000000) + " ms");
        }
        catch (Exception e) {
            System.out.println("Exception is caught");
        }
    }
}

public class MultiThreadPrimeNumGen {
    public static int [][] primeArray;
    public static long [] timeArray;
    private static int primeBase = 1;
    private static int counterOfPrimes = 0;
    private static int cores;
    private static  File file = null;
    private static PrintWriter out = null;

    private static void fillArray() {
        for (int i = 0; i < cores; i++) {
            primeArray[0][i] = -1;
        }

        for (int i = 0; i < cores; i++) {
            primeArray[1][i] = -1;
        }

        for (int i = 0; i < cores; i++) {
            timeArray[i] = -1;
        }
    }

    private static void CalculatePrimeNumberInBatches() {
        while (true) {
            primeArray = new int[2][cores];
            timeArray = new long[cores];
            fillArray();
            for (int i = 0; i < cores; i++) {
                MultithreadCalculate multithreadCalculate = new MultithreadCalculate();
                multithreadCalculate.setPrimeNumCalculate(primeBase += 2);
                multithreadCalculate.setIndexNum(i);
                multithreadCalculate.start();
            }
            while (true) {
                boolean flag = false;
                for (int i = 0; i < cores; i++) {
                    if ((primeArray[1][i] == 0) || (primeArray[1][i] == 1)) {
                        flag = true;
                    } else {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
            PrintPrimes();
        }
    }

    private static void PrintPrimes() {
        //            printMatrix(primeArray);
        for (int i = 0; i < cores; i++) {
            if (primeArray[1][i] == 1) {
                out.println(++counterOfPrimes + ". Time to calculate: " + timeArray[i] + "; Prime Number: " + primeArray[0][i]);
            }
        }

        out.flush();
    }

    public static void main(String[] args) throws FileNotFoundException {
        file = new File(System.getProperty("user.home") + "/Desktop" + "/PrimeNumber.txt");
        if (file.isFile()) {
            if (file.length() != 0) {
                System.out.print("File Already Exit. Would you like to continue? <Yes> or <No>: ");
                Scanner in = new Scanner(System.in);
                String flag = in.nextLine();

                while (!(flag.equalsIgnoreCase("Yes") || flag.equalsIgnoreCase("No"))) {
                    System.out.println("INVALID ANSWER. TYPE YES OR NO!");
                    System.out.print("Enter 'Yes' or 'No': ");
                    flag = in.nextLine();
                }

                if (flag.equalsIgnoreCase("Yes")) {
                    ArrayList<String> tempReadArr = new ArrayList<>();
                    Scanner readFile = new Scanner(file);
                    while (readFile.hasNextLine()) {
                        String specificLine = readFile.nextLine();
                        tempReadArr.add(specificLine);
                    }

                    out = new PrintWriter(file);
                    for (int i = 0; i < tempReadArr.size(); i++) {
                        out.println(tempReadArr.get(i));
                        out.flush();
                    }

                    String[] splitString = tempReadArr.get(tempReadArr.size() - 1).split(" ");
                    counterOfPrimes = Integer.parseInt(splitString[0].substring(0, splitString[0].length() - 1));
                    primeBase = Integer.parseInt(splitString[7]);

                    for (int i = 0; i < splitString.length; i++) {
                        System.out.println(i + ". " + splitString[i]);
                    }
                } else {
                    out = new PrintWriter(file);
                    long startTime = System.nanoTime();
                    long endTime = System.nanoTime();
                    long timeElapsed = endTime - startTime;
                    out.println(++counterOfPrimes + ". Time to calculate: " + (timeElapsed/1000000) + "; Prime Number: " + 2);
                    out.flush();
                }
            } else {
                out = new PrintWriter(file);
                long startTime = System.nanoTime();
                long endTime = System.nanoTime();
                long timeElapsed = endTime - startTime;
                out.println(++counterOfPrimes + ". Time to calculate: " + (timeElapsed/1000000) + "; Prime Number: " + 2);
                out.flush();
            }
        } else {
            out = new PrintWriter(file);
            long startTime = System.nanoTime();
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            out.println(++counterOfPrimes + ". Time to calculate: " + (timeElapsed/1000000) + "; Prime Number: " + 2);
            out.flush();
        }

        cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of Cores: " + cores);
        CalculatePrimeNumberInBatches();
    }

    public static void printMatrix(int[][] arr) {
        if (null == arr || arr.length == 0) {
            return;
        }
        int idx = -1;
        StringBuilder[] sbArr = new StringBuilder[arr.length];
        for (int[] row : arr) {
            sbArr[++idx] = new StringBuilder("[\t");
            for (int elem : row) {
                sbArr[idx].append(elem).append("\t");
            }
            sbArr[idx].append("]");
        }
        for (StringBuilder stringBuilder : sbArr) {
            System.out.println(stringBuilder);
        }
    }
}
