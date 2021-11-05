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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Runs the C function time() and prints the result.
 * 
 * <p>
 * C's time() is declared as
 * <pre>
 *   #include &lt;time.h&gt;
 *   time_t time(time_t *tloc);
 * </pre>
 * 
 * <code>time()</code> returns number of seconds since the Epoch. If <code>tloc</code> is non-NULL, the
 * return value is also stored in the memory pointed by <code>tloc</code>.
 * 
 * The type <code>time_t</code> has size 8, see SizeOfTime.c. 
 * 
 * @author Bernd Müller
 *
 */
public class Time {

	public static void main(String[] args) throws Throwable {

		MethodHandle time = CLinker.getInstance().downcallHandle(CLinker.systemLookup().lookup("time").get(),
				MethodType.methodType(long.class, MemoryAddress.class),
				FunctionDescriptor.of(C_LONG, C_POINTER));
		
		try (ResourceScope scope = ResourceScope.newConfinedScope()) {
			MemorySegment segment = SegmentAllocator.ofScope(scope).allocate(8);

			// more chatty version:
//			SegmentAllocator allocator = SegmentAllocator.arenaAllocator(8, scope);
//			MemorySegment segment = allocator.allocate(8);

			System.out.println("Allocated memory segment is native: " + segment.isNative());
			System.out.println("Allocated memory segment size is  : " + segment.byteSize());
			
			long secondsSinceEpoch = (long) time.invokeExact(segment.address());
//			long secondsSinceEpoch = (long) time.invokeExact(MemoryAddress.NULL);
			
			System.out.println("time() returns:   " + secondsSinceEpoch);
			System.out.println("time() prameter:  " + segment.toLongArray()[0]); 

			System.out.println("Europe/Berlin: " + LocalDateTime.ofEpochSecond(secondsSinceEpoch, 0, ZoneId.of("Europe/Berlin").getRules().getOffset(LocalDateTime.now())));
			System.out.println("UTC:           " + LocalDateTime.ofEpochSecond(secondsSinceEpoch, 0, ZoneOffset.UTC));

		}
	}
	
}
