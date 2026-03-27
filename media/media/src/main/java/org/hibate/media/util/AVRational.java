/*
 * Copyright (C) 2024 Hibate <ycaia86@126.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibate.media.util;

import java.util.Objects;

/**
 * @author Hibate
 * Created on 2024/03/27.
 */
public class AVRational extends Number implements Comparable<AVRational> {

    private int numerator;
    private int denominator;

    public AVRational() {
    }

    public AVRational(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    public int intValue() {
        return (int) doubleValue();
    }

    public long longValue() {
        return (long) doubleValue();
    }

    public float floatValue() {
        return (float) doubleValue();
    }

    public double doubleValue() {
        return (denominator == 0) ? 0 : (numerator / (double) denominator);
    }

    public AVRational reverse() {
        return new AVRational(this.denominator, this.numerator);
    }

    @Override
    public String toString() {
        return "AVRational{" +
                "numerator=" + numerator +
                ", denominator=" + denominator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AVRational rational = (AVRational) o;
        return numerator == rational.numerator && denominator == rational.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    @Override
    public int compareTo(AVRational o) {
        return AVRational.compare(this, o);
    }

    /**
     * Compares the two specified {@code AVRational} values. The sign
     * of the integer value returned is the same as that of the
     * integer that would be returned by the call:
     * <pre>
     *    new AVRational(num1, den1).compareTo(new AVRational(num2, den2))
     * </pre>
     *
     * @param   a        the first {@code AVRational} to compare
     * @param   b        the second {@code AVRational} to compare
     * @return  the value {@code 0} if {@code a} is
     *          numerically equal to {@code b}; a value less than
     *          {@code 0} if {@code a} is numerically less than
     *          {@code b}; and a value greater than {@code 0}
     *          if {@code a} is numerically greater than
     *          {@code b}.
     */
    public static int compare(AVRational a, AVRational b) {
        if (Objects.equals(a, b)) {
            return 0;
        }
        // null last
        if (a == null) {
            return -1;
        } else if (b == null) {
            return 1;
        }

        long tmp = a.numerator * (long) b.denominator - b.numerator * (long) a.denominator;
        if (tmp != 0) {
            return (int) ((tmp ^ a.denominator ^ b.denominator) >> 63) | 1;
        } else if ((b.denominator != 0) && (a.denominator != 0)) {
            return 0;
        } else if ((a.numerator != 0) && (b.numerator != 0)) {
            return (a.numerator >> 31) - (b.numerator >> 31);
        } else {
            return Integer.MIN_VALUE;
        }
    }
}
