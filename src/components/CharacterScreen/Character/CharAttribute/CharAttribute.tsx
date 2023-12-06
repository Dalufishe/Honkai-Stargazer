import React, { useContext, useEffect, useMemo, useState } from "react";
import { View, Text } from "react-native";
import { Info } from "phosphor-react-native";
import AttrSliderbar from "../../../global/Sliderbar/Sliderbar";
import { Image } from "expo-image";
import CharPageHeading from "../../../global/PageHeading/PageHeading";
import useDelayLoad from "../../../../hooks/useDelayLoad";
import MaterialList from "../../../global/MaterialList/MaterialList";
import CharacterContext from "../../../../context/CharacterContext";
import { CharacterName } from "../../../../types/character";
import { getCharFullData } from "../../../../utils/getDataFromMap";

const HPIcon = require("../../../../../assets/icons/HP.png");
const STRIcon = require("../../../../../assets/icons/STR.png");
const DEFIcon = require("../../../../../assets/icons/DEF.png");
const DEXIcon = require("../../../../../assets/icons/DEX.png");
const ELIcon = require("../../../../../assets/icons/EL.png");

const DownArrowIcon = require("../../../../../assets/icons/DownArrow.svg");

export default React.memo(function CharAttribute() {
  const loaded = useDelayLoad(100);

  const charData = useContext(CharacterContext);
  const charId = charData?.id!;
  const charFullData = getCharFullData(charId);
  const charLevelData = charFullData?.levelData;

  const [str, setStr] = useState(0);
  const [hp, setHp] = useState(0);
  const [def, setDef] = useState(0);
  const [dex, setDex] = useState(0);

  const [attrFromLevel, setAttrFromLevel] = useState(0);
  const [attrToLevel, setAttrToLevel] = useState(8);

  const handleFromLevelChange = (newLevel: number) => {
    if (newLevel >= attrToLevel) {
      setAttrFromLevel(attrToLevel - 1);
      return;
    }
    setAttrFromLevel(newLevel);
  };

  const handleToLevelChange = (newLevel: number) => {
    if (newLevel <= attrFromLevel) {
      setAttrToLevel(attrFromLevel + 1);
      return;
    }
    setAttrToLevel(newLevel);
  };

  useEffect(() => {
    setTimeout(() => {
      const charCurrentLevelData =
        charLevelData?.[attrFromLevel === 7 ? 6 : attrFromLevel];
      setStr(charCurrentLevelData?.attackBase || 0);
      setHp(charCurrentLevelData?.hpBase || 0);
      setDef(charCurrentLevelData?.defenseBase || 0);
      setDex(charCurrentLevelData?.speedBase || 0);
    });
  }, [attrFromLevel]);

  return (
    <>
      <View style={{ alignItems: "center" }}>
        <CharPageHeading Icon={Info}>基础属性</CharPageHeading>
        {loaded && (
          <>
            {/* 等級 - 起點 */}
            <View
              className="w-full"
              style={{
                flexDirection: "row",
                alignItems: "center",
                justifyContent: "space-between",
              }}
            >
              {/* 等級 */}
              <Text className="text-white text-[16px] font-medium">
                Lv.{attrFromLevel * 10 + 1}
              </Text>
              {/* 等級滑動欄 */}
              <AttrSliderbar
                point={9}
                value={attrFromLevel}
                onChange={handleFromLevelChange}
              />
            </View>
            <Image
              className="w-[10px] h-[10px] my-[5px] ml-[5px]"
              style={{ alignSelf: "flex-start" }}
              source={DownArrowIcon}
            />

            {/* 等級 - 終點 */}
            <View
              className="w-full"
              style={{
                flexDirection: "row",
                alignItems: "center",
                justifyContent: "space-between",
              }}
            >
              {/* 等級 */}
              <Text className="text-white text-[16px] font-medium">
                Lv.{attrToLevel === 0 ? "1" : attrToLevel * 10}
              </Text>
              {/* 等級滑動欄 */}
              <AttrSliderbar
                point={9}
                value={attrToLevel}
                onChange={handleToLevelChange}
              />
            </View>
            {/* 屬性數值 */}
            <View className="mt-4" style={{ flexDirection: "row", gap: 12 }}>
              <View style={{ flexDirection: "row", alignItems: "center" }}>
                <Image className="w-6 h-6" source={HPIcon} />
                <Text className="text-white text-[16px] font-medium">
                  {hp.toFixed(0)}
                </Text>
              </View>
              <View style={{ flexDirection: "row", alignItems: "center" }}>
                <Image className="w-6 h-6" source={STRIcon} />
                <Text className="text-white text-[16px] font-medium">
                  {str.toFixed(0)}
                </Text>
              </View>
              <View style={{ flexDirection: "row", alignItems: "center" }}>
                <Image className="w-6 h-6" source={DEFIcon} />
                <Text className="text-white text-[16px] font-medium">
                  {def.toFixed(0)}
                </Text>
              </View>
              <View style={{ flexDirection: "row", alignItems: "center" }}>
                <Image className="w-6 h-6" source={DEXIcon} />
                <Text className="text-white text-[16px] font-medium">
                  {dex.toFixed(0)}
                </Text>
              </View>
              <View style={{ flexDirection: "row", alignItems: "center" }}>
                <Image className="w-6 h-6" source={ELIcon} />
                <Text className="text-white text-[16px] font-medium">
                  {charFullData.spRequirement}
                </Text>
              </View>
            </View>
          </>
        )}
      </View>
      <MaterialList />
    </>
  );
});
