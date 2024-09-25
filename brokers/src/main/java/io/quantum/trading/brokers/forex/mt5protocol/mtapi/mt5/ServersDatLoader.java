package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ServersDatLoader {
  public final Server[] Load(String path) throws IOException {
    InBuf buf = new InBuf(Files.readAllBytes(Paths.get(path)), 0);
    DatHeader hdr = buf.Struct(new DatHeader());
    ArrayList<Server> lst = new ArrayList<Server>();
    while (buf.gethasData()) {
      Server res = new Server();
      if ((hdr.Id & 0xFFFFFFFFL) == 0x1F9 || (hdr.Id & 0xFFFFFFFFL) == 0x1FA) {
        res.ServerInfoEx = buf.<ServerInfoEx>CryptStruct(ServerInfoEx.Size, new ServerInfoEx());
        int num = buf.Int();
        if (num < 0 || num > 128) break;
        res.Accesses = new Access[num];
        for (int i = 0; i < num; i++) {
          res.Accesses[i] = new Access();
          res.Accesses[i].AccessRec = buf.<AccessRec>CryptStruct(AccessRec.Size, new AccessRec());
          res.Accesses[i].Addresses = buf.<AddressRec>CryptArray(AddressRec.Size, new AddressRec());
        }
        num = buf.Int();
        if (num < 0 || num > 128) break;
        res.AccessesEx = new AccessEx[num];
        for (int i = 0; i < num; i++) {
          res.AccessesEx[i] = new AccessEx();
          res.AccessesEx[i].AccessRec =
              buf.<AccessRecEx>CryptStruct(AccessRecEx.Size, new AccessRecEx());
          res.AccessesEx[i].Addresses =
              buf.<AddressRecEx>CryptArray(AddressRecEx.Size, new AddressRecEx());
        }
      } else if ((hdr.Id & 0xFFFFFFFFL) == 0x1F7 || (hdr.Id & 0xFFFFFFFFL) == 0x1F8) {
        res.ServerInfo = buf.<ServerInfo>CryptStruct(ServerInfo.Size, new ServerInfo());
        // buf.Bytes(8);
        int num = buf.Int();
        if (num < 0 || num > 128) break;
        res.Accesses = new Access[num];
        for (int i = 0; i < num; i++) {
          res.Accesses[i] = new Access();
          res.Accesses[i].AccessRec = buf.<AccessRec>CryptStruct(AccessRec.Size, new AccessRec());
          res.Accesses[i].Addresses = buf.<AddressRec>CryptArray(AddressRec.Size, new AddressRec());
        }
      } else {
        throw new UnsupportedOperationException(
            "hdr.Id = 0x" + String.format("%X", hdr.Id & 0xFFFFFFFFL));
      }
      lst.add(res);
    }
    return lst.toArray(new Server[0]);
  }
}
