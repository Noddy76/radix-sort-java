/*
 * Copyright © 2021 James Grant (james@queeg.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.queeg.sort.radix;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

public class RadixSortTest {
  static final Random r = new Random();
  static final int SIZE = 10_000_000;

  @Test
  public void test() throws Exception {
    System.out.println("Generating test data for " + SIZE + " elements.");
    Instant start = Instant.now();
    int a[] = new int[SIZE];
    for (int i = 0; i < SIZE; i++) {
      a[i] = r.nextInt();
    }
    Duration length = Duration.between(start, Instant.now());
    System.out.println("Generated in " + length);

    Duration total = Duration.ZERO;
    for (int l = 0; l < 100; l++) {
      System.out.print(String.format("Sort #%02d - ", l));
      start = Instant.now();
      RadixSort.sort(a);
      length = Duration.between(start, Instant.now());
      System.out.print(String.format("Sorted %d values in %6dms - ", SIZE, length.toMillis()));
      total = total.plus(length);
      for (int i = 1; i < SIZE; i++) {
        assertTrue(a[i - 1] <= a[i]);
      }
      System.out.println("Valid");
    }

    System.out.println(String.format("Average runtime %.2fms", total.toMillis() / 100.0));
  }

  @Test
  @Ignore
  public void compareTimes() throws Exception {
    for (int size : new int[] { 10, 100, 1000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000 }) {
      Duration libTime = Duration.ZERO;
      Duration radixTime = Duration.ZERO;
      for (int trial = 1; trial <= 10; trial++) {
        libTime = libTime.plus(timeArraysSort(size));
        radixTime = radixTime.plus(timeTest(size));
      }
      System.out.println(String.format("Size %10d : Arrays.sort : %10.3fms, Radix : %10.3fms", size,
          libTime.toNanos() / 10000000.0, radixTime.toNanos() / 10000000.0));
    }
  }

  public Duration timeArraysSort(int size) {
    int a[] = new int[size];
    for (int i = 0; i < size; i++) {
      a[i] = r.nextInt();
    }

    Instant start = Instant.now();
    Arrays.sort(a);
    Duration length = Duration.between(start, Instant.now());

    for (int i = 1; i < size; i++) {
      if (a[i - 1] > a[i]) {
        throw new IllegalStateException("result was not sorted");
      }
    }

    return length;
  }

  public Duration timeTest(int size) {
    int a[] = new int[size];
    for (int i = 0; i < size; i++) {
      a[i] = r.nextInt();
    }

    Instant start = Instant.now();
    RadixSort.sort(a);
    Duration length = Duration.between(start, Instant.now());

    for (int i = 1; i < size; i++) {
      if (a[i - 1] > a[i]) {
        throw new IllegalStateException("result was not sorted");
      }
    }

    return length;
  }
}
