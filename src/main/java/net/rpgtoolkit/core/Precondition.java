/*
 * Copyright (c) 2015, rpgtoolkit.net & Contributors
 *
 * See LICENSE.md in the distribution for the full license text including,
 * but not limited to, a notice of warranty and distribution rights.
 */
package net.rpgtoolkit.core;

/**
 *
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public class Precondition {

    public static void notNull(Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException(
                    "argument may not be null");
        }
    }

    public static void notNull(String name, Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException(String.format(
                    "argument %s may not be null", name));
        }
    }

    public static void range(String name, long min, long max, long arg) {
        if (arg < min) {
            throw new IllegalArgumentException(String.format(
                    "argument %s (%d) is under minimum of %d",
                    name, arg, min));
        }
        if (arg > max) {
            throw new IllegalArgumentException(String.format(
                    "argument %s (%d) is over maximum of %d",
                    name, arg, max));
        }
    }

    public static void positive(String name, long arg) {
        if (arg < 0) {
            throw new IllegalArgumentException(String.format(
                    "argument %s (%d) must be a positive integer",
                    name, arg));
        }
    }

    public static void positiveNonZero(String name, long arg) {
        if (arg <= 0) {
            throw new IllegalArgumentException(String.format(
                    "argument %s (%d) must be a positive non-zero integer",
                    name, arg));
        }
    }

    public static void powerOfTwo(String name, long arg) {
        // Complement and Compare method
        boolean pot = ((arg != 0) && ((arg & (~arg + 1)) == arg));
        if (!pot) {
            throw new IllegalArgumentException(String.format(
                    "argument %s (%d) must be a power of two",
                    name, arg));
        }
    }

}
