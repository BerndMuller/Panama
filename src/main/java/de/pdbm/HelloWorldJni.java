package de.pdbm;

public class HelloWorldJni {
	
	public static void main(String[] args) {
		System.loadLibrary("hello"); // use library libhello.so, omit prefix and file extension
		new HelloWorldJni().sayHello();
	}

	private native void sayHello();

}
