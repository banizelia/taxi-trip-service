//package com.web.temp;
//
//import com.web.export.TripExcelExporterPoi;
//import com.web.export.TripExcelExporterFastExcel;
//import com.web.repository.TripsRepository;
//import org.apache.commons.lang3.time.StopWatch;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//
//import java.util.Arrays;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.IntStream;
//
//@SpringBootApplication
//@ComponentScan(basePackages = "com.web")
//public class ExecutionTimeTester implements CommandLineRunner {
//    private static final Logger logger = LoggerFactory.getLogger(ExecutionTimeTester.class);
//    private static final int ITERATIONS = 10;
//    private static final int WARMUP_ITERATIONS = 2;
//    private static final boolean run = false;
//
//
//    @Autowired
//    private TripsRepository tripsRepository;
//
//    public static void main(String[] args) {
//        SpringApplication.run(ExecutionTimeTester.class, args);
//    }
//
//    @Override
//    public void run(String... args) {
//        logger.info("Starting performance comparison test...");
//
//        // Прогрев JVM
//        logger.info("Performing warmup iterations...");
//        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
//            TripExcelExporterPoi.tripsToExcel(tripsRepository);
//            TripExcelExporterFastExcel.tripsToExcel(tripsRepository);
//            System.gc(); // Подсказка GC выполнить очистку
//        }
//
//        // Тестирование Apache POI
//        long[] poiExecutionTimes = runTest("Apache POI",
//                () -> TripExcelExporterPoi.tripsToExcel(tripsRepository));
//
//        // Очистка памяти между тестами
//        System.gc();
//        try {
//            Thread.sleep(1000); // Даем время на очистку
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        // Тестирование FastExcel
//        long[] fastExcelExecutionTimes = runTest("FastExcel",
//                () -> TripExcelExporterFastExcel.tripsToExcel(tripsRepository));
//
//        // Вывод сравнительной статистики
//        printComparativeStats("Apache POI", poiExecutionTimes, "FastExcel", fastExcelExecutionTimes);
//    }
//
//    private long[] runTest(String implementationName, Runnable testMethod) {
//        logger.info("Testing {} implementation...", implementationName);
//        long[] executionTimes = new long[ITERATIONS];
//
//        IntStream.range(0, ITERATIONS).forEach(i -> {
//            logger.info("{}: Running test iteration {}", implementationName, i + 1);
//            StopWatch watch = StopWatch.createStarted();
//
//            try {
//                testMethod.run();
//                watch.stop();
//                executionTimes[i] = watch.getTime(TimeUnit.MILLISECONDS);
//                logger.info("{}: Iteration {} completed in {} ms",
//                        implementationName, i + 1, executionTimes[i]);
//            } catch (Exception e) {
//                logger.error("{}: Error in iteration {}: {}",
//                        implementationName, i + 1, e.getMessage(), e);
//                executionTimes[i] = -1;
//            }
//
//            // Очистка памяти после каждой итерации
//            System.gc();
//            try {
//                Thread.sleep(500); // Небольшая пауза между итерациями
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        });
//
//        return executionTimes;
//    }
//
//    private void printComparativeStats(String name1, long[] times1, String name2, long[] times2) {
//        logger.info("\nPerformance Comparison Results:");
//
//        // Статистика для первой реализации
//        printStats(name1, times1);
//
//        // Статистика для второй реализации
//        printStats(name2, times2);
//
//        // Сравнительный анализ
//        double avg1 = getAverage(times1);
//        double avg2 = getAverage(times2);
//        double speedup = (avg1 - avg2) / avg1 * 100.0;
//
//        logger.info("\nComparison:");
//        logger.info("Speed difference: {}{}%",
//                speedup > 0 ? "FastExcel is faster by " : "Apache POI is faster by ",
//                Math.abs(speedup));
//    }
//
//    private void printStats(String name, long[] times) {
//        double average = getAverage(times);
//        long min = getMin(times);
//        long max = getMax(times);
//        double stdDev = getStandardDeviation(times, average);
//
//        logger.info("\n{} Statistics:", name);
//        logger.info("Execution times: {}", Arrays.toString(times));
//        logger.info("Average execution time: {} ms", average);
//        logger.info("Min execution time: {} ms", min);
//        logger.info("Max execution time: {} ms", max);
//        logger.info("Standard deviation: {} ms", stdDev);
//    }
//
//    private double getAverage(long[] times) {
//        return Arrays.stream(times)
//                .filter(time -> time != -1)
//                .average()
//                .orElse(0.0);
//    }
//
//    private long getMin(long[] times) {
//        return Arrays.stream(times)
//                .filter(time -> time != -1)
//                .min()
//                .orElse(0);
//    }
//
//    private long getMax(long[] times) {
//        return Arrays.stream(times)
//                .filter(time -> time != -1)
//                .max()
//                .orElse(0);
//    }
//
//    private double getStandardDeviation(long[] times, double mean) {
//        return Math.sqrt(Arrays.stream(times)
//                .filter(time -> time != -1)
//                .mapToDouble(time -> time - mean)
//                .map(diff -> diff * diff)
//                .average()
//                .orElse(0.0));
//    }
//}