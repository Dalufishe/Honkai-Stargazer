import { View, Text, Dimensions } from "react-native";
import React from "react";
import LcStars from "../../../global/PageStars/PageStars";
import { Image } from "expo-image";
import Path from "../../../../../assets/images/images_map/path";
import useLcData from "../../../../context/LightconeData/hooks/useLcData";
import useRelicData from "../../../../context/RelicData/hooks/useRelicData";

export default function RelicInfo() {
  const { relicData } = useRelicData();
  return (
    <View
      style={{
        paddingTop: Dimensions.get("window").height - 244,
        gap: 8,
      }}
    >
      <View style={{ gap: 12 }}>
        <Text className="text-[32px] font-[HY65] text-white">
          {relicData?.name}
        </Text>
        <LcStars count={relicData?.rare || 5} />
      </View>
    </View>
  );
}
