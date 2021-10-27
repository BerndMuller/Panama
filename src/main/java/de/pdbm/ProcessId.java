package de.pdbm;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;


/**
 * Runs the C function getpid() and prints the result.
 * 
 * <p>
 * C's getpid() is declared as
 * <pre>
 *   #include &lt;sys/types.h&gt;
 *   #include &lt;unistd.h&gt;
 *   pid_t getpid(void); 
 * </pre>
 * 
 * The type <code>pid_t</code> has size 4, see SizeOfPid.c. 
 * 
 * @author bernd
 *
 */
public class ProcessId {

	public static void main(String[] args) throws Throwable {

		MethodHandle getpid =  CLinker.getInstance().downcallHandle(
				CLinker.systemLookup().lookup("getpid").get(),
				MethodType.methodType(int.class), 
				FunctionDescriptor.of(CLinker.C_INT));
		
		System.out.println("Process Id: " + (int) getpid.invokeExact());
	}

}
