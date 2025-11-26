package dev.httpmarco.polocloud.sdk.java.utils;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public final class FutureConverter {

    private static final ListeningExecutorService DIRECT_EXECUTOR =
            MoreExecutors.newDirectExecutorService();

    private FutureConverter() {
        // Utility class  no instantiation
    }

    public static <T, R> CompletableFuture<R> completableFromGuava(
            ListenableFuture<T> guavaFuture,
            Function<T, R> mapper
    ) {
        CompletableFuture<R> completable = new CompletableFuture<>();

        guavaFuture.addListener(() -> {
            try {
                T value = guavaFuture.get();
                R mapped = mapper.apply(value);
                completable.complete(mapped);
            } catch (ExecutionException | InterruptedException ex) {
                completable.completeExceptionally(ex);
            } catch (Exception ex) {
                completable.completeExceptionally(ex);
            }
        }, DIRECT_EXECUTOR);

        return completable;
    }

}
