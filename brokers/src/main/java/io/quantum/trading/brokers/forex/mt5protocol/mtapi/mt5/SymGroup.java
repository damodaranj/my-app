package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x4CC, CharSet = CharSet.Unicode)]*/
public class SymGroup extends FromBufReader {
  public double MaxLots() {
    return (double) MaxVolume / 100000000;
  }

  public double MinLots() {
    return (double) MinVolume / 100000000;
  }

  public double LotsStep() {
    return (double) VolumeStep / 100000000;
  }

  /*[FieldOffset(0)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s0;
  public byte[] s0;
  /*[FieldOffset(8)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 128)]*/ public String GroupName;
  /*[FieldOffset(264)]*/ public int DeviationRate;
  /*[FieldOffset(268)]*/ public int RoundRate;
  /*[FieldOffset(272)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 32)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s110;
  public byte[] s110;
  /*[FieldOffset(304)]*/ public TradeMode TradeMode;
  /*[FieldOffset(308)]*/ public int SL;
  /*[FieldOffset(312)]*/ public int TP;
  /*[FieldOffset(316)]*/ public ExecutionType TradeType = ExecutionType.values()[0];
  /*[FieldOffset(320)]*/ public int FillPolicy;
  /*[FieldOffset(324)]*/ public int Expiration;
  /*[FieldOffset(328)]*/ public int OrderFlags;
  /*[FieldOffset(332)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 52)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s14C;
  public byte[] s14C;
  /*[FieldOffset(384)]*/ public int s180;
  /*[FieldOffset(388)]*/ public int PriceTimeout;
  /*[FieldOffset(392)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 32)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s188;
  public byte[] s188;
  /*[FieldOffset(424)]*/ public int s1A8;
  /*[FieldOffset(428)]*/ public int RequoteTimeout;
  /*[FieldOffset(432)]*/ public int s1B0;
  /*[FieldOffset(436)]*/ public int s1B4;
  /*[FieldOffset(440)]*/ public int s1B8;
  /*[FieldOffset(444)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public uint RequestLots;
  public int RequestLots;
  /*[FieldOffset(448)]*/ public int s1C4;
  /*[FieldOffset(452)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 24)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s1C8;
  public byte[] s1C8;
  /*[FieldOffset(476)]*/ public int s1E0;
  /*[FieldOffset(480)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 128)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s1E4;
  public byte[] s1E4;
  /*[FieldOffset(608)]*/ public int s264;
  /*[FieldOffset(612)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 128)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s268;
  public byte[] s268;
  /*[FieldOffset(740)]*/ public int tmp1;
  /*[FieldOffset(744)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public uint MinVolume;
  public long MinVolume;
  /*[FieldOffset(748)]*/
  // public int tmp2;
  /*[FieldOffset(752)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public uint MaxVolume;
  public long MaxVolume;
  /*[FieldOffset(756)]*/
  // public int tmp3;
  /*[FieldOffset(760)]*/ public long VolumeStep;
  /*[FieldOffset(768)]*/ public long s300;
  /*[FieldOffset(776)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 56)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s308;
  public byte[] s308;
  /*[FieldOffset(832)]*/ public int s340;
  /*[FieldOffset(836)]*/ public double InitialMargin;
  /*[FieldOffset(844)]*/ public double MaintenanceMargin;
  /*[FieldOffset(852)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/ public double[] InitMarginRate;
  /*[FieldOffset(916)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/ public double[] MntnMarginRate;
  /*[FieldOffset(980)]*/ public double HedgedMargin;
  /*[FieldOffset(988)]*/ public double s3DC;
  /*[FieldOffset(996)]*/ public double s3E4;
  /*[FieldOffset(1004)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 40)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s3EC;
  public byte[] s3EC;
  /*[FieldOffset(1044)]*/ public SwapType SwapType;
  /*[FieldOffset(1048)]*/ public double SwapLong;
  /*[FieldOffset(1056)]*/ public double SwapShort;
  /*[FieldOffset(1064)]*/ public V3DaysSwap ThreeDaysSwap = V3DaysSwap.values()[0];
  /*[FieldOffset(1068)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 32)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s42C;
  public byte[] s42C;
  /*[FieldOffset(1100)]*/ public int s44C;
  /*[FieldOffset(1104)]*/ public int s450;
  /*[FieldOffset(1108)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 120)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s454;
  public byte[] s454;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int startInd = buf.Ind;
    int endInd = buf.Ind + 1228;
    SymGroup st = new SymGroup();
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s0 = new byte[8];
    st.s0 = new byte[8];
    for (int i = 0; i < 8; i++) {
      st.s0[i] = buf.Byte();
    }
    st.GroupName = GetString(buf.Bytes(256));
    st.DeviationRate = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.RoundRate = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s110 = new byte[32];
    st.s110 = new byte[32];
    for (int i = 0; i < 32; i++) {
      st.s110[i] = buf.Byte();
    }
    st.TradeMode = TradeMode.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.SL = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.TP = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.TradeType = ExecutionType.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.FillPolicy = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.Expiration = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.OrderFlags = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s14C = new byte[52];
    st.s14C = new byte[52];
    for (int i = 0; i < 52; i++) {
      st.s14C[i] = buf.Byte();
    }
    st.s180 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.PriceTimeout = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s188 = new byte[32];
    st.s188 = new byte[32];
    for (int i = 0; i < 32; i++) {
      st.s188[i] = buf.Byte();
    }
    st.s1A8 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.RequoteTimeout = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s1B0 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s1B4 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s1B8 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.RequestLots = (int) BitConverter.ToUInt32(buf.Bytes(4), 0);
    st.s1C4 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s1C8 = new byte[24];
    st.s1C8 = new byte[24];
    for (int i = 0; i < 24; i++) {
      st.s1C8[i] = buf.Byte();
    }
    st.s1E0 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s1E4 = new byte[128];
    st.s1E4 = new byte[128];
    for (int i = 0; i < 128; i++) {
      st.s1E4[i] = buf.Byte();
    }
    st.s264 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s268 = new byte[128];
    st.s268 = new byte[128];
    for (int i = 0; i < 128; i++) {
      st.s268[i] = buf.Byte();
    }
    st.tmp1 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.MinVolume = BitConverter.ToInt64(buf.Bytes(8), 0);
    // st.tmp2 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.MaxVolume = BitConverter.ToInt64(buf.Bytes(8), 0);
    // st.tmp3 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.VolumeStep = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s300 = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s308 = new byte[56];
    st.s308 = new byte[56];
    for (int i = 0; i < 56; i++) {
      st.s308[i] = buf.Byte();
    }
    st.s340 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.InitialMargin = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.MaintenanceMargin = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.InitMarginRate = new double[8];
    for (int i = 0; i < 8; i++) {
      st.InitMarginRate[i] = BitConverter.ToDouble(buf.Bytes(8), 0);
    }
    st.MntnMarginRate = new double[8];
    for (int i = 0; i < 8; i++) {
      st.MntnMarginRate[i] = BitConverter.ToDouble(buf.Bytes(8), 0);
    }
    st.HedgedMargin = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s3DC = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s3E4 = BitConverter.ToDouble(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s3EC = new byte[40];
    st.s3EC = new byte[40];
    for (int i = 0; i < 40; i++) {
      st.s3EC[i] = buf.Byte();
    }
    st.SwapType = SwapType.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.SwapLong = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.SwapShort = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.ThreeDaysSwap = V3DaysSwap.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s42C = new byte[32];
    st.s42C = new byte[32];
    for (int i = 0; i < 32; i++) {
      st.s42C[i] = buf.Byte();
    }
    st.s44C = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s450 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s454 = new byte[120];
    st.s454 = new byte[120];
    for (int i = 0; i < 120; i++) {
      st.s454[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }

  public void CopyValues(SymGroup grp) {
    if (grp.DeviationRate != Integer.MAX_VALUE) DeviationRate = grp.DeviationRate;
    if (grp.RoundRate != Integer.MAX_VALUE) RoundRate = grp.RoundRate;
    if (grp.TradeMode != null) TradeMode = grp.TradeMode;
    if (grp.SL != Integer.MAX_VALUE) SL = grp.SL;
    if (grp.TP != Integer.MAX_VALUE) TP = grp.TP;
    if (grp.TradeType != null) TradeType = grp.TradeType;
    if ((int) grp.FillPolicy != -1) FillPolicy = grp.FillPolicy;
    if (grp.Expiration != -1) Expiration = grp.Expiration;
    if (grp.OrderFlags != -1) OrderFlags = grp.OrderFlags;
    if (grp.s180 != -1) s180 = grp.s180;
    if (grp.PriceTimeout != -1) PriceTimeout = grp.PriceTimeout;
    if (grp.s1A8 != -1) s1A8 = grp.s1A8;
    if (grp.RequoteTimeout != -1) RequoteTimeout = grp.RequoteTimeout;
    if (grp.s1B0 != -1) s1B0 = grp.s1B0;
    if (grp.s1B4 != -1) s1B4 = grp.s1B4;
    if (grp.RequestLots != -1) RequestLots = grp.RequestLots;
    if (grp.s1C4 != -1) s1C4 = grp.s1C4;
    if (grp.MinVolume != -1) MinVolume = grp.MinVolume;
    if (grp.MaxVolume != -1) MaxVolume = grp.MaxVolume;
    if (grp.VolumeStep != -1) VolumeStep = grp.VolumeStep;
    if (grp.s300 != -1) s300 = grp.s300;
    if (grp.s340 != -1) s340 = grp.s340;
    if (grp.InitialMargin != Double.MAX_VALUE) InitialMargin = grp.InitialMargin;
    if (grp.MaintenanceMargin != Double.MAX_VALUE) MaintenanceMargin = grp.MaintenanceMargin;
    if (grp.InitMarginRate[0] != Double.MAX_VALUE) InitMarginRate[0] = grp.InitMarginRate[0];
    if (grp.InitMarginRate[1] != Double.MAX_VALUE) InitMarginRate[1] = grp.InitMarginRate[1];
    if (grp.InitMarginRate[2] != Double.MAX_VALUE) InitMarginRate[2] = grp.InitMarginRate[2];
    if (grp.InitMarginRate[3] != Double.MAX_VALUE) InitMarginRate[3] = grp.InitMarginRate[3];
    if (grp.InitMarginRate[4] != Double.MAX_VALUE) InitMarginRate[4] = grp.InitMarginRate[4];
    if (grp.InitMarginRate[5] != Double.MAX_VALUE) InitMarginRate[5] = grp.InitMarginRate[5];
    if (grp.InitMarginRate[6] != Double.MAX_VALUE) InitMarginRate[6] = grp.InitMarginRate[6];
    if (grp.InitMarginRate[7] != Double.MAX_VALUE) InitMarginRate[7] = grp.InitMarginRate[7];
    if (grp.MntnMarginRate[0] != Double.MAX_VALUE) MntnMarginRate[0] = grp.MntnMarginRate[0];
    if (grp.MntnMarginRate[1] != Double.MAX_VALUE) MntnMarginRate[1] = grp.MntnMarginRate[1];
    if (grp.MntnMarginRate[2] != Double.MAX_VALUE) MntnMarginRate[2] = grp.MntnMarginRate[2];
    if (grp.MntnMarginRate[3] != Double.MAX_VALUE) MntnMarginRate[3] = grp.MntnMarginRate[3];
    if (grp.MntnMarginRate[4] != Double.MAX_VALUE) MntnMarginRate[4] = grp.MntnMarginRate[4];
    if (grp.MntnMarginRate[5] != Double.MAX_VALUE) MntnMarginRate[5] = grp.MntnMarginRate[5];
    if (grp.MntnMarginRate[6] != Double.MAX_VALUE) MntnMarginRate[6] = grp.MntnMarginRate[6];
    if (grp.MntnMarginRate[7] != Double.MAX_VALUE) MntnMarginRate[7] = grp.MntnMarginRate[7];
    if (grp.HedgedMargin != Double.MAX_VALUE) HedgedMargin = grp.HedgedMargin;
    if (grp.s3DC != Double.MAX_VALUE) s3DC = grp.s3DC;
    if (grp.s3E4 != Double.MAX_VALUE) s3E4 = grp.s3E4;
    if (grp.SwapType != null) SwapType = grp.SwapType;
    if (grp.SwapLong != Double.MAX_VALUE) SwapLong = grp.SwapLong;
    if (grp.SwapShort != Double.MAX_VALUE) SwapShort = grp.SwapShort;
    if ((int) grp.ThreeDaysSwap.getValue() != Integer.MAX_VALUE) ThreeDaysSwap = grp.ThreeDaysSwap;
    s44C = grp.s44C;
    s450 = grp.s450;
  }
}
