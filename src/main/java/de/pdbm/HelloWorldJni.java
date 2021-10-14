package de.pdbm;

public class HelloWorldJni {
	
	public static void main(String[] args) {
		System.loadLibrary("hello");
		new HelloWorldJni().sayHello();
	}

	private native void sayHello();

}
