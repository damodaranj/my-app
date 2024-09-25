package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

// ----------------------------------------------------------------------------------------
//	Copyright © 2007 - 2017 Tangible Software Solutions Inc.
//	This class can be used by anyone provided that the copyright notice remains intact.
//
//	This class is used to simulate the ability to pass arguments by reference in Java.
// ----------------------------------------------------------------------------------------
public final class RefObject<T> {
  public T argValue;

  public RefObject(T refArg) {
    argValue = refArg;
  }

  public RefObject() {}
}
