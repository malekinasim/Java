package org.example;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static int test1() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 2000; i++)
            numbers.add(i);
        int[] sum = new int[1];
        numbers.parallelStream().forEach(n -> {
            sum[0] += n;
        });
        return sum[0];
    }

    public static int test2() {
        int sum = 0;
        for (int i = 1; i <= 2000; i++)
            sum = sum + i;
        return sum;
    }


    public static int test3() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 2000; i++)
            numbers.add(i);
        AtomicInteger sum = new AtomicInteger(0);
        numbers.parallelStream().forEach(sum::addAndGet);
        return sum.get();
    }

    public static Stream<BigInteger> primes() {
        return Stream.iterate(TWO, BigInteger::nextProbablePrime);
    }

    public static void test4() {
        primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE)).filter(mersenne -> mersenne.isProbablePrime(50))
                .parallel()
                .limit(20)
                .forEach(System.out::println);

    }


    public static void test5() {

        primes()
                .map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
                .parallel()
                .takeWhile(mersenne -> mersenne.isProbablePrime(50))
                .forEach(System.out::println);

    }
    //    public static long pi4WithTakeWhile(long n) {
//        SplittableRandom splittableRandom = new SplittableRandom();
//        return Stream.generate(() -> BigInteger.valueOf(splittableRandom.nextLong(2, n + 1)))
//                //.parallel()
//                .distinct()
//                .takeWhile(i -> i.compareTo(BigInteger.valueOf(n)) <= 0) // تا زمانی که مقدار از n بیشتر نشود
//                .filter(i -> i.isProbablePrime(50))
//                .count();
//    }

    public static void test6() {
        // Create an array list for doubles.
        ArrayList<Integer> al = new ArrayList<>();

        // Add values to the array list.
        al.add(1);
        al.add(2);
        al.add(-3);
        al.add(-4);
        al.add(5);

        // Obtain a Stream to the array list.
        Stream<Integer> str = al.stream();

        // getting Spliterator object on al
        Spliterator<Integer> splitr1 = str.spliterator();

        // estimateSize method
        System.out.println("estimate size : " + splitr1.estimateSize());

        // getExactSizeIfKnown method
        System.out.println("exact size : " + splitr1.getExactSizeIfKnown());

        // hasCharacteristics and characteristics method
        System.out.println(splitr1.hasCharacteristics(splitr1.characteristics()));

        System.out.println("Content of arraylist :");
        // forEachRemaining method
        splitr1.forEachRemaining((n) -> System.out.println(n));

        // Obtaining another  Stream to the array list.
        Stream<Integer> str1 = al.stream();
        splitr1 = str1.spliterator();

        // trySplit() method
        Spliterator<Integer> splitr2 = splitr1.trySplit();

        // If splitr1 could be split, use splitr2 first.
        if (splitr2 != null) {
            System.out.println("Output from splitr2: ");
            splitr2.forEachRemaining((n) -> System.out.println(n));
        }

        // Now, use the splitr
        System.out.println("\nOutput from splitr1: ");
        splitr1.forEachRemaining((n) -> System.out.println(n));

    }

    public static long pi1(long n) {
        return LongStream.rangeClosed(2, n)
                .mapToObj(  BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50)).count();
    }

    public static long pi2(long n) {
        return LongStream.rangeClosed(2, n)
                .parallel()
                .mapToObj(  BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50)).count();
    }

    public static long pi3_limit(long n) {
        return Stream.generate(() -> BigInteger.valueOf(ThreadLocalRandom.current().nextLong(2, n + 1))) // تولید مستقیم اعداد تصادفی
                .parallel()
                .distinct()
                 .limit(n)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }
    public static long pi4_limit(long n) {
        SplittableRandom splittableRandom = new SplittableRandom();
        return Stream.generate(() -> BigInteger.valueOf(splittableRandom.nextLong(2, n + 1))) // تولید مستقیم اعداد تصادفی
                .parallel()
                .distinct()
                .limit(n)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }
    public static long pi3_hashset(long n) {
        Set<BigInteger> uniquePrimes = new HashSet<>();
        while (uniquePrimes.size() < n) { // تا زمانی که به n عدد یکتا برسیم
            BigInteger candidate = BigInteger.valueOf(ThreadLocalRandom.current().nextLong(2, n + 1));
            if (candidate.isProbablePrime(50)) {
                uniquePrimes.add(candidate);
            }
        }
        return uniquePrimes.size(); // تعداد اعداد اول منحصربه‌فرد
    }

    public static long pi4_hashset(long n) {
        SplittableRandom splittableRandom = new SplittableRandom();
        Set<BigInteger> uniquePrimes = new HashSet<>();
        while (uniquePrimes.size() < n) { // تا زمانی که به n عدد یکتا برسیم
            BigInteger candidate = BigInteger.valueOf(splittableRandom.nextLong(2, n + 1));
            if (candidate.isProbablePrime(50)) {
                uniquePrimes.add(candidate);
            }
        }
        return uniquePrimes.size(); // تعداد اعداد اول منحصربه‌فرد
    }
//
//
//    public static long pi3WithTakeWhile(long n) {
//        return Stream.generate(() -> BigInteger.valueOf(ThreadLocalRandom.current().nextLong(2, n + 1)))
//               // .parallel()
//                .distinct()
//                .takeWhile(i -> i.compareTo(BigInteger.valueOf(n)) <= 0) // تا زمانی که مقدار از n بیشتر نشود
//                .filter(i -> i.isProbablePrime(50))
//                .count();
//    }
//
//    public static long pi4WithTakeWhile(long n) {
//        SplittableRandom splittableRandom = new SplittableRandom();
//        return Stream.generate(() -> BigInteger.valueOf(splittableRandom.nextLong(2, n + 1)))
//                //.parallel()
//                .distinct()
//                .takeWhile(i -> i.compareTo(BigInteger.valueOf(n)) <= 0) // تا زمانی که مقدار از n بیشتر نشود
//                .filter(i -> i.isProbablePrime(50))
//                .count();
//    }


    public static long random1 (int bound) {
        Random random = new Random();
        return LongStream.range(0, bound)
                .mapToObj(i -> random.nextInt(100))
                .filter(n -> n % 2 == 0)
                .count();
    }
    public static long random2 (int bound) {
        Random random = new Random();

        return  LongStream.range(0, bound)
                .parallel()
                .mapToObj(i -> random.nextInt(100))
                .filter(n -> n % 2 == 0)
                .count();
    }
    public static long random3 (int bound) {
      return LongStream.range(0, bound)
                .parallel()
                .mapToObj(i -> ThreadLocalRandom.current().nextInt(100))
                .filter(n -> n % 2 == 0)
                .count();
    }


    public static long random4 (int bound) {
        SplittableRandom splittableRandom = new SplittableRandom();
       return LongStream.range(0, bound)
                .parallel()
                .mapToObj(i -> splittableRandom.nextInt(100))
                .filter(n -> n % 2 == 0)
                .count();
    }



    public static void main(String[] args) {
        // safety violation
         //System.out.println("Total sum: " + test1()+" :  "+test2()+" : "+test3());
       // Liveness Violations deadlock
     //   test4();
       // test5();
        //Spliterator sample
        //test6

        //generating pi became better  by parallel
        Instant start = Instant.now();
        System.out.format("pi_count by  stream and  ThreadLocalRandom from 2 to %d is %d" ,10_000_000,pi1(10_000_000));
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end).toMillis());

        start = Instant.now();
        System.out.format("pi_count by parallel stream and  splittableRandom from 2 to %d is %d" ,10_000_000,pi2(10_000_000));
        end = Instant.now();
        System.out.println(Duration.between(start, end).toMillis());


        //random
//        Instant start = Instant.now();
//        System.out.format(" random_count  by stream from 2 to %d is %d" ,10_000_000,random1(10_000_000));
//        Instant end = Instant.now();
//        System.out.println(Duration.between(start, end).toMillis());
//
//
//        start = Instant.now();
//        System.out.format(" random_count by parallel stream  from 2 to %d is %d" ,10_000_000,random2(10_000_000));
//        end = Instant.now();
//        System.out.println(Duration.between(start, end).toMillis());
//
//
//        start = Instant.now();
//        System.out.format(" rando_count by parallel stream and  ThreadLocalRandom from 2 to %d is %d" ,10_000_000,random3(10_000_000));
//        end = Instant.now();
//        System.out.println(Duration.between(start, end).toMillis());
//
//        start = Instant.now();
//        System.out.format(" random_count by parallel stream and  splittableRandom from 2 to %d is %d" ,10_000_000,random4(10_000_000));
//        end = Instant.now();
//        System.out.println(Duration.between(start, end).toMillis());


    }
}










