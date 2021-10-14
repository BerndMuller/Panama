package de.pdbm;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.MemoryAccess;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.ResourceScope;
import jdk.incubator.foreign.SegmentAllocator;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;

import static jdk.incubator.foreign.CLinker.*;

/**
 * Simple example to call C from Java as well as Java from C
 * 
 * Based on https://github.com/openjdk/panama-foreign/blob/foreign-jextract/doc/panama_ffi.md
 * 
 * @author bernd
 *
 */
public class Quicksort {

    public static void main(String[] args) throws Throwable {
        qsort();
    }

    static class Comparator {
        static int compare(MemoryAddress addr1, MemoryAddress addr2) {
            int v1 = MemoryAccess.getIntAtOffset(MemorySegment.globalNativeSegment(), addr1.toRawLongValue());
            int v2 = MemoryAccess.getIntAtOffset(MemorySegment.globalNativeSegment(), addr2.toRawLongValue());
            return v1 - v2;
        }
    }

    public static void qsort() throws Throwable {
        MethodHandle qsort = CLinker.getInstance().downcallHandle(
                CLinker.systemLookup().lookup("qsort").get(),
                MethodType.methodType(void.class, MemoryAddress.class, long.class, long.class, MemoryAddress.class),
                FunctionDescriptor.ofVoid(C_POINTER, C_LONG, C_LONG, C_POINTER)
        );

        MethodHandle comparHandle = MethodHandles.lookup()
                .findStatic(Comparator.class, "compare",
                        MethodType.methodType(int.class, MemoryAddress.class, MemoryAddress.class));

        try (ResourceScope scope = ResourceScope.newConfinedScope()) {
            MemoryAddress comparFunc = CLinker.getInstance().upcallStub(
                comparHandle,
                FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER), scope);

            MemorySegment array = SegmentAllocator.ofScope(scope)
                                                  .allocateArray(C_INT, new int[] { 0, 9, 3, 4, 6, 5, 1, 8, 2, 7 });
            qsort.invokeExact(array.address(), 10L, 4L, comparFunc);
            int[] sorted = array.toIntArray(); // [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ]
            System.out.println(Arrays.toString(sorted));
        }
    }

}
