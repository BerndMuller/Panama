package de.pdbm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Optional;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.SymbolLookup;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.MemoryAddress;

/**
 * FFI's way of doing HelloWorldJni
 * 
 * @author bernd
 *
 */
public class HelloWorldPanama {
	
	public static void main(String[] args) throws Throwable {
		new HelloWorldPanama().sayHello();
	}
    
    public void sayHello() throws Throwable {
    	System.loadLibrary("hello"); // use library libhello.so, omit prefix and file extension
    	Optional<MemoryAddress> address = SymbolLookup.loaderLookup().lookup("hello"); // C function hello()
    	MethodHandle hello = CLinker.getInstance().downcallHandle(
    			address.get(),
    			MethodType.methodType(void.class),
    			FunctionDescriptor.ofVoid());
    	hello.invokeExact();
    }

}
