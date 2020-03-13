package caphe.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.NonNull;

public class ExecutorUtils {

    /**
     * Ref
     * https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorCompletionService.html
     *
     * @param <T>
     * @param callables
     * @param numberOfThreads
     * @return <T>
     */
    public static <T> T executeUsingFutureForFirstReturnValue(@NonNull List<Callable<T>> callables,
            @NonNull Integer numberOfThreads) {
        CompletionService<T> completionService = new ExecutorCompletionService<T>(
                Executors.newFixedThreadPool(numberOfThreads));
        int n = callables.size();
        List<Future<T>> futures = new ArrayList<Future<T>>(n);
        try {
            for (Callable<T> callable : callables) {
                futures.add(completionService.submit(callable));
            }
            for (int i = 0; i < n; ++i) {
                T result = completionService.take().get();
                if (result != null) {
                    return result;
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            for (Future<T> f : futures) {
                f.cancel(true);
            }
        }
        return null;
    }

    /**
     * Execute all callables and get a list of result
     *
     * @param <T>
     * @param callables
     * @param numberOfThreads
     * @return List<T>
     */
    public static <T> List<T> executeUsingFutureForAllReturnValue(@NonNull List<Callable<T>> callables,
            @NonNull Integer numberOfThreads) {
        CompletionService<T> completionService = new ExecutorCompletionService<T>(
                Executors.newFixedThreadPool(numberOfThreads));
        int n = callables.size();
        List<T> results = new ArrayList<>();
        List<Future<T>> futures = new ArrayList<Future<T>>(n);
        try {
            for (Callable<T> callable : callables) {
                futures.add(completionService.submit(callable));
            }
            for (int i = 0; i < n; ++i) {
                T result = completionService.take().get();
                results.add(result);
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
}
