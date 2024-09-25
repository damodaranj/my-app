package io.quantum.trading.provider;

import io.quantum.trading.entities.NseScriptMetadata;
import java.util.List;

public interface NseMetadataProvidable {
  List<NseScriptMetadata> getMetadata();
}
