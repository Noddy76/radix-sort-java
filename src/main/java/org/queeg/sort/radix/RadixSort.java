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

public class RadixSort {
  private static final int BITS_PER_PASS = 8;
  private static final int BUCKET_MASK = 0x000000ff;

  public static void sort(int a[]) {
    int b[] = new int[a.length];

    for (int shift = 0; shift < Integer.SIZE; shift += BITS_PER_PASS) {
      int bucketCounts[] = new int[1 << BITS_PER_PASS];
      for (int i = 0; i < a.length; i++) {
        bucketCounts[bucket(shift, a[i])]++;
      }
      int acc = 0;
      for (int i = 0; i < bucketCounts.length; i++) {
        int t = bucketCounts[i];
        bucketCounts[i] = acc;
        acc += t;
      }
      for (int i = 0; i < a.length; i++) {
        int bucket = bucket(shift, a[i]);
        int o = bucketCounts[bucket]++;
        b[o] = a[i];
      }

      int c[] = a;
      a = b;
      b = c;
    }
  }

  private static int bucket(int shift, int n) {
    n ^= 0x80000000;
    n >>= shift;
    n &= BUCKET_MASK;
    return n;
  }
}
