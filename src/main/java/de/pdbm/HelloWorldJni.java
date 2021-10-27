package de.pdbm;

/**
 * Most simple example of using JNI
 * 
 * @author bernd
 *
 */
public class HelloWorldJni {
	
	public static void main(String[] args) {
		System.loadLibrary("hello"); // use library libhello.so, omit prefix and file extension
		new HelloWorldJni().sayHello();
	}

	private native void sayHello();

}
