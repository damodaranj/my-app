package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

// C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
// ORIGINAL LINE: [StructLayout(LayoutKind.Explicit, Size = 0x320, CharSet = CharSet.Unicode)]
// public class TradeRequestInternal
public class TradeRequestInternal {
  // <summary>
  /** Request id */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(0)] public int RequestId;
  public int RequestId;

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: [FieldOffset(4)][MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 64)]
  // public byte[] s4;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  public byte[] s4;

  /** Trade type */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(68)] public TradeType TradeType;
  public TradeType TradeType;

  /** Account login */
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: [FieldOffset(72)] public ulong Login;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  public long Login;

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: [FieldOffset(80)][MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 72)]
  // public byte[] s50;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  public byte[] s50;

  /** Transfer login */
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: [FieldOffset(152)] public ulong TransferLogin;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  public long TransferLogin;

  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(160)][MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]
  // public string sA0;
  public String sA0;

  /** Symbol currency */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(288)][MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]
  // public string Currency;
  public String Currency;

  /** Lots */
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: [FieldOffset(352)] public ulong Lots;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  public long Lots;

  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(360)] public long s168;
  public long s168;

  /** Significant digits */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(368)] public int Digits;
  public int Digits;

  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(372)] public long s174;
  public long s174;
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: [FieldOffset(380)][MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 20)]
  // public byte[] s17C;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  public byte[] s17C;

  /** Order ticket */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(400)] public long OrderTicket;
  public long OrderTicket;

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: [FieldOffset(408)][MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 64)]
  // public byte[] s198;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  public byte[] s198;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(472)] public long s1D8;
  public long s1D8;

  /** Expiration time */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(480)] public long ExpirationTime;
  public long ExpirationTime;

  /** Order type */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(488)] public OrderType OrderType;
  public OrderType OrderType;

  /** Fill policy */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(492)] public FillPolicy FillPolicy;
  public FillPolicy FillPolicy;

  /** Expiration type */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(496)] public ExpirationDate ExpirationType;
  public ExpirationDate ExpirationType = ExpirationDate.values()[0];

  /** Request flags */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(500)] public long Flags;
  public long Flags;

  /** Placed type */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(508)] public PlacedType PlacedType;
  public PlacedType PlacedType;

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: [FieldOffset(512)][MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 16)]
  // public byte[] s200;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  public byte[] s200;

  /** Price */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(528)] public double Price;
  public double Price;

  /** Order price */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(536)] public double OrderPrice;
  public double OrderPrice;

  /** Stop loss */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(544)] public double StopLoss;
  public double StopLoss;

  /** Take profit */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(552)] public double TakeProfit;
  public double TakeProfit;

  /** Deviation */
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: [FieldOffset(560)] public ulong Deviation;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  public long Deviation;

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: [FieldOffset(568)][MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 32)]
  // public byte[] s238;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  public byte[] s238;

  /** Expert id */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(600)] public long ExpertId;
  public long ExpertId;

  /** Text comment */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(608)][MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]
  // public string Comment;
  public String Comment;

  /** Deal ticket */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(672)] public long DealTicket;
  public long DealTicket;

  /** By close deal ticket */
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  // ORIGINAL LINE: [FieldOffset(680)] public long ByCloseTicket;
  public long ByCloseTicket;

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: [FieldOffset(688)][MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst =
  // 112)] public byte[] s2B0;
  // C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
  public byte[] s2B0;
}
