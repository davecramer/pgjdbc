/*
 * Copyright (c) 2021, PostgreSQL Global Development Group
 * See the LICENSE file in the project root for more information.
 */

package org.postgresql.util.internal;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class LeakTracker<T> {

  public static final class LeakTraceHandle<T> extends PhantomReference<T> {
    @NonNull
    final Consumer<LeakTraceHandle<T>> action;

    LeakTraceHandle(T resource, ReferenceQueue<T> queue, @NonNull Consumer<LeakTraceHandle<T>> action) {
      super(resource, queue);
      this.action = action;
    }
  }

  private final ReferenceQueue<T> leakedResources = new ReferenceQueue<>();

  /**
   * We need to keep a strong reference to the {@link LeakTraceHandle} otherwise
   * it won't be enqueued to the reference queue.
   */
  private final Set<LeakTraceHandle<T>> resourcesInUse = ConcurrentHashMap.newKeySet();

  public LeakTraceHandle<T> register(T resource, Consumer<LeakTraceHandle<T>> action) {
    LeakTraceHandle<T> ref = new LeakTraceHandle<T>(resource, leakedResources, action);
    // We need to keep the strong reference to Phantom so it will be enqueued when resource leaks
    resourcesInUse.add(ref);
    return ref;
  }

  public void unregister(LeakTraceHandle<T> ref) {
    // If the user calls #close, we remove the strong reference to the PhantomReference,
    // so it won't be enqueued
    resourcesInUse.remove(ref);
  }

  public void processReferences() {
    Reference<? extends T> handle;
    while ((handle = leakedResources.poll()) != null) {
      //noinspection unchecked
      @SuppressWarnings("unchecked")
      final LeakTraceHandle<T> leakHandle = (LeakTraceHandle<T>) handle;
      leakHandle.action.accept(leakHandle);
      resourcesInUse.remove(leakHandle);
    }
  }
}
