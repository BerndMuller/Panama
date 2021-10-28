package de.pdbm;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.ResourceScope;
import jdk.incubator.foreign.SegmentAllocator;

import static jdk.incubator.foreign.CLinker.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

/**
 * Runs the C function printf() and prints the return value, i.e. the number of output characters.
 * 
 * <p>
 * C's printf() is declared as
 * <pre>
 *   #include &lt;stdio.h&gt;
 *   int printf(const char *format, ...);
 * </pre>
 * The second parameter is a varargs parameter.
 * 
 * @author bernd
 *
 */
public class Printf {

	public static void main(String[] args) throws Throwable {
		MethodHandle printf = CLinker.getInstance().downcallHandle(CLinker.systemLookup().lookup("printf").get(),
				MethodType.methodType(int.class, MemoryAddress.class, int.class, double.class, MemoryAddress.class),
				FunctionDescriptor.of(C_INT, C_POINTER, C_INT, C_DOUBLE, C_POINTER));
		
		int a = 42;
		double b = 42.0;
		String output = "ein einfacher String";

		try (ResourceScope scope = ResourceScope.newConfinedScope()) {
			SegmentAllocator allocator = SegmentAllocator.arenaAllocator(scope);
			MemorySegment formatString = CLinker.toCString("int: %d, double: %f, String: %s\n", allocator);
			MemorySegment outputString = CLinker.toCString(output, allocator);
			
			int rc = (int) printf.invokeExact(formatString.address(), a, b, outputString.address());
			System.out.println("Es wurden " + rc + " Zeichen ausgegeben");
		}
	}
	
}
