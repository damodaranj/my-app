package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

class IntContainer {
  public int Value;

  public static IntContainer fromData(int i) {
    IntContainer c = new IntContainer();
    c.Value = i;
    return c;
  }

  public int get() {
    return Value;
  }

  public void set(int i) {
    Value = i;
  }
}
